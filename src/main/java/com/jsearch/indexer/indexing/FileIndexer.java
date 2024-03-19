package com.jsearch.indexer.indexing;

import java.io.File;
import com.jsearch.indexer.IndexManager;

public interface FileIndexer {
  IndexManager indexManager = IndexManager.getInstance();
  void index(File file);
}

// index.txt:
// coffe -> example.txt, example2.txt, example3.txt, example4.txt
// cup -> example2.txt, example3.txt
// 
// map:
// coffe ->1
// cup->2
