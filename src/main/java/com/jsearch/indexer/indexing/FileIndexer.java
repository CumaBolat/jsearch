package com.jsearch.indexer.indexing;

import java.io.File;
import com.jsearch.indexer.IndexManager;

public abstract class FileIndexer {
  private IndexManager indexManager = IndexManager.getInstance();
  private String removeSpecialCharacterRegex = "[^a-z0-9]";

  public abstract void index(File file);

  protected void addWordToIndex(String word, String path, int lineNumber, int wordPosition) {
    word = removeSpecialCharacters(word).toLowerCase();
    
    indexManager.addWordToIndex(word, path, lineNumber, wordPosition);
  }

  private String removeSpecialCharacters(String word) {
    return word.replaceAll(removeSpecialCharacterRegex, "");
  }
}
