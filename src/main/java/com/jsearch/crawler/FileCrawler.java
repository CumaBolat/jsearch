package com.jsearch.crawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.jsearch.indexer.Indexer;

public class FileCrawler implements FileVisitor<Path> {

  private static final int NUM_THREADS = 5;
  private final List<String> supportedFileTypes = Arrays.asList("txt", "pdf", "doc", "docx", "ppt", "xls", "xlsx", "csv");

  public final ExecutorService executor;
  private final Indexer indexer;

  public FileCrawler() {
    this.executor = Executors.newFixedThreadPool(NUM_THREADS);
    this.indexer = new Indexer();
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
    String fileType = getFileType(path);
    if (fileType != null && supportedFileTypes.contains(fileType)) {
      executor.execute(new IndexTask(path.toFile(), fileType));
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
    String fileName = path.getFileName().toString();
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
  }

  private class IndexTask implements Runnable {
    private final File file;
    private final String fileType;

    public IndexTask(File file, String fileType) {
      this.file = file;
      this.fileType = fileType;
    }

    @Override
    public void run() {
      indexer.index(file, fileType);
    }
  }
}
