$(document).ready(function () {
    var token = localStorage.getItem('token')

    $('#profile').click(function (e) {
        e.preventDefault()

        $.ajax({
            url: 'http://localhost:8080/users',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (data) {
                var personalModal = `<div class="d-flex align-items-center mb-3">
                                        <div class="mr-3 bg-light rounded p-2 osahan-icon"><i class="mdi mdi-account-outline"></i></div>
                                        <div class="w-100">
                                            <p class="mb-0 small font-weight-bold text-dark">Full Name</p>
                                            <input id="full-name" type="text" class="form-control form-control-sm p-0 border-input border-0 rounded-0"
                                               value="${data.fullName}">
                                        </div>
                                    </div>
                                    <div class="d-flex align-items-center mb-3">
                                        <div class="mr-3 bg-light rounded p-2 osahan-icon"><i class="mdi mdi-email-outline"></i></i>
                                        </div>
                                        <div class="w-100">
                                            <p class="mb-0 small font-weight-bold text-dark">Email Address</p>
                                            <input id="email" type="email" class="form-control form-control-sm p-0 border-input border-0 rounded-0"
                                                value="${data.username}">
                                        </div>
                                    </div>
                                    <div class="d-flex align-items-center mb-3">
                                        <div class="mr-3 bg-light rounded p-2 osahan-icon"><i class="fas fa-phone-alt"></i></div>
                                        <div class="w-100">
                                            <p class="mb-0 small font-weight-bold text-dark">Phone Number</p>
                                            <input type="number" id="phone"
                                                class="form-control form-control-sm p-0 border-input border-0 rounded-0"
                                                value="${data.phone}">
                                        </div>
                                    </div>
                                    <div class="d-flex align-items-center mb-3">
                                        <div class="mr-3 bg-light rounded p-2 osahan-icon"><i class="fas fa-phone-alt"></i></div>
                                        <div class="w-100">
                                            <p class="mb-0 small font-weight-bold text-dark">Address</p>
                                            <input type="text" id="address"
                                                class="form-control form-control-sm p-0 border-input border-0 rounded-0"
                                                value="${data.address ? data.address : ''}">
                                        </div>
                                    </div>`
                $('#profile-content').html(personalModal)
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.replace('/signin.html')
                } else {
                    alert('Error: ' + xhr.responseText)
                }
            }
        })
    })

    $(document).on('click', '#btn-update-profile', function (e) {
        e.preventDefault()

        var info = {
            fullName: $('#full-name').val(),
            username: $('#email').val(),
            phone: $('#phone').val(),
            address: $('#address').val()
        }

        $.ajax({
            url: 'http://localhost:8080/users',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            contentType: "application/json",
            data: JSON.stringify(info),
            success: function () {
                $('#message-profile').removeClass("d-none alert-danger").addClass("alert-success").text("Update success").fadeTo(2000, 500).slideUp(500, function () {
                    $("#message-profile").slideUp(500)
                })
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.replace('/signin.html')
                } else {
                    $('#message-profile').removeClass("d-none alert-success").addClass("alert-danger").text("Error: " + xhr.responseJSON.description).fadeTo(2000, 500).slideUp(500, function () {
                        $("#message-profile").slideUp(500)
                    })
                }
            }
        })
    })
})