/**
 * appointment.js — Handles the appointment booking form logic.
 * Validates inputs, submits to the backend API, and shows success/error feedback.
 */

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('appointmentForm');
    const formWrapper = document.getElementById('formWrapper');
    const successMsg = document.getElementById('formSuccess');
    const submitBtn = document.getElementById('submitBtn');

    if (!form) return;

    // Set minimum datetime to now (prevent past bookings)
    const datetimeInput = document.getElementById('appointmentDatetime');
    if (datetimeInput) {
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        now.setMinutes(now.getMinutes() + 30); // min 30 mins ahead
        datetimeInput.min = now.toISOString().slice(0, 16);
    }

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        // Show loading state
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Booking...';

        const data = {
            patientName: form.patientName.value.trim(),
            phone: form.phone.value.trim(),
            patientEmail: form.patientEmail.value.trim(),
            appointmentDatetime: form.appointmentDatetime.value,
            reason: form.reason.value.trim(),
        };

        try {
            await apiFetch(`${API_BASE_URL}/api/appointments`, {
                method: 'POST',
                body: JSON.stringify(data),
            });

            // Show success state
            formWrapper.style.display = 'none';
            successMsg.classList.add('show');

        } catch (error) {
            showToast(error.message || 'Failed to book appointment. Please try again.', 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="fas fa-calendar-check"></i> Book Appointment';
        }
    });

    function validateForm() {
        let valid = true;

        const fields = [
            { id: 'patientName', msg: 'Please enter your full name.' },
            { id: 'phone', msg: 'Please enter a valid phone number.' },
            { id: 'appointmentDatetime', msg: 'Please select a date and time.' },
        ];

        fields.forEach(({ id, msg }) => {
            const input = document.getElementById(id);
            const err = document.getElementById(id + 'Error');
            if (!input || !input.value.trim()) {
                if (err) { err.textContent = msg; err.classList.add('show'); }
                if (input) input.classList.add('error');
                valid = false;
            } else {
                if (err) err.classList.remove('show');
                if (input) input.classList.remove('error');
            }
        });

        // Validate phone format
        const phone = document.getElementById('phone');
        const phoneErr = document.getElementById('phoneError');
        const phoneRx = /^[0-9+\-\s]{7,15}$/;
        if (phone?.value && !phoneRx.test(phone.value.trim())) {
            if (phoneErr) { phoneErr.textContent = 'Invalid phone number format.'; phoneErr.classList.add('show'); }
            phone.classList.add('error');
            valid = false;
        }

        // Validate email if provided
        const email = document.getElementById('patientEmail');
        const emailErr = document.getElementById('patientEmailError');
        if (email?.value.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            if (emailErr) { emailErr.textContent = 'Invalid email address.'; emailErr.classList.add('show'); }
            email.classList.add('error');
            valid = false;
        }

        return valid;
    }

    // Clear errors on input
    document.querySelectorAll('.form-control').forEach(input => {
        input.addEventListener('input', () => {
            input.classList.remove('error');
            const err = document.getElementById(input.id + 'Error');
            if (err) err.classList.remove('show');
        });
    });
});
