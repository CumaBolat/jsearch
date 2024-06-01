package com.jsearch.indexer.indexing.strategies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jsearch.indexer.indexing.FileIndexer;
import com.jsearch.indexer.stopwords.StopWords;

public class TextIndexingStrategy extends FileIndexer {
  @Override
  public void index(File file) {
    int currentTime = (int) System.currentTimeMillis();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      String filePath = file.getPath();
      int lineNumber = 0;
      int rowNumber = 0;

      while ((line = reader.readLine()) != null) {
        if (line.equals("")) continue;

        lineNumber++;
        String[] words = line.split(" ");
        for (int i = 0; i < words.length; i++) {
          String word = words[i];
          rowNumber++;

          if (StopWords.isStopWord(word)) {
            rowNumber--;
            System.out.println("Stop word found: " + word);
            continue;
          }
          addWordToIndex(word, filePath, lineNumber, rowNumber);
        }
        rowNumber = 0;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(
        "Time taken to index " + file.getName() + " is " + ((int) System.currentTimeMillis() - currentTime) + "ms");
  }
}
