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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;

import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.events.IApplicationEventListener;
import org.omegat.util.Preferences;

public class MarkovInstaller {
    
    static final String MARKOV_TRANSLATOR_PREFERENCE = "allow_markov_translator";
    static final String MENU_ITEM_TITLE = "Markov Completer";

    public static void loadPlugins() {
        CoreEvents.registerApplicationEventListener(new IApplicationEventListener() {
            @Override
            public void onApplicationStartup() {
                install();
            }

            @Override
            public void onApplicationShutdown() {
            }
        });
    }

    private static void install() {
        Core.getEditor().getAutoCompleter().addView(new MarkovCompleter());
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(MENU_ITEM_TITLE);
        item.setSelected(Preferences.isPreference(MARKOV_TRANSLATOR_PREFERENCE));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences.setPreference(MARKOV_TRANSLATOR_PREFERENCE, ((AbstractButton) e.getSource()).isSelected());
            }
        });
        Core.getMainWindow().getMainMenu().getAutoCompletionMenu().add(item);
    }

    public static void unloadPlugins() {
    }

}
