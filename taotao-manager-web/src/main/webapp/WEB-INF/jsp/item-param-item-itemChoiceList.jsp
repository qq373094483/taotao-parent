<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<table class="easyui-datagrid" id="itemChoiceList" title="商品选择列表" style="width:100%;height:100%;padding:0px;"
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
        onDblClickRow: function (rowIndex, rowData) {
            $('.addItemGroupTr li.param').remove();
            //  判断选择的商品是否已经添加过规格
            $.getJSON("/item/param/item/query/itemId/" + rowData.id, function (data) {
                if (data.data) {
                    $.messager.alert("提示", "该商品已经添加规格", "warning", function () {
                        // $("#itemParamAddTable .selectItemCat").click();
                    });
                    return;
                }
                $.getJSON("/item/param/query/itemcatid/" + rowData.cid, function (data) {
                    var paramData = JSON.parse(data.data.paramData);
                    $('#itemParamItemAddTable input[name="itemId"]').val(rowData.id);
                    $('#itemParamItemAddTable input[name="itemTitle"]').val(rowData.title);
                    $('#itemParamItemAddTable input[name="itemTitle"]').prevAll('span').text(rowData.title).attr("itemId", rowData.id);
                    $("#itemChoiceWindow").window("close");
                    $(".addItemGroupTr").show();
                    $(".addGroupItem").hide();
                    //规格组件
                    $.each(paramData, function (index, item) {
                        $(".addGroupItem").click();
                    });
                    //填充组和参数值
                    var paramComponent = $('.addItemGroupTr li.param input[name="group"]').parent('span').find('input');
                    $.each(paramComponent, function (index, item) {
                        var num = index + 1;
                        var paramDataIndex = num % 2 == 1 ? (num + 1) / 2 : num / 2;
                        var currentParamDataItem = paramData[paramDataIndex - 1];
                        $(item).val(currentParamDataItem.group);
                        if (num % 2 == 0) {
                            var params = paramData[paramDataIndex - 1].params;
                            //添加参数组件
                            $.each(params, function (paramIndex, paramItem) {
                                if(paramIndex!=0) {
                                    $(item).parent().next().click()
                                }
                                var paramComponent=$(item).parents("li:not('.param')").nextAll('li')[paramIndex];
                                $(paramComponent).find('input[name="key"]').parent('span').find('input').val(paramItem);
                            });

                        }
                    });
                    //关闭选择组件
                    $('#itemAddChoiceWindow').window("close");
                });
            });
        }
    });
</script>