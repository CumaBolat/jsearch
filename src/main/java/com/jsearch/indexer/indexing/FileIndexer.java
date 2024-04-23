package com.jsearch.indexer.indexing;

import java.io.File;

import com.jsearch.indexer.IndexManager;
import com.jsearch.lemmatizer.Lemmatize;

public abstract class FileIndexer {
  private IndexManager indexManager = IndexManager.getInstance();

  public abstract void index(File file);

  protected void addWordToIndex(String word, String path, int lineNumber, int wordPosition) {
    word = Lemmatize.lemmatizeString(word);

    if (word == null) return;

    indexManager.addWordToIndex(word, path, lineNumber, wordPosition);
  }
}
