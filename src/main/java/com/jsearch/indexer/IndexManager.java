package com.jsearch.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexManager {

  private static IndexManager instance = null;

  /*
   * This map stores the word and the index (line position) of the word in the
   * index file
   */
  private HashMap<String, List<String>> indexMap = new HashMap<>();

  private String indexFilePath = "src/main/java/com/jsearch/indexer/index";

  private int index = 0;
  private final int MAX_BLOCK_SIZE = 1_000_000;

  private IndexManager() {
    createIndexFile();
  }

  public static IndexManager getInstance() {
    if (instance == null) {
      instance = new IndexManager();
    }
    return instance;
  }

  public void createIndexFile() {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(indexFilePath, true));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addWordToIndex(String token, String path, int lineNumber, int wordPosition) {
    String line = " [" + path + ", " + lineNumber + ", " + wordPosition + "]";

    addWordToDictionary(token, line);

    if (this.indexMap.size() == this.MAX_BLOCK_SIZE) {
      writeBlockToDisk();
      this.indexMap.clear();
      System.out.println("Wrote a block.");
    }
  }

  private void addWordToDictionary(String word, String line) {
    if (this.indexMap.containsKey(word)) {
      this.indexMap.get(word).add(line);
    } else {
      List<String> list = new ArrayList<>();
      list.add(line);
      this.indexMap.put(word, list);
    }
  }

  private void writeBlockToDisk() {
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
