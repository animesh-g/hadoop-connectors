package com.google.cloud.hadoop.gcsio;

import com.google.storage.control.v2.DeleteFolderRequest;
import io.grpc.Status;
import io.grpc.StatusException;

public class MyStorageClient {
  public void deleteFolder(DeleteFolderRequest deleteFolderRequest) {
    String name = deleteFolderRequest.getName();
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {

    }
    //    if (name.contains("notfound")) {
    //      throw new RuntimeException(new StatusException(Status.NOT_FOUND));
    //    }
    //
    //    if (name.contains("failedpc")) {
    //      throw new RuntimeException(new StatusException(Status.FAILED_PRECONDITION));
    //    }
    //
    //    if (name.contains("failother")) {
    //      throw new RuntimeException(new StatusException(Status.INTERNAL));
    //    }
    //
    //    if (name.contains("failruntime")) {
    //      throw new RuntimeException("runtime error");
    //    }

    if (name.contains("sleep")) {
      try {
        System.out.println("Sleep:" + deleteFolderRequest.toString());
        Thread.sleep(1000 * 61);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }


    //    System.out.println("delete folder called1");
  }
}