package email;

import java.io.Serializable;

// clasa se ocupa cu gestionarea si trimiterea emailurilor in sistem
public class EmailService implements Runnable, Serializable {
    private static final long serialVersionUID = -6872857384878095572L;
    private Queue queue = new Queue();
    private boolean closed;
    private int sentEmails = 0;


    // constructorul clasei, porneste un nou thread care ruleaza metoda run()
    public EmailService() {
        new Thread(this).start();
    }

    // metoda care ruleaza in thread separat si proceseaza emailurile din coada
    @Override
    public void run() {
        Email email;
        while (true) {
            if (closed) {
                return;
            }

            if ((email = queue.get()) != null) {
                sendEmail(email);
            }
            try {
                synchronized (queue) { // sincronizeaza accesul la coada
                    queue.wait(); // asteapta pana cand un nou email este adaugat
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }


    //returneaza numarul total de emailuri trimise
    public int getSentEmails() {
        return sentEmails;
    }

    //simuleaza trimiterea unui email
    private void sendEmail(Email email) {
        System.out.println(email);
        sentEmails++;
    }

    // metoda care adauga un email in coada si notifica thread-ul sa il proceseze
    public void sendNotificationEmail(Email email) throws EmailException {
        if (!closed) {
            queue.add(email);
            synchronized (queue) { // sincronizeaza accesul la coada
                queue.notify();
            }
        } else {
            throw new EmailException("Mailbox is closed!");
        }
    }

    //inchide serviciul de email
    public void close() {
        closed = true;
        synchronized (queue) {
            queue.notify();
        }
    }

    public boolean isClosed() {
        return closed;
    }
}
