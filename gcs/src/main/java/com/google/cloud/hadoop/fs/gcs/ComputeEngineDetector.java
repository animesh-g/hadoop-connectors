/*
 * Copyright 2026 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.hadoop.fs.gcs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.flogger.GoogleLogger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Heuristic detector to identify if the underlying compute engine calling the GCS connector is
 * Apache Spark, Trino, or Presto via JVM stack trace inspection using {@link StackWalker}.
 */
final class ComputeEngineDetector {

  private static final GoogleLogger logger = GoogleLogger.forEnclosingClass();

  enum Engine {
    SPARK(", Spark"),
    TRINO(", Trino"),
    PRESTO(", Presto");

    private final String userAgentSuffix;

    Engine(String userAgentSuffix) {
      this.userAgentSuffix = userAgentSuffix;
    }

    public String getUserAgentSuffix() {
      return userAgentSuffix;
    }
  }

  private static volatile Engine detectedEngine = null;
  private static final Object LOCK = new Object();

  private ComputeEngineDetector() {}

  /**
   * Returns the user-agent suffix corresponding to the detected compute engine, or an empty string
   * if no supported engine was detected.
   */
  public static String detectEngineSuffix() {
    Engine engine = getOrDetectEngine();
    return engine != null ? engine.getUserAgentSuffix() : "";
  }

  @VisibleForTesting
  static Engine getOrDetectEngine() {
    if (detectedEngine == null) {
      synchronized (LOCK) {
        if (detectedEngine == null) {
          detectedEngine = detectEngineFromStackTrace();
        }
      }
    }
    return detectedEngine;
  }

  @VisibleForTesting
  static void resetForTesting() {
    synchronized (LOCK) {
      detectedEngine = null;
    }
  }

  private static Engine detectEngineFromStackTrace() {
    List<StackWalker.StackFrame> frames;
    try {
      frames =
          StackWalker.getInstance()
              .walk(stream -> stream.collect(Collectors.toList()));
    } catch (Throwable t) {
      logger.atWarning().withCause(t).log(
          "Failed to inspect stack trace via StackWalker; skipping compute engine detection");
      return null;
    }

    // Print the complete stack trace in logs to debug caller identity
    StringBuilder sb = new StringBuilder();
    sb.append("Inspecting caller stack trace for compute engine detection (")
        .append(frames.size())
        .append(" frames):\n");
    for (StackWalker.StackFrame frame : frames) {
      sb.append("\tat ").append(frame.toString()).append("\n");
    }
    logger.atInfo().log("%s", sb.toString());

    Engine engine = findMatchingEngine(frames);
    if (engine != null) {
      logger.atInfo().log("ComputeEngineDetector: detected engine '%s'", engine);
    } else {
      logger.atInfo().log(
          "ComputeEngineDetector: no known compute engine recognized in caller stack trace");
    }
    return engine;
  }

  @VisibleForTesting
  static Engine findMatchingEngine(List<StackWalker.StackFrame> frames) {
    for (StackWalker.StackFrame frame : frames) {
      Engine engine = matchEngineFromClassName(frame.getClassName());
      if (engine != null) {
        logger.atInfo().log(
            "ComputeEngineDetector: matched %s from stack frame: %s", engine, frame);
        return engine;
      }
    }
    return null;
  }

  @VisibleForTesting
  static Engine findMatchingEngineFromClassNames(List<String> classNames) {
    for (String className : classNames) {
      Engine engine = matchEngineFromClassName(className);
      if (engine != null) {
        return engine;
      }
    }
    return null;
  }

  @VisibleForTesting
  static Engine matchEngineFromClassName(String className) {
    if (className.startsWith("org.apache.spark.")) {
      return Engine.SPARK;
    }
    if (className.startsWith("io.trino.")) {
      return Engine.TRINO;
    }
    if (className.startsWith("com.facebook.presto.")) {
      return Engine.PRESTO;
    }
    return null;
  }
}
