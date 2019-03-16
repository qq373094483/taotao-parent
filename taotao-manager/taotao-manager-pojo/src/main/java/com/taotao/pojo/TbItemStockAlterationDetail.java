package com.taotao.pojo;

import java.util.Date;

/**
 * 商品库存变更明细
 */
public class TbItemStockAlterationDetail {
    private Long id;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 商品库存变更数量
     */
    private Integer num;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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