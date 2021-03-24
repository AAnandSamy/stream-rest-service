package com.org.aci.stream.service.controller;

import com.org.aci.stream.service.constants.AppConstant;
import com.org.aci.stream.service.constants.AppProperties;
import com.org.aci.stream.service.utils.DBUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Api(value = "data-repo", description = "Access the data from LLAP")
@RestController
public class DtaRepoController {

    public static final Logger logger = LoggerFactory.getLogger(DtaRepoController.class);
    public static final String DTA = "{\"ruledescription\":\"Chronicle-Map- challenge(soft) where prodcd = 6440 and cardtype = d and total > 5 and userdata01 < 30 and userdata03 < 2 and virtbillship is not equal to 0\",\"rulefraudyn\":\"N\",\"rulerecommend\":\"SOFT CHALLENGE\",\"rulegroupshort\":\"Other Products\"}";


    @Autowired
    private AppProperties appProps;

    @ApiOperation(value = "Get the data from LLAP as 'XML,JSON' ")
    @GetMapping(AppConstant.HIVE_DATA)
    public ResponseEntity<String> getDataFromHive(@RequestParam String hql, @RequestParam(required = false) String mediaType) throws Exception {
        String rs = "{}";
        Connection hiveCon = DBUtils.getYbConnection(appProps);
        JSONArray dscAry = DBUtils.getHiveResultSet(hiveCon, hql);
        JSONObject dscObj = new JSONObject();
        dscObj.put("result", dscAry);
        JSONObject wrap = new JSONObject();
        wrap.put("resultset", dscObj);
        logger.info("hive result: {}", wrap.toString());
        HttpHeaders responseHeaders = new HttpHeaders();
        if ((mediaType == null || mediaType.isEmpty()) || mediaType.equalsIgnoreCase("xml")) {
            responseHeaders.setContentType(MediaType.APPLICATION_XML);
            rs = XML.toString(wrap);
        } else if (!mediaType.isEmpty() && mediaType.equalsIgnoreCase("json")) {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            rs = wrap.toString();
        }

        return ResponseEntity.ok().headers(responseHeaders).body(rs);

    }


    @GetMapping("ws/data/hive/test")
    public ResponseEntity<String> hive_test(@RequestParam String hql, @RequestParam(required = false) String mediaType) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject dscObj = new JSONObject();
        Connection hiveCon = DBUtils.getYbConnection(appProps);
        Statement stmt = hiveCon.createStatement();
        LocalDateTime st = LocalDateTime.now();
        ResultSet hrs = stmt.executeQuery(hql);
        LocalDateTime et = LocalDateTime.now();
        long diffInSeconds = ChronoUnit.SECONDS.between(st, et);
        long diffInMilli = ChronoUnit.MILLIS.between(st, et);
        dscObj.put("time_taken", diffInSeconds + "." + diffInMilli);
        ResultSet qprs = stmt.executeQuery("EXPLAIN FORMATTED " + hql);
        int total_rows = qprs.getMetaData().getColumnCount();
        while (qprs.next()) {
            for (int i = 0; i < total_rows; i++) {
                dscObj.put(qprs.getMetaData().getColumnLabel(i + 1), new JSONObject(qprs.getString(i + 1)));
            }
        }

        return ResponseEntity.ok().headers(responseHeaders).body(dscObj.toString());

    }


}
