/*
 * Copyright (C) 2015 Aaron Madlon-Kay <aaron@madlon-kay.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.madlonkay.markov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.data.IProject.DefaultTranslationsIterator;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.TMXEntry;
import org.omegat.core.events.IEntryEventListener;
import org.omegat.core.events.IProjectEventListener;
import org.omegat.gui.editor.autocompleter.AutoCompleterItem;
import org.omegat.gui.editor.autocompleter.AutoCompleterListView;
import org.omegat.tokenizer.ITokenizer;
import org.omegat.util.Token;

public class MarkovCompleter extends AutoCompleterListView {

    private static final int LEVEL = 5;
    private static final int SUGGESTIONS = 5;
    
    private static final Random RAND = new Random(); 
    
    private final Map<String, ArrayList<Integer>> data = new HashMap<String, ArrayList<Integer>>();
    private SourceTextEntry currentEntry;
    private TMXEntry currentEntryTranslation;
    
    public MarkovCompleter() {
        super("Markov Completer");
        
        CoreEvents.registerProjectChangeListener(new IProjectEventListener() {
            @Override
            public void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {
                if (eventType == PROJECT_CHANGE_TYPE.LOAD) {
                    train();
                }
            }
        });
        CoreEvents.registerEntryEventListener(new IEntryEventListener() {            
            @Override
            public void onNewFile(String activeFileName) {
            }
            @Override
            public void onEntryActivated(SourceTextEntry newEntry) {
                SourceTextEntry lastEntry = currentEntry;
                TMXEntry lastEntryTranslation = currentEntryTranslation;
                if (lastEntry != null && lastEntryTranslation != null && !lastEntryTranslation.isTranslated()) {
                    TMXEntry newTranslation = Core.getProject().getTranslationInfo(lastEntry);
                    trainString(newTranslation.translation);
                }
                currentEntry = newEntry;
                currentEntryTranslation = Core.getProject().getTranslationInfo(newEntry);
            }
        });
    }
    
    private void train() {
        data.clear();
        Core.getProject().iterateByDefaultTranslations(new DefaultTranslationsIterator() {
            @Override
            public void iterate(String source, TMXEntry trans) {
                trainString(trans.translation);
            }
        });
    }
    
    private void trainString(String text) {
        if (text == null) {
            return;
        }
        if (text.codePointCount(0, text.length()) < LEVEL + 1) {
            return;
        }
        
        for (int cp, i = text.offsetByCodePoints(0, LEVEL), len = text.length(); i <= len; i += Character.charCount(cp)) {
            if (i == len) {
                cp = '\0';
            } else {                
                cp = text.codePointAt(i);
            }
            String key = text.substring(text.offsetByCodePoints(i, -LEVEL), i);
            ArrayList<Integer> chain = data.get(key);
            if (chain == null) {
                chain = new ArrayList<Integer>();
                data.put(key, chain);
            }
            chain.add(cp);
        }
    }
    
    protected String getLastToken(String text) {
        ITokenizer tokenizer = getTokenizer();
        Token[] tokens = tokenizer.tokenizeAllExactly(text);
        
        for (int i = tokens.length - 1; i >= 0; i--) {
            Token lastToken = tokens[i];
            String lastString = text.substring(lastToken.getOffset(), text.length()).trim();
            if (lastString.codePointCount(0, lastString.length()) >= LEVEL) {
                return lastString;
            }
        }
        return null;
    }
    
    private AutoCompleterItem generate(String prevText) {
        String seed = getLastToken(prevText);
        if (seed == null) {
            seed = prevText;
        }
        if (seed.codePointCount(0, seed.length()) < LEVEL) {
            return null;
        }
        StringBuilder sb = new StringBuilder(seed);
        boolean didAppend = false;
        while (true) {
            String key = sb.substring(sb.offsetByCodePoints(sb.length(), -LEVEL), sb.length());
            List<Integer> chain = data.get(key);
            if (chain == null) {
                break;
            }
            int next = chain.get(RAND.nextInt(chain.size()));
            if (next == '\0') {
                break;
            }
            sb.appendCodePoint(next);
            didAppend = true;
        }
        return didAppend ? new AutoCompleterItem(sb.toString(), null, seed.length()) : null;
    }
    
    @Override
    public List<AutoCompleterItem> computeListData(String prevText,
            boolean contextualOnly) {
        if (prevText.codePointCount(0, prevText.length()) < LEVEL) {
            return new ArrayList<AutoCompleterItem>(1);
        }
        List<AutoCompleterItem> result = new ArrayList<AutoCompleterItem>(SUGGESTIONS);
        for (int i = 0; i < SUGGESTIONS; i++) {
            AutoCompleterItem suggestion = generate(prevText);
            if (suggestion != null && !result.contains(suggestion)) {
                result.add(suggestion);
            }
        }
        return result;
    }

    @Override
    public String itemToString(AutoCompleterItem item) {
        return item.payload;
    }

}
