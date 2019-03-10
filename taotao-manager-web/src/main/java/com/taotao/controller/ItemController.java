package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "itemAddTopic")
    private Destination itemAddTopicDestination;

    @Resource(name = "itemDelTopic")
    private Destination itemDelTopicDestination;
    /**
     * 如果/item/{itemId}最后一个URI和参数名一致，可以不加@PathVariable，否则要在@PathVariable设置与路径一致的名称到注解中
     * 如果报406错误，有可能是因为没有加jackson包加进入，这里通过common包传递了该包的引用
     * @param itemId
     * @return
     */
    @RequestMapping("/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId){
        return itemService.getItemById(itemId);
    }

    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult<TbItem> getItemList(Integer page,Integer rows){
        return itemService.getItemList(page, rows);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult adItem(TbItem tbItem, String desc) {
        TaotaoResult<Long> taotaoResult = itemService.addItem(tbItem, desc);
        //向Activemq发送商品添加消息
        mqAddItem(taotaoResult.getData());
        taotaoResult.setData(null);
        return taotaoResult;
    }

    private void mqAddItem(Long itemId) {
        jmsTemplate.send(itemAddTopicDestination, session -> session.createTextMessage(itemId + ""));
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult delItem(@RequestParam("ids") String ids) {
        if (StringUtils.isBlank(ids)) {
            TaotaoResult.build(500, "请选择要删除的对象");
        }
        List<Long> itemIds = Arrays.stream(StringUtils.split(ids, ",")).map(id -> Long.valueOf(id)).collect(Collectors.toList());
        TaotaoResult taotaoResult = itemService.delItem(itemIds);
        //向Activemq发送商品删除消息
        mqBatchDelItem(itemIds);
        return taotaoResult;
    }
    private void mqBatchDelItem(List<Long> ids) {
        for (Long itemId : ids) {
            jmsTemplate.send(itemDelTopicDestination, session -> session.createTextMessage(itemId + ""));
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult update(TbItem tbItem, String desc) {
        if (tbItem.getId() == null) {
            return TaotaoResult.fail("id不能为空");
        }
        TaotaoResult taotaoResult = itemService.updateItem(tbItem, desc);
        //向Activemq发送商品添加消息
//        mqAddItem(taotaoResult.getData());
        return taotaoResult;
    }

    @RequestMapping(value = "query/item/desc/{itemId}")
    @ResponseBody
    public TaotaoResult queryItemDesc(@PathVariable  Long itemId) {
        return TaotaoResult.ok(itemService.getItemDescById(itemId));
    }


    @RequestMapping(value = "param/item/query/{itemId}")
    @ResponseBody
    public TaotaoResult queryItemParamItem(@PathVariable  Long itemId) {
        return TaotaoResult.ok(itemService.getItemParamItemByItemId(itemId));
    }
}
