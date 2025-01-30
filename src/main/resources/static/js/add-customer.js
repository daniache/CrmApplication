document.getElementById('addCustomerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    const customerData = {
        business_name: document.getElementById('business_name').value,
        address: document.getElementById('address').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        industry: document.getElementById('industry').value
    }

    fetch(`/api/customers/create`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(customerData)
    })
        .then(response => {
            if (response.ok) {
                alert('Customer added successfully!');
                window.location.href = '/customers';
            } else {
                alert('Failed to add customer.');
            }
        })
        .catch(error => console.error('Error:', error));
});