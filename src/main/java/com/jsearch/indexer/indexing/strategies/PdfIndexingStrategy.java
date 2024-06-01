package com.jsearch.indexer.indexing.strategies;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

import com.jsearch.indexer.indexing.FileIndexer;
import com.jsearch.indexer.stopwords.StopWords;

public class PdfIndexingStrategy extends FileIndexer {
  @Override
  public void index(File file) {
    int currentTime = (int) System.currentTimeMillis();
    try {
      PDDocument document = PDDocument.load(file);
      PDFTextStripper pdfStripper = new PDFTextStripper();

      String[] text = pdfStripper.getText(document).split("\n");
      document.close();

      for (int i = 0; i < text.length; i++) {
        String[] words = text[i].split(" ");
        if (words.length == 0 || words[0].equals("")) continue;
        int rowNumber = 0;
        for (int j = 0; j < words.length; j++) {
          String word = words[j];
          rowNumber++;

          if (StopWords.isStopWord(word)) {
            System.out.println("Stop word found: " + word);
            rowNumber--;
            continue;
          }

          addWordToIndex(word, file.toPath().toString(), i, rowNumber);
        }
        rowNumber = 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(
        "Time taken to index " + file.getName() + " is " + ((int) System.currentTimeMillis() - currentTime) + "ms");
  }
}
