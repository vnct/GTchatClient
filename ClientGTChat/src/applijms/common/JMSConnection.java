package applijms.common;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.region.Topic;
 

public class JMSConnection implements ExceptionListener{
	protected ActiveMQConnectionFactory connectionFactory;
	protected Connection connection;
	protected Session session;
	protected Destination queue;
	protected Destination topic;
	
	public Destination getTopic() {
		return topic;
	}

	public JMSConnection(String brokerUrl) throws JMSException {
		super();
	
		


		this.connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		open();
		 
	}
	public void open() throws JMSException
	{
		this.connection = connectionFactory.createConnection();
		
		this.connection.start();

		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	public void createQueue(String name) throws JMSException
	{
		String name_queue=name;
		queue =  session.createQueue(name_queue);
	}
	public void createTopic(String name) throws JMSException
	{
		String name_queue=name;
		topic =  session.createTopic(name);
	}
	public void close() throws JMSException
	{
		session.close();
        connection.close();
	}
	

	public ActiveMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Destination getQueue() {
		return queue;
	}



	@Override
	public void onException(JMSException arg0) {
		arg0.printStackTrace();
		
	}
	
	
	
	
}
