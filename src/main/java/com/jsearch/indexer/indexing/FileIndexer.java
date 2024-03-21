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

public abstract class FileIndexer {
  private IndexManager indexManager = IndexManager.getInstance();
  Pattern removeSpecialCharactersPattern = Pattern.compile("[^a-zA-Z0-9]");

  private static final StanfordCoreNLP pipeline;

  public abstract void index(File file);

  static {
    Properties props;
    props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma");
    pipeline = new StanfordCoreNLP(props);
  }

  protected void addWordToIndex(String word, String path, int lineNumber, int wordPosition) {
    word = removeSpecialCharactersPattern.matcher(word).replaceAll("");
    if (word.length() < 2 || word.equals("\n"))
      return;
    word = lemmatizeWord(word.toLowerCase());
    indexManager.addWordToIndex(word, path, lineNumber, wordPosition);
  }

  private String lemmatizeWord(String word) {
    Annotation document = new Annotation(word);
    pipeline.annotate(document);
    String lemmatizedWord = document.get(TokensAnnotation.class).get(0).get(LemmaAnnotation.class);
    return lemmatizedWord;
  }
}
