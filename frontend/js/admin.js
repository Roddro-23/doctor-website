/**
 * admin.js — Admin dashboard logic.
 * Handles login, fetching appointments, status updates, and deletion.
 */

document.addEventListener('DOMContentLoaded', () => {
    const loginOverlay = document.getElementById('adminLoginOverlay');
    const loginForm = document.getElementById('adminLoginForm');
    const adminPanel = document.getElementById('adminPanel');
    const logoutBtn = document.getElementById('logoutBtn');
    const tbody = document.getElementById('appointmentsTableBody');
    const loadingEl = document.getElementById('loadingSpinner');
    const emptyEl = document.getElementById('emptyState');

    let adminPassword = '';

    // ==================== LOGIN ====================
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            adminPassword = document.getElementById('adminPasswordInput').value;
            const loginBtn = document.getElementById('loginBtn');
            loginBtn.disabled = true;
            loginBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verifying...';

            try {
                await apiFetch(`${API_BASE_URL}/api/appointments?adminPassword=${encodeURIComponent(adminPassword)}`);
                // Access granted
                loginOverlay.style.display = 'none';
                adminPanel.style.display = 'block';
                await loadAppointments();
            } catch {
                showToast('Invalid admin password. Please try again.', 'error');
            } finally {
                loginBtn.disabled = false;
                loginBtn.innerHTML = '<i class="fas fa-unlock"></i> Access Dashboard';
            }
        });
    }

    // ==================== LOGOUT ====================
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            adminPassword = '';
            adminPanel.style.display = 'none';
            loginOverlay.style.display = 'flex';
            document.getElementById('adminPasswordInput').value = '';
        });
    }

    // ==================== LOAD APPOINTMENTS ====================
    async function loadAppointments() {
        if (loadingEl) loadingEl.style.display = 'flex';
        if (emptyEl) emptyEl.style.display = 'none';
        if (tbody) tbody.innerHTML = '';

        try {
            const res = await apiFetch(`${API_BASE_URL}/api/appointments?adminPassword=${encodeURIComponent(adminPassword)}`);
            const appointments = res.data || [];

            // Update stat counters
            updateStats(appointments);

            if (appointments.length === 0) {
                if (emptyEl) emptyEl.style.display = 'block';
                return;
            }

            // Sort newest first
            appointments.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
            appointments.forEach(appt => renderRow(appt));

        } catch (err) {
            showToast('Failed to load appointments: ' + err.message, 'error');
        } finally {
            if (loadingEl) loadingEl.style.display = 'none';
        }
    }

    // ==================== RENDER ROW ====================
    function renderRow(a) {
        if (!tbody) return;
        const dt = a.appointmentDatetime
            ? new Date(a.appointmentDatetime).toLocaleString('en-BD', { dateStyle: 'medium', timeStyle: 'short' })
            : '—';
        const created = a.createdAt
            ? new Date(a.createdAt).toLocaleDateString('en-BD', { dateStyle: 'medium' })
            : '—';

        const row = document.createElement('tr');
        row.id = `row-${a.id}`;
        row.innerHTML = `
      <td><strong>#${a.id}</strong></td>
      <td>${escHtml(a.patientName)}</td>
      <td>${escHtml(a.phone)}</td>
      <td>${a.patientEmail ? escHtml(a.patientEmail) : '<span style="color:#9ca3af">—</span>'}</td>
      <td>${dt}</td>
      <td>${a.reason ? escHtml(a.reason) : '<span style="color:#9ca3af">—</span>'}</td>
      <td><span class="badge-status badge-${a.status?.toLowerCase()}">${a.status}</span></td>
      <td>${created}</td>
      <td>
        <div class="action-btns">
          ${a.status !== 'CONFIRMED' ? `<button class="btn-confirm" onclick="updateStatus(${a.id},'CONFIRMED')"><i class="fas fa-check"></i> Confirm</button>` : ''}
          ${a.status !== 'CANCELLED' ? `<button class="btn-cancel" onclick="updateStatus(${a.id},'CANCELLED')"><i class="fas fa-times"></i> Cancel</button>` : ''}
          <button class="btn-cancel" onclick="deleteAppointment(${a.id})" style="background:#6b7280"><i class="fas fa-trash"></i></button>
        </div>
      </td>
    `;
        tbody.appendChild(row);
    }

    // ==================== UPDATE STATS ====================
    function updateStats(appts) {
        const total = appts.length;
        const pending = appts.filter(a => a.status === 'PENDING').length;
        const confirmed = appts.filter(a => a.status === 'CONFIRMED').length;
        const cancelled = appts.filter(a => a.status === 'CANCELLED').length;

        setEl('statTotal', total);
        setEl('statPending', pending);
        setEl('statConfirmed', confirmed);
        setEl('statCancelled', cancelled);
    }

    function setEl(id, val) { const el = document.getElementById(id); if (el) el.textContent = val; }

    // ==================== UPDATE STATUS (global) ====================
    window.updateStatus = async function (id, status) {
        try {
            await apiFetch(`${API_BASE_URL}/api/appointments/${id}/status?adminPassword=${encodeURIComponent(adminPassword)}`, {
                method: 'PUT',
                body: JSON.stringify({ status }),
            });
            showToast(`Appointment #${id} marked as ${status}.`, 'success');
            await loadAppointments();
        } catch (err) {
            showToast('Failed to update: ' + err.message, 'error');
        }
    };

    // ==================== DELETE (global) ====================
    window.deleteAppointment = async function (id) {
        if (!confirm(`Delete appointment #${id}? This cannot be undone.`)) return;
        try {
            await apiFetch(`${API_BASE_URL}/api/appointments/${id}?adminPassword=${encodeURIComponent(adminPassword)}`, {
                method: 'DELETE',
            });
            showToast(`Appointment #${id} deleted.`, 'success');
            document.getElementById(`row-${id}`)?.remove();
            await loadAppointments();
        } catch (err) {
            showToast('Failed to delete: ' + err.message, 'error');
        }
    };

    // ==================== REFRESH BUTTON ====================
    window.refreshAppointments = loadAppointments;

    // ==================== FILTER ====================
    window.filterByStatus = async function (status) {
        if (!tbody) return;
        try {
            const url = status
                ? `${API_BASE_URL}/api/appointments?adminPassword=${encodeURIComponent(adminPassword)}`
                : `${API_BASE_URL}/api/appointments?adminPassword=${encodeURIComponent(adminPassword)}`;
            const res = await apiFetch(url);
            const appts = status
                ? (res.data || []).filter(a => a.status === status)
                : (res.data || []);
            tbody.innerHTML = '';
            appts.forEach(renderRow);
        } catch (err) {
            showToast('Filter failed: ' + err.message, 'error');
        }
    };

    // ==================== HTML ESCAPE ====================
    function escHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>"']/g, m => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[m]));
    }
});
