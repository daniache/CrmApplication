function submitForm() {
    const roleId = document.getElementById('role').value;
    const permissionIds = Array.from(document.getElementById('permissions').selectedOptions)
        .map(option => option.value);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/assign-permissions', true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function() {
        if (xhr.status >= 200 && xhr.status < 300) {
            alert(xhr.responseText);
        } else {
            console.error('Error:', xhr.statusText);
            alert('Error assigning permissions.');
        }
    };

    xhr.onerror = function() {
        console.error('Request failed');
        alert('Request failed.');
    };

    xhr.send(JSON.stringify({
        roleId: roleId,
        permissionIds: permissionIds.map(id => parseInt(id))
    }));
}

document.addEventListener('DOMContentLoaded', function() {
    fetchRoles();
    fetchPermissions();
});

function fetchRoles() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/roles', true);

    xhr.onload = function() {
        if (xhr.status >= 200 && xhr.status < 300) {
            const roles = JSON.parse(xhr.responseText);
            const roleSelect = document.getElementById('role');
            roles.forEach(role => {
                let option = document.createElement('option');
                option.value = role.id;
                option.textContent = role.roleName;
                roleSelect.appendChild(option);
            });
        } else {
            console.error('Error:', xhr.statusText);
        }
    };

    xhr.onerror = function() {
        console.error('Request failed');
    };

    xhr.send();
}

function fetchPermissions() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/permissions', true);

    xhr.onload = function() {
        if (xhr.status >= 200 && xhr.status < 300) {
            const permissions = JSON.parse(xhr.responseText);
            const permissionSelect = document.getElementById('permissions');
            permissions.forEach(permission => {
                let option = document.createElement('option');
                option.value = permission.id;
                option.textContent = permission.permissionName;
                permissionSelect.appendChild(option);
            });
        } else {
            console.error('Error:', xhr.statusText);
        }
    };

    xhr.onerror = function() {
        console.error('Request failed');
    };

    xhr.send();
}