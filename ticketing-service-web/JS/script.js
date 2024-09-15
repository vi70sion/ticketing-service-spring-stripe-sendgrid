document.addEventListener('DOMContentLoaded', () => {
    fetchEvents();
});

const stripe = Stripe('pk_test_51PlEdJBsNoGKJEE7jvx0qXTpbYBJUgZXBpFflSuL3mRuahlDtMCWVZWmRfwXSd5Nb9VWyVc4uVCB02NNrtAaGJPb00vDUFdR1K');

async function fetchEvents() {
    try {
        const response = await fetch('http://localhost:8080/ticketing/events', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
        });
        const events = await response.json();
        displayEvents(events);
    } catch (error) {
        console.error('Klaida užkraunant renginius:', error);
    }
}

function displayEvents(events) {
    const eventsContainer = document.getElementById('events-container');

    events.forEach(event => {
        const eventCard = document.createElement('div');
        eventCard.classList.add('event-card');

        const eventDateTime = new Date(event.eventTime);
        const formattedDate = eventDateTime.getFullYear() + '-' + 
                              String(eventDateTime.getMonth() + 1).padStart(2, '0') + '-' + 
                              String(eventDateTime.getDate()).padStart(2, '0');
        const formattedTime = String(eventDateTime.getHours()).padStart(2, '0') + ':' + 
                              String(eventDateTime.getMinutes()).padStart(2, '0');
        const formattedDateTime = `${formattedDate} ${formattedTime}`;

        eventCard.innerHTML = `
            <img src="${event.imageUrl}" alt="${event.name}" class="event-image">
            <div class="event-details">
                <div class="event-name">${event.name}</div>
                <div class="event-description">${event.description}</div>
                <div class="event-time">Data: ${formattedDateTime}</div>
                <div class="event-price">Kaina: €${event.price.toFixed(2)}</div>
                <button class="buy-ticket-button">Pirkti bilietą</button>
            </div>
        `;

        // "Pirkti bilietą" mygtukas
        eventCard.querySelector('.buy-ticket-button').addEventListener('click', () => {
            showPurchasePopup(event.id, event.name, formattedDateTime, event.price.toFixed(2));
        });

        eventsContainer.appendChild(eventCard);
    });
}

function showPurchasePopup(eventId, eventName, eventTime, eventPrice) {
    document.getElementById('popup-event-name').textContent = eventName;
    document.getElementById('popup-event-time').textContent = `Data: ${eventTime}`;
    document.getElementById('popup-event-price').textContent = `Kaina: €${eventPrice}`;
    document.getElementById('purchase-popup').classList.remove('hidden');
    // renginio ID ir pradinė kaina į hidden fields or variables
    document.getElementById('purchase-popup').setAttribute('data-event-id', eventId);
    document.getElementById('purchase-popup').setAttribute('data-original-price', eventPrice);
    
}

document.getElementById('recalculate-button').addEventListener('click', async () => {
    const discountCode = document.getElementById('discount-code').value;
    let discount = 0;

    if (discountCode) {
        try {
            // pagal nuolaidos kodą gauti nuolaidą procentais
            const response = await fetch(`http://localhost:8080/ticketing/discount?discountCode=${discountCode}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
            });

            if (response.ok) {
                discount = await response.json(); // nuolaida procentais
                console.log('Nuolaida procentais:', discount);

                // skaičiuoti naują kainą su nuolaida
                const originalPrice = parseFloat(document.getElementById('purchase-popup').getAttribute('data-original-price'));
                const discountedPrice = originalPrice * (1 - discount / 100);

                // parodyti naują kainą
                document.getElementById('popup-event-price').textContent = `Mokėti: €${discountedPrice.toFixed(2)}`;
            } else {
                console.error('Klaida gaunant nuolaidą');
            }
        } catch (error) {
            console.error('Klaida gaunant nuolaidą:', error);
        }
    } else {
        // jei nuolaidos kodas neįvestas, grąžinam pradinę kainą
        const originalPrice = parseFloat(document.getElementById('purchase-popup').getAttribute('data-original-price'));
        document.getElementById('popup-event-price').textContent = `Mokėti: €${originalPrice.toFixed(2)}`;
    }
});

// ------------Mokėti mygtukas, kurti checkout session-----------------
document.getElementById('pay-button').addEventListener('click', async () => {
    const eventId = document.getElementById('purchase-popup').getAttribute('data-event-id');
    const userEmail = document.getElementById('email').value;
    localStorage.setItem('userEmail', userEmail);
    const discountCode = document.getElementById('discount-code').value;

    const order = {
        eventId: eventId,
        userEmail: userEmail,
        discount: discountCode
    };

    try {
        const response = await fetch('http://localhost:8080/ticketing/create-checkout-session', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify(order)
        });

        if (response.ok) {
            const data = await response.json();
            const sessionId = data.sessionId; // sessionId

            console.log('Stripe session ID:', sessionId);

            // Redirect to Stripe
            const { error } = await stripe.redirectToCheckout({
                sessionId: sessionId,
            });

            if (error) {
                console.error('Stripe checkout error:', error.message);
            }
        } else {
            console.error('Failed to create checkout session');
        }
    } catch (error) {
        console.error('Error creating checkout session:', error);
    }

    // po mokėjimo uždaryti popup langą
    document.getElementById('purchase-popup').classList.add('hidden');
});


// uždaryti popup event listener
document.getElementById('close-popup').addEventListener('click', () => {
    document.getElementById('purchase-popup').classList.add('hidden');
});