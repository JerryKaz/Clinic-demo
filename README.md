# UPSA Clinic Management System

A modern desktop-based clinic management application built with Java Swing to improve healthcare operations and patient record management within the UPSA Clinic environment.

---

##  Overview

The **UPSA Clinic Management System** is a Java Swing application designed to streamline clinical workflows for students, staff, nurses, doctors, pharmacists, and administrators.

The system provides an intuitive and modern interface for:

* Patient registration
* Medical file management
* Vitals monitoring
* Diagnoses and prescriptions
* Pharmacy operations
* Bed management
* Billing and appointments
* Role-based access control

The project demonstrates how traditional Java Swing applications can be redesigned into visually appealing and modular healthcare systems suitable for real-world environments.

---
##  Screenshots

<p align="center">
  <img width="45%" src="https://github.com/user-attachments/assets/6b185133-c9fd-4116-bbeb-0813d644574c" />
  <img width="45%" src="https://github.com/user-attachments/assets/149000a7-0e05-4c6f-95e2-66e2370ab566" />
</p>

<p align="center">
  <img width="45%" src="https://github.com/user-attachments/assets/7c47ede8-ee34-4581-97f0-81efed9a99bc" />
  <img width="45%" src="https://github.com/user-attachments/assets/c5c9c8a3-b847-432c-8bc1-2ca4ea22080e" />
</p>

<p align="center">
  <img width="45%" src="https://github.com/user-attachments/assets/7732d7b7-2bb6-43b2-8571-22f6d0da72ae" />
  <img width="45%" src="https://github.com/user-attachments/assets/09461719-68fd-464c-b325-78cb030e0562" />
</p>

<p align="center">
  <img width="45%" src="https://github.com/user-attachments/assets/11b4f32c-dbff-4ad4-81d4-a6516049bd23" />
  <img width="45%" src="https://github.com/user-attachments/assets/de98d58a-78f5-45a2-80fb-21abaf570345" />
</p>

<p align="center">
  <img width="45%" src="https://github.com/user-attachments/assets/f91c4b1f-1b66-4c0b-9b42-143600ffe82f" />
  <img width="45%" src="https://github.com/user-attachments/assets/73cce2fb-2233-4ec4-86bf-ad5de26c103e" />
</p>

<p align="center">
  <img width="45%" src="https://github.com/user-attachments/assets/b179e813-5ba0-4acc-b8f4-40809e8431c7" />
  <img width="45%" src="https://github.com/user-attachments/assets/220b93d3-f971-4f7d-a2d8-4da2dd798fbf" />
</p>

<p align="center">
  <img width="45%" src="https://github.com/user-attachments/assets/9c44f307-a2cd-4ece-9b85-d9bcdd31eb6b" />
  <img width="45%" src="https://github.com/user-attachments/assets/68495346-1e6b-4966-828e-14c05a5b612e" />
</p>

---

## ✨ Features

### 🧑‍⚕️ Patient Management

* Student and staff registration
* Digital patient records
* Medical history tracking
* Blood group and genotype records
* Contact and academic information storage

### ❤️ Vitals Monitoring

* Blood pressure recording
* Temperature tracking
* Weight and pulse monitoring
* Abnormal vitals warning system

### 🩺 Doctor Module

* Patient history review
* Diagnosis recording
* Prescription management
* Clinical notes entry

###  Pharmacy Module

* Prescription verification
* Medication dispensing confirmation
* Medicine tracking

### Bed Management

* Bed allocation
* Occupancy monitoring
* Availability tracking

###  Appointments & Billing

* Appointment scheduling
* Financial transaction recording
* Operational summaries

###  Role-Based Access

* Patient access
* Nurse dashboard
* Doctor dashboard
* Pharmacist dashboard
* Admin dashboard

---

##  Technologies Used

* **Java**
* **Java Swing**
* **Java AWT**
* **Java I/O**
* **CardLayout**
* **Object-Oriented Programming (OOP)**

---

##  System Architecture

The application follows a layered modular architecture:

### Presentation Layer

Handles all UI components using Java Swing:

* Dashboard
* Sidebar navigation
* Header bar
* Panels and forms

### Logic Layer

Processes:

* Patient management
* Vitals handling
* Diagnosis workflows
* Prescriptions
* Appointments

### Data Layer

Uses Java I/O for:

* Saving records
* Loading files
* Updating patient information

Future support:

* SQLite
* MySQL
* JSON storage

---

##  Main Modules

| Module        | Description                                 |
| ------------- | ------------------------------------------- |
| DashboardView | Main application container using CardLayout |
| HeaderBar     | Search, logout, and user controls           |
| SidebarMenu   | Navigation between modules                  |
| PatientPanel  | Patient registration and management         |
| VitalsPanel   | Nurse vitals entry system                   |
| DoctorPanel   | Diagnosis and prescriptions                 |
| PharmacyPanel | Medication confirmation                     |
| AdminPanel    | Administrative controls                     |

---

##  System Flow

### Login Process

1. User opens application
2. Login page appears
3. User role is verified
4. Dashboard loads based on role

### Patient Workflow

* Register/Login
* Submit personal information
* Medical file created
* Data stored using student index number or staff ID

### Nurse Workflow

* View patient queue
* Record vitals
* Update patient status

### Doctor Workflow

* Access patient records
* Review medical history
* Enter diagnosis
* Prescribe medication

### Pharmacist Workflow

* View prescriptions
* Dispense medication
* Confirm treatment completion

---

##  Class Structure

### Core Classes

#### `MainApp`

* Starts the application
* Loads login and dashboard views

#### `User`

Superclass for:

* Patient
* Nurse
* Doctor
* Pharmacist
* Admin

#### `Patient`

Stores:

* Index number
* Programme
* Medical history
* Blood group
* Genotype

#### `Vitals`

Handles:

* Temperature
* Blood pressure
* Pulse
* Weight

#### `Diagnosis`

Stores:

* Medical condition
* Notes
* Prescriptions

#### `Prescription`

Manages:

* Drug names
* Dosage
* Dispensing confirmation

#### `FileManager`

Handles Java I/O operations:

* Save records
* Load records
* Update files

---

##  UI Design Highlights

* Modern dashboard layout
* Rounded buttons and cards
* Unified colour palette
* Sidebar navigation
* Clean spacing and typography
* Hover effects and interactive controls

---

##  Design Goals

### Usability

Designed for fast and intuitive clinical operations.

### Consistency

Unified styling across all panels and modules.

### Scalability

Modular structure allows easy future expansion.

---

##  Future Improvements

* Database integration (MySQL / SQLite)
* Authentication encryption
* PDF report generation
* Analytics dashboard
* Printing support
* Electronic Health Records (EHR)
* Notifications and messaging
* Cloud backup


---

##  Installation

### Clone the Repository

```bash
git clone https://github.com/JerryKaz/upsa-clinic-management-system.git
```

### Open Project
## NOTE: have Java install on your PC
Open the project in:

* IntelliJ IDEA
* NetBeans
* Eclipse

### Run the Application

Run:

```bash
MainApp.java
```

---

##  References

* Oracle Java Documentation
  [Oracle Java Documentation](https://docs.oracle.com/javase/?utm_source=chatgpt.com)

* Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1995).
  *Design Patterns: Elements of Reusable Object-Oriented Software.*

* Nielsen, J. (1994).
  *Usability Engineering.*

