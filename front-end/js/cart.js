$(document).ready(function () {
    token = localStorage.getItem('token');
    var isEmptyCart = true;

    $('#btn-cart').click(function (e) {
        e.preventDefault();
        loadCart();
        getAddress();
    });

    function loadCart() {
        $.ajax({
            url: 'http://localhost:8080/api/carts',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                $('#osahan-my-cart-item').empty();
                var data = response.data;
                isEmptyCart = data.foodDTOList.length === 0;
                if (!isEmptyCart) {
                    var cartItemsHtml = '';
                    data.foodDTOList.forEach(function (item) {
                        cartItemsHtml += `<div class="d-flex align-items-center mb-3 cart-item">
                                        <div class="mr-2"><img style="height: 60px; width: 90px" src="http://localhost:8080/api/carts/file/${item.image}" class="img-fluid rounded"></div>
                                        <div class="small text-black-50 food-quantity" data-quantity=${item.quantity}>${item.quantity} x</div>
                                        <div class="text-dark ml-2">
                                            <p class="mb-0 text-black">${item.title}</p>
                                            <p class="mb-0 small food-price" data-price=${item.price}>${item.price.toLocaleString('vi-VN')}</p>
                                        </div>
                                        <a href="#" class="ml-auto"><i data-id=${item.id} class="btn btn-light text-danger mdi mdi-trash-can-outline rounded remove-food"></i></a>
                                    </div>`;
                    });
                    $('#osahan-my-cart-item').append(cartItemsHtml);
                    $("#cartModal #checkout").attr('data-total', data.totalPrice).text(`Checkout (${data.totalPrice.toLocaleString('vi-VN')})`)
                }
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.replace("/signin.html");
                } else {
                    alert("Error fetching cart data", xhr.responseText);
                }
            }
        });
    }

    $(document).on('click', '#myitemsModal #add-cart', function (e) {
        e.preventDefault();
        e.stopPropagation();

        var foodId = $('#food-id').val();
        var quantity = $('#quantity-input').val();

        var dataToSend = {
            foodId: foodId,
            quantity: quantity
        };

        $.ajax({
            url: 'http://localhost:8080/api/carts',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            contentType: 'application/json',
            data: JSON.stringify(dataToSend),
            success: function (response) {
                loadCart();
                getAddress();
                $('#cartModal').modal('show');
            },
            error: function (xhr, status, error) {
                if (xhr.status === 401) {
                    window.location.replace('/signin.html');
                } else {
                    $("#message-error").removeClass("d-none").text(xhr.responseJSON.description).fadeTo(2000, 500).slideUp(500, function () {
                        $("#message-error").slideUp(500);
                    });
                }
            }
        });
    });

    $(document).on('click', '#checkout', function (e) {
        if (isEmptyCart) {
            $("#error-address").removeClass("d-none").text("Empty cart").fadeTo(2000, 500).slideUp(500, function () {
                $("#error-address").slideUp(500);
            });
            return;
        }
        checkAddress();
    });

    function checkAddress() {
        $.ajax({
            url: 'http://localhost:8080/users',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                var address = response.address;
                if (address === null || address.trim() === "") {
                    $("#error-address").removeClass("d-none").text("Please fill address").fadeTo(2000, 500).slideUp(500, function () {
                        $("#error-address").slideUp(500);
                    });
                } else {
                    window.location.replace('/checkout.html');
                }
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.replace('/signin.html');
                } else {
                    alert('Error: ' + xhr.responseText);
                }
            }
        });
    }

    $(document).on('click', '.remove-food', function (e) {
        e.preventDefault();
        var foodId = $(this).data('id');
        var foodRemove = $(this);
        $.ajax({
            url: `http://localhost:8080/api/carts/${foodId}`,
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                var cartItem = $(foodRemove).closest('.cart-item');
                var quantity = $(cartItem).find('.food-quantity').data('quantity');
                var price = $(cartItem).find('.food-price').data('price');
                var total = $("#cartModal #checkout").attr('data-total');
                var newTotalPrice = total - (quantity * price);

                $("#cartModal #checkout").attr('data-total', newTotalPrice).text(`Checkout (${newTotalPrice.toLocaleString('vi-VN')})`)
                $(foodRemove).closest('.cart-item').remove();

                // Kiểm tra số lượng món ăn còn lại trong giỏ hàng
                var remainingItems = $('#osahan-my-cart-item .cart-item').length;
                isEmptyCart = remainingItems === 0;
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.href.replace('/signin.html');
                } else {
                    alert('Error: ' + xhr.responseJSON.description);
                }
            }
        });
    });

    function getAddress() {
        $.ajax({
            url: 'http://localhost:8080/users',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (data) {
                $('#address').html(data.address);
                $('#inputAddress').val(data.address);
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.replace('/signin.html')
                } else {
                    alert('Error: ' + xhr.responseText)
                }
            }
        })
    }

    $('#edit-address').click(function (e) {
        e.preventDefault()

        $.ajax({
            url: 'http://localhost:8080/users/address?address=' + $('#inputAddress').val(),
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                $('#address').html($('#inputAddress').val());
                $('#myaddressModal').modal('toggle');
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.replace('/signin.html')
                } else {
                    $('#message-address').removeClass("d-none alert-success").addClass("alert-danger").text("Error: " + xhr.responseJSON.description).fadeTo(2000, 500).slideUp(500, function () {
                        $("#message-address").slideUp(500)
                    })
                }
            }
        })
    })
});