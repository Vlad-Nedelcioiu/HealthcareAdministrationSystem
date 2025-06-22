package main;

import domain.*;
import exceptions.AppointmentConflictException;
import exceptions.InvalidCNPException;
import exceptions.MedicalStaffNotFoundException;
import exceptions.PatientAlreadyExistsException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// clasa gestioneaza pacientii, personalul medical, programarile, facturile si fisele medicale
public class HealthcareApplication {

    // mapare id pacient prin ConcurrentHashMap (thread-safe fara a fi nevoie de sincronizare)
    private final Map<Integer, Patient> patients = new ConcurrentHashMap<>();

    // mapare id medic prin ConcurrentHashMap (thread-safe fara a fi nevoie de sincronizare)
    private final Map<Integer, MedicalStaff> medicalStaff = new ConcurrentHashMap<>();

    // lista sincronizata care stocheaza programarile medicale
    private final List<Appointment> appointments = Collections.synchronizedList(new ArrayList<>());

    // lista sincronizata care stocheaza facturile medicale
    private final List<Billing> billings = Collections.synchronizedList(new ArrayList<>());

    // mapare pentru fiecare pacient cu o lista de fise medicale prin ConcurrentHashMap (thread-safe fara a fi nevoie de sincronizare)
    private final Map<Integer, List<MedicalRecord>> medicalRecords = new ConcurrentHashMap<>();


    // metoda care inregistreaza un nou pacient in sistem
    public void registerPatient(String firstName, String lastName, String cnp, String email, LocalDate dob) throws PatientAlreadyExistsException, InvalidCNPException {
        validateCNP(cnp);
        boolean exists = patients.values().stream()
                .anyMatch(patient -> patient.getCnp().equals(cnp));
        if (exists) {
            throw new PatientAlreadyExistsException("A patient with the CNP " + cnp + " already exists.");
        }
        Patient patient = new Patient(firstName, lastName, cnp, email, dob);
        patients.put(patient.getId(), patient);
    }

    // metoda inregistreaza un nou medic in sistem
    public void registerMedicalStaff(String name, String specialty) {
        MedicalStaff staff = new MedicalStaff(name, specialty);
        medicalStaff.put(staff.getId(), staff);
    }

    // metoda programeaza o consultatie intre pacient si medic
    public void scheduleAppointment(int patientId, int staffId, LocalDateTime dateTime) throws MedicalStaffNotFoundException, AppointmentConflictException {
        Patient patient = patients.get(patientId); // obtine pacientul din map
        MedicalStaff staff = medicalStaff.get(staffId); // obtine medicul din map

        if (staff == null) { // daca medicul nu exista, arunca exceptia
            throw new MedicalStaffNotFoundException("Medical staff with ID " + staffId + " does not exist.");
        }

        // verifica daca exista deja o programare la acelasi medic la aceeasi ora
        boolean conflict = appointments.stream()
                .anyMatch(a -> a.getDoctorName().equals(staff.getName()) && a.getAppointmentDate().isEqual(dateTime));
        if (conflict) {
            throw new AppointmentConflictException("The appointment conflicts with an existing appointment for Dr. " + staff.getName());
        }

        // creeaza si adauga o noua programare
        Appointment appointment = new Appointment(patient.getFirstName() + " " + patient.getLastName(), staff.getName(), dateTime);
        appointments.add(appointment);
    }

    // metoda returneaza toti pacientii inregistrati
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    // metoda returneaza toti medicii inregistrati
    public List<MedicalStaff> getAllMedicalStaff() {
        return new ArrayList<>(medicalStaff.values());
    }

    // metoda returneaza toate programarile existente
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    // metoda returneaza programarile pentru un anumit medic
    public List<Appointment> getAppointmentsByDoctor(String doctorName) {
        return appointments.stream()
                .filter(a -> a.getDoctorName().equalsIgnoreCase(doctorName))
                .collect(Collectors.toList());
    }

    // metoda care returneaza programarile pentru o anumita data
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointments.stream()
                .filter(a -> a.getAppointmentDate().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    // metoda care actualizeaza adresa de email a unui pacient
    public void updatePatientEmail(int patientId, String newEmail) {
        Patient patient = patients.get(patientId);
        if (patient != null) {
            patient.setEmail(newEmail);
        } else {
            throw new IllegalArgumentException("Patient not found");
        }
    }

    // metoda care verifica daca un CNP este valid
    public void validateCNP(String cnp) throws InvalidCNPException {
        if (cnp == null || cnp.length() != 13 || !cnp.matches("\\d+")) {
            throw new InvalidCNPException("Invalid CNP: " + cnp);
        }
    }

    // metoda care sterge un pacient din sistem
    public void deletePatient(int patientId) {
        patients.remove(patientId);
    }

    // metoda care sterge un medic din sistem
    public void deleteMedicalStaff(int staffId) {
        medicalStaff.remove(staffId);
    }

    // metoda care anuleaza o programare
    public void cancelAppointment(int appointmentId) {
        appointments.removeIf(a -> a.getId() == appointmentId);
    }

    // metoda care genereaza o factura pentru un pacient
    public void generateBill(int patientId, int staffId, String services, double cost) {
        Patient patient = patients.get(patientId);
        MedicalStaff staff = medicalStaff.get(staffId);

        if (patient == null || staff == null) {
            throw new IllegalArgumentException("Invalid patient or staff ID");
        }

        // verifica daca pacientul are o programare la medicul respectiv
        Optional<Appointment> appointment = appointments.stream()
                .filter(a -> a.getPatientFullName().equals(patient.getFirstName() + " " + patient.getLastName()) &&
                        a.getDoctorName().equals(staff.getName()))
                .findFirst();

        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("No appointment found for the given patient and staff");
        }

        Billing bill = new Billing(patient.getId(), patient.getFirstName() + " " + patient.getLastName(),
                services, cost, appointment.get().getAppointmentDate());
        billings.add(bill);
    }

    // metoda care returneaza toate facturile existente
    public List<Billing> getAllBills() {
        return new ArrayList<>(billings);
    }

    // metoda care returneaza facturile unui pacient
    public List<Billing> getBillsByPatientId(int patientId) {
        return billings.stream()
                .filter(bill -> bill.getPatientId() == patientId)
                .toList();
    }

    // metoda care adauga o fisa medicala pentru un pacient
    public void addMedicalRecord(int patientId, String diagnosis, String treatment, String medications, LocalDateTime appointmentDate, String doctorName) {
        Patient patient = patients.get(patientId); // obtine pacientul din map

        if (patient == null) {
            throw new IllegalArgumentException("Invalid patient ID: " + patientId); // daca pacientul nu exista, arunca exceptia
        }

        MedicalRecord record = new MedicalRecord(diagnosis, treatment, medications, appointmentDate, doctorName);
        medicalRecords.computeIfAbsent(patientId, k -> new ArrayList<>()).add(record);
    }

    // metoda care returneaza toate fisele medicale pentru un pacient
    public List<MedicalRecord> getMedicalRecordsByPatientId(int patientId) {
        return medicalRecords.getOrDefault(patientId, new ArrayList<>());
    }


}
