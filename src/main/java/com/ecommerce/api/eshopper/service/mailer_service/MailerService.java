package com.ecommerce.api.eshopper.service.mailer_service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ecommerce.api.eshopper.dto.MailInfo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailerService implements IMailerService {
    
    List<MailInfo> list = new ArrayList<>();
    
    private final JavaMailSender sender;

    @Override
    public void send(MailInfo mailInfo) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();

        //Sử dụng helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setFrom(mailInfo.getFrom());
        helper.setTo(mailInfo.getTo());
        helper.setSubject(mailInfo.getSubject());
        helper.setText(mailInfo.getBody(), true);
        helper.setReplyTo(mailInfo.getFrom());

        String[] cc = mailInfo.getCc();
        //Kiểm tra mảng cc có tồn tại hay không?
        if(cc != null && cc.length > 0) {
            helper.setCc(cc);
        }

        String[] bcc = mailInfo.getBcc();
        //Kiểm tra mảng bcc có tồn tại hay không?
        if(bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }

        //Mảng file
        List<File> files = mailInfo.getFiles();
        if (files.size() > 0) {
            for (File file : files) {
                helper.addAttachment(file.getName(), file);
            }
        }

        //Gửi message đến SMTP server
        sender.send(message);

    }

    @Override
    public void send(String to, String subject, String body) throws MessagingException {
        this.send(new MailInfo(to, subject, body));
    }

    //Add mail danh sách mail
    @Override
    public void queue(MailInfo mailInfo) {
        list.add(mailInfo);
    }

    public void queue(String to, String subject, String body) {
        list.add(new MailInfo(to, subject, body));
    }

    //Cứ mỗi 5s sẽ chạy hàm run() một lần
    @Scheduled(fixedDelay = 5000)
    public void run() {
        while (!list.isEmpty()) {
            MailInfo mail = list.remove(0);
            try {
                this.send(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
