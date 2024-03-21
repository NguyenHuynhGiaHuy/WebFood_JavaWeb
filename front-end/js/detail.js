$(document).ready(function () {
    var linkRestaurant = "http://localhost:8080/restaurant";
    let searchParams = new URLSearchParams(window.location.search);
    let id = searchParams.get('id');

    if(id) {
        $.ajax({
            method: "GET",
            url: `${linkRestaurant}/detail?id=${id}`,
            success: function (msg) {
                //feature-restaurant
                var value = msg.data;
                var html = `<div class="mt-n5 bg-white p-3 mb-4 rounded shadow position-relative">
                                <div class="osahan-single-top-info d-flex">
                                    <img style="width:60px; height:60px" src="${linkRestaurant}/file/${value.image}"
                                        class="img-fluid border p-2 mb-auto rounded brand-logo shadow-sm">
                                    <div class="ml-3">
                                        <h3 class="mb-0 font-weight-bold">${value.title}<small><i
                                                    class="mdi mdi-silverware-fork-knife ml-2 mr-1"></i> ${value.subtitle}</small></h3>
                                        <div class="restaurant-detail mt-2 mb-3">
                                            <span class="badge badge-light"><i class="mdi mdi-truck-fast-outline"></i> Free
                                                delivery</span>
                                            <span class="badge badge-success"><i class="mdi mdi-ticket-percent-outline"></i> 55%
                                                OFF</span>
                                            <span class="badge badge-info"><i class="mdi mdi-clock-outline"></i> ${value.openDate}</span>
                                        </div>
                                        <p class="text-muted p-0 mt-2 mb-2">${value.description}</p>
                                        <p class="mb-0 small">
                                            <i class="mdi mdi-star text-warning"></i> <span
                                                class="font-weight-bold text-dark">${value.rating !== "NaN" ? value.rating.toFixed(1) : 0}</span> - ${value.countCustomers} Ratings
                                            <i class="fas fa-map-marked-alt text-dark ml-3 mr-2"></i>${value.address}
                                        </p>
                                    </div>
                                </div>
                            </div>`
                var itemCategoryHtml = "";
                var menuHtml = "";
                $.each(value.categories, function (index, data) {
                    itemCategoryHtml += `<li class="nav-item mr-2" role="presentation">
                                            <a class="nav-link ${index == 0 ? 'active' : ''} border-0 btn btn-light" id="${data.name}-tab" data-toggle="tab"
                                                href="#tab-${index}" role="tab" aria-controls="${data.name}" aria-selected="${index == 0 ? 'true' : ''}">${data.name}</a>
                                        </li>`
                    var itemMenuHtml = "";
                    $.each(data.foodDTOList, function (index, itemMenu) {
                        itemMenuHtml += `<a href="#" class="text-decoration-none text-dark col-lg-3 col-md-6 mb-4 food" data-toggle="modal" data-target="#myitemsModal" data-id="${itemMenu.id}"
                        data-restaurant="${value.title}" data-title="${itemMenu.title}" data-description="${itemMenu.description}" data-image-url="${linkRestaurant}/file/${itemMenu.image}" data-price="${itemMenu.price.toFixed(0)}">
                                            <img style="width:295px; height:194px" src="${linkRestaurant}/file/${itemMenu.image}" class="img-fluid rounded">
                                            <div class="d-flex align-items-center mt-3 mb-2">
                                                <p class="text-black h6 m-0">${itemMenu.title}</p>
                                                ${value.freeship ? '<span class="badge badge-light ml-auto"><i class="mdi mdi-truck-fast-outline"></i> Free delivery</span>' : ''}
                                            </div>
                                            <p class="small mb-2"><i class="mdi mdi-star text-warning"></i> <span
                                                    class="font-weight-bold text-dark ml-1">${itemMenu.title}</span> <i
                                                    class="mdi mdi-silverware-fork-knife ml-2 mr-1"></i> ${data.name} <i
                                                    class="mdi mdi-motorbike ml-2 mr-2"></i>45
                                                - 55 min
                                            </p>
                                        </a>`
                    });
    
                    menuHtml += `<div class="tab-pane fade ${index == 0 ? 'show active' : ''}" id="tab-${index}" role="tabpanel"
                                    aria-labelledby="tab-${index}-tab">
    
                                    <div class="row">
                                        ${itemMenuHtml}
                                    </div>
                                </div>`;
                });
                var categoryHtml = `<ul class="nav nav-tabs border-0 mb-4" id="myTab" role="tablist">
                                        ${itemCategoryHtml}
                                    </ul>`
                $("#c-detail").append(html);
                $("#c-detail").append(categoryHtml);
                $("#c-detail").append(`<div class="tab-content" id="myTabContent">${menuHtml}</div>`);
            },
            error: function (xhr) {
                alert(xhr.responseJSON.description);
            }
        })
    }

    $(document).on('click', '.food', function (event) {
        event.preventDefault();

        var id = $(this).data('id');
        var title = $(this).data('title');
        var description = $(this).data('description');
        var imageUrl = $(this).data('image-url');
        var restaurant = $(this).data('restaurant');
        var price = $(this).data('price');
        var formattedPrice = parseInt(price).toLocaleString('vi-VN');

        var html = `<input hidden id="food-id" value=${id}>
                    <div class="modal-header border-0">
                        <h5 class="modal-title" id="exampleModalLabel">${restaurant}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body px-3 py-0">
                        <div class="pb-3 position-relative">
                            <div class="position-absolute heart-fav">
                                <a href="#"><i class="mdi mdi-heart"></i></a>
                            </div>
                            <img style="width:360px; height:237px" src="${imageUrl}" class="img-fluid col-md-12 p-0 rounded">
                        </div>
                        <h4 class="mb-2">${title}</h4>
                        <p>${description}</p>
                        <label style="font-size: 21px" class="btn osahan-radio btn-light btn-sm rounded">
                            Quantity:
                            <input style="width: 60px" type="number" id="quantity-input" min=1 value=1>
                        </label>
                        <div id="message-error" class="d-none alert alert-danger"></div>
                    </div>
                    <div class="modal-footer">
                        <button id="add-cart" class="btn btn-primary btn-block">Add ${formattedPrice}</button>
                    </div>`;

        $('#info-food').html(html)

    });

    $(document).on('click', '#apply-filter', function (e) {
        var categoryId = $('input[name="category"]:checked').val();
        var priceRange = $('input[name="price"]:checked').val();
        var sortBy = $('input[name="sort-by"]:checked').val();

        var [minPrice, maxPrice] = processPriceRange(priceRange);

        var queryParams = `categoryId=${categoryId}`;
        if (minPrice != null) queryParams += `&minPrice=${minPrice}`;
        if (maxPrice != null) queryParams += `&maxPrice=${maxPrice}`;
        queryParams += `&sortBy=${sortBy}`;

        window.location.replace(`/listing.html?${queryParams}`)
    })

    function processPriceRange(priceRange) {
        if (priceRange === "0-50000") {
            return [0, 50000];
        } else if (priceRange === "51000-100000") {
            return [51000, 100000];
        } else if (priceRange === "100000") {
            return [100000, null];
        }
        return [null, null];
    }

    $.ajax({
        url: 'http://localhost:8080/category/all',
        method: 'GET',
        success: function (response) {
            var categories = response.data
            $.each(categories, function (index, category) {
                var isChecked = index === 0 ? 'checked' : '';
                var focus = index === 0 ? 'focus active' : '';
                var html = `<label class="btn osahan-radio btn-light active btn-sm rounded ${focus}">
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
