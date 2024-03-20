package com.jsearch.indexer.indexing;

import java.io.File;
import com.jsearch.indexer.IndexManager;

public abstract class FileIndexer {
  public IndexManager indexManager = IndexManager.getInstance();
  public String removeSpecialCharacterRegex = "[^a-z0-9]";

  public abstract void index(File file);
}
