package com.jsearch.indexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IndexManager {

  /*        Visual Representation of Index Map:
   *
   *             {             (lineNumber, wordPosition) }
   *             { folderID -> (lineNumber, wordPosition) }
   *           / {             (lineNumber, wordPosition) }
   *    word      ---------------------------------------
   *           \ {             (lineNumber, wordPosition) }
   *             { folderID -> (lineNumber, wordPosition) }
   *             {             (lineNumber, wordPosition) }
   */
  private ConcurrentHashMap<String, List<ConcurrentHashMap<Integer, List<List<Integer>>>>> indexMap = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, Integer> pathMap = new ConcurrentHashMap<>();
  private ConcurrentHashMap<Integer, Integer> pathWordCountMap = new ConcurrentHashMap<>();

  private final String INDEX_FILE_LOCATION = "src/main/java/com/jsearch/indexer/index.txt";
  private final String PATH_FILE_LOCATION = "src/main/java/com/jsearch/indexer/path.txt";

  private final int MAX_BLOCK_SIZE = 10_000_000;
  
  private long lastAddWordTime = System.currentTimeMillis();

  private static IndexManager instance = null;
  private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  private IndexManager() {
    createIndexFile();
    createPathFile();
    executor.scheduleAtFixedRate(() -> {
      if (System.currentTimeMillis() - lastAddWordTime > 1000) {
        writePathToDisk();
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

  private synchronized void createIndexFile() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(INDEX_FILE_LOCATION, true));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private synchronized void createPathFile() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(PATH_FILE_LOCATION, true));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void addWordToIndex(String word, String path, int lineNumber, int wordPosition) {
    this.lastAddWordTime = System.currentTimeMillis();

    int pathID = -1;

    if (this.pathMap.containsKey(path)) {
      pathID = this.pathMap.get(path);
      this.pathWordCountMap.put(pathID, this.pathWordCountMap.get(pathID) + 1);
    } else {
      pathID = this.pathMap.size();
      this.pathMap.put(path, pathID);
      this.pathWordCountMap.put(pathID, 1);
    }

    if (this.indexMap.containsKey(word)) {
      if (documentContainsWord(this.indexMap.get(word), pathID)) {
        List<Integer> wordPositions = Collections.synchronizedList(new ArrayList<>());
        wordPositions.add(lineNumber);
        wordPositions.add(wordPosition);
        for (ConcurrentHashMap<Integer, List<List<Integer>>> pathIndex : this.indexMap.get(word)) {
          if (pathIndex.containsKey(pathID)) {
            pathIndex.get(pathID).add(wordPositions);
          }
        }
      } else {
        List<List<Integer>> pathIndexes = Collections.synchronizedList(new ArrayList<>());
        List<Integer> wordPositions = Collections.synchronizedList(new ArrayList<>());
        wordPositions.add(lineNumber);
        wordPositions.add(wordPosition);
        pathIndexes.add(wordPositions);
        ConcurrentHashMap<Integer, List<List<Integer>>> pathMap = new ConcurrentHashMap<>();
        pathMap.put(pathID, pathIndexes);
        this.indexMap.get(word).add(pathMap);
      }
    } else {
      List<Integer> wordPositions = Collections.synchronizedList(new ArrayList<>());
      wordPositions.add(lineNumber);
      wordPositions.add(wordPosition);
      List<List<Integer>> pathIndexes = Collections.synchronizedList(new ArrayList<>());
      pathIndexes.add(wordPositions);
      ConcurrentHashMap<Integer, List<List<Integer>>> pathMap = new ConcurrentHashMap<>();
      pathMap.put(pathID, pathIndexes);
      List<ConcurrentHashMap<Integer, List<List<Integer>>>> pathList = Collections.synchronizedList(new ArrayList<>());
      pathList.add(pathMap);
      this.indexMap.put(word, pathList);
    }

    if (this.indexMap.size() == this.MAX_BLOCK_SIZE) {
      writeBlockToDisk();
      this.indexMap.clear();
    }
  }

  private synchronized boolean documentContainsWord(List<ConcurrentHashMap<Integer, List<List<Integer>>>> pathIndexes, int pathID) {
    for (ConcurrentHashMap<Integer, List<List<Integer>>> pathIndex : pathIndexes) {
      if (pathIndex.containsKey(pathID)) return true;
    }

    return false;
  }

  private void writeBlockToDisk() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(INDEX_FILE_LOCATION, true));

      for (String token : this.indexMap.keySet()) {
        writer.write(token + " =>");
        for (int i = 0; i < this.indexMap.get(token).size(); i++) {
          for (int pathID : this.indexMap.get(token).get(i).keySet()) {
            for (int j = 0; j < this.indexMap.get(token).get(i).get(pathID).size(); j++) {
              writer.write(" " + pathID + " ");
              writer.write(this.indexMap.get(token).get(i).get(pathID).get(j).get(0) + " ");
              writer.write(this.indexMap.get(token).get(i).get(pathID).get(j).get(1) + ",");
            }
          }
        }
        writer.println();
      }

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writePathToDisk() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(PATH_FILE_LOCATION, true));
      Object[] pathIdArray = this.pathWordCountMap.keySet().toArray();
      Arrays.sort(pathIdArray);

      for (Object pathID : pathIdArray) {
        String path = getPathFromPathID((int) pathID);
        int wordCount = this.pathWordCountMap.get(pathID);

        writer.println(path + " => " + wordCount);
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getPathFromPathID(int pathID) {
    for (String path : this.pathMap.keySet()) {
      if (this.pathMap.get(path) == pathID) return path;
    }

    return null;
  }

  private void deleteIndexFile() {
    File file = new File(INDEX_FILE_LOCATION);
    file.delete();
  }
}
