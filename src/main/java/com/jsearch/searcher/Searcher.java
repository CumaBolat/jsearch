package com.jsearch.searcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsearch.lemmatizer.Lemmatize;

public class Searcher {
  private final String INDEX_DIRECTORY = "src/main/java/com/jsearch/indexer/index";
  
  private String[] lemmatizedQuery;
  private Map<String, List<String>> queryIndexes = new HashMap<>();

  public void search(String searchQuery) {
    lemmatizeQuery(searchQuery);
    getQueryIndexes();
  }

  private void lemmatizeQuery(String searchQuery) {
    String lemmatizedString = Lemmatize.lemmatizeString(searchQuery);
    this.lemmatizedQuery = lemmatizedString.split(" ");

    for (String word : this.lemmatizedQuery) {
      System.out.println("Lemmatized word: " + word);
    }
  }

  private void getQueryIndexes() {
    System.out.println("inside getQueryIndexes");
    for (String word : this.lemmatizedQuery) {
      List<String> indexes = new ArrayList<>();
      try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get(INDEX_DIRECTORY).toFile()))) {
        String line;

        while ((line = reader.readLine()) != null) {
          String[] lineContents = line.split(" -> ");
          if (lineContents[0].equals(word)) {
            String[] indexesArray = lineContents[1].split("]");
            for (String index : indexesArray) {
              indexes.add(index);
              System.out.println("Word " + word + " found in " + index);
            }
          }
        }
      } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
      }
      this.queryIndexes.put(word, indexes);
    }
  }
}
