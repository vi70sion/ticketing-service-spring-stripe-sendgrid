document.addEventListener('DOMContentLoaded', () => {
    
    // tikrinam ar jau yra pasirinkta: užsisakyta/neužsisakyta
    const isSubscribed = getCookie('newsletter_subscribed');
    if (!isSubscribed) {
        document.getElementById('newsletter-modal').classList.remove('hidden');
    }    

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    
    function setCookie(name, value, days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        const expires = `expires=${date.toUTCString()}`;
        document.cookie = `${name}=${value};${expires};path=/`;
    }

    document.getElementById('close-modal').addEventListener('click', () => {
        document.getElementById('newsletter-modal').classList.add('hidden');
        setCookie('newsletter_subscribed', 'dismissed', 365); // atsisakė, metams
    });

    // užsakyti naujienlaiškį
    document.getElementById('subscribe-button').addEventListener('click', async () => {
        const email = document.getElementById('newsletter-email').value;
        if (email) {
            try {
                const response = await fetch('http://localhost:8080/ticketing/subscribe', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ userEmail: email }),
                });

                if (response.ok) {
                    document.getElementById('newsletter-modal').classList.add('hidden');
                    setCookie('newsletter_subscribed', 'true', 365); // užsakyta, metams
                    alert('Ačiū, kad užsiprenumeravote!');
                } else {
                    alert('Nepavyko užsiprenumeruoti. Bandykite dar kartą.');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('Nepavyko užsiprenumeruoti. Bandykite dar kartą.');
            }
        } else {
            alert('Įveskite el. paštą.');
        }
    });

    // atsisakyta
    document.getElementById('unsubscribe-button').addEventListener('click', () => {
        document.getElementById('newsletter-modal').classList.add('hidden');
        setCookie('newsletter_subscribed', 'dismissed', 365); // atsisakė, metams
    });
});