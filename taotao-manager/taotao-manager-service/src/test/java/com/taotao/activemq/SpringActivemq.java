package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.TextMessage;

public class SpringActivemq {

	//使用jsmTemplate 发送消息
	@Test
	public void testJmsTemplate() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//从容器中获得JmsTemplate对象
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		//从容器中获得Destination对象
		Destination destination = (Destination) applicationContext.getBean("test-queue");
		//发送消息
		jmsTemplate.send(destination, session -> {
			TextMessage message = session.createTextMessage("spring activemq send queue message");
			return message;
		});
		
	}
}
