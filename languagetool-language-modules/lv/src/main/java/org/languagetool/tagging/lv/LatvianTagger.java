package org.languagetool.tagging.lv;

import lv.semti.morphology.analyzer.Analyzer;
import lv.semti.morphology.analyzer.Word;
import lv.semti.morphology.analyzer.Wordform;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.Tagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LatvianTagger implements Tagger {

  private Analyzer analyzer;

  public LatvianTagger(Analyzer analyzer) {
    this.analyzer = analyzer;
  }
  @Override
  public List<AnalyzedTokenReadings> tag(List<String> sentenceTokens) throws IOException {
    List<AnalyzedTokenReadings> tokenReadings = new ArrayList<>();
    for (String word: sentenceTokens) {
      List<AnalyzedToken> l = new ArrayList<>();
      Word w = analyzer.analyze(word);
      for (Wordform wf: w.wordforms) {
        l.add(new AnalyzedToken(word, wf.getTag(), wf.getValue("Pamatforma")));
      }
      if(w.wordforms.size() == 0) {
        l.add(new AnalyzedToken(word, null, null));
      }
//      System.out.println(word);
//      System.out.println(l.toString());
      tokenReadings.add(new AnalyzedTokenReadings(l, 0));
    }
    return tokenReadings;
  }

  @Override
  public AnalyzedTokenReadings createNullToken(String token, int startPos) {
    return new AnalyzedTokenReadings(new AnalyzedToken(token, null, null), startPos);
  }

  @Override
  public AnalyzedToken createToken(String token, String posTag) {
    return new AnalyzedToken(token, posTag, null);
  }
}
