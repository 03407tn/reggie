package com.tn.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 短信发送工具类
 */
@Component
public class SMSUtils {
	@Value("${spring.mail.username}")
	String from;

	@Autowired
	JavaMailSender mailSender;


	//简单邮件
	public void sendSimpleMail(String to, String subject, String content){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from); //发件人
		message.setTo(to);//收件人
		message.setSubject(subject); //标题
		message.setText(content);   //文件内容

		try {
			mailSender.send(message);
			System.out.println("简单邮件发送成功！");
		} catch (Exception e){
			System.out.println("发送简单邮件时发生异常！"+e);
		}
	}
}
