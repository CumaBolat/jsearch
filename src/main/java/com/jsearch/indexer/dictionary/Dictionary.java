package com.jsearch.indexer.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {

  private static Set<String> words = new HashSet<>();

  static {
    constructDictionary();
  }

  private static void constructDictionary(){
    System.out.println("Constructing dictionary");
    try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/jsearch/indexer/dictionary/words.txt"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        words.add(line.toLowerCase());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isWord(String word){
    return words.contains(word);
  }
}
