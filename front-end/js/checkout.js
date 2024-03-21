// This is your test publishable API key.
const stripe = Stripe("pk_test_51OJy1oFFtiwvOqhyqUYxcueT5MuUv58uSLjDyY9Ri9SGJdtQM3FZPYzocNY5BVvZoiKkXQdZKcZLhyVQRWm2cTHM00sBYHibZC");

let elements;
const token = localStorage.getItem('token');

initialize();

document
    .querySelector("#payment-form")
    .addEventListener("submit", handleSubmit);

let paymentIntentID = '';
async function initialize() {
    try {
        const response = await fetch("http://localhost:8080/create-payment-intent", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
        });

        if (!response.ok) {
            if (response.status === 401) {
                window.location.replace('/signin.html');
            } 
            return;
        }

        const { intentID, clientSecret } = await response.json();

        if (intentID === "-1") {
            alert('Please update address');
            window.location.replace('/index.html')
            return;
        } else if (intentID === "0") {
            alert('Cart is empty');
            window.location.replace('/index.html')
            return;
        }

        paymentIntentID = intentID;
        const appearance = { theme: 'stripe' };
        elements = stripe.elements({ appearance, clientSecret });

        const paymentElementOptions = { layout: "tabs" };
        const paymentElement = elements.create("payment", paymentElementOptions);
        paymentElement.mount("#payment-element");

    } catch (error) {
        alert('An unexpected error occurred.' + error);
    }
}


async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);

    const { error } = await stripe.confirmPayment({
        elements,
        confirmParams: {
            return_url: "http://127.0.0.1:5500/orders.html"
        },
    });

    // This point will only be reached if there is an immediate error when
    // confirming the payment. Otherwise, your customer will be redirected to
    // your `return_url`. For some payment methods like iDEAL, your customer will
    // be redirected to an intermediate site first to authorize the payment, then
    // redirected to the `return_url`.
    if (error.type === "card_error" || error.type === "validation_error") {
        showMessage(error.message);
    } else {
        showMessage("An unexpected error occurred.");
    }

    setLoading(false);
}

// ------- UI helpers -------

function showMessage(messageText) {
    const messageContainer = document.querySelector("#payment-message");

    messageContainer.classList.remove("hidden");
    messageContainer.textContent = messageText;

    setTimeout(function () {
        messageContainer.classList.add("hidden");
        messageContainer.textContent = "";
    }, 4000);
}

// Show a spinner on payment submission
function setLoading(isLoading) {
    if (isLoading) {
        // Disable the button and show a spinner
        document.querySelector("#submit").disabled = true;
        document.querySelector("#spinner").classList.remove("hidden");
        document.querySelector("#button-text").classList.add("hidden");
    } else {
        document.querySelector("#submit").disabled = false;
        document.querySelector("#spinner").classList.add("hidden");
        document.querySelector("#button-text").classList.remove("hidden");
    }
}
