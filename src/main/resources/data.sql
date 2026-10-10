INSERT INTO patients (id, name, age, gender, phone, email, address, medical_history) VALUES
(1, 'Alice Johnson', 32, 'Female', '555-1010', 'alice@example.com', '123 Main St', 'No major allergies'),
(2, 'Robert Smith', 45, 'Male', '555-2020', 'robert@example.com', '45 Oak Ave', 'Hypertension monitored');

INSERT INTO doctors (id, name, specialty, department, phone, email) VALUES
(1, 'Dr. Emily Carter', 'Cardiology', 'Cardiology', '555-3001', 'emily.carter@hospital.com'),
(2, 'Dr. Michael Lee', 'Orthopedics', 'Orthopedics', '555-3002', 'michael.lee@hospital.com');

INSERT INTO appointments (id, patient_id, doctor_id, appointment_date, time_slot, status) VALUES
(1, 1, 1, '2026-10-10', '09:30 AM', 'Scheduled'),
(2, 2, 2, '2026-10-10', '11:00 AM', 'Confirmed');

INSERT INTO billings (id, patient_id, amount, billing_type, status, billing_date) VALUES
(1, 1, 250.00, 'Consultation', 'Pending', '2026-10-09'),
(2, 2, 450.00, 'Procedure', 'Paid', '2026-10-09');
