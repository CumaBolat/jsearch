package com.jsearch.crawler;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileCrawler implements FileVisitor<Path> {
  private Map<String, Set<String>> invertedIndex;

  public FileCrawler() {
    invertedIndex = new HashMap<>();
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    indexFile(file);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    System.err.println("Failed to visit file: " + file);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  private void indexFile(Path filePath) {
  }

  public void writeIndexToFile(String indexPath) {
  }
}
