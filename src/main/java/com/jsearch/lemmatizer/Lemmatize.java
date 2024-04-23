package com.jsearch.lemmatizer;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.logging.RedwoodConfiguration;

public class Lemmatize {

  private static final StanfordCoreNLP pipeline;
  private static final Pattern removeSpecialCharactersPattern = Pattern.compile("[^a-zA-Z0-9]");


  static {
    RedwoodConfiguration.current().clear().apply();
    Properties props;
    props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma");
    pipeline = new StanfordCoreNLP(props);
  }

  public static String lemmatizeString(String word) {
    word = removeSpecialCharactersPattern.matcher(word.toLowerCase()).replaceAll("");
    if (isTextEmpty(word)) return null;

    Annotation document = new Annotation(word);
    pipeline.annotate(document);
    
    String lemmatizedString = document.get(TokensAnnotation.class).get(0).get(LemmaAnnotation.class);
    return lemmatizedString;
  }

  public static List<String> lemmatizeSentence(String text) {
    if (isTextEmpty(text)) return null;

    Sentence sentence = new Sentence(text);
    return sentence.lemmas();
  }

  private static boolean isTextEmpty(String word) {
    return (word == null || word.isEmpty());
  }
}
