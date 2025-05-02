document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/account');
        if (response.ok) {
            const userInfo = await response.json();

            // Populate the form fields with user data
            document.getElementById('userId').value = userInfo.id || ''; // Populate hidden user ID field
            document.getElementById('firstName').value = userInfo.first_name || '';
            document.getElementById('lastName').value = userInfo.last_name || '';
            document.getElementById('username').value = userInfo.username || '';
            document.getElementById('email').value = userInfo.email || '';
            document.getElementById('password').value = userInfo.password || '';
        } else {
            document.getElementById('message').textContent = 'Failed to load user information.';
        }
    } catch (error) {
        console.error('Error fetching user data:', error);
        document.getElementById('message').textContent = 'An error occurred while loading user information.';
    }
});

// Handle form submission to update user information
document.getElementById('accountForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const userInfo = {
        id: document.getElementById('userId').value, // Include user ID in the payload
        first_name: document.getElementById('firstName').value,
        last_name: document.getElementById('lastName').value,
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
    };

    try {
        const response = await fetch('/account', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userInfo),
        });

        if (response.ok) {
            document.getElementById('message').textContent = 'Information updated successfully.';
            document.getElementById('message').style.color = 'green';
        } else {
            document.getElementById('message').textContent = 'Failed to update information.';
            document.getElementById('message').style.color = 'red';
        }
    } catch (error) {
        console.error('Error updating user information:', error);
        document.getElementById('message').textContent = 'An error occurred while updating information.';
        document.getElementById('message').style.color = 'red';
    }
});