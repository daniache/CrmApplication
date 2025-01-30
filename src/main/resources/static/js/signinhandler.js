// Handle login form submission
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default form submission

    // Manually extract form values
    const data = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
    };

    fetch('/api/auth/signin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
        //credentials: 'include' // Allow cookies to be sent
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // Parse JSON if response is OK
            } else {
                throw new Error('Login failed'); // Throw error for non-200 responses
            }
        })
        .then(data => {
            console.log('Response data:', data); // Debug the response structure
            if (data.message === 'User authenticated successfully') {

                // Redirect to the dashboard
                window.location.href = '/dashboard'; // Redirect to the dashboard page
            } else {
                // Handle case where token is not received
                console.error('Authentication successful but no token received:', data);
                alert('Login successful, but something went wrong.');
            }
        })
        .catch(error => {
            console.error('Login error:', error);
            alert('Login failed. Please check your credentials and try again.');
        });
});
