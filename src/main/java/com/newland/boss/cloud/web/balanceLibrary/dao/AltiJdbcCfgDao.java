package com.newland.boss.cloud.web.balanceLibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.newland.boss.cloud.web.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.newland.boss.cloud.web.balanceLibrary.entity.AltiJdbcCfg;

public class AltiJdbcCfgDao extends BaseDao {

    private static final Logger LOGGER = LogManager.getLogger(AltiJdbcCfgDao.class);
    private static final String SQL_EXCEPTION = "SQLException ";
    private static final String PROJECT_TYPE = AppConfig.getProperty("project.flag").toUpperCase();

    /**
     * 查询出所有的jdbc连接信息集合
     *
     * @return
     * @throws SQLException
     */
    public List<AltiJdbcCfg> loadJdbcCfgList(DruidDataSource druidDataSource) throws SQLException {

        String sql = "select * from alti_jdbc_cfg WHERE TYPE in (1,2,3) ";
        if (PROJECT_TYPE != null) {
            sql += "and PROJECT_FLAG = '" + PROJECT_TYPE + "'";
        }else{
            throw new SQLException("alti_jdbc_cfg表字段PROJECT_TYPE 为空");
        }

        List<AltiJdbcCfg> jdbcLists = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = druidDataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                AltiJdbcCfg jdbcCfg = new AltiJdbcCfg();
                jdbcCfg.setDbName(rs.getString("DB_NAME"));
                jdbcCfg.setJdbcUrl(rs.getString("JDBC_URL"));
                jdbcCfg.setJdbcUser(rs.getString("JDBC_USER"));
                jdbcCfg.setJdbcPwd(rs.getString("JDBC_PASSWORD"));
                jdbcCfg.setJdbcDesc(rs.getString("JDBC_DESC"));
                jdbcCfg.setDriverClass(rs.getString("DRIVER_CLASS"));
                jdbcLists.add(jdbcCfg);
            }
        } catch (SQLException e) {
            LOGGER.error(SQL_EXCEPTION, e);
        } finally {
            closeResource(conn, ps, rs);
        }
        return jdbcLists;
    }

}
