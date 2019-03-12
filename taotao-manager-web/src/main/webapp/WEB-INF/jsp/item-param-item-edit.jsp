<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<table cellpadding="5" style="margin-left: 30px" id="itemParamItemEditTable" class="itemParam">
	<tr>
		<td>商品:</td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton selectItem" disabled="disabled">选择商品</a>
			<input type="hidden" name="itemId"/>
			<input type="hidden" name="itemTitle"/>
		</td>
	</tr>
	<tr class="hide addItemGroupTr">
		<td>规格参数:</td>
		<td>
			<ul>
				<li><a href="javascript:void(0)" class="easyui-linkbutton addGroup">添加分组</a></li>
			</ul>
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<a href="javascript:void(0)" class="easyui-linkbutton submit">提交</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton close">关闭</a>
		</td>
	</tr>
</table>
<div id="itemChoiceWindow" class="easyui-window" title="选择商品" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/item-param-item-itemChoiceList'" style="width:50%;height:40%;padding:10px;">
<div  class="itemParamEditTemplate" style="display: none;">
	<li class="param">
		<ul>
			<li>
				<input class="easyui-textbox" style="width: 150px;" name="group"/>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton addParam"  title="添加参数" data-options="plain:true,iconCls:'icon-add'"><a href="javascript:void(0)" class="easyui-linkbutton delParams"  title="删除参数" data-options="plain:true,iconCls:'icon-cancel'"></a>
			</li>
			<li>
				<span>|-------</span><input  style="width: 150px;" class="easyui-textbox" name="param"/>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton delParam" title="删除" data-options="plain:true,iconCls:'icon-cancel'"></a>						
			</li>
		</ul>
	</li>
</div>
<script style="text/javascript">
	$(function(){
		$(".addItemGroupTr").show();
		$(".addGroup").click(function(){
			  var temple = $(".itemParamEditTemplate li").eq(0).clone();
			  $(this).parent().parent().append(temple);
			  temple.find(".addParam").click(function(){
				  var li = $(".itemParamEditTemplate li").eq(2).clone();
				  li.find(".delParam").click(function(){
					  $(this).parent().remove();
				  });
				  li.appendTo($(this).parentsUntil("ul").parent());
			  });
			  temple.find(".delParam").click(function(){
				  $(this).parent().remove();
			  });
				//删除单个分组
				temple.find(".delParams").click(function(){
					$(this).parents('li.param').remove();
				});
		 });
		
		$("#itemParamItemEditTable .close").click(function(){
			$(".panel-tool-close").click();
		});
		
		$("#itemParamItemEditTable .submit").click(function(){
			var params = [];
			var groups = $("#itemParamItemEditTable [name=group]");
			groups.each(function(i,e){
				var p = $(e).parentsUntil("ul").parent().find("[name=param]");
				var _ps = [];
				p.each(function(_i,_e){
					var _val = $(_e).siblings("input").val();
					if($.trim(_val).length>0){
						_ps.push(_val);						
					}
				});
				var _val = $(e).siblings("input").val();
				if($.trim(_val).length>0 && _ps.length > 0){
					params.push({
						"group":_val,
						"params":_ps
					});					
				}
			});
			if(params.length<=0){
				$.messager.alert('提示','至少要添加一组规格数据');
				return;
			}
			var url = "/item/param/update/"+$("#itemParamItemEditTable [name=cid]").val();
			$.post(url,{"paramData":JSON.stringify(params),"id":$("#itemParamItemEditTable [name=id]").val()},function(data){
				if(data.status == 200){
					$.messager.alert('提示','修改商品规格成功!',undefined,function(){
						$(".panel-tool-close").click();
    					$("#itemParamItemList").datagrid("reload");
    				});
				}
			});
		});
		//编辑状态下，解除click事件
		$("#itemParamItemEditTable .selectItem").unbind('click')
	});
</script>