$(document).ready(function () {

    var linkRestaurant = "http://localhost:8080/restaurant"
    var linkMenu = "http://localhost:8080/menu"

    loadResultSearch();

    $('#btn-search').click(function (e) {
        e.preventDefault();
        var keyword = $('#search-value').val();
        history.replaceState(null, '', '/search.html?keyword=' + encodeURIComponent(keyword));
        loadResultSearch();
    })

    function loadResultSearch() {
        var searchParams = new URLSearchParams(window.location.search);
        var keyword = searchParams.get('keyword');
        $.ajax({
            url: 'http://localhost:8080/menu?keyword=' + keyword,
            method: 'GET',
            success: function (response) {
                var data = response.data
                $('#restaurant').empty();
                $('#foods').empty();
                $.each(data.restaurants, function (index, restaurant) {
                    var restaurantHtml = `<a href="detail.html?id=${restaurant.id}" class="text-dark 
                                            text-decoration-none col-xl-4 col-lg-12 col-md-12">
                                            <div class="bg-white shadow-sm rounded d-flex align-items-center p-1 mb-4 osahan-list">
                                                <div class="bg-light p-3 rounded">
                                                    <img src="${linkRestaurant}/file/${restaurant.image}" class="img-fluid">
                                                </div>
                                                <div class="mx-3 py-2 w-100">
                                                    <p class="mb-2 text-black">${restaurant.title}</p>
                                                    <p class="small mb-2">
                                                        <i class="mdi mdi-star text-warning mr-1"></i> <span
                                                            class="font-weight-bold text-dark">${restaurant.rating.toFixed(1)}</span> ${restaurant.countCustomers}
                                                        <i class="mdi mdi-silverware-fork-knife ml-3 mr-1"></i> ${restaurant.subtitle}
                                                    </p>
                                                    ${restaurant.freeship ? '<p class="mb-0 text-muted d-flex align-items-center"><span class="badge badge-light"><i class="mdi mdi-truck-fast-outline"></i>Free delivery</span>' : ''}
                                                    </p>
                                                </div>
                                            </div>
                                        </a>`
                    $('#restaurant').append(restaurantHtml)
                })

                $.each(data.foods, function (index, food) {
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
})