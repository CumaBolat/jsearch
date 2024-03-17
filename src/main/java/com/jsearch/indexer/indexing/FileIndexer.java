package com.jsearch.indexer.indexing;

import java.io.File;

public interface FileIndexer {
  final String INDEX_FILE_NAME = "index";
  final String INDEX_FILE_PATH = "../index";
  void index(File file); 
}
