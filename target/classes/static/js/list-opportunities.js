document.getElementById("menu-toggle").addEventListener("click", function() {
    var sidebar = document.getElementById("sidebar-wrapper");
    if (sidebar.style.display === "none") {
        sidebar.style.display = "block";
    } else {
        sidebar.style.display = "none";
    }
});

function loadOpportunitiesData() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/opportunities/all', true);

    xhr.onload = function() {
        if (xhr.status === 200) {
            var data = JSON.parse(xhr.responseText);
            //console.log(data); // Log the JSON response to inspect its structure
            var tableBody = document.getElementById('opportunitiesTableBody');
            tableBody.innerHTML = ''; // Clear existing table data

            data.forEach(function(opportunity) {
                var row = document.createElement('tr');
                row.innerHTML = `
                    <td>${opportunity.id}</td>
                    <td>${opportunity.dealName}</td>
                    <td>${opportunity.description}</td>
                    <td>${opportunity.dealValue}</td>
                    <td>${opportunity.status}</td>
                    <td>
                        <a href="/opportunities/edit/${opportunity.id}" class="btn btn-warning btn-sm">Edit</a>
                        <a href="#" onclick="confirmDeleteOpportunity(${opportunity.id})" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        } else {
            console.error('Error fetching opportunities data:', xhr.statusText);
        }
    };

    xhr.onerror = function() {
        console.error('Request error...');
    };

    xhr.send();
}
// Function for confirming and deleting an opportunity
function confirmDeleteOpportunity(opportunityId) {
    if (confirm("Are you sure you want to delete this opportunity?")) {
        fetch(`/api/opportunities/${opportunityId}/delete`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    alert("Opportunity deleted successfully.");
                    location.reload();  // Reload the page to reflect changes
                } else {
                    alert("Failed to delete the opportunity. Status: " + response.status);
                }
            })
            .catch(error => alert("Network error, couldn't delete the opportunity."));
    }
}
// Load data when the page loads
window.onload = loadOpportunitiesData;

// Filter table based on search input
document.getElementById('searchInput').addEventListener('keyup', function() {
    var input = this.value.toLowerCase();
    var rows = document.querySelectorAll('#opportunitiesTable tbody tr');
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