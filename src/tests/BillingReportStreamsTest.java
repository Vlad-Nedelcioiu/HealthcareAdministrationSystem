package tests;

import exceptions.AppointmentConflictException;
import exceptions.InvalidCNPException;
import exceptions.MedicalStaffNotFoundException;
import exceptions.PatientAlreadyExistsException;
import main.HealthcareApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import report.BillingReportStreams;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BillingReportStreamsTest {
    private HealthcareApplication healthcareApp;
    private BillingReportStreams reportStreams;

    // metoda care initializeaza datele pentru testare
    @BeforeEach
    void setUp() throws AppointmentConflictException, MedicalStaffNotFoundException, PatientAlreadyExistsException, InvalidCNPException {
        healthcareApp = new HealthcareApplication();
        reportStreams = new BillingReportStreams();


        healthcareApp.registerPatient("John", "Doe", "1234567890123", "john.doe@example.com", LocalDate.of(1990, 1, 1));
        healthcareApp.registerPatient("Jane", "Doe", "1234567890124", "jane.doe@example.com", LocalDate.of(1995, 5, 15));

        healthcareApp.registerMedicalStaff("Dr. Smith", "Cardiology");
        healthcareApp.registerMedicalStaff("Dr. Adams", "Neurology");

        // id uri pentru pacienti si cadre medicale
        int patient1Id = healthcareApp.getAllPatients().get(0).getId();
        int patient2Id = healthcareApp.getAllPatients().get(1).getId();
        int staff1Id = healthcareApp.getAllMedicalStaff().get(0).getId();
        int staff2Id = healthcareApp.getAllMedicalStaff().get(1).getId();

        // programeaza consultatii
        healthcareApp.scheduleAppointment(patient1Id, staff1Id, LocalDateTime.of(2025, 2, 1, 10, 0));
        healthcareApp.scheduleAppointment(patient2Id, staff2Id, LocalDateTime.of(2025, 2, 2, 14, 0));

        // genereaza facturi
        healthcareApp.generateBill(patient1Id, staff1Id, "Consultation", 150.0);
        healthcareApp.generateBill(patient2Id, staff2Id, "Check-up", 100.0);
        healthcareApp.generateBill(patient1Id, staff1Id, "X-Ray", 200.0);
        healthcareApp.generateBill(patient2Id, staff2Id, "Consultation", 150.0);
    }


    @Test
    void testGetTotalRevenue() {
        assertEquals(600.0, reportStreams.getTotalRevenue(healthcareApp));
    }

    @Test
    void testGetRevenuePerPatient() {
        Map<String, Double> revenuePerPatient = reportStreams.getRevenuePerPatient(healthcareApp);
        assertEquals(350.0, revenuePerPatient.get("John Doe"));
        assertEquals(250.0, revenuePerPatient.get("Jane Doe"));
    }

    @Test
    void testGetMostFrequentServices() {
        Map<String, Long> frequentServices = reportStreams.getMostFrequentServices(healthcareApp);
        assertEquals(2, frequentServices.get("Consultation"));
        assertEquals(1, frequentServices.get("X-Ray"));
        assertEquals(1, frequentServices.get("Check-up"));
    }

    @Test
    void testGetRevenuePerDate() {
        Map<LocalDate, Double> revenuePerDate = reportStreams.getRevenuePerDate(healthcareApp);
        assertEquals(350.0, revenuePerDate.get(LocalDate.of(2025, 2, 1)));
        assertEquals(250.0, revenuePerDate.get(LocalDate.of(2025, 2, 2)));
    }

    @Test
    void testGetTopPayingPatients() {
        List<Map.Entry<String, Double>> topPayingPatients = reportStreams.getTopPayingPatients(healthcareApp, 2);
        assertEquals(2, topPayingPatients.size());
        assertEquals("John Doe", topPayingPatients.get(0).getKey());
        assertEquals("Jane Doe", topPayingPatients.get(1).getKey());
    }
}