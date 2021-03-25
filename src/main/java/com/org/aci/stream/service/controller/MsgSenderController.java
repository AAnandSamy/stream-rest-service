package com.org.aci.stream.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.org.aci.stream.service.constants.AppConstant;
import com.org.aci.stream.service.constants.AppProperties;
import com.org.aci.stream.service.dta.repo.ETLValCriteriaRepo;
import com.org.aci.stream.service.dta.repo.ETLValResultSetRepo;
import com.org.aci.stream.service.model.ReqModel;
import com.org.aci.stream.service.model.Response;
import com.org.aci.stream.service.utils.KafkaConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Sender", description = "Send message to kafka")
@RestController
public class MsgSenderController {

    public static final Logger logger = LoggerFactory.getLogger(MsgSenderController.class);

    @Autowired
    private AppProperties appProps;

    @Autowired
    ETLValResultSetRepo etlRsRepo;

    @Autowired
    ETLValCriteriaRepo etlCriRepo;

    @ApiOperation(value = "get the message criteria's ", response = ReqModel.class)
    @GetMapping(AppConstant.SENDER + "/criteria")
    public ReqModel getSamplePayload() throws Exception {
        return new ReqModel();
    }

    @ApiOperation(value = "get the sample message", response = JsonNode.class)
    @GetMapping(AppConstant.SENDER)
    public JsonNode getMsgSample() throws Exception {
        return KafkaConfigUtils.getMapper().readTree(AppConstant.redi_txn);
    }

    @ApiOperation(value = "send message with different criteria ", response = JsonNode.class)
    @PostMapping(AppConstant.SENDER)
    public Response postMsg(@RequestBody JsonNode req) throws Exception {
        return KafkaConfigUtils.msgSender(appProps, req);
    }

    /**
     *  Good to see you back
     */

}
