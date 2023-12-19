package com.ecommerce.api.eshopper.controller.admin.mailer_controller;

import com.ecommerce.api.eshopper.dto.MailInfo;
import com.ecommerce.api.eshopper.service.mailer_service.IMailerService;
import lombok.RequiredArgsConstructor;

import java.util.Random;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mailer")
public class MailerApi {

    private final IMailerService mailerService;

    @PostMapping("/send")
    public ResponseEntity<?> mail(@RequestParam String txtTo) {

            try {
                MailInfo mailInfo = new MailInfo();
                mailInfo.setTo(txtTo);

                StringBuilder txtContent = new StringBuilder();
                txtContent.append("Verification code: ");
                Random random = new Random();
                int code = random.nextInt(900000) + 100000;
                txtContent.append(code);

                mailInfo.setBody(txtContent.toString());

                // mailerService.send(mailInfo);
                mailerService.queue(mailInfo);
                return new ResponseEntity(mailInfo, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

    }

}
