package org.omegat.core.events;

public interface IProjectEventListener {
    enum PROJECT_CHANGE_TYPE {
        LOAD
    }
    
    public void onProjectChanged(PROJECT_CHANGE_TYPE eventType);
}
