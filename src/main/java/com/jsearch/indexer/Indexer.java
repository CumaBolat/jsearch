package com.jsearch.indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Indexer {

  private IndexStrategyFactory indexStrategyFactory = new IndexStrategyFactory();

  public void index(File file, String fileType) {
    this.indexStrategyFactory.getIndexStrategy(fileType).index(file);
  }
}
