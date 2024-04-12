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
  
  private List<String> lemmatizedQuery;
  private Map<String, List<String>> queryIndexes = new HashMap<>();


  /*
   * Initial Query: "I am a software engineer"
   * Lemmatized Query: ["I", "be", "a", "software", "engineer"]
   * 
   * 
   * 
   */
  public void search(String searchQuery) {
    lemmatizeQuery(searchQuery);
    initializeQueryIndexMap();
  }

  private void lemmatizeQuery(String searchQuery) {
    this.lemmatizedQuery = Lemmatize.lemmatizeSentence(searchQuery.toLowerCase());

    System.out.print("Lemmatized Query: ");
    for (String word : this.lemmatizedQuery) {
      System.out.print(word + " ");
    }
    System.out.println();
  }

  private void initializeQueryIndexMap() {
  }
}
