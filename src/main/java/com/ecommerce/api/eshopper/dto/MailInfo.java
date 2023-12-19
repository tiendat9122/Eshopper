package com.ecommerce.api.eshopper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {

    //Người gửi
    private String from = "EshopperStore";

    //Người nhận
    private String to;

    //Hình thức gửi nhiều người - và người nhận biết được danh sách người gửi là ai
    private String[] cc;

    //Hình thức gửi nhiều người - và người nhận không biết được danh sách người gửi
    private String[] bcc;

    //Tiêu đề của thư
    private String subject = "Forgot password";

    //Nội dung thư
    private String body;

    //Những File đính kèm
    private List<File> files = new ArrayList<>();

    public MailInfo(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

}
