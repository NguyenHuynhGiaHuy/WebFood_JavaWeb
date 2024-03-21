$(document).ready(function () {
    var linkRestaurant = "http://localhost:8080/restaurant"
    var linkMenu = "http://localhost:8080/menu"

    loadResultSearch();
    filter();

    function loadResultSearch() {
        var searchParams = new URLSearchParams(window.location.search);
        var categoryId = searchParams.get('categoryId');
        if (categoryId) {
            $.ajax({
                url: 'http://localhost:8080/category/' + categoryId,
                method: 'GET',
                success: function (response) {
                    var data = response.data
                    $('#foods').empty();
                    $.each(data, function (index, food) {
                        var foodHtml = `<a href="#" class="text-decoration-none text-dark col-lg-3 col-md-6 mb-4 food" data-toggle="modal" data-target="#myitemsModal" data-id="${food.id}"
                        data-restaurant="${food.restaurant}" data-title="${food.title}" data-description="${food.description}" data-image-url="${linkRestaurant}/file/${food.image}" data-price="${food.price.toFixed(0)}">
                                            <img src="${linkMenu}/file/${food.image}" class="img-fluid rounded">
                                            <div class="d-flex align-items-center mt-3 mb-2">
                                                <p class="text-black h6 m-0">${food.title}</p>
                                                ${food.freeShip ? '<span class="badge badge-light ml-auto"><i class="mdi mdi-truck-fast-outline"></i> Free delivery</span>' : ''}
                                            </div>
                                        </a>`
                        $('#foods').append(foodHtml)
                    })
                },
                error: function (xhr) {
                    alert('Error: ' + xhr.responseText)
                }
            })
        }
    }

    $(document).on('click', '#apply-filter', function (e) {
        var categoryId = $('input[name="category"]:checked').val();
        var priceRange = $('input[name="price"]:checked').val();
        var sortBy = $('input[name="sort-by"]:checked').val();

        var [minPrice, maxPrice] = processPriceRange(priceRange);

        var queryParams = `categoryId=${categoryId}`;
        if (minPrice != null) queryParams += `&minPrice=${minPrice}`;
        if (maxPrice != null) queryParams += `&maxPrice=${maxPrice}`;
        queryParams += `&sortBy=${sortBy}`;

        var newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?' + queryParams;
        window.history.pushState({ path: newUrl }, '', newUrl);

        filter();
    })

    function filter() {
        var params = new URLSearchParams(window.location.search);
        var categoryId = params.get('categoryId');
        var sortBy = params.get('sortBy');

        if(!categoryId && !sortBy) {
            return;
        }   

        var url = getQueryParams();

        $.ajax({
            url: url,
            method: 'GET',
            contentType: 'application/json',
            success: function (response) {
                var data = response.data
                $('#foods').empty();
                $.each(data, function (index, food) {
                    var foodHtml = `<a href="#" class="text-decoration-none text-dark col-lg-3 col-md-6 mb-4 food" data-toggle="modal" data-target="#myitemsModal" data-id="${food.id}"
                    data-restaurant="${food.restaurant}" data-title="${food.title}" data-description="${food.description}" data-image-url="${linkRestaurant}/file/${food.image}" data-price="${food.price.toFixed(0)}">
                                        <img src="${linkMenu}/file/${food.image}" class="img-fluid rounded">
                                        <div class="d-flex align-items-center mt-3 mb-2">
                                            <p class="text-black h6 m-0">${food.title}</p>
                                            ${food.freeShip ? '<span class="badge badge-light ml-auto"><i class="mdi mdi-truck-fast-outline"></i> Free delivery</span>' : ''}
                                        </div>
                                    </a>`
                    $('#foods').append(foodHtml)
                })
            },
            error: function (xhr) {
                alert("Error: " + xhr.responseText);
            }
        });
    }

    function getQueryParams() {
        var params = new URLSearchParams(window.location.search);
        var categoryId = params.get('categoryId');
        var minPrice = params.get('minPrice');
        var maxPrice = params.get('maxPrice');
        var sortBy = params.get('sortBy');

        return `http://localhost:8080/category/filter?categoryId=${categoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sortBy=${sortBy}`;
    }

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
})