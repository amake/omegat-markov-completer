package org.omegat.gui.editor.autocompleter;

import org.omegat.tokenizer.ITokenizer;

public abstract class AbstractAutoCompleterView {
    public AbstractAutoCompleterView(String name) {
    }
    
    public ITokenizer getTokenizer() {
        return null;
    }
}
