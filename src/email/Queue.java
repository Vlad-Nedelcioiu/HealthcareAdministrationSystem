package email;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// clasa o coada sincronizata care stocheaza obiecte de tip Email. Trimite mailuri in mod asincron
public class Queue implements Serializable {
    private static final long serialVersionUID = -367534955230149744L;

    // lista sincronizata in care sunt stocate emailurile, pentru a evita probleme in multi-threading
    private List<Email> emails = Collections.synchronizedList(new LinkedList<>());

    // metoda care adauga un email in coada
    public void add(Email email) {
        emails.add(email);
    }


    // metoda care returneaza si elimina ultimul email din coada
    public Email get() {
        if (!emails.isEmpty()) {
            return emails.remove(emails.size() - 1); // elimina si returneaza ultimul email adaugat
        }
        return null;
    }
}
