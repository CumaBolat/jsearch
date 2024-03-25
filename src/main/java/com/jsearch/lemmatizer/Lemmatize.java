package com.jsearch.lemmatizer;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Lemmatize {

  private static final StanfordCoreNLP pipeline;

  static {
    Properties props;
    props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma");
    pipeline = new StanfordCoreNLP(props);
  }

  public static String lemmatizeString(String word) {
    Annotation document = new Annotation(word);
    pipeline.annotate(document);
    String lemmatizedString = document.get(TokensAnnotation.class).get(0).get(LemmaAnnotation.class);
    return lemmatizedString;
  }
}
