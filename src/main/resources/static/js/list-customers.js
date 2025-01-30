document.getElementById("menu-toggle").addEventListener("click", function() {
    var sidebar = document.getElementById("sidebar-wrapper");
    if (sidebar.style.display === "none") {
        sidebar.style.display = "block";
    } else {
        sidebar.style.display = "none";
    }
});

function loadCustomerData() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/customers/all', true);

    xhr.onload = function() {
        if (xhr.status === 200) {
            var data = JSON.parse(xhr.responseText);
            //console.log(data); // Log the JSON response to inspect its structure
            var tableBody = document.getElementById('customerTableBody');
            tableBody.innerHTML = ''; // Clear existing table data

            data.forEach(function(customer) {
                var row = document.createElement('tr');
                row.innerHTML = `
                    <td>${customer.id}</td>
                    <td>${customer.business_name}</td>
                    <td>${customer.address}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>${customer.industry}</td>
                    <td>
                        <a href="/customers/edit/${customer.id}" class="btn btn-warning btn-sm">Edit</a>
                        <a href="#" onclick="confirmDeleteCustomer(${customer.id})" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        } else {
            console.error('Error fetching customer data:', xhr.statusText);
        }
    };

    xhr.onerror = function() {
        console.error('Request error...');
    };

    xhr.send();
}
// Function for confirming and deleting a customer
function confirmDeleteCustomer(customerId) {
    if (confirm("Are you sure you want to delete this customer?")) {
        fetch(`/api/customers/${customerId}/delete`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    alert("Customer deleted successfully.");
                    location.reload();  // Reload the page to reflect changes
                } else {
                    alert("Failed to delete the customer. Status: " + response.status);
                }
            })
            .catch(error => alert("Network error, couldn't delete the customer."));
    }
}
// Load data when the page loads
window.onload = loadCustomerData;

// Filter table based on search input
document.getElementById('searchInput').addEventListener('keyup', function() {
    var input = this.value.toLowerCase();
    var rows = document.querySelectorAll('#customerTableBody tbody tr');
    rows.forEach(function(row) {
        var cells = row.querySelectorAll('td');
        var match = false;
        cells.forEach(function(cell) {
            if (cell.innerText.toLowerCase().indexOf(input) > -1) {
                match = true;
            }
        });
        row.style.display = match ? '' : 'none';
    });
});