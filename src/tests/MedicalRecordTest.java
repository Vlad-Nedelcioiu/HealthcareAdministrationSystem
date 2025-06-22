package tests;

import domain.MedicalRecord;
import exceptions.AppointmentConflictException;
import exceptions.InvalidCNPException;
import exceptions.MedicalStaffNotFoundException;
import exceptions.PatientAlreadyExistsException;
import main.HealthcareApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordTest {
    private HealthcareApplication healthcareApp;
    private int patient1Id;
    private int staff1Id;
    private LocalDateTime appointmentTime;

    @BeforeEach
    void setUp() throws PatientAlreadyExistsException, InvalidCNPException, AppointmentConflictException, MedicalStaffNotFoundException {
        healthcareApp = new HealthcareApplication();

        // pacient
        healthcareApp.registerPatient("John", "Doe", "1234567890123", "john.doe@example.com", LocalDate.of(1990, 1, 1));

        // doctor
        healthcareApp.registerMedicalStaff("Dr. Smith", "Cardiology");

        // ids
        patient1Id = healthcareApp.getAllPatients().get(0).getId();
        staff1Id = healthcareApp.getAllMedicalStaff().get(0).getId();

        // programari
        appointmentTime = LocalDateTime.of(2025, 2, 1, 10, 0);
        healthcareApp.scheduleAppointment(patient1Id, staff1Id, appointmentTime);
    }

    @Test
    void testAddMedicalRecordSuccessfully() {

        healthcareApp.addMedicalRecord(patient1Id, "Hypertension", "Lifestyle changes", "Blood pressure medication", appointmentTime, "Dr. Smith");

        List<MedicalRecord> records = healthcareApp.getMedicalRecordsByPatientId(patient1Id);

        assertEquals(1, records.size(), "There should be exactly one medical record.");
        MedicalRecord record = records.get(0);
        assertEquals("Hypertension", record.getDiagnosis(), "Diagnosis should match.");
        assertEquals("Lifestyle changes", record.getTreatment(), "Treatment should match.");
        assertEquals("Blood pressure medication", record.getMedications(), "Medications should match.");
        assertEquals("Dr. Smith", record.getDoctorName(), "Doctor name should match.");
    }

    @Test
    void testAddMedicalRecordForInvalidPatient() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                healthcareApp.addMedicalRecord(999, "Hypertension", "Lifestyle changes", "Blood pressure medication",
                        appointmentTime, "Dr. Smith")
        );
        assertEquals("Invalid patient ID: 999", exception.getMessage(), "Expected invalid patient ID exception.");
    }

    @Test
    void testGetMedicalRecordsByPatientId() {
        healthcareApp.addMedicalRecord(patient1Id, "Hypertension", "Lifestyle changes", "Blood pressure medication", appointmentTime, "Dr. Smith");
        healthcareApp.addMedicalRecord(patient1Id, "Diabetes", "Diet management", "Insulin", appointmentTime.plusMonths(1), "Dr. Smith");

        List<MedicalRecord> records = healthcareApp.getMedicalRecordsByPatientId(patient1Id);

        assertEquals(2, records.size(), "The patient should have exactly two medical records.");
        assertEquals("Hypertension", records.get(0).getDiagnosis(), "First diagnosis should be Hypertension.");
        assertEquals("Diabetes", records.get(1).getDiagnosis(), "Second diagnosis should be Diabetes.");
    }

    @Test
    void testRetrieveMedicalRecordsForNonexistentPatient() {
        List<MedicalRecord> records = healthcareApp.getMedicalRecordsByPatientId(999);
        assertTrue(records.isEmpty(), "Medical records list should be empty for a non-existent patient.");
    }
}
