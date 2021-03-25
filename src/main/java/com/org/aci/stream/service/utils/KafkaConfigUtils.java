package com.org.aci.stream.service.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.org.aci.stream.service.constants.AppConstant;
import com.org.aci.stream.service.constants.AppProperties;
import com.org.aci.stream.service.dta.repo.ETLValCriteriaRepo;
import com.org.aci.stream.service.dta.repo.ETLValResultSetRepo;
import com.org.aci.stream.service.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static com.org.aci.stream.service.constants.AppConstant.MSTR_WR_FIlter;
import static com.org.aci.stream.service.constants.AppConstant.redi_txn;

public class KafkaConfigUtils {

    public static final Logger logger = LoggerFactory.getLogger(KafkaConfigUtils.class);

    public static final String SCHEMA_REGISTRY_URL = "schema.registry.url";

    /*Kafka prefix value for each property*/
    public static final String KAFKA_PREFIX = "kafka.";
    /* truststore location*/
    public static final String KEYSTORE_LOC = "ssl.keystore.location";
    /* truststore password*/
    public static final String KEYSTORE_PWD = "ssl.keystore.password";
    /* truststore location*/
    public static final String TRUSTSTORE_LOC = "ssl.truststore.location";
    /* truststore password*/
    public static final String TRUSTSTORE_PWD = "ssl.truststore.password";

    public static final String SSL_KEY_PWD = "ssl.key.password";

    /*kerberos service name*/
    public static final String KAFKA_SERVICE_NAME = "sasl.kerberos.service.name";

    /*producer topics*/
    public static final String PRODUCER_TOPICS = "kafka.producer.topics";


