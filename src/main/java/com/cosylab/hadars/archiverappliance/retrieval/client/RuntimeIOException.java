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

package com.cosylab.hadars.archiverappliance.retrieval.client;

import java.io.IOException;

/**
 * Iterators do not let you throw Exceptions and the like. This is a hack to get around this limitation in Java.
 */
class RuntimeIOException extends RuntimeException {
	private static final long serialVersionUID = -5720516062453402104L;

	RuntimeIOException(String msg, IOException ex) {
		super(msg, ex);
	}
}
