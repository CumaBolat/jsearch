package com.jsearch.indexer.indexing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import com.jsearch.indexer.IndexManager;
import com.jsearch.indexer.dictionary.Dictionary;

import com.jsearch.lemmatizer.Lemmatize;

public abstract class FileIndexer {
  private IndexManager indexManager = IndexManager.getInstance();
  Pattern removeSpecialCharactersPattern = Pattern.compile("[^a-zA-Z0-9]");

  private static Dictionary dictionary = new Dictionary();

  public abstract void index(File file);

  protected void addWordToIndex(String word, String path, int lineNumber, int wordPosition) {
    word = removeSpecialCharactersPattern.matcher(word).replaceAll("");
    if (word.length() < 2 || word.equals("\n") || !dictionary.isWord(word))
      return;
    word = Lemmatize.lemmatizeString(word.toLowerCase());
    indexManager.addWordToIndex(word, path, lineNumber, wordPosition);
  }
}
