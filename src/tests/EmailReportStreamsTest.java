package tests;

import domain.MedicalStaff;
import domain.Patient;
import email.Email;
import email.EmailService;
import email.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import report.EmailReportStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmailReportStreamsTest {
    private EmailService emailService;
    private EmailReportStreams reportStreams;
    private List<Email> emailList;

    @BeforeEach
    void setUp() {
        emailService = new EmailService();
        reportStreams = new EmailReportStreams();
        emailList = new ArrayList<>();

        // creaza cadre medicale si pacienti
        MedicalStaff sender1 = new MedicalStaff("Dr. Smith", "Cardiology");
        MedicalStaff sender2 = new MedicalStaff("Dr. Adams", "Neurology");
        Patient recipient1 = new Patient("John", "Doe", "1234567890123", "john.doe@example.com", null);
        Patient recipient2 = new Patient("Jane", "Doe", "1234567890124", "jane.doe@example.com", null);

        // creaza emailuri
        Email email1 = new Email().setFrom(sender1).setTo(recipient1).setTitle("Follow-up Reminder").setBody("Your next appointment is in a week.");
        Email email2 = new Email().setFrom(sender1).setTo(recipient2).setTitle("Medical Report").setBody("Your latest medical report is attached.");
        Email email3 = new Email().setFrom(sender2).setTo(recipient1).setTitle("Follow-up Reminder").setBody("Reminder for your upcoming visit.");

        emailList.add(email1);
        emailList.add(email2);
        emailList.add(email3);

        // simuleaza trimiterea de emailuri
        try {
            emailService.sendNotificationEmail(email1);
            emailService.sendNotificationEmail(email2);
            emailService.sendNotificationEmail(email3);
        } catch (EmailException e) {
            fail("Exception occurred while sending emails: " + e.getMessage());
        }
    }

    @Test
    void testGetEmailsPerSender() {
        Map<String, Long> emailsPerSender = reportStreams.getEmailsPerSender(emailList);
        assertEquals(2, emailsPerSender.get("Dr. Smith"));
        assertEquals(1, emailsPerSender.get("Dr. Adams"));
    }

    @Test
    void testGetEmailsPerRecipient() {
        Map<String, Long> emailsPerRecipient = reportStreams.getEmailsPerRecipient(emailList);
        assertEquals(2, emailsPerRecipient.get("John Doe"));
        assertEquals(1, emailsPerRecipient.get("Jane Doe"));
    }

    @Test
    void testGetMostFrequentSubjects() {
        Map<String, Long> frequentSubjects = reportStreams.getMostFrequentSubjects(emailList);
        assertEquals(2, frequentSubjects.get("Follow-up Reminder"));
        assertEquals(1, frequentSubjects.get("Medical Report"));
    }
}
