package com.jsearch.indexer.indexing.strategies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jsearch.indexer.indexing.FileIndexer;

public class TextIndexingStrategy extends FileIndexer {
  @Override
  public void index(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      String filePath = file.getPath();
      int lineNumber = 0;

      while ((line = reader.readLine()) != null) {
        lineNumber++;
        String[] words = line.split(" ");
        for (int i = 0; i < words.length; i++) {
          String word = words[i];

          addWordToIndex(word, filePath, lineNumber, i);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
