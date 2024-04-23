package com.jsearch.searcher.searchmodels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BooleanModel implements SearchModel {

  private List<Integer> docIdsContainingSearchQuery = new ArrayList<>();
  private List<String> searchQuery;

  /*
   * Visual representation of the queryIndexes map:
   *     {
   *     "word1": {
   *                0: [[1, 2, 3], [4, 5, 6]],
   *                1: [[1, 2, 3], [4, 5, 6]]
   *    },
   *      word2": {
   *                0: [[1, 2, 3], [4, 5, 6]],
   *                1: [[1, 2, 3], [4, 5, 6]]
   *    }
   * 
   * 0 and 1 are the document IDs
   */
  private Map<String, Map<Integer, List<List<Integer>>>> queryIndexes;

  @Override
  public List<String> performSearch(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> searchQuery) {
    this.queryIndexes = queryIndexes;
    this.searchQuery = searchQuery;
    this.docIdsContainingSearchQuery = getDocIdsContainingSearchQuery();

    List<String> searchResults = new ArrayList<>();

    for (int i = 0; i < docIdsContainingSearchQuery.size(); i++) {
      int docID = docIdsContainingSearchQuery.get(i);

      if (documentContainsExactQuery(docID)) {
        searchResults.add(getDocumentNameFromDocumentId(docID));
        System.out.println("Document Name: " + getDocumentNameFromDocumentId(docID));
      }
    }

    return searchResults;
  }

  @Override
  public int getWordDistance(List<Integer> firstWordPosition, List<Integer> secondWordPosition) {
      if ((secondWordPosition.get(1) - firstWordPosition.get(1) < 2 &&
            firstWordPosition.get(0).equals(secondWordPosition.get(0))) ||
            (firstWordPosition.get(0) + 1 == secondWordPosition.get(0) && secondWordPosition.get(1) == 0)) return 1;

      return -1;
  }


  private List<Integer> getDocIdsContainingSearchQuery() {
    List<Integer> commonIDs = new ArrayList<>();

    for (int i = 0; i < searchQuery.size(); i++) {
      if (!queryIndexes.containsKey(searchQuery.get(i))) return new ArrayList<>();

      if (i == 0) {
        commonIDs.addAll(queryIndexes.get(searchQuery.get(i)).keySet());
      } else {
        commonIDs.retainAll(queryIndexes.get(searchQuery.get(i)).keySet());
      }
    }

    return commonIDs;
  }

  private boolean documentContainsExactQuery(int docID) {
    List<List<Integer>> lastIndexesSet = new ArrayList<>();
    
    for (int i = 0; i < searchQuery.size(); i++) {
      if (i == 0) {
        lastIndexesSet.addAll(queryIndexes.get(searchQuery.get(i)).get(docID));
      } else {
        List<List<Integer>> currentIndexesSet = new ArrayList<>(queryIndexes.get(searchQuery.get(i)).get(docID));
        List<List<Integer>> newIndexesSet = new ArrayList<>();

        for (List<Integer> lastIndex : lastIndexesSet) {
          for (List<Integer> currentIndex : currentIndexesSet) {
            if (getWordDistance(lastIndex, currentIndex) == 1) {
              newIndexesSet.add(currentIndex);
            }
          }
        }

        lastIndexesSet = newIndexesSet;
      }
    }

    return lastIndexesSet.size() > 0;
  }

  private String getDocumentNameFromDocumentId(int docID) {
    int i = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (i == docID) {
          return line + " (Exact Match)";
        }
        i++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
