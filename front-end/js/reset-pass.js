$(document).ready(function () {

    var urlParams = new URLSearchParams(window.location.search);
    var token = urlParams.get("token");
    if (!token) {
        window.location.replace("/signin.html");
    }

    $("#btn-reset").click(function (e) {
        e.preventDefault();

        var password = $("#password").val();
        var confirmPassword = $("#repassword").val();

        if (password !== confirmPassword) {
            $('#message').removeClass("d-none").text("Passwords do not match").fadeTo(2000, 500).slideUp(500, function () {
                $("#message").slideUp(500)
            })
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/user/reset-password',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                token: token,
                password: password,
                confirmPassword: confirmPassword
            }),
            success: function (response) {
                window.location.replace('/signin.html');
            },
            error: function (xhr) {
                $('#message').removeClass("d-none").text(xhr.responseJSON.description).fadeTo(2000, 500).slideUp(500, function () {
                    $("#message").slideUp(500)
                })
            }
        });
    });
});