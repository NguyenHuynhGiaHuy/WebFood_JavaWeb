function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

$(document).ready(function () {
    $("#btn-signin").click(function (e) {
        e.preventDefault();
        var username = $("#username").val();
        var password = $("#password").val();
        $.ajax({
            method: "POST",
            url: "http://localhost:8080/login/signin",
            data: {
                username: username,
                password: password,
            },
            success: function (response) {
                var token = response.data
                localStorage.setItem("token", token);

                if (parseJwt(token).roles === "ROLE_ADMIN") {
                    window.location.replace('/admin');
                } else {
                    window.location.replace('/index.html');
                }
            },
            error: function (xhr, status, error) {
                $("#message").removeClass("d-none").text(xhr.responseJSON.description).fadeTo(2000, 500).slideUp(500, function () {
                    $("#message").slideUp(500);
                });
            }
        })
    });
});
