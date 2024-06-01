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
import com.jsearch.indexer.dictionary.Dictionary;
import com.jsearch.indexer.stopwords.StopWords;

public class App {

  private static Path searchDirectoryPath;
  private static FileCrawler fileCrawler;

  private static Scanner scanner = new Scanner(System.in);

  private static List<String> ignoredDirectories = new ArrayList<>();

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_BLUE = "\u001B[34m";

  static {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
  }

  public static void main(String[] args) {
    printJSearchAsciiArt();
    addDefaultIgnoredDirectories();

    if (!indexFileExists()) {
      getSearchDirectoryFromUser();
      getIgnoredDirectoriesFromUser();
      initializeDictionary();
      initializeStopWords();
      startFileCrawling();
      scanner.nextLine();
    } else {
      initializeStopWords();
      System.out.println("An index file has been found.");
    }

    while (true) {
      Searcher searcher = new Searcher();

      System.out.println("Please enter the query you want to search (Press" + ANSI_RED + " q " + ANSI_RESET + "to quit): \r" + ANSI_RED);
      String searchQuery = scanner.nextLine();
      System.out.print(ANSI_RESET);

      if (searchQuery.equals("q")) {
        break;
      } else if (!searcher.search(searchQuery)) {
        System.out.println("Please enter a valid query.");
      }
    }
  }

  private static boolean indexFileExists() {
    return Files.exists(Paths.get("src/main/java/com/jsearch/indexer/index.txt"));
  }

  private static void getSearchDirectoryFromUser() {
    System.out.println("An index file has not been previously created.");
    System.out.println("Please enter the search directory path (Press a for the whole computer)\r");
    String directoryPath = scanner.next();

    if (directoryPath.equals("a")) {
      searchDirectoryPath = Paths.get("/home/cuma/personal_projects/jsearch_test");
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

  private static void initializeDictionary() {
    Dictionary dictionary = new Dictionary();
  }

  private static void initializeStopWords() {
    StopWords stopWords = new StopWords();
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
        "#                                         made by" + ANSI_BLUE + " cumabolat\n" + ANSI_RESET);
  }

  private static void addDefaultIgnoredDirectories() {
    ignoredDirectories.addAll(Arrays.asList("node_modules", "target", ".git", "rbenv", ".idea", ".rspec", ".steam",
        ".gradle", "words.txt", "cache", "logs", "build", "dist", "bin", "obj", "out", "vendor", "tmp", "temp", "examples", "samples"));
  }
}
