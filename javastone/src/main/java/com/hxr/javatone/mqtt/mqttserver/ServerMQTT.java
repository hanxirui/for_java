package com.hxr.javatone.mqtt.mqttserver;

import org.apache.commons.lang.math.RandomUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Title:Server 这是发送消息的服务端 Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * 
 * @author rao
 */
public class ServerMQTT {

	// tcp://MQTT安装的服务器地址:MQTT定义的端口号
	public static final String HOST = "tcp://172.16.3.197:61613";
	// 定义一个主题
	public static final String TOPIC = "20180820/reportrecord";
	// 定义MQTT的ID，可以在MQTT服务配置中指定
	private static final String clientid = "clientmenjin";

	private MqttClient client;
	private MqttTopic topic11;
	private String userName = "admin"; // 非必须
	private String passWord = "password"; // 非必须

	private MqttMessage message;

	/**
	 * 构造函数
	 * 
	 * @throws MqttException
	 */
	public ServerMQTT() throws MqttException {
		// MemoryPersistence设置clientid的保存形式，默认为以内存保存
		client = new MqttClient(HOST, clientid, new MemoryPersistence());
		connect();
	}

	/**
	 * 用来连接服务器
	 */
	private void connect() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setUserName(userName);
		options.setPassword(passWord.toCharArray());
		// 设置超时时间
		options.setConnectionTimeout(10);
		// 设置会话心跳时间
		options.setKeepAliveInterval(20);
		try {
			client.setCallback(new PushCallback());
			client.connect(options);

			topic11 = client.getTopic(TOPIC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param topic
	 * @param message
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println("message is published completely! " + token.isComplete());
	}

	/**
	 * 启动入口
	 * 
	 * @param args
	 * @throws MqttException
	 */
	public static void main(String[] args) throws MqttException, InterruptedException {
		ServerMQTT server = new ServerMQTT();

		server.message = new MqttMessage();
		server.message.setQos(1); // 保证消息能到达一次
		server.message.setRetained(true);
		int count = 0;
		String message = "{\"SerialNo\" : " + count
				+ ",\"DeviceAddr\" : \"fjJct3xrb0Up\",   \"PassRecords\" :[   { \"IdentityType\" : 101, \"IdentityCode\" : \"757d6d9b\", \"OriginalCode\" : \"757d6d9b\", \"PassTime\" : "
				+ 1411384196
				+ ",  \"PassResult\" : 1},{ \"IdentityType\" : 102, \"IdentityCode\" : \"757d6d9b\", \"OriginalCode\" : \"757d6d9b\", \"PassTime\" : "
				+ 1411384196 + ",  \"PassResult\" : 1}]}";
		String identityCode = "757d6d9b";
		int result = 1;
		while (true) {
			count++;
			identityCode = "757d6d" + RandomUtils.nextInt();
			server.message.setPayload(message.getBytes());
			result = count % 100 == 0 ? -1 : 1;
			message = "{\"SerialNo\" : " + count
					+ ",\"DeviceAddr\" : \"fjJct3xrb0Up\",   \"PassRecords\" :[   { \"IdentityType\" : 101, \"IdentityCode\" : \""
					+ identityCode + "\", \"OriginalCode\" : \"" + identityCode + "\", \"PassTime\" : "
					+ (1411384196 + (count / 10) * 5) + ",  \"PassResult\" : " + result + "},"
					+ "{ \"IdentityType\" : 102, \"IdentityCode\" : \"" + identityCode + "\", \"OriginalCode\" : \""
					+ identityCode + "\", \"PassTime\" : " + (1411384196 + (count / 10) * 5) + ",  \"PassResult\" : "
					+ result + "}]}";
			;
			server.publish(server.topic11, server.message);
			Thread.sleep(getRandom(10000, 20000));
		}

	}

	public static int getRandom(int min, int max) {
		return min + (int) (Math.random() * (max - min + 1));
	}

}