document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault();

    // Get the role from the query string
    const urlParams = new URLSearchParams(window.location.search);
    const role = urlParams.get('role') || 'user';  // default to 'user' if not specified

    // Construct the data object from form fields
    const data = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        role: role
    };

    // Send the data as JSON
    fetch('/api/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.message === "User registered successfully") {
                // Show a pop-up message indicating success
                alert('Registration successful! You will be redirected to the login page.');

                // Delay the redirection to allow the user to read the message
                setTimeout(() => {
                    window.location.href = '/signin';
                }, 2000); // Redirect after 2 seconds
            } else {
                // Display error message
                console.error('Sign-up error:', data.message || 'Unknown error');
            }
        })
        .catch(error => console.error('Sign-up error:', error));
});