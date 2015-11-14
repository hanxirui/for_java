package com.hxr.javatone.util;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//@author Michael.wu
// 发送复杂的邮件(文本内容，附件，图片)
public class JavaMail {

    public static void main(final String[] args) throws MessagingException, GeneralSecurityException {
        // 发送邮件的协议
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");// 设置验证机制
        properties.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议
        properties.setProperty("mail.smtp.host", "smtp.163.com");// 设置邮箱服务器地址
        properties.setProperty("mail.smtp.port", "25");
        
//        ssl连接方式
//        MailSSLSocketFactory sf = new MailSSLSocketFactory();  
//        sf.setTrustAllHosts(true); 
//        properties.put("mail.smtp.ssl.enable", "true");  
//        properties.put("mail.smtp.ssl.socketFactory", sf); 
        Session session = Session.getInstance(properties, new MyAuthenticator());
        session.setDebug(true);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("hxirui@163.com"));
        message.setSubject("一封复杂的邮件");
        message.setRecipients(RecipientType.TO, InternetAddress.parse("hxirui@163.com"));// 接收人
//        message.setRecipients(RecipientType.CC, InternetAddress.parse("1348800595@qq.com"));// 抄送人
//        message.setRecipients(RecipientType.BCC, InternetAddress.parse("1348800595@qq.com"));// 密送人
        MimeBodyPart bodyPartAttch = createAttachMent("log.txt");// 附件
//        MimeBodyPart bodyPartContentAndPic = createContentAndPic("I just want to Fuck",
//                "C:\\Users\\Administrator\\Desktop\\0.jpg");// 文本内容
        MimeMultipart mimeMuti = new MimeMultipart("mixed");
        mimeMuti.addBodyPart(bodyPartAttch);
//        mimeMuti.addBodyPart(bodyPartContentAndPic);
        message.setContent(mimeMuti);
        message.saveChanges();
        // message.setContent("Michael", "text/html;charset=gbk");
        // 发送邮件
        Transport.send(message);// 设置邮件接收人
    }

    // 创建附件
    public static MimeBodyPart createAttachMent(final String path) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        FileDataSource dataSource = new FileDataSource(new File(path));
        mimeBodyPart.setDataHandler(new DataHandler(dataSource));
        mimeBodyPart.setFileName(dataSource.getName());
        return mimeBodyPart;
    }

    // 创建文本和图片
    public static MimeBodyPart createContentAndPic(final String content, final String path) throws MessagingException {
        MimeMultipart mimeMutiPart = new MimeMultipart("related");
        // 图片
        MimeBodyPart picBodyPart = new MimeBodyPart();
        FileDataSource fileDataSource = new FileDataSource(new File(path));
        picBodyPart.setDataHandler(new DataHandler(fileDataSource));
        picBodyPart.setFileName(fileDataSource.getName());
        mimeMutiPart.addBodyPart(picBodyPart);
        // 文本
        MimeBodyPart contentBodyPart = new MimeBodyPart();
        contentBodyPart.setContent(content, "text/html;charset=gbk");
        mimeMutiPart.addBodyPart(contentBodyPart);
        // 图片和文本结合
        MimeBodyPart allBodyPart = new MimeBodyPart();
        allBodyPart.setContent(mimeMutiPart);
        return allBodyPart;
    }
}

class MyAuthenticator extends Authenticator {

    private static final String userName = "hxirui@163.com";
    private static final String passWord = "!xxx#EDssC";

    // * @author Michael.wu
    // * 密码和用户的验证
    public MyAuthenticator() {
        super();
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, passWord);
    }

}



