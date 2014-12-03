package com.component.jms;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import applijms.common.JMSConnection;

public class JMSMessageProducer extends JMSConnection {

	protected MessageProducer producer = null;
	public JMSMessageProducer(String brokerUrl,String nickname) throws JMSException {
		super(brokerUrl);
		createTopic(nickname);
		
		 producer = session.createProducer(topic);
		 producer.setPriority(1);
         producer.setDeliveryMode(DeliveryMode.PERSISTENT);

         

	}
	public void sendMessage(Message msg) throws JMSException
	{
		//TextMessage message = session.createTextMessage("TEXT");
		System.out.println("J'envoie sur " + (topic.toString()));
		this.producer.send(msg);
	}

}
