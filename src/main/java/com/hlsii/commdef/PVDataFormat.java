package com.hlsii.commdef;

public enum PVDataFormat {
    JSON("json"),  // It has all fields: "secs", "val", "nanos", "severity" and "status".
    QW("qw");      // It is for the quick chart, just have "millis" and "val".

    private final String pvDataFormat;

    PVDataFormat(final String pvDataFormat) {
        this.pvDataFormat = pvDataFormat;
    }

    /**
     * Returns description instead of enum name
     *
     * @see Enum#toString()
     */
    @Override
    public String toString() {
        return this.pvDataFormat;
    }
}
