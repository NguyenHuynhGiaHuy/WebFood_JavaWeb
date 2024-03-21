$(document).ready(function () {
    $('#btn-signup').click(function (e) {
        e.preventDefault();

        var signupData = {
            fullName: $('#fullName').val(),
            email: $('#email').val(),
            phone: $('#phone').val(),
            password: $('#password').val(),
            repassword: $('#repassword').val()
        };

        $.ajax({
            url: 'http://localhost:8080/login/signup',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(signupData),
            success: function (response) {
                window.location.replace('signin.html');
            },
            error: function (xhr, status, error) {
                $("#message").removeClass("d-none").text(xhr.responseJSON.description).fadeTo(2000, 500).slideUp(500, function () {
                    $("#message").slideUp(500);
                });
            }
        });
    });
});
