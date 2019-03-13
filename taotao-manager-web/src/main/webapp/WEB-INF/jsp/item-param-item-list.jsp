<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<table class="easyui-datagrid" id="itemParamItemList" title="商品规格列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/item/param/item/list',method:'get',pageSize:30,toolbar:itemParamItemListToolbar">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'id',width:60">ID</th>
        <th data-options="field:'itemId',width:80">商品ID</th>
        <th data-options="field:'itemTitle',width:100">商品名称</th>
        <th data-options="field:'paramData',width:300,formatter:formatItemParamItemData">规格(只显示分组名称)</th>
        <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
        <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
    </tr>
    </thead>
</table>
<div id="itemParamItemEditWindow" class="easyui-window" title="编辑商品规格"
     data-options="modal:true,closed:true,iconCls:'icon-save',href:'/item-param-item-edit'"
     style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function formatItemParamItemData(value, index) {
        if (value == null) {
            return "";
        }
        var json = JSON.parse(value);
        var array = [];
        $.each(json, function (i, e) {
            array.push(e.group);
        });
        return array.join(",");
    }

    function getItemSelectionsIds() {
        var itemList = $("#itemParamItemList");
        var sels = itemList.datagrid("getSelections");
        var ids = [];
        for (var i in sels) {
            ids.push(sels[i].id);
        }
        ids = ids.join(",");
        return ids;
    }

    var itemParamItemListToolbar = [{
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            $("<div>").css({padding:"5px"}).window({
                width : "80%",
                height : "80%",
                modal:true,
                title : "新增商品规格",
                href :"/item-param-item-add",
                onClose : function(){
                    $(this).window("destroy");
                },
                onLoad : function(){

                }
            }).window("open");
        }
    }, {
        text: '编辑',
        iconCls: 'icon-edit',
        handler: function () {
            var ids = getItemSelectionsIds();
            console.log(ids);
            if (ids.length == 0) {
                $.messager.alert('提示', '必须选择一种规格才能编辑!');
                return;
            }
            if (ids.indexOf(',') > 0) {
                $.messager.alert('提示', '只能选择一种规格!');
                return;
            }
            $("#itemParamItemEditWindow").window({
                onLoad: function () {
                    var data = $("#itemParamItemList").datagrid("getSelections")[0]
                    // 加载商品描述
                    $.getJSON('/item/param/item/query/' + data.id, function (_data) {
                        console.log('aaaaaaaaa');
                        if (_data.status == 200) {
                            $('input[name="id"]').before("<span style='margin-left:10px;' itemId="+data.itemId+">"+data.itemTitle+"</span>");
                            $('input[name="id"]').val(data.id);
                            $('input[name="itemId"]').val(data.itemId);
                            $('input[name="itemTitle"]').val(data.itemTitle);
                            //TODO 渲染数据
                            //显示添加分组的组件
                            $(".addGroupTr").show();
                            var paramData = JSON.parse(data.paramData);
                            $.each(paramData, function (groupIndex, groupItem) {
                                $(".addGroupItem").click();
                                //填充一级目录
                                var group = $($('li.param')[groupIndex]).find('input[name="group"]');
                                $(group).val(groupItem.group);
                                $(group).prev('input').val(groupItem.group);

                                //填充二级目录
                                var params = groupItem.params;
                                $.each(params, function (paramIndex, paramItem) {
                                    var addParamItem = $($(group).parent()[0]).next('.addParamItem')[0];
                                    if (paramIndex > 0) {
                                        $(addParamItem).click();
                                    }
                                    //二组目录组件
                                    var param=$($(addParamItem).parent('li')[0]).nextAll('li')[paramIndex];
                                    $(param).find('input').val(paramItem);
                                });
                            })
                        }
                    });
                }
            }).window("open");
        }
    }, {
        text: '删除',
        iconCls: 'icon-cancel',
        handler: function () {
            var ids = getItemSelectionsIds();
            if (ids.length == 0) {
                $.messager.alert('提示', '未选中商品规格!');
                return;
            }
            $.messager.confirm('确认', '确定删除ID为 ' + ids + ' 的商品规格吗？', function (r) {
                if (r) {
                    var params = {"ids": ids};
                    $.post("/item/param/delete", params, function (data) {
                        if (data.status == 200) {
                            $.messager.alert('提示', '删除商品规格成功!', undefined, function () {
                                $("#itemParamItemList").datagrid("reload");
                            });
                        }
                    });
                }
            });
        }
    }];
</script>