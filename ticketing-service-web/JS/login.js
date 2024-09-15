document.getElementById('login-button').addEventListener('click', async function () {
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const loginData = {
        name: username,
        password: password
    };

    try {
        const response = await fetch('http://localhost:8080/ticketing/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginData)  
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;  

            document.cookie = `LoginCookies=${token}; path=/; max-age=${7 * 60 * 60 * 24}`; // 7 days

            // Save the token and userEmail in localStorage
            localStorage.setItem('LoginCookies', token); 
            localStorage.setItem('userEmail', username); 

            window.location.href = 'userservice.html';

        } else {
            // login failed
            alert('Blogas vartotojo vardas arba slaptažodis. Bandykite dar kartą.');
            window.location.reload();
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Įvyko klaida bandant prisijungti. Bandykite dar kartą.');
    }
});

