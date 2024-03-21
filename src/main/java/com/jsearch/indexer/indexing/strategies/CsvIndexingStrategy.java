package com.jsearch.indexer.indexing.strategies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jsearch.indexer.indexing.FileIndexer;

public class CsvIndexingStrategy extends FileIndexer {

  /*
   * Need optimization. Currently, applying regex takes a lot of time.
   */
  @Override
  public void index(File file) {
    int currentTime = (int) System.currentTimeMillis();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      String filePath = file.getPath();
      int lineNumber = 0;

      while ((line = reader.readLine()) != null) {
        lineNumber++;
        String[] cells = line.split(",");
        for (int i = 0; i < cells.length; i++) {
          String[] words = cells[i].split(" ");
          for (int j = 0; j < words.length; j++) {
            String word = words[j];

            addWordToIndex(word, filePath, lineNumber, i);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Time taken to index " + file.getName() + " is " + ((int) System.currentTimeMillis() - currentTime) + "ms");
  }
}
