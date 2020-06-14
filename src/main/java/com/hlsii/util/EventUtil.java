package com.hlsii.util;


import org.epics.archiverappliance.config.ArchDBRTypes;

public class EventUtil {
    public static int getDbrTypeSize(ArchDBRTypes type) {
        switch (type){
            case DBR_SCALAR_STRING:
            case DBR_WAVEFORM_STRING:
                return 128;
            case DBR_SCALAR_SHORT:
            case DBR_WAVEFORM_SHORT:
                Short s = Short.MAX_VALUE;
                return s.toString().length();
            case DBR_SCALAR_FLOAT:
            case DBR_WAVEFORM_FLOAT:
                Float f = Float.MAX_VALUE;
                return f.toString().length();
            case DBR_SCALAR_ENUM:
            case DBR_WAVEFORM_ENUM:
            case DBR_SCALAR_INT:
            case DBR_WAVEFORM_INT:
                Integer i = Integer.MAX_VALUE;
                return i.toString().length();
            case DBR_SCALAR_BYTE:
            case DBR_WAVEFORM_BYTE:
            case DBR_V4_GENERIC_BYTES:
                Byte b = Byte.MAX_VALUE;
                return b.toString().length();
            case DBR_SCALAR_DOUBLE:
            case DBR_WAVEFORM_DOUBLE:
                Double d = Double.MAX_VALUE;
                return d.toString().length();
            default:
                return 0;
        }
    }
}