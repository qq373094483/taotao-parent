<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<table class="easyui-datagrid" id="itemParamList" title="商品分类规格列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/item/param/list',method:'get',pageSize:30,toolbar:itemParamListToolbar">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'id',width:60">ID</th>
        <th data-options="field:'itemCatId',width:80">商品类目ID</th>
        <th data-options="field:'itemCatName',width:100">商品类目</th>
        <th data-options="field:'paramData',width:300,formatter:formatItemParamData">规格(只显示分组名称)</th>
        <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
        <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
    </tr>
    </thead>
</table>
<div id="itemParamEditWindow" class="easyui-window" title="编辑商品分类规格"
     data-options="modal:true,closed:true,iconCls:'icon-save',href:'/item-param-edit'"
     style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function formatItemParamData(value, index) {
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

    function getSelectionsIds() {
        var itemList = $("#itemParamList");
        var sels = itemList.datagrid("getSelections");
        var ids = [];
        for (var i in sels) {
            ids.push(sels[i].id);
        }
        ids = ids.join(",");
        return ids;
    }

    var itemParamListToolbar = [{
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            TAOTAO.createWindow({
                url: "/item-param-add",
                title:"新增商品分类规格"
            });
        }
    }, {
        text: '编辑',
        iconCls: 'icon-edit',
        handler: function () {
            var ids = getSelectionsIds();
            if (ids.length == 0) {
                $.messager.alert('提示', '必须选择一种分类规格才能编辑!');
                return;
            }
            if (ids.indexOf(',') > 0) {
                $.messager.alert('提示', '只能选择一种规格!');
                return;
            }
            $("#itemParamEditWindow").window({
                onLoad: function () {
                    var data = $("#itemParamList").datagrid("getSelections")[0]
                    // 加载商品描述
                    $.getJSON('/item/param/query/' + data.id, function (_data) {
                        if (_data.status == 200) {
                            $('input[name="id"]').val(data.id);
                            $('input[name="cid"]').val(data.itemCatId);
                            $('input[name="cid"]').prev().text(data.itemCatName).attr("cid", data.itemCatId);
                            //显示添加分组的组件
                            $(".addGroupTr").show();
                            var paramData = JSON.parse(data.paramData);
                            $.each(paramData, function (groupIndex, groupItem) {
                                $(".addGroup").click();
                                //填充一级目录
                                var group = $($('li.param')[groupIndex]).find('input[name="group"]');
                                $(group).val(groupItem.group);
                                $(group).prev('input').val(groupItem.group);

                                //填充二级目录
                                var params = groupItem.params;
                                $.each(params, function (paramIndex, paramItem) {
                                    var addParam = $($(group).parent()[0]).next('.addParam')[0];
                                    if (paramIndex > 0) {
                                        $(addParam).click();
                                    }
                                    //二组目录组件
                                    var param=$($(addParam).parent('li')[0]).nextAll('li')[paramIndex];
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
            var ids = getSelectionsIds();
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
                                $("#itemParamList").datagrid("reload");
                            });
                        }
                    });
                }
            });
        }
    }];
</script>