package com.taotao.pojo;

/**
 * 用户年消费表
 */
public class TbUserConsumeYearReport {
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 消费金额
     */
    private Integer amount;

    /**
     * 消费类型,1.商品支出，2.退款，3.充值，4.官方充值
     */
    private Byte type;

    private Integer year;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}