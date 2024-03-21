const stripe = Stripe("pk_test_51OJy1oFFtiwvOqhyqUYxcueT5MuUv58uSLjDyY9Ri9SGJdtQM3FZPYzocNY5BVvZoiKkXQdZKcZLhyVQRWm2cTHM00sBYHibZC");

const token = localStorage.getItem("token");

$(document).ready(function () {
    checkStatus();

    // Fetches the payment intent status after payment submission
    async function checkStatus() {
        const clientSecret = new URLSearchParams(window.location.search).get(
            "payment_intent_client_secret"
        );

        if (!clientSecret) {
            getOrders();
            return;
        }

        const { paymentIntent } = await stripe.retrievePaymentIntent(clientSecret);

        switch (paymentIntent.status) {
            case "succeeded":
                createOrder();
                break;
            case "processing":
                showMessage("Your payment is processing.");
                break;
            case "requires_payment_method":
                showMessage("Your payment was not successful, please try again.", false);
                break;
            default:
                showMessage("Something went wrong.", false);
                break;
        }
    }

    function createOrder() {
        $.ajax({
            url: "http://localhost:8080/orders",
            type: "POST",
            headers: {
                "Authorization": "Bearer " + token,
            },
            success: function (response) {
                showMessage("Payment succeeded!");
                const urlWithoutQueryParams = window.location.protocol + "//" + window.location.host + window.location.pathname;
                window.history.pushState({ path: urlWithoutQueryParams }, '', urlWithoutQueryParams);
            },
            error: function (xhr) {
                alert('Failed to create order.' + xhr.responseText);
            }
        });
    }

    function showMessage(messageText, isSuccess = true) {
        const messageContainer = document.querySelector("#payment-message");

        messageContainer.classList.remove("d-none", "alert-success", "alert-danger");
        messageContainer.classList.add(isSuccess ? "alert-success" : "alert-danger");

        messageContainer.textContent = messageText;

        setTimeout(function () {
            messageContainer.classList.add("d-none");
            messageContainer.textContent = "";
        }, 4000);
    }

    function getOrders() {
        $.ajax({
            url: "http://localhost:8080/orders",
            type: "GET",
            headers: {
                "Authorization": "Bearer " + token,
            },
            success: function (response) {
                data = response.data;

                $.each(data, function (index , order){
                    var time = order.date.split(" ");

                    var htmlCategories = '';
                    $.each(order.categoryList, function (index , value){
                        htmlCategories += `<p class="text-dark mb-2"><span class="mr-2 text-black">
                                                ${index + 1}</span>${value}
                                            </p>`
                    })
    
                    var htmlOrders = `<div class="col-lg-4 col-md-6">
                                    <div class="bg-white shadow-sm rounded p-3 mb-4">
                                        <div class="d-flex align-items-center mb-1">
                                            <h6 class="mb-0">${order.restaurant}</h6>
                                            <p class="badge badge-success mb-0 ml-auto"><i class="mdi mdi-check-circle"></i>
                                                Completed</p>
                                        </div>
                                        <div class="d-flex align-items-center">
                                            <p class="small"><i class="mdi mdi-calendar mr-1"></i>${time[0]}<span
                                                    class="ml-2"><i class="mdi mdi-clock-outline mr-1"></i>${time[1]}</span></p>
                                        </div>
                                        ${htmlCategories}
                                        <div class="d-flex align-items-center row pt-2 mt-3">
                                            <div class="col-6 pr-2">
                                                <a href="#" data-id=${order.orderId} class="btn btn-primary btn-block order-item" data-toggle="modal"
                                                    data-target="#detailsModal">Details</a>
                                            </div>
                                            <div class="col-6 pl-2">
                                                <a href="settings.html" class="btn btn-outline-primary btn-block">Get help</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>`;
                    $('#list-orders').append(htmlOrders);
                })
            },
            error: function (xhr) {
                if(!xhr.status === 401) {
                    alert('Failed to create order.' + xhr.responseText);
                }
            }
        });
    }


    $(document).on('click', '.order-item', function (e) {
        e.preventDefault();
        $.ajax({
            url: "http://localhost:8080/orders/" + $(this).data('id'),
            type: "GET",
            headers: {
                "Authorization": "Bearer " + token,
            },
            success: function (response) {
                $('#order-detail').empty();
                data = response.data;
                var htmlFoods = '';

                $.each(data.foodDTOList, function (index, food) {
                    htmlFoods += `<div class="d-flex align-items-center">
                                    <p class="bg-light rounded px-2 mr-3">${food.quantity}</p>
                                    <p class="text-dark">${food.title}</p>
                                    <p class="ml-auto">${food.price.toLocaleString('vi-VN')}</p>
                                </div>`
                });

                var htmlDetail = `<div class="d-flex align-items-center mb-3">
                                    <div class>
                                        <p class="mb-1 text-danger">Delivered to</p>
                                        <p class="mb-0 font-weight-bold text-dark">${data.address}</p>
                                    </div>
                                    <div class="ml-auto">
                                        <p class="mb-0"><i class="mdi mdi-chevron-right"></i></p>
                                    </div>
                                </div>
                                <div class="details-page border-top pt-3">
                                    <h6 class="mb-3">${data.restaurant}</h6>
                                    ${htmlFoods}
                                    <div class="d-flex align-items-center py-2 border-top">
                                        <p class="text-dark m-0">Subtotal</p>
                                        <p class="ml-auto text-danger m-0">${data.total.toLocaleString('vi-VN')}</p>
                                    </div>
                                    <div class="d-flex align-items-center py-2 border-top">
                                        <p class="text-dark m-0">Delivery fee</p>
                                        <p class="ml-auto text-danger m-0">$4</p>
                                    </div>
                                    <div class="d-flex align-items-center py-3 border-top">
                                        <p class="text-dark h6 m-0">Total</p>
                                        <p class="ml-auto text-danger h6 m-0">${data.total.toLocaleString('vi-VN')}</p>
                                    </div>
                                    <p class="text-dark mb-0">Review your order</p>
                                    <div class="rating-star d-flex align-items-center">
                                        <i class="fas fa-star text-warning mr-2"></i> <i class="fas fa-star text-warning mr-2"></i>
                                        <i class="fas fa-star text-warning mr-2"></i> <i class="fas fa-star text-warning mr-2"></i>
                                        <i class="fas fa-star mr-2"></i>
                                        <button class="btn btn-primary ml-auto">Submit</button>
                                    </div>`;
                $('#order-detail').append(htmlDetail);
            },
            error: function (xhr) {
                alert('Failed to create order.' + xhr.responseText);
            }
        });
    })

    $.ajax({
        url: 'http://localhost:8080/category/all',
        method: 'GET',
        success: function (response) {
            var categories = response.data
            $.each(categories, function (index, category) {
                var isChecked = index === 0 ? 'checked' : '';
                var html = `<label class="btn osahan-radio btn-light active btn-sm rounded">
                                <input type="radio" name="category" value=${category.categoryId} ${isChecked}> ${category.name}
                            </label>`
                $('#filter-categories').append(html);
            })
        },
        error: function (xhr) {
            alert("Error fetching cart data", xhr.responseText);
        }
    });
});