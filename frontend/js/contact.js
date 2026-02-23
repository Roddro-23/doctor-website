/**
 * contact.js — Handles contact form submission.
 */

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('contactForm');
    const submitBtn = document.getElementById('contactSubmitBtn');

    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!validateContactForm()) return;

        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';

        const data = {
            name: form.contactName.value.trim(),
            email: form.contactEmail.value.trim(),
            phone: form.contactPhone.value.trim(),
            message: form.contactMessage.value.trim(),
        };

        try {
            await apiFetch(`${API_BASE_URL}/api/contact`, {
                method: 'POST',
                body: JSON.stringify(data),
            });
            form.reset();
            showToast('Message sent! We\'ll get back to you within 24 hours.', 'success');
        } catch (error) {
            showToast(error.message || 'Failed to send message. Please try again.', 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Send Message';
        }
    });

    function validateContactForm() {
        let valid = true;
        const fields = [
            { id: 'contactName', msg: 'Please enter your name.' },
            { id: 'contactEmail', msg: 'Please enter a valid email.' },
            { id: 'contactMessage', msg: 'Please enter your message.' },
        ];
        fields.forEach(({ id, msg }) => {
            const el = document.getElementById(id);
            const err = document.getElementById(id + 'Error');
            if (!el?.value.trim()) {
                if (err) { err.textContent = msg; err.classList.add('show'); }
                el?.classList.add('error');
                valid = false;
            } else {
                err?.classList.remove('show');
                el?.classList.remove('error');
            }
        });
        const email = document.getElementById('contactEmail');
        const emailErr = document.getElementById('contactEmailError');
        if (email?.value.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            if (emailErr) { emailErr.textContent = 'Invalid email address.'; emailErr.classList.add('show'); }
            email?.classList.add('error');
            valid = false;
        }
        return valid;
    }

    document.querySelectorAll('.form-control').forEach(input => {
        input.addEventListener('input', () => {
            input.classList.remove('error');
            const err = document.getElementById(input.id + 'Error');
            if (err) err.classList.remove('show');
        });
    });
});
