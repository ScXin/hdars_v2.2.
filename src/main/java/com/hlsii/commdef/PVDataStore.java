package com.hlsii.commdef;


public enum PVDataStore {
    HADARS("Hadars"),
    AA("ArchiverAppliance"),
    HADOOP("Hadoop");

    private final String pvDataStore;

    PVDataStore(final String pvDataStore) {
        this.pvDataStore = pvDataStore;
    }

    /**
     * Returns description instead of enum name
     *
     * @see Enum#toString()
     */
    @Override
    public String toString() {
        return this.pvDataStore;
    }
}
