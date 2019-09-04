import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Title:Server 这是发送消息的服务端 Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 *
 * @author rao
 */
public class ServerMQTTTemperature {

    // tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://172.16.3.197:61613";
    // 定义一个主题
    public static final String TOPIC = "deta/temperature";
    // 定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientid = "clienttemperature";

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
    public ServerMQTTTemperature() throws MqttException {
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
        ServerMQTTTemperature server = new ServerMQTTTemperature();
        System.out.println(System.currentTimeMillis());
        server.message = new MqttMessage();
        // 保证消息能到达一次
        server.message.setQos(1);
        server.message.setRetained(true);
        String message = "";
        while (true) {

            for (int i = 0; i < 10; i++) {
                message = "{\"SerialNo\" : 31415926" + i + ",\"temperature\" : " + getRandom(19, 21) + ",   \"humidity\" : " + getRandom(40, 66) + ", \"submit\" : " + System.currentTimeMillis() + "}";

                server.message.setPayload(message.getBytes());

                server.publish(server.topic11, server.message);
            }

            Thread.sleep(1000*60);
        }

    }

    public static int getRandom(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}