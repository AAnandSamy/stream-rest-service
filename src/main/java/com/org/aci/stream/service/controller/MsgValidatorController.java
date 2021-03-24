package com.org.aci.stream.service.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.org.aci.stream.service.constants.AppConstant;
import com.org.aci.stream.service.constants.AppProperties;
import com.org.aci.stream.service.dta.repo.ETLValCriteriaRepo;
import com.org.aci.stream.service.dta.repo.ETLValResultSetRepo;
import com.org.aci.stream.service.model.*;
import com.org.aci.stream.service.utils.KafkaConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Api(value="validator", description="Send message to kafka and validate the ETL")
@RestController
public class MsgValidatorController {

	public static final Logger logger = LoggerFactory.getLogger(MsgValidatorController.class);

	@Autowired
	private AppProperties appProps;

	@Autowired
	ETLValResultSetRepo etlRsRepo;

	@Autowired
	ETLValCriteriaRepo etlCriRepo;

	@ApiOperation(value = "get the validation criteria's ", response = JsonNode.class)
	@GetMapping(AppConstant.VALIDATOR)
	public JsonNode getEventValPayload() throws Exception
	{
		String samp= "{\"delays\":10,\"IsSave\":true,\"msgValReq\":[{\"sql\":\"select matchcountrybillship from redi_v2.bi_trans_master_core\",\"exp_rs_XML\":\"<resultset><result><matchcountrybillship>N</matchcountrybillship></result></resultset>\",\"functionality_name\":\"Bill & ship country match\",\"msg\":{\"ShipCountry\":\"US\",\"BillCountry\":\"US\"}}]}";
		return KafkaConfigUtils.getMapper().readTree(samp);
	}

	@ApiOperation(value = "send message with validation criteria and save criteria,results ", response = JsonNode.class)
	@PostMapping(AppConstant.VALIDATOR)
	public JsonNode validateEvent2ETLh2Persist11(@RequestBody JsonNode req)
	{
		ReqValidateModels reqMdls = new ReqValidateModels();
		List<ReqValidateModel> msgReqMdls = new ArrayList<ReqValidateModel>();
		List<ETLValCriteria> ipcs = new ArrayList<>();
		ObjectNode obNd = KafkaConfigUtils.getMapper().createObjectNode();
		try {
		JsonNode msgReqs = req.at("/msgValReq");
		reqMdls.setDelays(req.get("delays").asInt());

		if(msgReqs.isArray()){
			for(JsonNode objNd : msgReqs){
				ReqValidateModel reqMdl = new ReqValidateModel();
				ETLValCriteria ipc = new ETLValCriteria();
				reqMdl.setSql(objNd.get("sql").asText());
				reqMdl.setExp_rs_XML(objNd.get("exp_rs_XML").asText());
				reqMdl.setFunctionality_name(objNd.get("functionality_name").asText());

				JSONObject uMsg = new JSONObject(objNd.get("msg").toString());

				// ETLValCriteria to H2
				ipc.setFunctionality_name(objNd.get("functionality_name").asText());
				ipc.setExp_rs_XML(objNd.get("exp_rs_XML").asText());
				ipc.setSql(objNd.get("sql").asText());
				ipc.setMsg(uMsg.toString());
				ipc.setActionDt(LocalDateTime.now());
				ipcs.add(ipc);

				// Update the inputs msg

				JSONObject srcMsg = new JSONObject(AppConstant.redi_txn);
				for (String key:uMsg.keySet())
				{
					if(srcMsg.has(key)){
						srcMsg.put(key,uMsg.get(key));
					}
				}
				CoreTransaction txn = KafkaConfigUtils.getMapper().readValue(srcMsg.toString(), CoreTransaction.class);
				reqMdl.setMsg(txn);
				msgReqMdls.add(reqMdl);
			}
		}
		reqMdls.setMsgValReq(msgReqMdls);
		Properties kprops = KafkaConfigUtils.kProducerConf(appProps);
		JsonNode valrs = KafkaConfigUtils.msgValidator(appProps,kprops,reqMdls);
		// Save input criteria and results

		if(req.at("/IsSave").asBoolean()){
			Boolean isPresent = etlCriRepo.findById(ipcs.get(0).getFunctionality_name()).isPresent();
			if(!isPresent){
				etlCriRepo.saveAll(ipcs);
				logger.info("ETL Criteria : {} has saved",ipcs.size());
			}else{
				obNd.put("message","test criteria Functionality_name : '"+ipcs.get(0).getFunctionality_name()+"' already exists, Please review existing one and pick unique name for new one");
			}
				List<ETLValResult> oprs = new ArrayList<>();

					for(JsonNode rs : valrs.at("/result_set")){
						ETLValResult opr = new ETLValResult();
						opr.setTest(rs.get("test").asText());
						opr.setFunctionality_name(valrs.get("functionality_name").asText());
						if(rs.has("field_name"))
						opr.setField_name(rs.get("field_name").asText());
						opr.setActionDt(LocalDateTime.now());
						oprs.add(opr);
					}
				etlRsRepo.saveAll(oprs);
				logger.info("ETL Result : {} has saved",oprs.size());

		}
		obNd.put("result",valrs);


		}catch(Exception e) {
			e.printStackTrace();
			obNd.put("error",e.getMessage());
		}
		return obNd;
	}

	@ApiOperation(value = "validate all the criteria and save results", response = JsonNode.class)
	@PostMapping("ws/msg/validator/run")
	public JsonNode getValidationCriteria(@RequestBody JsonNode req)
	{
		ObjectNode obNd = KafkaConfigUtils.getMapper().createObjectNode();
		try {
			obNd.put("Summary",KafkaConfigUtils.msgValidator(appProps,req,etlCriRepo,etlRsRepo));
		}catch(Exception e) {
			e.printStackTrace();
			obNd.put("error",e.getMessage());
		}

		return obNd;
	}

	/**
	 *  Good to see you back
	 */

}
