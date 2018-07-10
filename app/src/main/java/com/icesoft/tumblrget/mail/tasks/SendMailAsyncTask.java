package com.icesoft.tumblrget.mail.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.icesoft.tumblrget.R;
import com.icesoft.tumblrget.mail.authenticator.MyAuthenticator;
import com.icesoft.tumblrget.mail.models.MailInfo;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailAsyncTask extends AsyncTask<String,Integer,String> {
    private static final String HOST = "smtp-mail.outlook.com";
    private static final String PORT = "587";
    private static final String FROM_ADD = "hanqiyg@outlook.com";
    private static final String FROM_PSW = "link,hqy123";
    private static final String TO_ADD = "hanqiyg@outlook.com";

    private Context mContext;
    public SendMailAsyncTask(Context context){
        this.mContext = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD);
        mailInfo.setPassword(FROM_PSW);
        mailInfo.setFromAddress(FROM_ADD);
        mailInfo.setToAddress(TO_ADD);
        mailInfo.setSubject("GetTumblr");
        mailInfo.setContent(strings[0]);

        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());

            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
            // 发送邮件
            Transport.send(mailMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(mContext, R.string.string_mail_sent,Toast.LENGTH_LONG).show();
    }
}
