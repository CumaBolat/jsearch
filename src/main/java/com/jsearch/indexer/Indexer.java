package com.jsearch.indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Indexer {

  private static final String INDEX_FILE_NAME = "index";

  public void index(File file) {
    if (file.isDirectory()) {
      // indexDirectory(file);
    } else {
      indexFile(file);
    }
  }

  private void indexFile(File file) {
    System.out.println("Indexing file: " + file);
    try {
      String content = new String(Files.readAllBytes(file.toPath()));
      String[] words = content.split("\\W+");

      for (String word : words) {
        System.out.println("Indexing word: " + word);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
