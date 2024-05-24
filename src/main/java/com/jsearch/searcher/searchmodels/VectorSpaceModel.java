package com.jsearch.searcher.searchmodels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VectorSpaceModel implements SearchModel {

  private Map<String, Map<Integer, Double>> tfIdfWeights;

  @Override
  public List<String> performSearch(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query) {
    computeTfIdfWeights(queryIndexes);

    Map<Integer, Double> documentScores = calculateCosineSimilarity(queryIndexes, query);

    List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(documentScores.entrySet());
    sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

    List<String> searchResults = new ArrayList<>();
    int i = 0;

    for (Map.Entry<Integer, Double> entry : sortedEntries) {
      searchResults.add(getDocumentNameFromDocumentId(entry.getKey()) + " ---- (Vector Space Model)");
      i++;

      if (i == 3)
        break;
    }

    if (searchResults.size() == 0) {
      searchResults.add("No relevant documents found for the search query.");
    }

    return searchResults;
  }

  private void computeTfIdfWeights(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes) {
    tfIdfWeights = new HashMap<>();

    int totalDocuments = queryIndexes.values().stream()
        .flatMap(map -> map.keySet().stream())
        .collect(Collectors.toSet()).size();

    for (String term : queryIndexes.keySet()) {
      Map<Integer, Double> termWeights = new HashMap<>();

      int documentsWithTerm = queryIndexes.get(term).size();
      double idf = Math.log((double) totalDocuments / (documentsWithTerm + 1));

      for (Map.Entry<Integer, List<List<Integer>>> entry : queryIndexes.get(term).entrySet()) {
        int docId = entry.getKey();
        double tf = calculateTf(entry.getValue().size(), docId);
        termWeights.put(docId, tf * idf);
      }

      tfIdfWeights.put(term, termWeights);
    }
  }

  private double calculateTf(int termFrequency, int docId) {
    double totalWordsInDoc = getTotalWordsInDocument(docId);
    return termFrequency / totalWordsInDoc;
  }

  private double getTotalWordsInDocument(int docId) {
    int i = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (i == docId) {
          return (double) Integer.valueOf(line.split(" => ")[1]);
        }
        i++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 1.0;
  }

  private Map<Integer, Double> calculateCosineSimilarity(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes,
      List<String> query) {
    Map<Integer, Double> documentScores = new HashMap<>();
    Map<Integer, Double> documentLengths = new HashMap<>();

    for (String term : query) {
      if (!tfIdfWeights.containsKey(term))
        continue;

      Map<Integer, Double> termWeights = tfIdfWeights.get(term);

      for (Map.Entry<Integer, Double> entry : termWeights.entrySet()) {
        int docId = entry.getKey();
        double weight = entry.getValue();

        documentScores.put(docId, documentScores.getOrDefault(docId, 0.0) + weight);
        documentLengths.put(docId, documentLengths.getOrDefault(docId, 0.0) + weight * weight);
      }
    }

    for (Map.Entry<Integer, Double> entry : documentScores.entrySet()) {
      int docId = entry.getKey();
      double score = entry.getValue();
      double length = Math.sqrt(documentLengths.get(docId));
      documentScores.put(docId, score / length);
    }

    return documentScores;
  }

  private String getDocumentNameFromDocumentId(int docID) {
    int i = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (i == docID) {
          return line.split(" => ")[0];
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
    // Placeholder method, not used in Vector Space Model
    return 0;
  }
}
