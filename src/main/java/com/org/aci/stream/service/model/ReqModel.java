package com.org.aci.stream.service.model;
import java.util.List;
import java.util.StringJoiner;

public class ReqModel {
    public String clientId;
    public List<String> currCds;
    public List<String> reasonCodes;
    public List<String> subClientIds;
    public int numberOfMsg;
    public List<String> recommendations;
    public String oidDate;
    public List<String> virtCardBins;
    public List<String> virtIpidCarriers;
    public List<String> virtCustEmailDomains;
    public List<String> billStates;
    public List<String> billCountries;
    public List<String> billZipcds;
    public List<String> shipStates;
    public List<String> shipZipcds;
    public List<String> shipCountry;
    public List<String> deviceIds;
    public List<String> custEmails;
    public int seqNumber;
    public String seqFieldName;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getCurrCds() {
        return currCds;
    }

    public void setCurrCds(List<String> currCds) {
        this.currCds = currCds;
    }

    public List<String> getReasonCodes() {
        return reasonCodes;
    }

    public void setReasonCodes(List<String> reasonCodes) {
        this.reasonCodes = reasonCodes;
    }

    public List<String> getSubClientIds() {
        return subClientIds;
    }

    public void setSubClientIds(List<String> subClientIds) {
        this.subClientIds = subClientIds;
    }

    public int getNumberOfMsg() {
        return numberOfMsg;
    }

    public void setNumberOfMsg(int numberOfMsg) {
        this.numberOfMsg = numberOfMsg;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public String getOidDate() {
        return oidDate;
    }

    public void setOidDate(String oidDate) {
        this.oidDate = oidDate;
    }

    public List<String> getVirtCardBins() {
        return virtCardBins;
    }

    public void setVirtCardBins(List<String> virtCardBins) {
        this.virtCardBins = virtCardBins;
    }

    public List<String> getVirtIpidCarriers() {
        return virtIpidCarriers;
    }

    public void setVirtIpidCarriers(List<String> virtIpidCarriers) {
        this.virtIpidCarriers = virtIpidCarriers;
    }

    public List<String> getVirtCustEmailDomains() {
        return virtCustEmailDomains;
    }

    public void setVirtCustEmailDomains(List<String> virtCustEmailDomains) {
        this.virtCustEmailDomains = virtCustEmailDomains;
    }

    public List<String> getBillStates() {
        return billStates;
    }

    public void setBillStates(List<String> billStates) {
        this.billStates = billStates;
    }

    public List<String> getBillCountries() {
        return billCountries;
    }

    public void setBillCountries(List<String> billCountries) {
        this.billCountries = billCountries;
    }

    public List<String> getBillZipcds() {
        return billZipcds;
    }

    public void setBillZipcds(List<String> billZipcds) {
        this.billZipcds = billZipcds;
    }

    public List<String> getShipStates() {
        return shipStates;
    }

    public void setShipStates(List<String> shipStates) {
        this.shipStates = shipStates;
    }

    public List<String> getShipZipcds() {
        return shipZipcds;
    }

    public void setShipZipcds(List<String> shipZipcds) {
        this.shipZipcds = shipZipcds;
    }

    public List<String> getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(List<String> shipCountry) {
        this.shipCountry = shipCountry;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public List<String> getCustEmails() {
        return custEmails;
    }

    public void setCustEmails(List<String> custEmails) {
        this.custEmails = custEmails;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getSeqFieldName() {
        return seqFieldName;
    }

    public void setSeqFieldName(String seqFieldName) {
        this.seqFieldName = seqFieldName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReqModel.class.getSimpleName() + "[", "]")
                .add("clientId='" + clientId + "'")
                .add("currCds=" + currCds)
                .add("reasonCodes=" + reasonCodes)
                .add("subClientIds=" + subClientIds)
                .add("numberOfMsg=" + numberOfMsg)
                .add("recommendations=" + recommendations)
                .add("oidDate='" + oidDate + "'")
                .add("virtCardBins=" + virtCardBins)
                .add("virtIpidCarriers=" + virtIpidCarriers)
                .add("virtCustEmailDomains=" + virtCustEmailDomains)
                .add("billStates=" + billStates)
                .add("billCountries=" + billCountries)
                .add("billZipcds=" + billZipcds)
                .add("shipStates=" + shipStates)
                .add("shipZipcds=" + shipZipcds)
                .add("shipCountry=" + shipCountry)
                .add("deviceIds=" + deviceIds)
                .add("custEmails=" + custEmails)
                .add("seqNumber=" + seqNumber)
                .toString();
    }
}
