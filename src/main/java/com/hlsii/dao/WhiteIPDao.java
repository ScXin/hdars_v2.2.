package com.hlsii.dao;

import com.hlsii.entity.WhiteIP;
import com.hlsii.vo.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


/**
 * CREATE TABLE `white_ip` (
 * `id` VARCHAR (64) NOT NULL,
 * `ip_addr` VARCHAR (100) NOT NULL,
 * `del_flag` CHAR(1) NULL DEFAULT NULL,
 * PRIMARY KEY (`id`),
 * INDEX `ip_addr` (`ip_addr`),
 * INDEX `del_flag` (`del_flag`)
 * );
 */
@Repository
public class WhiteIPDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //The SQL has been tested

    /**
     * 得到白名单的数量
     * @param ip_addr
     * @return
     */
    public int getCount(String ip_addr) {
        String sql = "select count(*) from white_ip where ip_addr='" + ip_addr + "'";
        int k = jdbcTemplate.queryForObject(sql, Integer.class);
        return k;
    }

    //The SQL has been tested

    /**
     * 得到全部的白名单
     * @return
     */
    public List<WhiteIP> getAll() {
        String sql = "select * from white_ip";

        List<WhiteIP> whiteIPs = null;
        try {
            whiteIPs = jdbcTemplate.query(sql, new WhiteIPMapper());
        } catch (Exception ex) {

        }
        return whiteIPs;
    }

    //The SQL has been tested

    /**
     * 通过ip地址得到白名单信息
     * @param ip_addr
     * @return
     */
    public WhiteIP getWhiteByAddr(String ip_addr) {

        String sql = "select * from white_ip where ip_addr='" + ip_addr + "'";

        WhiteIP whiteIP = null;
        try {
            whiteIP = jdbcTemplate.queryForObject(sql, new WhiteIPMapper());
            return whiteIP;
        } catch (Exception ex) {

        }
        return whiteIP;
    }


    //The SQL has been tested

    /**
     * 保存白名单ip
     * @param ipAddr
     * @return
     */
    public ReturnCode save(String ipAddr) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String sql = "INSERT INTO `white_ip` (`id`, `ip_addr`)VALUES('" + id + "','" + ipAddr + "')";
        try {
            jdbcTemplate.execute(sql);
            String msg = "Save whiteIP sucessful!";
            ReturnCode returnCode = new ReturnCode(true, msg);
            return returnCode;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ReturnCode(false, "Save whiteIP failed!");
    }


    //The  SQL has been tested

    /**
     * 伤处白名单ip
     * @param ip
     * @return
     */
    public boolean delete(String ip) {
        String sql = "delete from white_ip where ip_addr='" + ip + "'";

//        System.out.println(sql);
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 修改白名单ip
     * @param
     * @param newIpAddr
     * @return
     */
    public boolean modify(String id, String newIpAddr) {


        String sql = "update white_ip set ip_addr='" + newIpAddr + "' where id='" + id + "'";
        System.out.println("sql=="+sql);
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    class WhiteIPMapper implements RowMapper<WhiteIP> {
        @Override
        public WhiteIP mapRow(ResultSet rs, int rowNum) throws SQLException {
            WhiteIP whiteIP = new WhiteIP();
            whiteIP.setId(rs.getString("id"));
            whiteIP.setIpAddr(rs.getString("ip_addr"));
            whiteIP.setDelFlag(rs.getString("del_flag"));
            return whiteIP;
        }
    }


}







