$(document).ready(function() {
    $("#btn-forgot").click(function(e) {
        e.preventDefault();
        var email = $("#email").val();

        if (email) {
            $.ajax({
                url: 'http://localhost:8080/user/forgot-password/' + email,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(email),
                success: function(response) {
                    $('#email-sent').text(email);
                    $("#myforgotModal").modal('show');
                },
                error: function(xhr) {
                    $('#error-message').removeClass("d-none").text("Error: " + xhr.responseJSON.description).fadeTo(2000, 500).slideUp(500, function () {
                        $("#error-message").slideUp(500)
                    })
                }
            });
        } else {
            $('#error-message').removeClass("d-none").text("Please enter an email").fadeTo(2000, 500).slideUp(500, function () {
                $("#error-message").slideUp(500)
            })
        }
    });
});
