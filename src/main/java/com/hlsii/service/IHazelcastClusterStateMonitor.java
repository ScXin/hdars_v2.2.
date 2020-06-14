package com.hlsii.service;


import com.hazelcast.core.HazelcastInstance;

import java.util.Set;

public interface IHazelcastClusterStateMonitor {
    /**
     * Initialize monitor, add listeners.
     *
     * @param client
     *          the {@link HazelcastInstance}
     */
    void initialize(HazelcastInstance client);

    /**
     * Get available AA (ip:port)
     * @return a set of {@link String}, or null if any exception.
     */
    Set<String> getAvailableAA();
}