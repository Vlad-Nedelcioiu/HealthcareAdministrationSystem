package tests;

import domain.Appointment;
import domain.MedicalStaff;
import domain.Patient;
import exceptions.AppointmentConflictException;
import exceptions.InvalidCNPException;
import exceptions.MedicalStaffNotFoundException;
import exceptions.PatientAlreadyExistsException;
import main.HealthcareApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import report.HealthcareReportStreams;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthcareReportStreamsTest {
    private HealthcareApplication healthcareApp;
    private HealthcareReportStreams reportStreams;

    @BeforeEach
    void setUp() throws PatientAlreadyExistsException, AppointmentConflictException, MedicalStaffNotFoundException, InvalidCNPException {
        healthcareApp = new HealthcareApplication();
        reportStreams = new HealthcareReportStreams();

        healthcareApp.registerMedicalStaff("Dr. Smith", "Cardiology");
        healthcareApp.registerMedicalStaff("Dr. Adams", "Neurology");
        healthcareApp.registerMedicalStaff("Dr. Taylor", "Orthopedics");

        healthcareApp.registerPatient("John", "Doe", "1234567890123", "john.doe@example.com", LocalDate.of(1990, 1, 1));
        healthcareApp.registerPatient("Jane", "Doe", "1234567890124", "jane.doe@example.com", LocalDate.of(1995, 5, 15));

        // ids
        int patient1Id = healthcareApp.getAllPatients().get(0).getId();
        int patient2Id = healthcareApp.getAllPatients().get(1).getId();
        int staff1Id = healthcareApp.getAllMedicalStaff().get(0).getId();
        int staff2Id = healthcareApp.getAllMedicalStaff().get(1).getId();

        // programari
        healthcareApp.scheduleAppointment(patient1Id, staff1Id, LocalDateTime.of(2025, 1, 30, 10, 0));
        healthcareApp.scheduleAppointment(patient2Id, staff2Id, LocalDateTime.of(2025, 1, 31, 11, 0));
    }


    @Test
    void testGetNumberOfPatients() {
        int patientCount = reportStreams.getNumberOfPatients(healthcareApp);
        assertEquals(2, patientCount, "The total number of patients should be 2.");
    }

    @Test
    void testGetNumberOfMedicalStaff() {
        int staffCount = reportStreams.getNumberOfMedicalStaff(healthcareApp);
        assertEquals(3, staffCount, "The total number of medical staff should be 3.");
    }

    @Test
    void testGetNumberOfAppointments() {
        int appointmentCount = reportStreams.getNumberOfAppointments(healthcareApp);
        assertEquals(2, appointmentCount, "The total number of appointments should be 2.");
    }

    @Test
    void testGetPatientsSorted() {
        SortedSet<Patient> sortedPatients = reportStreams.getPatientsSorted(healthcareApp);
        assertEquals(2, sortedPatients.size(), "There should be 2 sorted patients.");
        assertTrue(sortedPatients.first().getFirstName().equals("Jane"), "The first patient should be Jane.");
    }

    @Test
    void testGetMedicalStaffSorted() {
        SortedSet<MedicalStaff> sortedStaff = reportStreams.getMedicalStaffSorted(healthcareApp);
        assertEquals(3, sortedStaff.size(), "There should be 3 sorted staff members.");
        assertTrue(sortedStaff.first().getName().equals("Dr. Adams"), "The first staff member should be Dr. Adams.");
    }

    @Test
    void testGetAppointmentsCountByDate() {
        long appointmentCount = reportStreams.getAppointmentsCountByDate(healthcareApp, LocalDate.of(2025, 1, 30));
        assertEquals(1, appointmentCount, "There should be 1 appointment on 2025-01-30.");
    }

    @Test
    void testGetAppointmentsByPatient() {
        Map<Patient, List<Appointment>> appointmentsByPatient = reportStreams.getAppointmentsByPatient(healthcareApp);
        assertEquals(2, appointmentsByPatient.size(), "There should be 2 patients in the map.");
        assertEquals(1, appointmentsByPatient.get(healthcareApp.getAllPatients().get(0)).size(), "John Doe should have 1 appointment.");
    }

    @Test
    void testGetUniqueSpecialistsCount() {
        long uniqueSpecialists = reportStreams.getUniqueSpecialistsCount(healthcareApp);
        assertEquals(3, uniqueSpecialists, "There should be 3 unique specialists.");
    }
}
