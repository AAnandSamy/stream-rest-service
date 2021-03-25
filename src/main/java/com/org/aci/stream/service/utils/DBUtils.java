package com.org.aci.stream.service.utils;

import com.org.aci.stream.service.constants.AppProperties;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtils {

    public static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public static Connection getYbConnection(AppProperties appProps) throws SQLException {
        return DriverManager.getConnection(appProps.getJdbc_url(),appProps.getJdbc_usr(),appProps.getJdbc_pwd());
    }

    /**
     * Get Hive result set as JSON
     *
     * @param con
     * @param sql
     * @return JSON Datats
     * @throws Exception
     */
    public static JSONArray JsonRS(Connection con, String sql) throws Exception {
        JSONArray jResults = new JSONArray();
        logger.info("sql : {}", sql);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        int total_rows = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            JSONObject jResult = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                jResult.put(rs.getMetaData().getColumnLabel(i + 1), rs.getString(i + 1));
            }
            jResults.put(jResult);
        }
        return jResults;
    }


}
