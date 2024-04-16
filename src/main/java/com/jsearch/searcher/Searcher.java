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
import com.jsearch.searcher.QueryIndexMapInitializer;

public class Searcher {

  private List<String> lemmatizedQuery = new ArrayList<>();
  private Map<String, Map<Integer, List<List<Integer>>>> queryIndexes = new HashMap<>();

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
    for (String word : searchQuery.split(" ")) {
      this.lemmatizedQuery.add(Lemmatize.lemmatizeString(word));
    }

    System.out.print("Lemmatized Query: ");
    for (String word : this.lemmatizedQuery) {
      System.out.print(word + " ");
    }
    System.out.println();
  }

  private void initializeQueryIndexMap() {
    QueryIndexMapInitializer queryIndexMapInitializer = new QueryIndexMapInitializer();

    this.queryIndexes = queryIndexMapInitializer.initializeQueryIndexMap(this.lemmatizedQuery);
  }
}

