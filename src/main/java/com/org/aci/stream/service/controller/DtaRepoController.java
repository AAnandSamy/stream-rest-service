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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;


@Api(value = "data-repo", description = "Access the data from Yellow bricks")
@RestController
public class DtaRepoController {

    public static final Logger logger = LoggerFactory.getLogger(DtaRepoController.class);

    @Autowired
    private AppProperties appProps;

    @ApiOperation(value = "Get the data from LLAP as 'XML,JSON' ")
    @GetMapping(AppConstant.YB_DATA)
    public ResponseEntity<String> getDataFromHive(@RequestParam String hql, @RequestParam(required = false) String mediaType) throws Exception {
        String rs = "{}";
        Connection conn = DBUtils.getYbConnection(appProps);
        JSONArray dscAry = DBUtils.JsonRS(conn, hql);
        JSONObject dscObj = new JSONObject();
        dscObj.put("resultset", dscAry);

        HttpHeaders responseHeaders = new HttpHeaders();
        if ((mediaType == null || mediaType.isEmpty()) || mediaType.equalsIgnoreCase("xml")) {
            responseHeaders.setContentType(MediaType.APPLICATION_XML);
            rs = XML.toString(dscObj);
        } else if (!mediaType.isEmpty() && mediaType.equalsIgnoreCase("json")) {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            rs = dscObj.toString();
        }

        return ResponseEntity.ok().headers(responseHeaders).body(rs);

    }


}
