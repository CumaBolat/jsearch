package com.jsearch.indexer;

import com.jsearch.indexer.indexing.*;
import com.jsearch.indexer.indexing.strategies.*;

public class IndexStrategyFactory {
  public FileIndexer getIndexStrategy(String fileType) {
    switch (fileType) {
      case "txt":
        return new TextIndexingStrategy();
      case "pdf":
        return new PdfIndexingStrategy();
      case "doc":
        return new DocIndexingStrategy();
      case "docx":
        return new DocxIndexingStrategy();
      case "ppt":
        return new PptIndexingStrategy();
      case "xls":
        return new XlsIndexingStrategy();
      case "xlsx":
        return new XlsxIndexingStrategy();
      case "csv":
        return new CsvIndexingStrategy();
      default:
        throw new IllegalArgumentException("Unknown file type: " + fileType);
    }
  }
}
