package com.jsearch.searcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsearch.lemmatizer.Lemmatize;
import com.jsearch.searcher.QueryIndexMapInitializer;
import com.jsearch.searcher.searchmodels.*;

public class Searcher {

  private List<String> lemmatizedQuery = new ArrayList<>();
  private List<SearchModel> searchModels = Arrays.asList(new BooleanModel(), new VectorSpaceModel());

  private Map<String, Map<Integer, List<List<Integer>>>> queryIndexes = new HashMap<>();
  
  public void search(String searchQuery) {
    lemmatizeQuery(searchQuery);
    initializeQueryIndexMap();

    List<String> searchResults = this.fetchSearchResults();
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

  private List<String> fetchSearchResults() {
    List<String> searchResults = new ArrayList<>();

    for (int i = 0; i < this.searchModels.size(); i++) {
      searchResults.addAll(this.searchModels.get(i).performSearch());
    }

    return searchResults;
  }
}

