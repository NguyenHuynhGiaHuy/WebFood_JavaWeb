$(document).ready(function () {
    var linkRestaurant = "http://localhost:8080/restaurant"
    var linkCategory = "http://localhost:8080/category"
    var linkMenu = "http://localhost:8080/menu"
    $.ajax({
        method: "GET",
        url: linkRestaurant,
        success: function (response) {
            $.each(response.data, function (index, value) {
                var html = `<a href="detail.html?id=${value.id}" class="text-dark text-decoration-none col-xl-4 col-lg-12 col-md-12">
                                <div class="bg-white shadow-sm rounded d-flex align-items-center p-1 mb-4 osahan-list">
                                    <div class="bg-light p-3 rounded">
                                        <img src="${linkRestaurant}/file/${value.image}" class="img-fluid">
                                    </div>
                                    <div class="mx-3 py-2 w-100">
                                        <p class="mb-2 text-black">${value.title}</p>
                                        <p class="small mb-2">
                                            <i class="mdi mdi-star text-warning mr-1"></i> <span
                                                class="font-weight-bold text-dark">${value.rating !== "NaN" ? value.rating.toFixed(1) : 0}</span> ${value.countCustomers}
                                            <i class="mdi mdi-silverware-fork-knife ml-3 mr-1"></i> ${value.subtitle}
                                        </p>
                                        ${value.freeship ? '<p class="mb-0 text-muted d-flex align-items-center"><span class="badge badge-light"><i class="mdi mdi-truck-fast-outline"></i>Free delivery</span>' : ''}
                                        </p>
                                    </div>
                                </div>
                            </a>`
                $("#feature-restaurant").append(html)
            });
        },
        error: function (xhr, status, error) {
            alert(xhr.responseJSON.description);
        }
    })
    //Load categories
    $.ajax({
        method: "GET",
        url: linkCategory,
        success: function (response) {
            $.each(response.data, function (index, value) {
                if(value.foodDTOList.length > 0) {
                    //show category
                    var htmlCategory = `<div class="d-flex align-items-center justify-content-between mb-3 mt-2">
                                            <h5 class="mb-0">${value.name}</h5>
                                            <a href="listing.html?categoryId=${value.categoryId}" class="small font-weight-bold text-dark">See all <i
                                                    class="mdi mdi-chevron-right mr-2"></i></a>
                                        </div>`
                    htmlCategory += `<div class="row">`

                    $.each(value.foodDTOList, function(index, value) {
                    htmlCategory += `<a href="#" class="text-decoration-none text-dark col-lg-3 col-md-6 mb-4 food" data-toggle="modal" data-target="#myitemsModal" data-id="${value.id}"
                    data-restaurant="${value.restaurant}" data-title="${value.title}" data-description="${value.description}" data-image-url="${linkRestaurant}/file/${value.image}" data-price="${value.price.toFixed(0)}">
                                        <img style="width:295px; height:194px" src="${linkMenu}/file/${value.image}" class="img-fluid rounded">
                                        <div class="d-flex align-items-center mt-3">
                                            <p class="text-black h6 m-0">${value.title}</p>
                                            ${value.freeShip ? '<span class="badge badge-light ml-auto"><i class="mdi mdi-truck-fast-outline"></i> Free delivery</span>' : ''}
                                        </div>
                                    </a>`
                    });
                    htmlCategory += `</div>`;
                    $("#body-osahaneat").append(htmlCategory);
                }
                
            });
        },
        error: function (xhr, status, error) {
            alert(xhr.responseJSON.description);
        }
    })

    $('#logout').click(function(e) {
        e.preventDefault();
        localStorage.removeItem('token');
        window.location.replace('/signin.html');
    })

    $('#btn-search').click(function (e) {
        e.preventDefault();
        var keyword = $('#search-value').val();
        window.location.replace('/search.html?keyword=' + keyword);
    })

});