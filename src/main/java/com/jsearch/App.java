package com.jsearch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import com.jsearch.crawler.FileCrawler;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {

  private static Path searchDirectoryPath;

  public static void main(String[] args) {
    printJSearchAsciiArt();
    System.out.println(Runtime.getRuntime().availableProcessors() + " processors are available.");

    Scanner scanner = new Scanner(System.in);
    FileCrawler fileCrawler = new FileCrawler();

    if (!isIndexFileExists()) {
      System.out.println("An index file has not been previously created.");
      System.out.println("Please enter the search directory path (Press a for the whole computer)\r");
      String directoryPath = scanner.next();
      System.out.print("Please wait while the index file is being created...");
      System.out.println("It may take a while depending on the size of the files.");

      if (directoryPath.equals("a")) {
        searchDirectoryPath = Paths.get("/home/cuma/bitirme/jsearch_test");
      } else {
        searchDirectoryPath = Paths.get(directoryPath);
      }
      try {
        Files.walkFileTree(searchDirectoryPath, fileCrawler);
        fileCrawler.executor.shutdown();
        fileCrawler.executor.awaitTermination(10, TimeUnit.MINUTES);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("An index file has been found.");
    }

    while (true) {
      System.out.println("Please enter the query you want to search (Press q to quit): \r");
      String searchQuery = scanner.next();
      if (searchQuery.equals("q")) break;      
    }
  }

  private static boolean isIndexFileExists() {
    return Files.exists(Paths.get("src/main/java/com/jsearch/indexer/index"));
  }

  private static void printJSearchAsciiArt() {
    System.out.println("# Welcome To \n" + 
                      "#                                                          \n" +
                      "#       ██╗███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗\n" +
                      "#       ██║██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║\n" +
                      "#       ██║███████╗█████╗  ███████║██████╔╝██║     ███████║\n" +
                      "#  ██   ██║╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║\n" +
                      "#  ╚█████╔╝███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║\n" +
                      "#   ╚════╝ ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝\n" +
                      "#                                                          \n" +
                      "#                                         made by cumabolat\n");
  }
}
