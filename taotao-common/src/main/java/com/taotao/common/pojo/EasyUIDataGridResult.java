package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * easyui返回数据结构
 * @param <T>
 */
public class EasyUIDataGridResult<T> implements Serializable{
    private Long total;
    private List<T> rows;

    public static<T> EasyUIDataGridResult<T> build(Long total,List<T> rows){
        EasyUIDataGridResult<T> easyUIDataGridResult=new EasyUIDataGridResult<>();
        easyUIDataGridResult.setTotal(total);
        easyUIDataGridResult.setRows(rows);
        return easyUIDataGridResult;
    }
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
