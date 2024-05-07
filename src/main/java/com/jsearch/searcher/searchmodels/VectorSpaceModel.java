package com.jsearch.searcher.searchmodels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class VectorSpaceModel implements SearchModel {

  private Map<String, Map<Integer, Double>> tfIdfWeights;

  @Override
  public List<String> performSearch(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query) {
    computeTfIdfWeights(queryIndexes, query);

    Map<Integer, Double> documentScores = calculateCosineSimilarity(queryIndexes, query);

    List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(documentScores.entrySet());
    sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

    List<String> searchResults = new ArrayList<>();
    int i = 0;

    for (Map.Entry<Integer, Double> entry : sortedEntries) {
      searchResults.add(getDocumentNameFromDocumentId(entry.getKey()) + " ---- (Vector Space Model)");
      i++;

      if (i == 3) break;
    }

    if (searchResults.size() == 0) {
      searchResults.add("No relevant documents found for the search query.");
    }

    return searchResults;
  }

  private void computeTfIdfWeights(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query) {
    tfIdfWeights = new HashMap<>();

    for (String term : query) {
      if (!queryIndexes.containsKey(term)) continue;

      Map<Integer, Double> termWeights = new HashMap<>();

      int totalDocuments = queryIndexes.size();
      double idf = Math.log((double) totalDocuments / queryIndexes.get(term).size());

      for (Map.Entry<Integer, List<List<Integer>>> entry : queryIndexes.get(term).entrySet()) {
        int docId = entry.getKey();
        double tf = entry.getValue().size();
        termWeights.put(docId, tf * idf);
      }

      tfIdfWeights.put(term, termWeights);
    }
  }

  private Map<Integer, Double> calculateCosineSimilarity(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query) {
    Map<Integer, Double> documentScores = new HashMap<>();

    int totalTermsInCollection = 0;

    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(INDEX_FILE_PATH));
      while (reader.readLine() != null)
        totalTermsInCollection++;
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (String term : query) {
      if (!tfIdfWeights.containsKey(term)) continue;

      Map<Integer, Double> termWeights = tfIdfWeights.get(term);
      for (Map.Entry<Integer, Double> entry : termWeights.entrySet()) {
        int docId = entry.getKey();
        double docScore = documentScores.getOrDefault(docId, 0.0) + entry.getValue();
        documentScores.put(docId, docScore);
      }
    }

    for (Map.Entry<Integer, Double> entry : documentScores.entrySet()) {
      int docId = entry.getKey();
      double docLength = Math.sqrt(calculateDocumentLength(docId, queryIndexes, totalTermsInCollection));
      
      entry.setValue(entry.getValue() / docLength);
    }

    return documentScores;
  }

  private double calculateDocumentLength(int docId, Map<String, Map<Integer, List<List<Integer>>>> queryIndexes,
      int totalTermsInCollection) {
    double docLength = 0.0;

    for (String term : queryIndexes.keySet()) {
      if (queryIndexes.get(term).containsKey(docId)) {
        double termFrequency = queryIndexes.get(term).get(docId).size();
        double idf = Math.log((double) totalTermsInCollection / queryIndexes.get(term).size());
        docLength += Math.pow(termFrequency * idf, 2);
      }
    }

    return docLength;
  }

  private String getDocumentNameFromDocumentId(int docID) {
    int i = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (i == docID) {
          return line;
        }
        i++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public int getWordDistance(List<Integer> firstWordPosition, List<Integer> secondWordPosition) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getWordDistance'");
  }
}