    public static Properties kProducerConf(AppProperties props) {
        Properties kprops = new Properties();
        kprops.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getKafka_bootstrap_servers());
        kprops.put(ProducerConfig.ACKS_CONFIG, "0");
        kprops.put(ProducerConfig.RETRIES_CONFIG, 0);
        kprops.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kprops.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kprops.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, props.getKafka_security_protocol());
        kprops.put(TRUSTSTORE_LOC, props.getKafka_ssl_truststore_location());
        kprops.put(TRUSTSTORE_PWD, props.getKafka_ssl_truststore_password());

        return kprops;
    }

    public static Response sendMsg2KafkaJSON(AppProperties appProps, Properties props, List<CoreTransaction> txns) {
        KafkaProducer producer = new KafkaProducer(props);
        List<String> roids = new ArrayList();
        Response res = new Response();
        ObjectMapper mapper = new ObjectMapper();
        String bf = DateTime.now().toString();

        try {

            if (txns != null && !txns.isEmpty()) {
                for (CoreTransaction txn : txns) {
                    ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsg(txn)));
                    roids.add(txn.Oid);
                    producer.send(record);
                }
            }
            producer.flush();
            producer.close();

            logger.info("be : " + bf + System.lineSeparator() + "af : " + DateTime.now().toString());
            res.setMessage("Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics());
            res.setOids(roids);
            logger.info("Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics());
        } catch (Exception e) {
            e.printStackTrace();
            addResponse(roids, "failed to produced messages to a topic called " + appProps.getKafka_producer_topics(), e.getMessage(), res);
            logger.info("failed to produced messages to a topic called " + appProps.getKafka_producer_topics());
            producer.close();

        }
        return res;

    }

    public static Response sendSysMsg2Kafka(AppProperties appProps, Properties props, ReqModel req) {
        KafkaProducer producer = new KafkaProducer(props);
        List<String> roids = new ArrayList();
        Response res = new Response();
        String bf = DateTime.now().toString();

        try {
            CoreTransaction txn = KafkaConfigUtils.getMapper().readValue(redi_txn, CoreTransaction.class);
            if (req == null) {
                ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsg(txn)));
                roids.add(txn.Oid);
                producer.send(record);
            } else if ((req != null && req.getNumberOfMsg() > 0)) {
                logger.info("Request model : " + req.toString());
                int i = 0;
                while (i < req.getNumberOfMsg()) {
                    if (req.getClientId() != null || !req.getClientId().isEmpty())
                        txn.ClientId = req.getClientId();

                    if (req.getClientId() == null) {
                    } else if (req.getClientId() != null || !req.getClientId().isEmpty())
                        txn.ClientId = req.getClientId();

                    if (req.getCurrCds() == null) {
                    } else if (req.getCurrCds() != null || !req.getCurrCds().isEmpty())
                        txn.CurrCd = req.getCurrCds().get(new Random().nextInt(req.getCurrCds().size()));

                    if (req.getSubClientIds() == null) {
                    } else if (req.getSubClientIds() != null || !req.getSubClientIds().isEmpty())
                        txn.SubclientId = req.getSubClientIds().get(new Random().nextInt(req.getSubClientIds().size()));

                    if (req.getRecommendations() == null) {
                    } else if (req.getRecommendations() != null || !req.getRecommendations().isEmpty())
                        txn.Recommendation = req.getRecommendations().get(new Random().nextInt(req.getRecommendations().size()));

                    if (req.getOidDate() == null) {
                        ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsg(txn)));
                        producer.send(record);
                    } else if (req.getOidDate() != null || !req.getOidDate().isEmpty()) {
                        ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsgWitDat(txn, req)));
                        producer.send(record);
                    } else {

                    }
                    roids.add(txn.Oid);
                    i++;
                }
            } else {
                addResponse(null, "failed to produced messages to a topic called " + appProps.getKafka_producer_topics(), "validate input request " + req.toString(), res);
                logger.info("failed to produced messages to a topic called " + appProps.getKafka_producer_topics());
                producer.close();
            }
            logger.info("be : " + bf + System.lineSeparator() + "af : " + DateTime.now().toString());
            logger.info("Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics() + ", Response have only top 100 oids");
            addResponse(roids, "Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics() + ", Response have only top 100 oids", "", res);


        } catch (Exception e) {
            e.printStackTrace();
            addResponse(roids, "failed to produced messages to a topic called " + appProps.getKafka_producer_topics(), e.getMessage(), res);
            logger.info("failed to produced messages to a topic called " + appProps.getKafka_producer_topics());
            producer.close();

        }
        return res;

    }

    public static Response sendSysMsg2KafkaWithDate(AppProperties appProps, Properties props, ReqModel req) {
        KafkaProducer producer = new KafkaProducer(props);
        List<String> roids = new ArrayList();
        Response res = new Response();
        String bf = DateTime.now().toString();
        logger.info("Request model : " + req.toString());
        try {
            CoreTransaction txn = KafkaConfigUtils.getMapper().readValue(redi_txn, CoreTransaction.class);

            // Multiple client & sub clients and criteria

            if ((req != null && req.getNumberOfMsg() > 0 && (req.getOidDate() == null || req.getOidDate().isEmpty()) && (req.getRecommendations() == null || req.getRecommendations().isEmpty())) && (req.getClientId() != null || !req.getClientId().isEmpty()) && (req.getCurrCds() != null || !req.getCurrCds().isEmpty()) && (req.getSubClientIds() != null || !req.getSubClientIds().isEmpty())) {
                int i = 0;
                while (i < req.getNumberOfMsg()) {
                    txn.ClientId = req.getClientId();
                    txn.CurrCd = req.getCurrCds().get(new Random().nextInt(req.getCurrCds().size()));
                    txn.SubclientId = req.getSubClientIds().get(new Random().nextInt(req.getSubClientIds().size()));
                    txn.Recommendation = req.getRecommendations().get(new Random().nextInt(req.getRecommendations().size()));
                    ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsg(txn)));
                    producer.send(record);
                    roids.add(txn.Oid);
                    i++;
                }
            } else if ((req != null && req.getNumberOfMsg() > 0 && (!req.getOidDate().isEmpty()) && (req.getRecommendations() != null || !req.getRecommendations().isEmpty())) && (req.getClientId() != null || !req.getClientId().isEmpty()) && (req.getCurrCds() != null || !req.getCurrCds().isEmpty()) && (req.getSubClientIds() != null || !req.getSubClientIds().isEmpty())) {
                int i = 0;
                while (i < req.getNumberOfMsg()) {
                    txn.ClientId = req.getClientId();
                    txn.CurrCd = req.getCurrCds().get(new Random().nextInt(req.getCurrCds().size()));
                    txn.SubclientId = req.getSubClientIds().get(new Random().nextInt(req.getSubClientIds().size()));
                    txn.Recommendation = req.getRecommendations().get(new Random().nextInt(req.getRecommendations().size()));
                    ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsgWitDat(txn, req)));
                    producer.send(record);
                    roids.add(txn.Oid);
                    i++;

                }
            } else {
                addResponse(null, "failed to produced messages to a topic called " + appProps.getKafka_producer_topics(), "validate input request " + req.toString(), res);
                logger.info("failed to produced messages to a topic called " + appProps.getKafka_producer_topics());
                producer.close();
            }
            logger.info("be : " + bf + System.lineSeparator() + "af : " + DateTime.now().toString());
            logger.info("Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics() + ", Response have only top 100 oids");
            addResponse(roids, "Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics() + ", Response have only top 100 oids", "", res);


        } catch (Exception e) {
            e.printStackTrace();
            addResponse(roids, "failed to produced messages to a topic called " + appProps.getKafka_producer_topics(), e.getMessage(), res);
            logger.info("failed to produced messages to a topic called " + appProps.getKafka_producer_topics());
            producer.close();

        }
        return res;

    }


    public static Response sendMsg2KafkaString(AppProperties appProps, Properties props, String msg) {
        KafkaProducer producer = new KafkaProducer(props);
        Response res = new Response();
        ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), msg);
        producer.send(record);
        producer.close();
        res.setMessage("Successfully produced messages to a topic called " + appProps.getKafka_producer_topics());
        return res;
    }

    public static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        return mapper;

    }

    public static CoreTransaction addMsg(CoreTransaction txn) {
        String clid = txn.ClientId;
        String sclid = txn.SubclientId;
        String oidDt = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmssSSS"));
        String OidNew = clid + sclid + RandomStringUtils.randomAlphabetic(3).toUpperCase() + oidDt;
        txn.Oid = OidNew;
        txn.OidDate = oidDt;
        return txn;

    }

    public static CoreTransaction addMsgWitDat(CoreTransaction txn, ReqModel req) {
        String clid = txn.ClientId;
        String sclid = txn.SubclientId;
        String coidDt = DateTime.parse(req.getOidDate()).toString(DateTimeFormat.forPattern("yyyyMMdd"));
        String oidDtTm = DateTime.now().toString(DateTimeFormat.forPattern("HHmmssSSS"));
        String oidDt = coidDt + oidDtTm;
        String OidNew = clid + sclid + RandomStringUtils.randomAlphabetic(3).toUpperCase() + oidDt;
        txn.Oid = OidNew;
        txn.OidDate = oidDt;
        return txn;

    }

    public static Response addResponse(List<String> roids, String msg, String errorMsg, Response res) {
        res.setMessage(msg);
        if (msg.contains("failed")) {
            res.setError(errorMsg);
        } else {
            if (roids.size() > 100) {
                res.setOids(roids.subList(0, 100));
            } else {
                res.setOids(roids);
            }
        }

        return res;
    }


    /**
     * This method is used to send messages to kafka and validate the ETL data flows for the given criteria and share validation results
     *
     * @param appProps
     * @param props
     * @param
     * @return
     */
    public static JsonNode msgValidator(AppProperties appProps, Properties props, ReqValidateModels reqVal) throws Exception {
        KafkaProducer producer = new KafkaProducer(props);
        String oid = "";
        JSONObject valResOb = new JSONObject();
        logger.info("ReqValidateModels : {} ", reqVal.toString());

        if (reqVal != null) {

            for (ReqValidateModel req : reqVal.getMsgValReq()) {
                CoreTransaction txn = addMsg(req.getMsg());
                ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(txn));
                producer.send(record);
                oid = txn.Oid;
                logger.info("Oid: {} ", txn.Oid);
            }
            producer.flush();
            producer.close();
            logger.info("Successfully produced messages to a topic called: {} ", appProps.getKafka_producer_topics());

            // eval msg validate criteria
            for (int i = 0; i < reqVal.getMsgValReq().size(); i++) {
                ReqValidateModel vReq = reqVal.getMsgValReq().get(i);
                valResOb.put("functionality_name", vReq.getFunctionality_name());
                JSONArray valResultAry = null;
                // prepare criteria
                String inSql = "";
                String clnId = oid.substring(0, 6);
                String clnDt = oid.substring(15, 23);
                String mSql = vReq.getSql() + MSTR_WR_FIlter;
                inSql = String.format(mSql, clnDt, clnId, oid);
                JSONObject x2j = XML.toJSONObject(vReq.getExp_rs_XML()).getJSONObject("resultset");

                Connection hiveCon = DBUtils.getYbConnection(appProps);
                // goto sleep until ETL complete
                Thread.sleep(reqVal.getDelays() * 1000);
                JSONArray dscAry = DBUtils.JsonRS(hiveCon, inSql);

                if (x2j.optJSONArray("result") != null) {
                    JSONArray srcAry = x2j.optJSONArray("result");
                    valResultAry = DataAPI.compare2Ds(srcAry, dscAry);

                } else if (x2j.optJSONObject("result") != null) {
                    JSONArray srcAry = new JSONArray();
                    srcAry.put(x2j.optJSONObject("result"));
                    valResultAry = DataAPI.compare2Ds(srcAry, dscAry);

                }
                valResOb.put("result_set", valResultAry);
                hiveCon.close();


            }
        }


        return getMapper().readTree(valResOb.toString());

    }


    /**
     * This method is used to send messages to kafka and validate the ETL data flows for the given criteria and share validation results
     *
     * @param appProps
     * @param
     * @return
     */
    public static JsonNode msgValidator(AppProperties appProps, JsonNode req, ETLValCriteriaRepo etlCriRepo, ETLValResultSetRepo etlRsRepo) throws Exception {
        Properties kprops = KafkaConfigUtils.kProducerConf(appProps);
        KafkaProducer producer = new KafkaProducer(kprops);
        ObjectNode rsNd = getMapper().createObjectNode();
        int totalCriters = 0;
        int totalTest = 0;
        int delays = 20;
        if (req.has("command")) {
            String cmd = req.get("command").asText();
            if (cmd.contains("clear")) etlRsRepo.deleteAll();
        }
        if (req.has("delays"))
            delays = req.get("delays").asInt();

        JSONObject srcMsg = new JSONObject(AppConstant.redi_txn);
        List<ETLValCriteria> etlCris = etlCriRepo.findAll();
        List<ReqValidateModel> reqMdls = new ArrayList<>();
        totalCriters = etlCris.size();
        // populate the message with criteria
        for (ETLValCriteria etlCri : etlCris) {
            ReqValidateModel reqMdl = new ReqValidateModel();
            JSONObject uMsg = new JSONObject(etlCri.getMsg());
            for (String key : uMsg.keySet()) {
                if (srcMsg.has(key)) {
                    srcMsg.put(key, uMsg.get(key));
                }
            }
            CoreTransaction txn = KafkaConfigUtils.getMapper().readValue(srcMsg.toString(), CoreTransaction.class);
            reqMdl.setMsg(txn);
            reqMdl.setFunctionality_name(etlCri.getFunctionality_name());
            reqMdl.setSql(etlCri.getSql());
            reqMdl.setExp_rs_XML(etlCri.getExp_rs_XML());
            reqMdls.add(reqMdl);

        }
        // Send message to Kafka
        for (ReqValidateModel reMdl : reqMdls) {
            CoreTransaction kTxn = addMsg(reMdl.getMsg());
            reMdl.setMsg(kTxn);
            ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(kTxn));
            producer.send(record);
        }
        producer.flush();
        producer.close();

        logger.info("{} : messages send to kafka", reqMdls.size());

        Thread.sleep(delays * 1000);
        //  Open YB connection
        Connection conn = DBUtils.getYbConnection(appProps);

        for (ReqValidateModel rsMdl : reqMdls) {
            JSONArray valResultAry = null;
            CoreTransaction rsTxn = rsMdl.getMsg();
            String oid = rsTxn.Oid;
            String inSql = "";
            String clnId = oid.substring(0, 6);
            String clnDt = oid.substring(15, 23);
            String mSql = rsMdl.getSql() + MSTR_WR_FIlter;
            inSql = String.format(mSql, clnDt, clnId, oid);
            JSONObject x2j = XML.toJSONObject(rsMdl.getExp_rs_XML()).getJSONObject("resultset");
            JSONArray dscAry = DBUtils.JsonRS(conn, inSql);


            if (x2j.optJSONArray("result") != null) {
                JSONArray srcAry = x2j.optJSONArray("result");
                valResultAry = DataAPI.compare2Ds(srcAry, dscAry);

            } else if (x2j.optJSONObject("result") != null) {
                JSONArray srcAry = new JSONArray();
                srcAry.put(x2j.optJSONObject("result"));
                valResultAry = DataAPI.compare2Ds(srcAry, dscAry);
            }


            // Save the test Results into DB

            List<ETLValResult> oprs = new ArrayList<>();
            valResultAry.forEach(it -> {
                JSONObject rsObj = (JSONObject) it;
                ETLValResult opr = new ETLValResult();
                opr.setFunctionality_name(rsMdl.getFunctionality_name());
                if (rsObj.has("field_name"))
                    opr.setField_name(rsObj.getString("field_name"));
                opr.setTest(rsObj.getString("test"));
                opr.setActionDt(LocalDateTime.now());
                oprs.add(opr);
            });

            etlRsRepo.saveAll(oprs);
            totalTest = totalTest + oprs.size();
        }
        conn.close();
        rsNd.put("totalCriteria", totalCriters);
        rsNd.put("totalTests", totalTest);
        rsNd.put("message", "All the test results are saved into 'TEST_RESULTS' ");

        return rsNd;
    }


    public static Response msgSender(AppProperties appProps, JsonNode req) {
        Properties kprops = KafkaConfigUtils.kProducerConf(appProps);
        KafkaProducer producer = new KafkaProducer(kprops);
        List<String> roids = new ArrayList();
        Response res = new Response();
        logger.info("Request JSON :{} ", req.toString());
        try {
            CoreTransaction txn = KafkaConfigUtils.getMapper().readValue(redi_txn, CoreTransaction.class);
            ReqModel reqMdl = KafkaConfigUtils.getMapper().readValue(req.toString(), ReqModel.class);

            if (req.size() == 0) {
                ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsg(txn)));
                roids.add(txn.Oid);
                producer.send(record);
            } else if (req.has("override")) {
                JSONObject uMsg = new JSONObject(req.get("override").toString());
                JSONObject srcMsg = new JSONObject(AppConstant.redi_txn);
                for (String key : uMsg.keySet()) {
                    if (srcMsg.has(key)) {
                        srcMsg.put(key, uMsg.get(key));
                    }
                }
                CoreTransaction myTxn = KafkaConfigUtils.getMapper().readValue(srcMsg.toString(), CoreTransaction.class);

                ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsg(myTxn)));
                roids.add(myTxn.Oid);
                producer.send(record);
            } else if ((reqMdl != null && reqMdl.getNumberOfMsg() > 0)) {
                int i = 0;
                while (i < reqMdl.getNumberOfMsg()) {
                    if (reqMdl.getClientId() == null) {
                    } else if (reqMdl.getClientId() != null || !reqMdl.getClientId().isEmpty())
                        txn.ClientId = reqMdl.getClientId();
                    if (reqMdl.getCurrCds() == null) {
                    } else if (reqMdl.getCurrCds() != null || !reqMdl.getCurrCds().isEmpty())
                        txn.CurrCd = reqMdl.getCurrCds().get(new Random().nextInt(reqMdl.getCurrCds().size()));

                    if (reqMdl.getSubClientIds() == null) {
                    } else if (reqMdl.getSubClientIds() != null || !reqMdl.getSubClientIds().isEmpty())
                        txn.SubclientId = reqMdl.getSubClientIds().get(new Random().nextInt(reqMdl.getSubClientIds().size()));

                    if (reqMdl.getRecommendations() == null) {
                    } else if (reqMdl.getRecommendations() != null || !reqMdl.getRecommendations().isEmpty())
                        txn.Recommendation = reqMdl.getRecommendations().get(new Random().nextInt(reqMdl.getRecommendations().size()));

                    if (reqMdl.getReasonCodes() == null) {
                    } else if (reqMdl.getRecommendations() != null || !reqMdl.getReasonCodes().isEmpty())
                        txn.ReasonCode = reqMdl.getReasonCodes().get(new Random().nextInt(reqMdl.getReasonCodes().size()));
                    if (reqMdl.getVirtCardBins() == null) {
                    } else if (reqMdl.getVirtCardBins() != null || !reqMdl.getVirtCardBins().isEmpty())
                        txn.VirtCardBin = reqMdl.getVirtCardBins().get(new Random().nextInt(reqMdl.getVirtCardBins().size()));

                    if (reqMdl.getVirtIpidCarriers() == null) {
                    } else if (reqMdl.getVirtIpidCarriers() != null || !reqMdl.getVirtIpidCarriers().isEmpty())
                        txn.VirtIpidCarrier = reqMdl.getVirtIpidCarriers().get(new Random().nextInt(reqMdl.getVirtIpidCarriers().size()));

                    if (reqMdl.getVirtCustEmailDomains() == null) {
                    } else if (reqMdl.getVirtCustEmailDomains() != null || !reqMdl.getVirtCustEmailDomains().isEmpty())
                        txn.VirtCustEmailDomain = reqMdl.getVirtCustEmailDomains().get(new Random().nextInt(reqMdl.getVirtCustEmailDomains().size()));

                    if (reqMdl.getDeviceIds() == null) {
                    } else if (reqMdl.getDeviceIds() != null || !reqMdl.getDeviceIds().isEmpty())
                        txn.DeviceId = reqMdl.getDeviceIds().get(new Random().nextInt(reqMdl.getDeviceIds().size()));

                    if (reqMdl.getCustEmails() == null) {
                    } else if (reqMdl.getCustEmails() != null || !reqMdl.getCustEmails().isEmpty())
                        txn.CustEmail = reqMdl.getCustEmails().get(new Random().nextInt(reqMdl.getCustEmails().size()));
                    if (reqMdl.getBillStates() == null) {
                    } else if

                    (reqMdl.getBillStates() != null || !reqMdl.getBillStates().isEmpty()) {
                        txn.BillState = reqMdl.getBillStates().get(new Random().nextInt(reqMdl.getBillStates().size()));
                    }
                    if (reqMdl.getBillCountries() == null) {
                    } else if (reqMdl.getBillCountries() != null || !reqMdl.getBillCountries().isEmpty())
                        txn.BillCountry = reqMdl.getBillCountries().get(new Random().nextInt(reqMdl.getBillCountries().size()));

                    if (reqMdl.getBillZipcds() == null) {
                    } else if (reqMdl.getBillZipcds() != null || !reqMdl.getBillZipcds().isEmpty())
                        txn.BillZipcd = reqMdl.getBillZipcds().get(new Random().nextInt(reqMdl.getBillZipcds().size()));

                    if (reqMdl.getShipStates() == null) {
                    } else if (reqMdl.getShipStates() != null || !reqMdl.getShipStates().isEmpty())
                        txn.ShipState = reqMdl.getShipStates().get(new Random().nextInt(reqMdl.getShipStates().size()));

                    if (reqMdl.getShipZipcds() == null) {
                    } else if (reqMdl.getShipZipcds() != null || !reqMdl.getShipZipcds().isEmpty())
                        txn.ShipZipcd = reqMdl.getShipZipcds().get(new Random().nextInt(reqMdl.getShipZipcds().size()));

                    if (reqMdl.getShipCountry() == null) {
                    } else if (reqMdl.getShipCountry() != null || !reqMdl.getShipCountry().isEmpty())
                        txn.ShipCountry = reqMdl.getShipCountry().get(new Random().nextInt(reqMdl.getShipCountry().size()));

                    // todo sequencer
                 /*   if (reqMdl.getSeqNumber() > 0) {
                        if(reqMdl.getSeqFieldName()!=null){

                        }
                    }*/

                    if (reqMdl.getOidDate() == null) {
                        ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsg(txn)));
                        producer.send(record);
                    } else if (reqMdl.getOidDate() != null || !reqMdl.getOidDate().isEmpty()) {
                        ProducerRecord<String, String> record = new ProducerRecord(appProps.getKafka_producer_topics(), getMapper().writeValueAsString(addMsgWitDat(txn, reqMdl)));
                        producer.send(record);
                    } else {

                    }
                    roids.add(txn.Oid);
                    i++;
                }
            } else {
            }

            logger.info("Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics() + ", Response have only top 100 oids");
            addResponse(roids, "Successfully produced: " + roids.size() + " messages to a topic called " + appProps.getKafka_producer_topics() + ", Response have only top 100 oids", "", res);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addResponse(roids, "failed to produced messages to a topic called " + appProps.getKafka_producer_topics(), e.getMessage(), res);
            logger.info("failed to produced messages to a topic called " + appProps.getKafka_producer_topics());
            producer.close();

        }

        return res;
    }


}
