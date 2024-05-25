package com.jsearch.searcher.searchmodels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class VectorSpaceModel implements SearchModel {

  private Map<String, Map<Integer, Double>> tfIdfWeights;

  @Override
  public List<String> performSearch(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query) {
    Map<String, Double> keywordBoosts = getKeywordBoosts(query, queryIndexes);
    computeTfIdfWeights(queryIndexes, query, keywordBoosts);

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

  private void computeTfIdfWeights(Map<String, Map<Integer, List<List<Integer>>>> queryIndexes, List<String> query,
      Map<String, Double> keywordBoosts) {
    tfIdfWeights = new HashMap<>();

    int totalDocuments = getTotalNumberOfDocuments();

    for (String term : query) {
      if (!queryIndexes.containsKey(term))
        continue;

      Map<Integer, Double> termWeights = new HashMap<>();

      int documentsWithTerm = queryIndexes.get(term).size();
      double idf = Math.log((double) totalDocuments / (documentsWithTerm + 1));

      double boost = keywordBoosts.getOrDefault(term, 1.0);
      for (Map.Entry<Integer, List<List<Integer>>> entry : queryIndexes.get(term).entrySet()) {
        int docId = entry.getKey();
        double tf = calculateTf(entry.getValue().size(), getTotalWordsInDocument(docId));
        termWeights.put(docId, tf * idf * boost);
      }

      tfIdfWeights.put(term, termWeights);
    }
  }

  private int getTotalNumberOfDocuments() {
    try (BufferedReader br = new BufferedReader(new FileReader(INDEX_FILE_PATH))) {
      return (int) br.lines().count();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return 0;
  }

  private double calculateTf(int termFrequency, double totalWordsInDocument) {
    return termFrequency > 0 ? (1 + Math.log(termFrequency)) / totalWordsInDocument : 0;
  }

  private double getTotalWordsInDocument(int docId) {
    int i = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (i == docId) {
          return Double.parseDouble(line.split(" => ")[1]);
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

  private Map<String, Double> getKeywordBoosts(List<String> query,
      Map<String, Map<Integer, List<List<Integer>>>> queryIndexes) {
    Map<String, Double> keywordBoosts = new HashMap<>();
    Map<String, Integer> keywordFoundInDifferentFilesCount = new HashMap<>();
    Map<String, Integer> totalOccurrenceOfWord = new HashMap<>();

    for (String term : query) {
      if (queryIndexes.containsKey(term)) {
        keywordFoundInDifferentFilesCount.put(term, queryIndexes.get(term).size());
      }
    }

    for (String term : query) {
      if (queryIndexes.containsKey(term)) {
        int totalOccurrence = 0;
        for (Map.Entry<Integer, List<List<Integer>>> entry : queryIndexes.get(term).entrySet()) {
          totalOccurrence += entry.getValue().size();
        }
        totalOccurrenceOfWord.put(term, totalOccurrence);
      }
    }

    for (String term : query) {
      int docCount = keywordFoundInDifferentFilesCount.getOrDefault(term, 0);
      int totalOccurrence = totalOccurrenceOfWord.getOrDefault(term, 0);

      double boost = 1.0;
      if (docCount > 0 && totalOccurrence > 0) {
        boost = Math.log((double) totalOccurrence / docCount) + 1;
      }
      keywordBoosts.put(term, boost);
    }

    return keywordBoosts;
  }

  @Override
  public int getWordDistance(List<Integer> firstWordPosition, List<Integer> secondWordPosition) {
    // Placeholder method, not used in Vector Space Model
    return 0;
  }
}
