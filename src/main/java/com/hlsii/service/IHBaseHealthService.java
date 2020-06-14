package com.hlsii.service;

/**
 * @author Shangcong Xin
 * @date 4/1/20
 */


//Interface to check HBase current status

public interface IHBaseHealthService {
    /**
     * HBase in service?
     * @return - true if HBase is in service
     */
    boolean hbaseInService();
}
