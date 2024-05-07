package org.languagetool.synthesis.lv;

import lv.semti.morphology.analyzer.Analyzer;
import lv.semti.morphology.analyzer.Wordform;
import org.languagetool.AnalyzedToken;
import org.languagetool.synthesis.Synthesizer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatvianSynthesizer implements Synthesizer {

  private Analyzer analyzer;


  public LatvianSynthesizer(Analyzer analyzer) {
      this.analyzer = analyzer;
  }

  @Override
  public String[] synthesize(AnalyzedToken token, String posTag) throws IOException {
    return synthesize(token, posTag, false);
  }

  private boolean matches(String generatedTag, String postTag, boolean postTagRegExp) {
      if(postTagRegExp == false) {
        return generatedTag.equals(postTag);
      }
      Pattern pattern = Pattern.compile(postTag);
      Matcher matcher = pattern.matcher(generatedTag);
      return matcher.matches();
  }

  @Override
  public String[] synthesize(AnalyzedToken token, String posTag, boolean posTagRegExp) throws IOException {
//    System.out.println("----");
    List<String> words = new LinkedList<>();
    String lemma = token.getLemma();
    if(lemma == null) lemma = token.getToken();
    if(lemma != null) {
//      System.out.println(lemma);
//      System.out.println(posTag);
//      System.out.println(token);
//      System.out.println(posTagRegExp);
      List<Wordform> wfs = analyzer.generateInflections(lemma);
      for(Wordform wf: wfs) {
        String word = wf.getToken();
//        System.out.println("Word:" + word);
        boolean match = matches(wf.getTag(), posTag, posTagRegExp);
//        System.out.println("Match:" + match);
        if(match) {
          words.add(word);
        }
//        words.add(wf.getToken());
      }
    }
//    System.out.println("----");
    return words.toArray(new String[0]);
  }

  @Override
  public String getPosTagCorrection(String posTag) {
    // Do some fixin
    return posTag;
  }

  @Override
  public String getSpelledNumber(String arabicNumeral) {
    // TODO currently lazy
    return arabicNumeral;
  }

  @Override
  public String getTargetPosTag(List<String> posTags, String targetPosTag) {
    // TODO what does this do, I simply copied from BaseSynthesizer
    if (posTags.isEmpty()) {
      return targetPosTag;
    }
    // return the last one to keep the previous results
    return posTags.get(posTags.size() - 1);
  }
}
