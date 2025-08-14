package lk.wms.aquaflow.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailUtil {

    private static final String FROM_EMAIL = "infodinukalk@gmail.com";
    private static final String EMAIL_PASSWORD = "moodxpfozpmtpssw"; // Replace with your actual app password

    public static boolean sendEmail(String to, String subject, String body) {

        if (to == null || to.isEmpty() || subject == null || body == null) {
            return false;
        }
        

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        properties.put("mail.smtp.port", "587"); // TLS Port
        properties.put("mail.smtp.auth", "true"); // Enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        
        // Create session with authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
        
        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            
            // Send message
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String createComplaintResolutionEmailBody(String complaintId, String description) {
        return "Dear Customer,\n\n" +
               "We are pleased to inform you that your complaint (ID: " + complaintId + ") has been resolved.\n\n" +
               "Complaint Details:\n" +
               "------------------------\n" +
               description + "\n\n" +
               "If you have any further questions or concerns, please don't hesitate to contact us.\n\n" +
               "Thank you for your patience and understanding.\n\n" +
               "Best Regards,\n" +
               "AquaFlow Water Management System Team";
    }
}
