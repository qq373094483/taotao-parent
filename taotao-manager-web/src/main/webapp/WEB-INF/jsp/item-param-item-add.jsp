<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<table cellpadding="5" style="margin-left: 30px" id="itemParamItemAddTable" class="itemParam">
	<tr>
		<td>商品:</td>
		<td><a href="javascript:void(0)" class="easyui-linkbutton selectItemAdd">选择商品</a>
			<input type="hidden" name="itemId"/>
			<input type="hidden" name="itemTitle"/>
		</td>
	</tr>
	<tr class="hide addItemGroupTr">
		<td>规格参数:</td>
		<td>
			<ul>
				<li><a href="javascript:void(0)" class="easyui-linkbutton addGroupItem">添加分组</a></li>
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
<div id="itemAddChoiceWindow" class="easyui-window" title="选择商品" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/item-param-item-itemChoiceList'" style="width:50%;height:40%;padding:0px;">
<div  class="itemParamItemAddTemplate" style="display: none;">
	<li class="param">
		<ul>
			<li>
				<label>组：</label><input class="easyui-textbox" style="width: 150px;" name="group" readonly/>&nbsp<a href="javascript:void(0)" class="hide easyui-linkbutton addParamItem"  title="添加参数" data-options="plain:true,iconCls:'icon-add'"/>
			</li>
			<li>
				<span>|-------</span><label>参数：</label><input  style="width: 150px;" class="easyui-textbox" name="key" readonly/>&nbsp&nbsp<label>参数值：</label><input  style="width: 150px;" class="easyui-textbox" name="param"/>&nbsp;
			</li>
		</ul>
	</li>
</div>
<script style="text/javascript">
	$(function(){
		$('.selectItemAdd').unbind('click').click(function(){
			$(this).after("<span style='margin-left:10px;'></span>")
			$("#itemAddChoiceWindow").window({
			onLoad :function(){
			},
			onOpen: function () {
			},
			onClose : function(){
				<%--$(this).window("destroy");--%>
			}
			}).window("open");
		})
		$(".addGroupItem").click(function(){
			  var temple = $(".itemParamItemAddTemplate li").eq(0).clone();
			  $(this).parent().parent().append(temple);
			  temple.find(".addParamItem").click(function(){
				  var li = $(".itemParamItemAddTemplate li").eq(2).clone();
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

		$("#itemParamItemAddTable .close").click(function(){
			$(".panel-tool-close").click();
		});

		$("#itemParamItemAddTable .submit").click(function(){
			var params = [];
			var groups = $("#itemParamItemAddTable [name=group]");
			groups.each(function(i,e){
				var p = $(e).parentsUntil("ul").parent().find("[name=param]");
				var k = $(e).parentsUntil("ul").parent().find("[name=key]");
				var _ps = [];
				p.each(function(_i,_e){
					var paramsTem={};
					var _val = $(_e).siblings("input").val();
					var key=$(k[_i]).val();
					if($.trim(_val).length>0){
						paramsTem['k']=key;
						paramsTem['v']=_val;
						_ps.push(paramsTem)
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
			var url = "/item/param/item/save/"+$("#itemParamItemAddTable [name=itemId]").val();
			$.post(url,{"paramData":JSON.stringify(params)},function(data){
				if(data.status == 200){
					$.messager.alert('提示','新增商品规格成功!',undefined,function(){
						$(".panel-tool-close").click();
    					$("#itemParamItemList").datagrid("reload");
						$('#itemAddChoiceWindow').window("destroy");
    				});
				}
			});
		});
	});
</script>