package com.jsearch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import com.jsearch.crawler.FileCrawler;

import java.util.Scanner;

public class App {

  private static Path searchDirectoryPath;

  public static void main(String[] args) {
    printJSearchAsciiArt();
    Scanner scanner = new Scanner(System.in);
    FileCrawler fileCrawler = new FileCrawler();

    while (true) {
      System.out.println("Please enter the query you want to search (Press q to quit): \r");
      String searchQuery = scanner.next();
      if (searchQuery.equals("q")) break;

      System.out.println("Please enter the search directory path (Press a for the whole computer)\r");
      String directoryPath = scanner.next();

      if (directoryPath.equals("a")) {
        searchDirectoryPath = Paths.get(System.getProperty("user.home"));
      } else {
        searchDirectoryPath = Paths.get(directoryPath);
      }

      try {
        Files.walkFileTree(searchDirectoryPath, fileCrawler);
      } catch (IOException e) {
        System.err.println("Error while walking the file tree: " + e.getMessage());
      }
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
}
