package org.omegat.core.events;

import org.omegat.core.data.SourceTextEntry;

public interface IEntryEventListener {
    public void onNewFile(String activeFileName);
    public void onEntryActivated(SourceTextEntry newEntry);
}
