package com.hlsii.dao;


//import com.hlsii.entity.Permission;
import com.commonuser.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class PermissionDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 通过角色id查询对应的权限
     * @param roleId
     * @return
     */
    public List<Permission> findPermissionListByRoleId(int roleId){
        String sql = "select p.id as id, p.name as name, p.url as url from role_permission rp" +
                     " left join permission p on rp.permission_id = p.id" +
                     " where rp.role_id = " + roleId;
        return jdbcTemplate.query(sql, new permissionRowMapper());
    }

    /**
     * 将数据库查询结果封装成实体类对象的方法
     */
    class permissionRowMapper implements RowMapper<Permission> {
        @Override
        public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
            Permission permission = new Permission();
            permission.setId(rs.getInt("id"));
            permission.setName(rs.getString("name"));
            permission.setUrl(rs.getString("url"));
            return permission;
        }
    }
}
