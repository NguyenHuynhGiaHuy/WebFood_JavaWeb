$(document).ready(function() {
    var linkAdmin = "http://localhost:8080/admin/users";

    // Lấy danh sách tất cả người dùng
    function getAllUsers() {
        $.ajax({
            url: linkAdmin,
            method: 'GET',
            success: function(response) {
                displayUsers(response);
            },
            error: function(xhr) {
                console.error(xhr.responseText);
            }
        });
    }

    // Hiển thị thông tin người dùng trên trang web
    function displayUsers(users) {
        var userList = $("#user-list");
        userList.empty(); // Xóa nội dung cũ trước khi thêm mới

        users.forEach(function(user) {
            var userHtml = `<div>
                                <p>ID: ${user.id}</p>
                                <p>Username: ${user.username}</p>
                                <p>Full Name: ${user.fullName}</p>
                                <p>Create Date: ${user.createDate}</p>
                                <button class="delete-user" data-user-id="${user.id}">Delete</button>
                                <hr>
                            </div>`;
            userList.append(userHtml);
        });
    }

    function deleteUser(userId) {
        $.ajax({
            url: `${linkAdmin}/${userId}`,
            method: 'DELETE',
            success: function(response) {
                console.log(response);
                getAllUsers(); // Refresh user list after deletion
            },
            error: function(xhr) {
                console.error(xhr.responseText);
            }
        });
    }

    // Event listener for delete buttons
    $(document).on("click", ".delete-user", function() {
        var userId = $(this).data("user-id");
        deleteUser(userId);
    });

    getAllUsers(); // Gọi hàm lấy danh sách người dùng khi trang tải lần đầu
});
