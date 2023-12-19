package com.ecommerce.api.eshopper.service.mailer_service;

import com.ecommerce.api.eshopper.dto.MailInfo;
import jakarta.mail.MessagingException;

public interface IMailerService {

   //Gửi thư
   void send(MailInfo mail) throws MessagingException;

   //Gửi thư với hình thức đơn giản - chỉ gồm người nhận, tiêu đề và nội dung
   void send(String to, String subject, String body) throws MessagingException;

   void queue(MailInfo mailInfo);

   void queue(String to, String subject, String body);

}
