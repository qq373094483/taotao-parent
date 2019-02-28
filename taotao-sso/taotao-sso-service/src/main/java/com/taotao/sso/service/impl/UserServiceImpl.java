package com.taotao.sso.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;
    @Value("${USER_SESSION}")
    private String USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String data, int type) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //设置查询条件
        //1.判断用户名是否可用
        if(type==1){
            criteria.andUsernameEqualTo(data);
        }
        //判断手机号是否可用使用
        else if(type==2){
            criteria.andPhoneEqualTo(data);
        }
        //判断邮箱是否可用
        else if (type == 3) {
            criteria.andEmailEqualTo(data);
        }else{
            return TaotaoResult.build(400, "type参数包含非法数据");
        }
        List<TbUser> tbUsers = userMapper.selectByExample(tbUserExample);
        if (CollectionUtils.isNotEmpty(tbUsers)) {
            //查询到数据，返回false
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser tbUser) {
        if (StringUtils.isBlank(tbUser.getUsername())) {
            return TaotaoResult.build(400, "用户名不能为空");
        }
        if(!(boolean)checkData(tbUser.getUsername(),1).getData()){
            return TaotaoResult.build(400, "用户名重复");
        }
        if (StringUtils.isBlank(tbUser.getPassword())) {
            return TaotaoResult.build(400, "密码不能为空");
        }
        if (StringUtils.isNotBlank(tbUser.getPhone())) {
            TaotaoResult taotaoResult = checkData(tbUser.getPhone(), 2);
            if (!(boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "电话号码重复");
            }
        }
        if (StringUtils.isNotBlank(tbUser.getEmail())) {
            TaotaoResult taotaoResult = checkData(tbUser.getEmail(), 3);
            if (!(boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "email重复");
            }
        }
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        userMapper.insert(tbUser);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(tbUsers)) {
            return TaotaoResult.build(400, "用户名或密码不正确");
        }
        TbUser tbUser = tbUsers.get(0);
        //判断用户名和密码是否正确
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
            return TaotaoResult.build(400, "用户名或密码不正确");
        }
        String token = UUID.randomUUID().toString();
        tbUser.setPassword(null);
        jedisClient.set(USER_SESSION+":"+token, JsonUtils.objectToJson(tbUser));
        jedisClient.expire(USER_SESSION+":" + token, SESSION_EXPIRE);
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String json = jedisClient.get(USER_SESSION + ":" + token);
        if (StringUtils.isBlank(json)) {
            return TaotaoResult.build(400, "用户登录已经过期");
        }
        jedisClient.expire(USER_SESSION+":" + token, SESSION_EXPIRE);
        return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
    }

}
