
package com.component.jms;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

import applijms.common.JMSConnection;

public class JMSMessageConsumer extends JMSConnection {

	protected MessageConsumer consumer = null;
	public JMSMessageConsumer(String brokerUrl,String nickname) throws JMSException {
		super(brokerUrl);
		createTopic(nickname);
		this.connection.setExceptionListener(this);
		this.consumer = session.createConsumer(getTopic());
	}
	public TextMessage receiveMessage() throws JMSException
	{
		Message message = consumer.receive(1000);
		 TextMessage textMessage  = null;
    	if (message instanceof TextMessage) {
            textMessage = (TextMessage) message;
            String text = textMessage.getText();
           // System.out.println("Received: " + text);
        } else {
        	textMessage=null;
        }
        return textMessage;
	}

}
