package com.jsearch.searcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryIndexMapInitializer {

  private final String INDEX_DIRECTORY = "src/main/java/com/jsearch/indexer/index.txt";

  public Map<String, Map<Integer, List<List<Integer>>>> initializeQueryIndexMap(List<String> query) {
    Map<String, Map<Integer, List<List<Integer>>>> queryIndexes = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get(this.INDEX_DIRECTORY).toFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String word = getInitialWord(line);

        if (!query.contains(word)) continue;

        String[] lineParts = line.split(" => ")[1].split(", ");

        for (int i = 0; i < lineParts.length; i++) {
          String[] pathParts = this.getPathParts(lineParts[i]);
          int pathIndex = Integer.valueOf(pathParts[0].replaceAll(",", ""));
          int rowIndex = Integer.valueOf(pathParts[1].replaceAll(",", ""));
          int colIndex = Integer.valueOf(pathParts[2].replaceAll(",", ""));

          List<Integer> wordPosition = new ArrayList<>();
          wordPosition.add(rowIndex);
          wordPosition.add(colIndex);

          if (queryIndexes.containsKey(word)) {
            if (queryIndexes.get(word).containsKey(pathIndex)) {
              queryIndexes.get(word).get(pathIndex).add(wordPosition);
            } else {
              List<List<Integer>> wordPositions = new ArrayList<>();
              wordPositions.add(wordPosition);
              queryIndexes.get(word).put(pathIndex, wordPositions);
            }
          } else {
            Map<Integer, List<List<Integer>>> pathMap = new HashMap<>();
            List<List<Integer>> wordPositions = new ArrayList<>();
            wordPositions.add(wordPosition);
            pathMap.put(pathIndex, wordPositions);
            queryIndexes.put(word, pathMap);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return queryIndexes;
  }

  private String getInitialWord(String line) {
    return line.split(" => ")[0];
  }

  private String[] getPathParts(String line) {
    return line.split(" ");
  }
}
