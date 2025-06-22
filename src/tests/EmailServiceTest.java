package tests;

import domain.MedicalStaff;
import domain.Patient;
import email.Email;
import email.EmailException;
import email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {
    private EmailService emailService;
    private Email email;
    private MedicalStaff sender;
    private Patient receiver;

    @BeforeEach
    void setUp() {
        emailService = new EmailService();

        sender = new MedicalStaff("Dr. Smith", "Cardiology");
        receiver = new Patient("Jane", "Doe", "1234567890123", "jane.doe@example.com", null);

        // creaza un email de notificare
        email = new Email()
                .setFrom(sender)
                .setTo(receiver)
                .setTitle("Follow-up Appointment Reminder")
                .setBody("Your follow-up appointment is scheduled for next week.");
    }

    @Test
    void testSendNotificationEmail() throws EmailException {
        emailService.sendNotificationEmail(email);
        assertEquals(0, emailService.getSentEmails(), "Email should not be sent immediately.");

        synchronized (emailService) {
            emailService.notify(); // notifica serviciul de emailuri
        }

        try {
            Thread.sleep(100); // lasa timp pentru procesarea emailului
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(1, emailService.getSentEmails(), "Email should have been sent.");
    }

    @Test
    void testSendEmailAfterClosingService() {
        emailService.close();
        EmailException exception = assertThrows(EmailException.class, () -> {
            emailService.sendNotificationEmail(email);
        });
        assertEquals("Mailbox is closed!", exception.getMessage(), "Expected mailbox closed exception.");
    }

    @Test
    void testQueueStoresEmailsBeforeProcessing() {
        if (!emailService.isClosed()) { // Ensure the email service is open
            assertDoesNotThrow(() -> emailService.sendNotificationEmail(email));
        } else {
            fail("Email service should not be closed before sending emails.");
        }
    }
}
