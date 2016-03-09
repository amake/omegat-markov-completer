package org.omegat.core.events;

public interface IApplicationEventListener {
    void onApplicationStartup();

    void onApplicationShutdown();
}
