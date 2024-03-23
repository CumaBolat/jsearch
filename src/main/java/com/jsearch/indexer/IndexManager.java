package com.jsearch.indexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IndexManager {

  private final ConcurrentHashMap<String, List<String>> indexMap = new ConcurrentHashMap<>();

  private final String indexFilePath = "src/main/java/com/jsearch/indexer/index";
  private final int MAX_BLOCK_SIZE = 10_000;
  
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
    }, 10, 1, TimeUnit.SECONDS);
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

    String line = " [" + path + ", " + lineNumber + ", " + wordPosition + "]";
    addWordToBlock(word, line);

    if (this.indexMap.size() == this.MAX_BLOCK_SIZE) {
      writeBlockToDisk();
      this.indexMap.clear();
    }
  }

  private synchronized void addWordToBlock(String word, String line) {
    if (this.indexMap.containsKey(word)) {
      this.indexMap.get(word).add(line);
    } else {
      List<String> list = new ArrayList<>();
      list.add(line);
      this.indexMap.put(word, list);
    }
  }

  private synchronized void writeBlockToDisk() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(indexFilePath, true));

      for (String token : this.indexMap.keySet()) {
        writer.write(token + " -> ");
        for (int i = 0; i < this.indexMap.get(token).size(); i++) {
          writer.write(this.indexMap.get(token).get(i) + ", ");
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
