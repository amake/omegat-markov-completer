package org.omegat.tokenizer;

import org.omegat.util.Token;

public interface ITokenizer {
    public Token[] tokenizeVerbatim(String string);
}
