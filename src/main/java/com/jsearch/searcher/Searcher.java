package com.jsearch.searcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsearch.lemmatizer.Lemmatize;
import com.jsearch.searcher.searchmodels.*;

public class Searcher {

  private List<String> lemmatizedQuery = new ArrayList<>();
  private List<SearchModel> searchModels = Arrays.asList(new BooleanModel(), new VectorSpaceModel());

  private Map<String, Map<Integer, List<List<Integer>>>> queryIndexes = new HashMap<>();
  
  public boolean search(String searchQuery) {
    if (!queryIsValid(searchQuery)) return false;

    lemmatizeQuery(searchQuery);
    initializeQueryIndexMap();

    List<String> searchResults = this.fetchSearchResults();
    return true;
  }

  private boolean queryIsValid(String searchQuery) {
    return searchQuery.matches(".*\\w.*");
  }

  private void lemmatizeQuery(String searchQuery) {
    for (String word : searchQuery.split(" ")) {
      String lemmatizedWord = Lemmatize.lemmatizeString(word);

      if (lemmatizedWord == null) continue;

      this.lemmatizedQuery.add(lemmatizedWord);
    }

    System.out.println(this.lemmatizedQuery);
  }

  private void initializeQueryIndexMap() {
    QueryIndexMapInitializer queryIndexMapInitializer = new QueryIndexMapInitializer();

    this.queryIndexes = queryIndexMapInitializer.initializeQueryIndexMap(this.lemmatizedQuery);
  }

  private List<String> fetchSearchResults() {
    List<String> searchResults = new ArrayList<>();

    for (int i = 0; i < this.searchModels.size(); i++) {
      searchResults.addAll(this.searchModels.get(i).performSearch(queryIndexes, this.lemmatizedQuery));
    }

    return searchResults;
  }
}

