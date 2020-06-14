package com.hlsii.service;

import com.hlsii.dao.WhiteIPDao;
import com.hlsii.entity.WhiteIP;
import com.hlsii.util.IPUtil;
import com.hlsii.vo.ReturnCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import javafx.scene.web.WebHistory;

@Service
@Transactional(readOnly = true)
//@Transactional(readOnly = false)
public class WhiteIPService {
    /**
     * Check if the IP is a white IP
     *
     * @param ip - IP to be checked
     * @return - true when the IP in white IP list
     */

    @Autowired
    private WhiteIPDao whiteIPDao;

    public boolean isWhiteIP(String ip) {
        if (StringUtils.isNotEmpty(ip)) {
            // Check if the IP white list has the IP
            if (whiteIPDao.getCount(ip.trim()) > 0) {
                return true;
            }
            // Check the white IP pattern
            List<WhiteIP> ipPatterns = whiteIPDao.getAll();
            if (!ipPatterns.isEmpty()) {
                for (WhiteIP pattern : ipPatterns) {
                    if (IPUtil.ipInPattern(ip, pattern.getIpAddr())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    public ReturnCode save(String ipAddr) {

        return whiteIPDao.save(ipAddr);

    }

    public List<WhiteIP> getAll() {
        return whiteIPDao.getAll();
    }


    public WhiteIP getWhiteIPByAddr(String ipAddr) {
        return whiteIPDao.getWhiteByAddr(ipAddr);
    }

    /**
     * Check if the IP is already existed in DB
     *
     * @param ip - IP to be checked
     * @return - true if the IP in DB
     */
    public boolean isIPExisted(String ip) {
        return existedIP(ip) != null;
    }

    /**
     * Get existed IP by IP address
     *
     * @param ip - IP address
     * @return - existed IP in DB (null if not existed)
     */
    public WhiteIP existedIP(String ip) {
        WhiteIP whiteIP = whiteIPDao.getWhiteByAddr(ip.trim());
        return whiteIP;
    }

    @Transactional(readOnly = false)
    public boolean delete(String ip) {
        return whiteIPDao.delete(ip);
    }


    /**
     * 修改白名单Ip地址
     *
     * @param
     * @param newIpAddr
     * @return
     */
    @Transactional(readOnly =false)
    public boolean modify(String id, String newIpAddr) {
        return whiteIPDao.modify(id, newIpAddr);
    }

}
