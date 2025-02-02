// Function to open modal
function openModal(modalId) {
    document.getElementById(modalId).style.display = "block";
}

// Function to close modal
function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

// Close modal if clicked outside the modal content
window.onclick = function(event) {
    const modals = ['loginModal', 'signupModal', 'otpModal'];
    modals.forEach(modalId => {
        if (event.target == document.getElementById(modalId)) {
            closeModal(modalId);
        }
    });
}

// Handle signup form submission
document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form from refreshing the page

    const phoneNumber = document.getElementById('phoneNumber').value;  // Capture phone number

    // Make an AJAX request to send OTP to the phone number
    fetch('/generate-otp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ phoneNumber: phoneNumber }),  // Send phone number
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            closeModal('signupModal'); // Close signup modal
            openModal('otpModal'); // Open OTP modal
        } else {
            alert('Error in sending OTP');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
});

// Handle OTP form submission
document.getElementById('otpForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form from refreshing the page

    const otpInput = document.getElementById('otp-input').value;
    const phoneNumber = document.getElementById('phoneNumber').value;  // Use the same phone number

    // Make an AJAX request to verify OTP
    fetch('/verify-otp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ otp: otpInput, phoneNumber: phoneNumber }),  // Send phone number and OTP
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('OTP verified successfully!');
            closeModal('otpModal'); // Close OTP modal
        } else {
            document.getElementById('otp-error').textContent = 'Invalid OTP. Please try again.';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
});
