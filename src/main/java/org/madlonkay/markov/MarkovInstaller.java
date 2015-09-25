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

import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.util.Language;

public class MarkovInstaller extends BaseTranslate {
    
    static final String MARKOV_TRANSLATOR_PREFERENCE = "allow_markov_translator";

    public MarkovInstaller() {
        Core.getEditor().getAutoCompleter().addView(new MarkovCompleter());
    }
    
    public String getName() {
        return "Markov Translator";
    }

    @Override
    protected String getPreferenceName() {
        return MARKOV_TRANSLATOR_PREFERENCE;
    }

    @Override
    protected String translate(Language sLang, Language tLang, String text)
            throws Exception {
        return null;
    }

}
