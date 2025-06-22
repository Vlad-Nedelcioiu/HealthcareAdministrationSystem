package report;

import email.Email;
import email.EmailService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmailReportStreams {

    // metoda returneaza numarul total de emailuri trimise
    public int getTotalEmailsSent(EmailService emailService) {
        return emailService.getSentEmails();
    }

    // metoda returneaza numarul de emailuri trimise de fiecare expeditor
    public Map<String, Long> getEmailsPerSender(List<Email> emails) {
        return emails.stream()
                .collect(Collectors.groupingBy
                        (email -> email.getFrom().getName(),
                                Collectors.counting()));
    }

    // metoda returneaza numarul de emailuri primite de fiecare destinatar
    public Map<String, Long> getEmailsPerRecipient(List<Email> emails) {
        return emails.stream()
                .flatMap(email -> email.getTo().stream())
                .collect(Collectors.groupingBy
                        (patient -> patient.getFirstName() + " " + patient.getLastName(),
                                Collectors.counting()));
    }

    // metoda returneaza cele mai frecvente subiecte ale emailurilor
    public Map<String, Long> getMostFrequentSubjects(List<Email> emails) {
        return emails.stream()
                .collect(Collectors.groupingBy
                        (Email::getTitle,
                                Collectors.counting()));
    }
}
