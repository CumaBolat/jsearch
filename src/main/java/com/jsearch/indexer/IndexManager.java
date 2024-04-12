package com.jsearch.indexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IndexManager {

  /*        Visual Representation of Index Map:
   *
   *             {            (lineNumber, wordPosition) }
   *             { folder1 -> (lineNumber, wordPosition) }
   *           / {            (lineNumber, wordPosition) }
   *    word      ---------------------------------------
   *           \ {            (lineNumber, wordPosition) }
   *             { folder2 -> (lineNumber, wordPosition) }
   *             {            (lineNumber, wordPosition) }
   * 
   * 
   *                        (lineNumber, wordPosition)
   *              folder3 -> (lineNumber, wordPosition)
   *            /           (lineNumber, wordPosition)
   *     word2   
   *            \            (lineNumber, wordPosition)
   *              folder4 -> (lineNumber, wordPosition)
   *                         (lineNumber, wordPosition)
   */
  private final ConcurrentHashMap<String, List<ConcurrentHashMap<String, List<List<Integer>>>>> indexMap = new ConcurrentHashMap<>();

  private final String indexFilePath = "src/main/java/com/jsearch/indexer/index";
  private final int MAX_BLOCK_SIZE = 100_000;
  
  private long lastAddWordTime = System.currentTimeMillis();

  private static IndexManager instance = null;
  private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  private IndexManager() {
    createIndexFile();
    executor.scheduleAtFixedRate(() -> {
      if (System.currentTimeMillis() - lastAddWordTime > 1000) {
        writeBlockToDisk();
        this.indexMap.clear();
        executor.shutdown();
      }
    }, 20, 3, TimeUnit.SECONDS);
  }

  public static synchronized IndexManager getInstance() {
    if (instance == null) {
      instance = new IndexManager();
    }
    return instance;
  }

  public synchronized void createIndexFile() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(indexFilePath, true));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void addWordToIndex(String word, String path, int lineNumber, int wordPosition) {
    this.lastAddWordTime = System.currentTimeMillis();

    if (this.indexMap.containsKey(word)) {
      if (documentContainsWord(this.indexMap.get(word), path)) {
        List<Integer> wordPositions = Collections.synchronizedList(new ArrayList<>());
        wordPositions.add(lineNumber);
        wordPositions.add(wordPosition);
        for (ConcurrentHashMap<String, List<List<Integer>>> pathIndex : this.indexMap.get(word)) {
          if (pathIndex.containsKey(path)) {
            pathIndex.get(path).add(wordPositions);
          }
        }
      } else {
        List<List<Integer>> pathIndexes = Collections.synchronizedList(new ArrayList<>());
        List<Integer> wordPositions = Collections.synchronizedList(new ArrayList<>());
        wordPositions.add(lineNumber);
        wordPositions.add(wordPosition);
        pathIndexes.add(wordPositions);
        ConcurrentHashMap<String, List<List<Integer>>> pathMap = new ConcurrentHashMap<>();
        pathMap.put(path, pathIndexes);
        this.indexMap.get(word).add(pathMap);
      }
    } else {
      List<Integer> wordPositions = Collections.synchronizedList(new ArrayList<>());
      wordPositions.add(lineNumber);
      wordPositions.add(wordPosition);
      List<List<Integer>> pathIndexes = Collections.synchronizedList(new ArrayList<>());
      pathIndexes.add(wordPositions);
      ConcurrentHashMap<String, List<List<Integer>>> pathMap = new ConcurrentHashMap<>();
      pathMap.put(path, pathIndexes);
      List<ConcurrentHashMap<String, List<List<Integer>>>> pathList = Collections.synchronizedList(new ArrayList<>());
      pathList.add(pathMap);
      this.indexMap.put(word, pathList);
    }

    if (this.indexMap.size() == this.MAX_BLOCK_SIZE) {
      writeBlockToDisk();
      this.indexMap.clear();
    }
  }

  private synchronized boolean documentContainsWord(List<ConcurrentHashMap<String, List<List<Integer>>>> pathIndexes, String path) {
    for (ConcurrentHashMap<String, List<List<Integer>>> pathIndex : pathIndexes) {
      if (pathIndex.containsKey(path)) return true;
    }

    return false;
  }

  private void writeBlockToDisk() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(indexFilePath, true));

      for (String token : this.indexMap.keySet()) {
        writer.write(token + " => ");
        for (int i = 0; i < this.indexMap.get(token).size(); i++) {
          for (String path : this.indexMap.get(token).get(i).keySet()) {
            writer.write("[" + path + " -> ");
            for (int j = 0; j < this.indexMap.get(token).get(i).get(path).size(); j++) {
              writer.write("[" + this.indexMap.get(token).get(i).get(path).get(j).get(0) + ",");
              writer.write(this.indexMap.get(token).get(i).get(path).get(j).get(1) + "]");
            }
            writer.write("]");
          }
        }
        writer.println();
      }

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void deleteIndexFile() {
    File file = new File(indexFilePath);
    file.delete();
  }
}
