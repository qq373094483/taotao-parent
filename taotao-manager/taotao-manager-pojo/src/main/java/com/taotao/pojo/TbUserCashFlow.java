package com.taotao.pojo;

import java.util.Date;

/**
 * 用户现金流水
 */
public class TbUserCashFlow {
    private Long id;

    /**
     * 流水单号
     */
    private String cashFlowNo;

    /**
     * 用户ID
     */
    private Long userId;

    private Integer amount;

    /**
     * 流水类型,1.商品支出，2.退款，3.充值，4.官方充值
     */
    private Byte type;

    /**
     * 余额
     */
    private Integer balance;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCashFlowNo() {
        return cashFlowNo;
    }

    public void setCashFlowNo(String cashFlowNo) {
        this.cashFlowNo = cashFlowNo == null ? null : cashFlowNo.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}