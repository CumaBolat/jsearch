package com.jsearch.indexer;

import java.io.File;

public class Indexer {

  private IndexStrategyFactory indexStrategyFactory = new IndexStrategyFactory();

  public void index(File file, String fileType) {
    this.indexStrategyFactory.getIndexStrategy(fileType).index(file);
  }
}
