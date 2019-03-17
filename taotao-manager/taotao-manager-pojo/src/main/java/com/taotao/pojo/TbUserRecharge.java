package com.taotao.pojo;

import java.util.Date;

/**
 * 用户充值表
 */
public class TbUserRecharge extends BasePO {
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 充值单号
     */
    private String rechargeNo;

    /**
     * 充值金额
     */
    private Integer amount;

    /**
     * 余额
     */
    private Integer balance;

    /**
     * 充值方式,1.支付宝，2.微信
     */
    private Byte way;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo == null ? null : rechargeNo.trim();
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Byte getWay() {
        return way;
    }

    public void setWay(Byte way) {
        this.way = way;
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