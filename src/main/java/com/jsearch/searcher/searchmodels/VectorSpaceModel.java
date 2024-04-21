package com.jsearch.searcher.searchmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VectorSpaceModel implements SearchModel {

  @Override
  public List<String> performSearch(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query) {
    // TODO Auto-generated method stub
    return new ArrayList<>();
  }

  @Override
  public int getWordDistance(List<Integer> firstWordPosition, List<Integer> secondWordPosition) {
    // TODO Auto-generated method stub
    return 0;
  }
  
}
