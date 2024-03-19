package com.jsearch.crawler;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import com.jsearch.indexer.Indexer;

public class FileCrawler implements FileVisitor<Path> {

  List<String> supportedFileTypes = Arrays.asList("text", "pdf", "doc", "docx", "ppt", "xls", "xlsx", "csv");

  private Indexer indexer;

  public FileCrawler() {
    indexer = new Indexer();
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
    String fileType = getFileType(path);
    if (fileType != null && supportedFileTypes.contains(fileType)) {
      indexer.index(path.toFile(), fileType);
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
    System.err.println("Failed to visit file: " + path);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  private String getFileType(Path path) {
    try {
      return Files.probeContentType(path).split("/")[0];
    } catch (Exception e) {
      return null;
    }
  }
}
