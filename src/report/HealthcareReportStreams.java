package report;

import domain.Appointment;
import domain.MedicalStaff;
import domain.Patient;
import main.HealthcareApplication;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// genereaza rapoarte pe baza aplicatiei medicale
public class HealthcareReportStreams {

    // metoda returneaza numarul total de pacienti
    public int getNumberOfPatients(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllPatients().size();
    }

    // metoda returneaza numarul total de cadre medicale
    public int getNumberOfMedicalStaff(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllMedicalStaff().size();
    }

    // metoda returneaza numarul total de programari
    public int getNumberOfAppointments(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllAppointments().size();
    }

    // metoda returneaza un set ordonat de pacienti dupa numele complet
    public SortedSet<Patient> getPatientsSorted(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllPatients().stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(p -> p.getFirstName() + " " + p.getLastName()))));
    }

    // metoda returneaza un set ordonat de cadre medicale dupa nume
    public SortedSet<MedicalStaff> getMedicalStaffSorted(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllMedicalStaff().stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(MedicalStaff::getName))));
    }

    // metoda returneaza numarul total de programari intr-o zi specifica
    public long getAppointmentsCountByDate(HealthcareApplication healthcareApp, LocalDate date) {
        return healthcareApp.getAppointmentsByDate(date).size();
    }

    // metoda returneaza un map cu medicii si programarile lor
    public Map<MedicalStaff, List<Appointment>> getAppointmentsByDoctor(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllAppointments().stream()
                .collect(Collectors.groupingBy(appointment ->
                        healthcareApp.getAllMedicalStaff().stream()
                                .filter(staff -> staff.getName().equals(appointment.getDoctorName()))
                                .findFirst().orElse(null)));
    }

    // metoda returneaza un map cu pacientii si programarile lor
    public Map<Patient, List<Appointment>> getAppointmentsByPatient(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllAppointments().stream()
                .collect(Collectors.groupingBy(appointment ->
                        healthcareApp.getAllPatients().stream()
                                .filter(patient -> (patient.getFirstName() + " " + patient.getLastName())
                                        .equals(appointment.getPatientFullName()))
                                .findFirst().orElse(null)));
    }

    // metoda returneaza numarul total de specialitati unice
    public long getUniqueSpecialistsCount(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllMedicalStaff().stream()
                .map(MedicalStaff::getSpecialty)
                .distinct()
                .count();
    }
}
