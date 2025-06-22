# Healthcare Administration System

A lightweight Java application that demonstrates a **mini hospital back-office**: patient registry, medical staff management, appointment scheduling with conflict detection, billing, email notifications and stream-based reports – all implemented with plain **Java 17** and thoroughly unit-tested with **JUnit 5**.

---

## Features

| Module | Highlights |
|--------|------------|
| **Patient & Medical Staff Registry** | Validates Romanian CNPs, prevents duplicates, full CRUD operations |
| **Appointment Scheduling** | Detects overlapping slots and raises `AppointmentConflictException` |
| **Billing** | Generates invoices from appointments and aggregates totals |
| **Email Service & Queue** | Async mail dispatch with retry / failure handling |
| **Streams-Based Reporting** | `BillingReportStreams`, `EmailReportStreams`, `HealthcareReportStreams` showcase Java Streams analytics |
| **Unit-Test Suite** | 40+ tests written with JUnit 5 (100 % of business code covered) |

---

## Project layout
```text
src/
 ├─ domain/              ← POJOs: Patient, MedicalStaff, Appointment, Billing, MedicalRecord
 ├─ email/               ← Email, EmailService, Queue
 ├─ exceptions/          ← Custom runtime exceptions
 ├─ report/              ← Stream-based report generators
 ├─ main/                ← Application entry point (`HealthcareApplication`)
 └─ tests/               ← Unit tests
lib/                     ← JUnit 5 & test harness JARs
.idea/ *.iml             ← IntelliJ project metadata
```

---

## Getting started

### Prerequisites
* **JDK 17+**
* (Optional) **IntelliJ IDEA 2023.3+**

### 1. Clone
```bash
git clone https://github.com/Vlad-Nedelcioiu/HealthcareAdministrationSystem.git
cd HealthcareAdministrationSystem
```

### 2. Build / run in the IDE
1. *File ▸ Open* → select the project root.  
2. Ensure *Project SDK* is **JDK 17**.  
3. Right-click `HealthcareApplication` → **Run**.

### 3. Build / run from the command-line
```bash
# compile everything
javac -cp "lib/*" -d out $(find src -name "*.java")

# run the app
java -cp "out:lib/*" main.HealthcareApplication
```

### 4. Execute tests
```bash
java -jar lib/junit-platform-console-standalone-1.8.1.jar      --classpath out --scan-classpath
```

---

## Used technologies
* **Java 17** (plain Java SE)
* **JUnit 5** (Jupiter) for testing
* **OpenTest4j** for assertion failures

---

## Roadmap
* Persist data with a relational DB (JPA / Hibernate)
* Expose REST API with Spring Boot
* Add JavaFX or React front-end
* Dockerise & add CI/CD (GitHub Actions)

---

## Contributing
1. **Fork** the repository  
2. Create a feature branch (`git checkout -b feat/awesome`)  
3. Commit your changes & push (`git commit -m "Add awesome"`)  
4. Open a **Pull Request**

Please keep **100 % test coverage**

---

## License
_TBD_ – choose **MIT**, **Apache-2.0** or similar before public release.

---

> _Generated on 22 June 2025 – based on commit `9c5b5ea`._
