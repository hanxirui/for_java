package com.javapush.apns.sample;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.ClientNotConnectedException;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * APNS推送,已经测试成功
 * Created by hanxirui on 2017/5/27.
 */
public class APNSTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        final ApnsClient apnsClient = new ApnsClientBuilder()
                .setClientCredentials(new File("/Users/hanxirui/Documents/workspace/github/for_java/pushy-master/java_push/src/main/resources/DevPush_05.05.p12"), "123456")
                .build();

        final Future<Void> connectFuture = apnsClient.connect(ApnsClient.DEVELOPMENT_APNS_HOST);
        connectFuture.await();

        final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setAlertBody("新的来自德塔的智慧推送!");

        final String payload = payloadBuilder.buildWithDefaultMaximumLength();
        final String token = TokenUtil.sanitizeTokenString("<aab02718 0fa33079 c68b4b90 5c7e4f44 2c29905d 980cda9f f476ccf0 d18ba458>");

        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "info.zznet.YYLink", payload);

        final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture =
                apnsClient.sendNotification(pushNotification);

        try {
            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    sendNotificationFuture.get();

            if (pushNotificationResponse.isAccepted()) {
                System.out.println("Push notification accepted by APNs gateway.");
            } else {
                System.out.println("Notification rejected by the APNs gateway: " +
                        pushNotificationResponse.getRejectionReason());

                if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                    System.out.println("\t…and the token is invalid as of " +
                            pushNotificationResponse.getTokenInvalidationTimestamp());
                }
            }
        } catch (final ExecutionException e) {
            System.err.println("Failed to send push notification.");
            e.printStackTrace();

            if (e.getCause() instanceof ClientNotConnectedException) {
                System.out.println("Waiting for client to reconnect…");
                apnsClient.getReconnectionFuture().await();
                System.out.println("Reconnected.");
            }
        }

        final Future<Void> disconnectFuture = apnsClient.disconnect();
        disconnectFuture.await();

    }
}
