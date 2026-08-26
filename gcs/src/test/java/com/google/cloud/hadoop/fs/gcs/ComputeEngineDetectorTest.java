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

import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link ComputeEngineDetector}. */
@RunWith(JUnit4.class)
public class ComputeEngineDetectorTest {

  @Before
  public void setUp() {
    ComputeEngineDetector.resetForTesting();
  }

  @After
  public void tearDown() {
    ComputeEngineDetector.resetForTesting();
  }

  @Test
  public void matchEngineFromClassName_detectsSpark() {
    assertThat(
            ComputeEngineDetector.matchEngineFromClassName(
                "org.apache.spark.deploy.SparkSubmit"))
        .isEqualTo(ComputeEngineDetector.Engine.SPARK);

    assertThat(
            ComputeEngineDetector.matchEngineFromClassName(
                "org.apache.spark.sql.execution.FileFormatWriter"))
        .isEqualTo(ComputeEngineDetector.Engine.SPARK);

    assertThat(
            ComputeEngineDetector.matchEngineFromClassName(
                "org.apache.spark.executor.Executor"))
        .isEqualTo(ComputeEngineDetector.Engine.SPARK);
  }

  @Test
  public void matchEngineFromClassName_detectsTrino() {
    assertThat(
            ComputeEngineDetector.matchEngineFromClassName(
                "io.trino.execution.SplitRunner"))
        .isEqualTo(ComputeEngineDetector.Engine.TRINO);

    assertThat(
            ComputeEngineDetector.matchEngineFromClassName(
                "io.trino.plugin.hive.HdfsEnvironment"))
        .isEqualTo(ComputeEngineDetector.Engine.TRINO);
  }

  @Test
  public void matchEngineFromClassName_detectsPresto() {
    assertThat(
            ComputeEngineDetector.matchEngineFromClassName(
                "com.facebook.presto.execution.TaskExecutor"))
        .isEqualTo(ComputeEngineDetector.Engine.PRESTO);

    assertThat(
            ComputeEngineDetector.matchEngineFromClassName(
                "com.facebook.presto.hive.HdfsEnvironment"))
        .isEqualTo(ComputeEngineDetector.Engine.PRESTO);
  }

  @Test
  public void matchEngineFromClassName_returnsNullForUnknown() {
    assertThat(ComputeEngineDetector.matchEngineFromClassName("org.apache.hadoop.fs.FileSystem"))
        .isNull();
    assertThat(ComputeEngineDetector.matchEngineFromClassName("java.lang.Thread"))
        .isNull();
  }

  @Test
  public void findMatchingEngineFromClassNames_findsFirstMatch() {
    List<String> frames =
        Arrays.asList(
            "java.lang.Thread",
            "org.apache.hadoop.fs.FileSystem",
            "org.apache.spark.sql.execution.datasources.FileScanRDD",
            "io.trino.execution.SplitRunner");

    assertThat(ComputeEngineDetector.findMatchingEngineFromClassNames(frames))
        .isEqualTo(ComputeEngineDetector.Engine.SPARK);
  }

  @Test
  public void findMatchingEngineFromClassNames_returnsNullWhenNoMatch() {
    List<String> frames =
        Arrays.asList(
            "java.lang.Thread",
            "org.apache.hadoop.fs.FileSystem",
            "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem");

    assertThat(ComputeEngineDetector.findMatchingEngineFromClassNames(frames))
        .isNull();
  }

  @Test
  public void getApplicationNameSuffix_prefersExplicitConfig() {
    Configuration config = new Configuration();
    config.set("fs.gs.application.name.suffix", "-custom-suffix");

    assertThat(GoogleHadoopFileSystemConfiguration.getApplicationNameSuffix(config))
        .isEqualTo("-custom-suffix");
  }

  @Test
  public void getApplicationNameSuffix_fallsBackToStackWalker() {
    Configuration config = new Configuration();
    // In standard JUnit runner, caller stack trace does not contain Spark or Trino,
    // so it should cleanly return an empty string without throwing.
    String suffix = GoogleHadoopFileSystemConfiguration.getApplicationNameSuffix(config);
    assertThat(suffix).isEqualTo("");
  }

  @Test
  public void getApplicationName_constructsExpectedAppName() {
    Configuration config = new Configuration();
    config.set("fs.gs.application.name.suffix", ", CustomApp");

    assertThat(GoogleHadoopFileSystemConfiguration.getApplicationName(config))
        .isEqualTo(GoogleHadoopFileSystem.GHFS_ID + ", CustomApp");
  }
}
