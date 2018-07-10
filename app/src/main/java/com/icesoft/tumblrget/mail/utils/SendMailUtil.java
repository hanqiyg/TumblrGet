package com.icesoft.tumblrget.mail.utils;

import android.support.annotation.NonNull;

import com.icesoft.tumblrget.mail.MailSender;
import com.icesoft.tumblrget.mail.models.MailInfo;

import java.io.File;

/**
 * Created by Administrator on 2017/4/10.
 */

public class SendMailUtil {

    //qq
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "465";
    private static final String FROM_ADD = "hanqiyg@gmail.com";
    private static final String FROM_PSW = "link,hqy123";
    private static final String TO_ADD = "hanqiyg@gmail.com";

    public static void send(String subject,String content){
        final MailInfo mailInfo = creatMail(subject,content);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
                System.out.println(mailInfo.toString());
            }
        }).start();
    }

    @NonNull
    private static MailInfo creatMail(String subject,String content) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(TO_ADD); // 发到哪个邮件去
        mailInfo.setSubject(subject); // 邮件主题
        mailInfo.setContent(content); // 邮件文本
        return mailInfo;
    }

}
