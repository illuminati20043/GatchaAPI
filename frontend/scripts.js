function showLogin() {
    document.getElementById('loginForm').style.display = 'block';
    document.getElementById('signupForm').style.display = 'none';
}

function showSignup() {
    document.getElementById('loginForm').style.display = 'none';
    document.getElementById('signupForm').style.display = 'block';
}

async function handleFormSubmit(event, url, successMessage, errorMessage) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            const responseData = await response.json();
            console.log(successMessage, responseData);
            alert(successMessage);
            // You can redirect the user to another page or perform other actions here
        } else {
            const errorData = await response.json();
            console.error(errorMessage, errorData);
            alert(`${errorMessage}: ${errorData.message}`);
        }
    } catch (error) {
        console.error(errorMessage, error);
        alert(`${errorMessage}: ${error.message}`);
    }
}

document.getElementById('login').addEventListener('submit', function (e) {
    handleFormSubmit(e, 'http://localhost:8080/api/auth/login', 'Login successful!', 'Login failed');
});

document.getElementById('signup').addEventListener('submit', function (e) {
    handleFormSubmit(e, 'http://localhost:8080/api/auth/register', 'Signup successful!', 'Signup failed');
});