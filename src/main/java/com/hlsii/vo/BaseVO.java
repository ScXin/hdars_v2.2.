package com.hlsii.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Base Variable Object, for JSON serializing.
 *
 */
public abstract class BaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    public BaseVO() {
        super();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}