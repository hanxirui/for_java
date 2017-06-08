package com.hxr.javatone.apns;

import com.hxr.javatone.apns.clevertap.ApnsClient;
import com.hxr.javatone.apns.clevertap.Notification;
import com.hxr.javatone.apns.clevertap.NotificationResponse;
import com.hxr.javatone.apns.clevertap.NotificationResponseListener;
import com.hxr.javatone.apns.clevertap.clients.ApnsClientBuilder;

import java.io.FileInputStream;

public class Apnsios {
    public static void main(String[] args) throws Exception {

//        Using provider certificates
        FileInputStream cert = new FileInputStream("/Users/hanxirui/Documents/workspace/github/for_java/javastone/src/main/resources/DevPush_05.05.p12");

        final ApnsClient client = new ApnsClientBuilder()
                .withDevelopmentGateway()
                .inSynchronousMode()
                .withCertificate(cert)
                .withPassword("123456")
                .withDefaultTopic("info.zznet.YYLink")
                .build();

        Notification n = new Notification.Builder("aab027180fa33079c68b4b905c7e4f442c29905d980cda9ff476ccf0d18ba458")
                .alertBody("Hello").build();

//        inAsynchronousMode
//        client.push(n, new NotificationResponseListener() {
//            @Override
//            public void onSuccess(Notification notification) {
//                System.out.println("success!");
//            }
//            @Override
//            public void onFailure(Notification notification, NotificationResponse nr) {
//                System.out.println("failure: " + nr);
//            }
//        });

//        Synchronous

        NotificationResponse result = client.push(n);
        System.out.println(result);

//        Using provider authentication tokens

//        final ApnsClient client2 = new ApnsClientBuilder()
//                .inSynchronousMode()
//                .withProductionGateway()
//                .withApnsAuthKey("<your APNS auth key, excluding -----BEGIN PRIVATE KEY----- and -----END PRIVATE KEY----->")
//                .withTeamID("<your team ID here>")
//                .withKeyID("<your key ID here, present in the auth key file name>")
//                .withDefaultTopic("<your app's topic>")
//                .build();
    }
}