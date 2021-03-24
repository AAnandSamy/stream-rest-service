package com.org.aci.stream.service.utils;

import com.org.aci.stream.service.constants.AppProperties;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtils {

    public static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public static Connection getYbConnection(AppProperties appProps) throws Exception {
        // load the driver
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection con = null;
        try {
            con = DriverManager.getConnection(appProps.getJdbc_url());
            logger.info("connected with hive server");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * Get Hive result set as JSON
     *
     * @param con
     * @param hql
     * @return JSON Datats
     * @throws Exception
     */
    public static JSONArray getHiveResultSet(Connection con, String hql) throws Exception {
        JSONArray jResults = new JSONArray();

        logger.info("hql : {}", hql);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(hql);
        int total_rows = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            JSONObject jResult = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                jResult.put(rs.getMetaData().getColumnLabel(i + 1), rs.getString(i + 1));
            }
            jResults.put(jResult);
        }
        //logger.info("jResults : {}",jResults.toString());

        return jResults;
    }


}
