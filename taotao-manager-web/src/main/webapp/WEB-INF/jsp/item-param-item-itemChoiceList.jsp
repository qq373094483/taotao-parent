<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemChoiceList" title="商品选择列表"  style="width:100%;height:100%;padding:0px;"
       data-options="singleSelect:false,striped:true,singleSelect:true,collapsible:true,pagination:true,url:'/item/list',method:'get',pageSize:30">
    <thead>
        <tr>
        	<th data-options="field:'id',width:150">商品ID</th>
            <th data-options="field:'title',width:200">商品标题</th>
            <th data-options="field:'cid',width:100">叶子类目</th>
            <th data-options="field:'sellPoint',width:100">卖点</th>
            <th data-options="field:'price',width:70,align:'right',formatter:TAOTAO.formatPrice">价格</th>
            <th data-options="field:'num',width:70,align:'right'">库存数量</th>
            <th data-options="field:'barcode',width:100">条形码</th>
            <th data-options="field:'status',width:60,align:'center',formatter:TAOTAO.formatItemStatus">状态</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
</div>
<script>
    $("#itemChoiceList").datagrid({
        onDblClickRow:function(rowIndex, rowData) {
            $('#itemParamItemAddTable input[name="itemId"]').val(rowData.id);
            $('#itemParamItemAddTable input[name="itemTitle"]').val(rowData.title);
            $('#itemParamItemAddTable input[name="itemTitle"]').prevAll('span').text(rowData.title).attr("itemId",rowData.id);
            $("#itemChoiceWindow").window("close");
            $(".addItemGroupTr").show();
        }
    });
</script>