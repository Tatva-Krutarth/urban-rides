package com.urbanrides.helper;

import com.urbanrides.dtos.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import com.urbanrides.helper.EmailConfig;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailSend {


    private final JavaMailSender mailSender;

    @Autowired
    public EmailSend(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void userRegistrationOtp(UserRegistrationDto userRegistrationDto) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(userRegistrationDto.getEmail());
            message.setSubject("Urban Rides");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We have received an account creation request for ")
                    .append(userRegistrationDto.getEmail())
                    .append("</p>")
                    .append("<p>Your OTP is: <strong>")
                    .append(userRegistrationDto.getOtp())
                    .append("</strong></p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }
}
