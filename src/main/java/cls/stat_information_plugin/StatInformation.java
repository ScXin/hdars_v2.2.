/*
 * HADARS - Cosylab Hadoop-based Accelerator Data Archiver and Retrieval System
 * Copyright (c) 2018 Cosylab d.d.
 *
 * mailto:info AT cosylab DOT com
 * Gerbiceva 64, 1000 Ljubljana, Slovenia
 *
 * This software is distributed under the terms found
 * in file LICENSE-CSL-2.0.txt that is included with this distribution.
 */


package cls.stat_information_plugin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
/**
 * the statistical information
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableStatInformation.class)
public interface StatInformation {
    long count();
    double sum();
    double squareSum();
    double mean();
    double deviation();
    double rms();
    double max();
    double min();
}
