document.addEventListener('DOMContentLoaded', async () => {
    const userEmail = localStorage.getItem('userEmail');
    const orderHistoryContainer = document.getElementById('order-history');

    if (!userEmail) {
        alert('Vartotojo el. paštas nerastas. Prašome prisijungti iš naujo.');
        window.location.href = 'login.html';  // redirektinam į login puslapį, userEmail nerastas
        return;
    }

    try {
        // visi orderiai pagal userEmail
        const ordersResponse = await fetch(`http://localhost:8080/ticketing/orders?userEmail=${userEmail}`);
        const orders = await ordersResponse.json();

        // kiekvienam orderiui, fetch event
        for (const order of orders) {
            const eventResponse = await fetch(`http://localhost:8080/ticketing/event?id=${order.eventId}`);
            const event = await eventResponse.json();

            // card kiekvienam order
            const orderCard = document.createElement('div');
            orderCard.classList.add('order-card');

            // pridedame informaciją į card
            orderCard.innerHTML = `
                <h3>Užsakymo numeris: ${order.id}</h3>
                <p><strong>Renginio pavadinimas:</strong> ${event.name}</p>
                <p><strong>Renginio laikas:</strong> ${new Date(event.eventTime).toLocaleString()}</p>
                <p><strong>Kaina:</strong> ${order.price.toFixed(2)}€</p>
                <p><strong>Nuolaidos kodas:</strong> ${order.discount}</p>
                <p><strong>Mokėjimo suma:</strong> ${order.orderTotal.toFixed(2)}€</p>
                <p><strong>Mokėjimo būsena:</strong> ${order.paymentStatus ? 'Apmokėta' : 'Neapmokėta'}</p>
            `;

            if (order.paymentStatus) {
                // jei orderis apmokėtas, rodyti "Atšaukti pirkimą" button
                const cancelButton = document.createElement('button');
                cancelButton.classList.add('buy-ticket-button');
                cancelButton.textContent = 'Atšaukti pirkimą';

                cancelButton.addEventListener('click', async () => {
                    try {
                        // token iš LoginCookies
                        const token = document.cookie
                            .split('; ')
                            .find(row => row.startsWith('LoginCookies='))
                            ?.split('=')[1]; // Extract token from LoginCookies

                        if (!token) {
                            alert('Autentifikacija negalima. Prašome prisijungti iš naujo.');
                            window.location.href = 'login.html'; // jei nėra token, grąžinti į login.html
                            return;
                        }                        
                        // POST requestas į refund endpointą su orderio ID
                        const response = await fetch(`http://localhost:8080/ticketing/refund?orderId=${order.id}`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `${token}` 
                            },
                            body: null
                        });

                        if (response.ok) {
                            alert('Pirkimas sėkmingai atšauktas ir grąžinimas inicijuotas.');
                            window.location.reload();  
                        } else {
                            alert('Nepavyko atšaukti pirkimo.');
                        }
                    } catch (error) {
                        console.error('Error:', error);
                        alert('Įvyko klaida bandant atšaukti pirkimą.');
                    }
                });
                orderCard.appendChild(cancelButton);

            } else {
                // jei neapmokėta, parodyti "Apmokėti" mygtuką
                const payButton = document.createElement('a');
                payButton.href = `http://localhost:8080/ticketing/pay-order?id=${order.id}`;  
                payButton.classList.add('buy-ticket-button');
                payButton.textContent = 'Apmokėti';
                orderCard.appendChild(payButton);
            }

            // Append the card to the order history container
            orderHistoryContainer.appendChild(orderCard);
        }
    } catch (error) {
        console.error('Klaida gaunant užsakymų istoriją:', error);
        orderHistoryContainer.innerHTML = `<p>Įvyko klaida bandant gauti užsakymų istoriją.</p>`;
    }
});

