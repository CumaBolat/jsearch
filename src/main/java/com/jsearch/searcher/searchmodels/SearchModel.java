package com.jsearch.searcher.searchmodels;

import java.util.List;
import java.util.Map;

public interface SearchModel {
  final String INDEX_FILE_PATH = "src/main/java/com/jsearch/indexer/index.txt";
  final String PATH_FILE = "src/main/java/com/jsearch/indexer/path.txt";

  public List<String> performSearch(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query);
  public int getWordDistance(List<Integer> firstWordPosition, List<Integer> secondWordPosition);
}
