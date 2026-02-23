/**
 * main.js — Shared utilities used across all frontend pages.
 * Handles: navbar scroll, hamburger menu, scroll animations,
 * scroll-to-top button, toast notifications, and API config.
 */

// ==================== API CONFIGURATION ====================
// Change this to your Render backend URL after deployment.
const API_BASE_URL = (window.location.hostname === 'localhost' ||
    window.location.hostname === '127.0.0.1' ||
    window.location.protocol === 'file:')
    ? 'http://localhost:8080'
    : 'https://doctor-website-api.onrender.com'; // Replace with your Render backend URL after deployment

// ==================== NAVBAR SCROLL BEHAVIOR ====================
const navbar = document.querySelector('.navbar');
if (navbar) {
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    });
}

// ==================== HAMBURGER MENU ====================
const hamburger = document.querySelector('.hamburger');
const navMenu = document.querySelector('.nav-menu');
if (hamburger && navMenu) {
    hamburger.addEventListener('click', () => {
        hamburger.classList.toggle('open');
        navMenu.classList.toggle('open');
    });

    // Close menu when a link is clicked
    navMenu.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', () => {
            hamburger.classList.remove('open');
            navMenu.classList.remove('open');
        });
    });
}

// ==================== ACTIVE NAV LINK ====================
(function () {
    const page = window.location.pathname.split('/').pop() || 'index.html';
    document.querySelectorAll('.nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (href && (href === page || (page === '' && href === 'index.html') || href.includes(page))) {
            link.classList.add('active');
        }
    });
})();

// ==================== SCROLL ANIMATIONS ====================
const animatedEls = document.querySelectorAll('.fade-up, .fade-in');
if (animatedEls.length > 0) {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry, i) => {
            if (entry.isIntersecting) {
                setTimeout(() => {
                    entry.target.classList.add('visible');
                }, i * 100);
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });

    animatedEls.forEach(el => observer.observe(el));
}

// ==================== SCROLL-TO-TOP BUTTON ====================
const scrollTopBtn = document.querySelector('.scroll-top');
if (scrollTopBtn) {
    window.addEventListener('scroll', () => {
        if (window.scrollY > 400) {
            scrollTopBtn.classList.add('visible');
        } else {
            scrollTopBtn.classList.remove('visible');
        }
    });

    scrollTopBtn.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
}

// ==================== TOAST NOTIFICATION ====================
/**
 * Show a toast message.
 * @param {string} message  - Text to display
 * @param {'success'|'error'} type - Toast type
 * @param {number} duration - Auto-dismiss delay ms (default 4000)
 */
function showToast(message, type = 'success', duration = 4000) {
    // Remove existing toast
    const existing = document.querySelector('.toast');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
    <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'}"></i>
    <span>${message}</span>
  `;
    document.body.appendChild(toast);

    // Trigger animation
    requestAnimationFrame(() => {
        requestAnimationFrame(() => toast.classList.add('show'));
    });

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 400);
    }, duration);
}

// ==================== GENERIC FETCH HELPER ====================
/**
 * Wrapper around fetch with JSON handling and error propagation.
 * @param {string} url
 * @param {RequestInit} options
 * @returns {Promise<any>} Parsed JSON response
 */
async function apiFetch(url, options = {}) {
    const defaults = {
        headers: { 'Content-Type': 'application/json' },
    };
    const response = await fetch(url, { ...defaults, ...options });
    const data = await response.json();
    if (!response.ok || !data.success) {
        throw new Error(data.message || 'Something went wrong');
    }
    return data;
}

// ==================== NUMBER COUNTER ANIMATION ====================
function animateCounter(el, target, duration = 2000) {
    let start = 0;
    const step = target / (duration / 16);
    const timer = setInterval(() => {
        start += step;
        if (start >= target) {
            el.textContent = target + (el.dataset.suffix || '');
            clearInterval(timer);
        } else {
            el.textContent = Math.floor(start) + (el.dataset.suffix || '');
        }
    }, 16);
}

// Init counters when visible
const counters = document.querySelectorAll('[data-count]');
if (counters.length) {
    const cObserver = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const el = entry.target;
                const val = parseInt(el.dataset.count, 10);
                animateCounter(el, val);
                cObserver.unobserve(el);
            }
        });
    }, { threshold: 0.5 });
    counters.forEach(c => cObserver.observe(c));
}
