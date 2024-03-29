package com.jsearch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.jsearch.crawler.FileCrawler;
import com.jsearch.searcher.Searcher;

public class App {

  private static Path searchDirectoryPath;
  private static FileCrawler fileCrawler;
  private static Searcher searcher = new Searcher();

  private static Scanner scanner = new Scanner(System.in);

  private static List<String> ignoredDirectories = new ArrayList<>();

  public static void main(String[] args) {
    printJSearchAsciiArt();
    addDefaultIgnoredDirectories();
    
    if (!indexFileExists()) {
      getSearchDirectoryFromUser();
      getIgnoredDirectoriesFromUser();
      startFileCrawling();
    } else {
      System.out.println("An index file has been found.");
    }

    while (true) {
      System.out.println("Please enter the query you want to search (Press q to quit): \r");
      String searchQuery = scanner.next();
      if (searchQuery.equals("q")) break;
      searcher.search(searchQuery);
    }
  }

  private static boolean indexFileExists() {
    return Files.exists(Paths.get("src/main/java/com/jsearch/indexer/index"));
  }

  private static void getSearchDirectoryFromUser() {
    System.out.println("An index file has not been previously created.");
    System.out.println("Please enter the search directory path (Press a for the whole computer)\r");
    String directoryPath = scanner.next();

    if (directoryPath.equals("a")) {
      searchDirectoryPath = Paths.get("/home/cuma/bitirme");
    } else {
      searchDirectoryPath = Paths.get(directoryPath);
    }
  }

  private static void getIgnoredDirectoriesFromUser() {
    System.out.print("The default ignored directories are: ");
    ignoredDirectories.forEach(directory -> System.out.print(directory + " "));

    System.out.println("Please enter the directories you want to ignore with space inbetween (Press n to ignore none)");
    String ignoredDirectory = scanner.next();

    if (!ignoredDirectory.equals("n")) {
      String[] userEnteredList = ignoredDirectory.split(" ");
      for (String directory : userEnteredList) {
        ignoredDirectories.add(directory);
      }
    }

    fileCrawler = new FileCrawler(ignoredDirectories);
  }

  private static void startFileCrawling() {
    System.out.print("Please wait while the index file is being created...");
    System.out.println("It may take up to 10 minutes depending on the size of the directory.");

    try {
      Files.walkFileTree(searchDirectoryPath, fileCrawler);
      fileCrawler.executor.shutdown();
      fileCrawler.executor.awaitTermination(10, TimeUnit.MINUTES);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
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

  private static void addDefaultIgnoredDirectories() {
    ignoredDirectories.addAll(Arrays.asList("node_modules", "target", ".git", "rbenv", ".idea", ".rspec", ".steam", ".gradle" , "words.txt", "cache", "logs",
                                                  "build", "dist", "bin", "obj", "out", "vendor", "tmp", "temp", "examples", "samples"));
  }
}
