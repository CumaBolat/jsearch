package com.jsearch.searcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsearch.indexer.stopwords.StopWords;
import com.jsearch.lemmatizer.Lemmatize;
import com.jsearch.searcher.searchmodels.*;
import com.jsearch.summarizer.Summarizer;

public class Searcher {
  public final String ANSI_RESET = "\u001B[0m";
  public final String ANSI_BLUE = "\u001B[34m";
  public final String ANSI_CYAN = "\u001B[36m";
  
  private List<String> lemmatizedQuery = new ArrayList<>();
  private List<SearchModel> searchModels = Arrays.asList(new BooleanModel(), new VectorSpaceModel());

  private Map<String, Map<Integer, List<List<Integer>>>> queryIndexes = new HashMap<>();

  private String initialQuery = "";
  
  public boolean search(String searchQuery) {
    if (!queryIsValid(searchQuery)) return false;
    this.initialQuery = searchQuery;

    searchQuery = removeStopWords(searchQuery);
    lemmatizeQuery(searchQuery);
    initializeQueryIndexMap();

    List<List<String>> searchResults = this.fetchSearchResults();

    printSearchResults(searchResults);
    printSearchAnswer(searchResults);
    return true;
  }

  private boolean queryIsValid(String searchQuery) {
    return searchQuery.matches(".*\\w.*");
  }

  private String removeStopWords(String searchQuery) {
    return StopWords.removeStopWords(searchQuery);
  }

  private void lemmatizeQuery(String searchQuery) {
    for (String word : searchQuery.split(" ")) {
      String lemmatizedWord = Lemmatize.lemmatizeString(word);

      if (lemmatizedWord == null) continue;

      this.lemmatizedQuery.add(lemmatizedWord);
    }
  }

  private void initializeQueryIndexMap() {
    QueryIndexMapInitializer queryIndexMapInitializer = new QueryIndexMapInitializer();

    this.queryIndexes = queryIndexMapInitializer.initializeQueryIndexMap(this.lemmatizedQuery);
  }

  private List<List<String>> fetchSearchResults() {
    List<List<String>> searchResults = new ArrayList<>();

    for (int i = 0; i < this.searchModels.size(); i++) {
      searchResults.add(this.searchModels.get(i).performSearch(queryIndexes, this.lemmatizedQuery));
    }

    return searchResults;
  }

  private void printSearchResults(List<List<String>> searchResults) {
    System.out.println("Search Results: ");
    
    for (int i = 0; i < searchResults.size(); i++) {
      for (int j = 0; j < searchResults.get(i).size(); j++) {
        String result = "•" + ANSI_CYAN + removeModelNameFromPath(searchResults.get(i).get(j)) + " " + ANSI_RESET;
        String model;
        try {
          model = ANSI_BLUE + searchResults.get(i).get(j).split(" ---- ")[1] + ANSI_RESET;
        } catch (ArrayIndexOutOfBoundsException e) {
          model = "";
        }
        System.out.println(result + model);
      }
    }
  }

  private void printSearchAnswer(List<List<String>> searchResults) {
    Summarizer summarizer = new Summarizer();



  }

  private String removeModelNameFromPath(String path) {
    return path.split(" ---- ")[0];
  }
}

