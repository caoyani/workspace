package com.hsbc.cloudchatbot.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicole on 2019/5/8.
 */
public class PolicyVO {
    private String chdrnum;
    private String contractType;
    private String contractName;
    private String status;
    private String nextBillDate;
    private double premium;
    private String policyOwner;
    private String beneficiary;
    private String description;
    private double cashValue = 0;
    private Map<String, String> components;

    public PolicyVO(String chdrnum) {
        this.initChdrnum(chdrnum);
        this.chdrnum = chdrnum;
    }

    public PolicyVO() {
        init();
    }

    private void initChdrnum(String chdrnum) {
        this.init();
        this.chdrnum = chdrnum;
    }

    private void init() {
        this.chdrnum = "23658563";
        this.contractType = "DC2";
        this.contractName = "EarlyIncome Annuity Plan 3P3";
        this.status = "In Force";
        this.nextBillDate = "2020/01/01";
        this.premium = 1360;
        this.policyOwner = "Harry Smith";
        this.beneficiary = "Harry Smith";
        this.description = "policy detail...";
        this.cashValue = this.getCashValue();
        this.components = new HashMap<String, String>();
        this.components.put("DC02", "Basic Plan");
        this.components.put("TI30", "Terminal Illness Benefit");
        this.components.put("AD75", "Additional Accidental Death");
        this.components.put("DP36", "Unemployment Benefit");
    }

    public double getCashValue() {
        return this.premium * 0.8;
    }


    public String getChdrnum() {
        return chdrnum;
    }

    public void setChdrnum(String chdrnum) {
        this.chdrnum = chdrnum;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNextBillDate() {
        return nextBillDate;
    }

    public void setNextBillDate(String nextBillDate) {
        this.nextBillDate = nextBillDate;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCashValue(double cashValue) {
        this.cashValue = cashValue;
    }

    public Map<String, String> getComponents() {
        return components;
    }

    public void setComponents(Map<String, String> components) {
        this.components = components;
    }
}
