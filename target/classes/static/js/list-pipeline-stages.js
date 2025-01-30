document.getElementById("menu-toggle").addEventListener("click", function() {
    var sidebar = document.getElementById("sidebar-wrapper");
    if (sidebar.style.display === "none") {
        sidebar.style.display = "block";
    } else {
        sidebar.style.display = "none";
    }
});

function loadPipelineStageData() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/pipeline-stages', true);

    xhr.onload = function() {
        if (xhr.status === 200) {
            var data = JSON.parse(xhr.responseText);
            var tableBody = document.getElementById('pipelineStageTableBody');
            tableBody.innerHTML = ''; // Clear existing table data

            data.forEach(function(stage) {
                var row = document.createElement('tr');
                row.innerHTML = `
            <td>${stage.id}</td>
            <td>${stage.pipelineStageName}</td>
            <td>${stage.description}</td>
            <td>${stage.stageOrder}</td>
            <td>${stage.active ? 'Yes' : 'No'}</td>
            <td>
              <a href="edit-pipeline-stage.html?id=${stage.id}" class="btn btn-warning btn-sm">Edit</a>
              <a href="#" onclick="confirmDelete(${stage.id})" class="btn btn-danger btn-sm">Delete</a>
            </td>
          `;
                tableBody.appendChild(row);
            });
        } else {
            console.error('Error fetching pipeline stage data:', xhr.statusText);
        }
    };

    xhr.onerror = function() {
        console.error('Request error...');
    };

    xhr.send();
}
// Function for confirming and deleting a pipeline stage
function confirmDelete(stageId) {
    if (confirm("Are you sure you want to delete this stage?")) {
        fetch(`/api/pipeline-stages/${stageId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    alert("Pipeline Stage deleted successfully.");
                    location.reload();  // Reload the page to reflect changes
                } else {
                    alert("Failed to delete the Pipeline Stage. Status: " + response.status);
                }
            })
            .catch(error => alert("Network error, couldn't delete the pipeline stage."));
    }
}
// Load data when the page loads
window.onload = loadPipelineStageData;

// Filter table based on search input
document.getElementById('searchInput').addEventListener('keyup', function() {
    var input = this.value.toLowerCase();
    var rows = document.querySelectorAll('#pipelineStageTableBody tbody tr');
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