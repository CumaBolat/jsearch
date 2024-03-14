package com.jsearch;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
    printJSearchAsciiArt();
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("Please enter the query you want to search \r");
      String query = scanner.next();
      if (query.equals("q")) break;

      System.out.println("Please enter the search directory name \r");
      System.out.println("(Press a for the whole computer) \r");
      String directoryPath = scanner.next();

      if (directoryPath.equals("a")) {
        // Do something bla bla..
        System.out.println(query);
      } else {
        System.out.println(directoryPath);
        // Do something bla bla..
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
