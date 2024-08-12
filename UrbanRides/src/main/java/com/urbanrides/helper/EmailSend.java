package com.urbanrides.helper;

import com.urbanrides.dtos.PackageServiceDto;
import com.urbanrides.dtos.UserRegistrationDto;
import com.urbanrides.model.SupportTypeLogs;
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

    public void userRegistrationOtp(UserRegistrationDto userRegistrationDto, int otp) {
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
                    .append(otp)
                    .append("</strong></p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }

    public void userForgetOtp(String email, int otp) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We have received an account forget password request for ")
                    .append(email)
                    .append("</p>")
                    .append("<p>Your OTP is: <strong>")
                    .append(otp)
                    .append("</strong></p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }

    public void notifyRiderAboutTrip(PackageServiceDto packageServiceDto, String email, String tripCode) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Trip Request Details");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We have received a ")
                    .append(packageServiceDto.getServiceType())
                    .append(" request for your account: ")
                    .append(email)
                    .append("</p>")
                    .append("<p>Here are the details of your request:</p>")
                    .append("<ul>")
                    .append("<li><strong>Service Type:</strong> ")
                    .append(packageServiceDto.getServiceType())
                    .append("</li>");

            if (packageServiceDto.getServiceType().equals("Rent a taxi")) {
                content.append("<li><strong>Pickup/Drop-off Location:</strong> ")
                        .append(packageServiceDto.getPickup())
                        .append("</li>");
            } else {
                content.append("<li><strong>Pickup Location:</strong> ")
                        .append(packageServiceDto.getPickup())
                        .append("</li>")
                        .append("<li><strong>Drop-off Location:</strong> ")
                        .append(packageServiceDto.getDropOff())
                        .append("</li>");
            }

            content.append("<li><strong>Pickup Date:</strong> ")
                    .append(packageServiceDto.getPickUpDate())
                    .append("</li>")
                    .append("<li><strong>Drop-off Date:</strong> ")
                    .append(packageServiceDto.getDropOffDate())
                    .append("</li>")
                    .append("<li><strong>Pickup Time:</strong> ")
                    .append(packageServiceDto.getPickUpTime())
                    .append("</li>")
                    .append("<li><strong>Drop-off Time:</strong> ")
                    .append("</li>")
                    .append("<li><strong>Number of Passengers:</strong> ")
                    .append(packageServiceDto.getNumberOfPassengers())
                    .append("</li>")
                    .append("<li><strong>Emergency Contact:</strong> ")
                    .append(packageServiceDto.getEmergencyContact())
                    .append("</li>")
                    .append("<li><strong>Charges:</strong> ")
                    .append(packageServiceDto.getCharges())
                    .append("</li>")
                    .append("<li><strong>Trip Code:</strong> ")
                    .append(tripCode)
                    .append("</li>");

            if (packageServiceDto.getSpecialInstructions() != null && !packageServiceDto.getSpecialInstructions().isEmpty()) {
                content.append("<li><strong>Special Instructions:</strong> ")
                        .append(packageServiceDto.getSpecialInstructions())
                        .append("</li>");
            }

            content.append("</ul>")
                    .append("<p>If this request was not made by you, please contact our customer care at 8849430122.</p>")
                    .append("<p>If you have any questions or need further assistance, please contact our support team.</p>")
                    .append("<p>Thank you for choosing Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }



    public void concludeRentTaxiEmail(String email, String tripId, String conclusionDetails) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Rent Taxi Service Concluded");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>Your 'Rent a Taxi' service with trip ID <strong>")
                    .append(tripId)
                    .append("</strong> has been concluded by the captain.</p>")
                    .append("<p>Details:</p>")
                    .append("<p>")
                    .append(conclusionDetails)
                    .append("</p>")
                    .append("<p>Thank you for using Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }


    public void concludeDailyPickup(String email, String tripId, String pickupDetails) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Daily Pickup Service Concluded");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>Your 'Daily Pickup' service with trip ID <strong>")
                    .append(tripId)
                    .append("</strong> has been concluded.</p>")
                    .append("<p>Details:</p>")
                    .append("<p>")
                    .append(pickupDetails)
                    .append("</p>")
                    .append("<p>Thank you for using Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }


    public void acceptPackageRide(String email, String serviceType, String tripId) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Package Ride Accepted");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>Your package ride request for service type <strong>")
                    .append(serviceType)
                    .append("</strong> has been accepted.</p>")
                    .append("<p>Your trip ID is: <strong>")
                    .append(tripId)
                    .append("</strong></p>")
                    .append("<p>Thank you for using Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }

    public void blockUserMail(String email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Account Blocked");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello User</h2>")
                    .append("<p>We regret to inform you that your account has been blocked.</p>")
                    .append("<p>If you believe this is an error, please contact our support team for further assistance.</p>")
                    .append("<p>Thank you for your understanding.</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }
    public void unblockUserMail(String email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Account Unblocked");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello User</h2>")
                    .append("<p>We are pleased to inform you that your account has been unblocked.</p>")
                    .append("<p>You can now log in and continue using our services.</p>")
                    .append("<p>Thank you for your patience.</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }


    public void documentsNotApproved(String email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Document Not Approved");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We regret to inform you that the documents you submitted for verification have not been approved.</p>")
                    .append("<p>Please review the submission guidelines and resubmit the required documents.</p>")
                    .append("<p>To re-upload your documents, please <a href='https://yourwebsite.com/login'>log in</a> to your account and follow the instructions.</p>")
                    .append("<p>If you have any questions, feel free to contact our support team for assistance.</p>")
                    .append("<p>Thank you for your attention to this matter.</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }



    public void allDocumentsApproved(String email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Documents Approved");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We are pleased to inform you that all the documents you submitted have been approved.</p>")
                    .append("<p>Your account is now fully verified and you can start using all the features of Urban Rides.</p>")
                    .append("<p>Thank you for your patience and for choosing Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }
    public void getSupportRequest(String email, String requestCaseId, String requestType, String description) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Request Raised");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We have received your request and it is currently being processed.</p>")
                    .append("<p><strong>Request Details:</strong></p>")
                    .append("<ul>")
                    .append("<li><strong>Request Case ID:</strong> ").append(requestCaseId).append("</li>")
                    .append("<li><strong>Request Type:</strong> ").append(requestType).append("</li>")
                    .append("<li><strong>Description:</strong> ").append(description).append("</li>")
                    .append("</ul>")
                    .append("<p>We recommend you check the status of your request to ensure everything is in order.</p>")
                    .append("<p>Thank you for your patience and for choosing Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }



    public void loginCredentialUpdated(String email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(email);
            message.setSubject("Urban Rides - Login Credentials Updated");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We wanted to inform you that your login credentials have been successfully updated.</p>")
                    .append("<p>If you did not request this change, please contact our support team immediately.</p>")
                    .append("<p>Thank you for using Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }

    public void acceptSupportRequest(SupportTypeLogs supportTypeLogs) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(supportTypeLogs.getUserObj().getEmail());
            message.setSubject("Urban Rides - Support Request Accepted");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We are pleased to inform you that your support request has been accepted and is being processed.</p>")
                    .append("<p>Our support team will get in touch with you soon to provide further assistance.</p>")
                    .append("<p>Thank you for reaching out to Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }


    public void concludeSupportRequest(SupportTypeLogs supportTypeLogs) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom("urbanrides070@outlook.com");
            message.setTo(supportTypeLogs.getUserObj().getEmail());
            message.setSubject("Urban Rides - Support Request Concluded");

            StringBuilder content = new StringBuilder();
            content.append("<html><h1>Urban Rides</h1>")
                    .append("<br>")
                    .append("<h2>Hello Rider</h2>")
                    .append("<p>We wanted to inform you that your support request has been successfully concluded.</p>")
                    .append("<p>If you have any further questions or need additional assistance, please do not hesitate to contact us.</p>")
                    .append("<p>Thank you for reaching out to Urban Rides!</p>")
                    .append("</html>");

            message.setText(content.toString(), true);
        };
        this.mailSender.send(messagePreparator);
    }

}
