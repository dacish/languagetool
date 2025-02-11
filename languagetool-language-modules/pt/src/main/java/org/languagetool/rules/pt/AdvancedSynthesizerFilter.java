/* LanguageTool, a natural language style checker 
 * Copyright (C) 2021 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package org.languagetool.rules.pt;

import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.rules.AbstractAdvancedSynthesizerFilter;
import org.languagetool.rules.RuleMatch;
import org.languagetool.synthesis.Synthesizer;
import org.languagetool.synthesis.pt.PortugueseSynthesizer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * Synthesize suggestions using the lemma from one token (lemma_from) 
 * and the POS tag from another one (postag_from).
 * 
 * The lemma_select and postag_select attributes are required 
 * to choose one among several possible readings.
 */
public class AdvancedSynthesizerFilter extends AbstractAdvancedSynthesizerFilter {

  @Override
  protected Synthesizer getSynthesizer() {
    return PortugueseSynthesizer.INSTANCE;
  }
  
  @Override
  protected boolean isSuggestionException(String token, String desiredPostag) {
    if ((desiredPostag.equals("VMIP1P0") || desiredPostag.equals("VMIP2P0"))
        && !token.endsWith("s")) {
      return true;
    }
    return false;
  }

  protected String movePronounTag(String sourceTag, String destinationTag) {
    String[] sourceTagParts = sourceTag.split(":");
    String newTag = destinationTag;
    if (sourceTagParts.length == 2) {
      String[] destinationTagParts = destinationTag.split(":");
      newTag = destinationTagParts[0] + ":" + sourceTagParts[1];
    }
    return newTag;
  }

  @Override
  public String getCompositePostag(String lemmaSelect, String postagSelect, String originalPostag,
                                   String desiredPostag, String postagReplace) {
    if (Objects.equals(postagReplace, "keepPronoun")) {
      return movePronounTag(originalPostag, desiredPostag);
    }
    return super.getCompositePostag(lemmaSelect, postagSelect, originalPostag, desiredPostag, postagReplace);
  }
}