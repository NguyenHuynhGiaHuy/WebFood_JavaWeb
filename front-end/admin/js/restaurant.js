var token = localStorage.getItem('token');

function editRes(id) {
    $.ajax({
        url: "http://localhost:8080/restaurant/" + id,
        type: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (response) {
            var data = response.data;

            $('#editId').val(data.id);
            $('#editResName').val(data.title)
            $('#editSubtitle').val(data.subtitle)
            $('#editOpenTime').val(data.openDate)
            $('#editAddress').val(data.address)
            $('#editDescription').val(data.description)
            $('#edit-image').html(`<img data-img=${data.image} src="http://localhost:8080/restaurant/file/${data.image}" alt="Image" style="width: 60px; height: 60px">`);

        },
        error: function (xhr) {
            if (xhr.status === 401) {
                window.location.replace('/index.html');
            }
        }
    });
}

function deleteRes(id) {
    $.ajax({
        url: 'http://localhost:8080/restaurant/' + id,
        type: 'DELETE',
        contentType: 'application/json',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (response) {
            $('#restaurants').DataTable().ajax.reload(null, false);
        },
        error: function (xhr, status, error) {
            alert(xhr.responseJSON.description);
        }
    });
}

$(document).ready(function () {
    $('#restaurants').DataTable({
        "processing": true,
        "serverSide": true,
        "info": false,
        "sort": false,
        "ajax": {
            "url": "http://localhost:8080/restaurant/restaurants",
            "type": "GET",
            headers: {
                'Authorization': 'Bearer ' + token
            },
            "data": function (d) {
                d.page = d.start / d.length;
                d.size = d.length;
                d.search = d.search.value;
            },
            "dataSrc": function (json) {
                json.recordsTotal = json.totalElements;
                json.recordsFiltered = json.totalElements;
                return json.content;
            }
        },
        "pageLength": 2,
        "lengthMenu": [2, 10, 15, 20],
        "columns": [
            { "data": "id" },
            {
                "data": "image",
                "render": function (data) {
                    return `<img src="http://localhost:8080/restaurant/file/${data}" alt="Image" style="width: 60px; height: 60px">`;
                }
            },
            { "data": "title" },
            { "data": "subtitle" },
            { "data": "description" },
            { "data": "address" },
            {
                "data": "freeship",
                "render": function (data) {
                    return data ? "Yes" : "No";
                }
            },
            { "data": "openDate" },
            {
                "data": null,
                "defaultContent": "",
                "render": function (data, type, row) {
                    return `<div class="d-flex">
                    <button class="btn btn-primary mx-2" data-bs-toggle="modal" data-bs-target="#editRestaurantModal" onclick="editRes(${row.id})">Edit</button>
                    <button class="btn btn-danger mx-2" onclick="deleteRes(${row.id})">Delete</button>
                    </div>`;
                }
            }
        ]
    });



    $("#addRestaurantForm").on("submit", function (e) {
        e.preventDefault();

        var formData = new FormData(this);
        formData.append('title', $('#addResName').val());
        formData.append('subTitle', $('#addSubtitle').val());
        formData.append('description', $('#addDescription').val());
        formData.append('file', $('#addImage')[0].files[0]);
        formData.append('isFreeship', $('#freeship').val() === 'true');
        formData.append('address', $('#addAddress').val());
        formData.append('openDate', $('#addOpenTime').val());

        $.ajax({
            url: 'http://localhost:8080/restaurant/add',
            type: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            data: formData,
            contentType: false,
            processData: false,
            success: function (response) {
                if (response.data === true) {
                    $('#restaurants').DataTable().ajax.reload(null, false);
                } else {
                    alert("Error:", response.description);
                }
            },
            error: function (xhr, status, error) {
                alert("Error:", xhr.responseText);
            }
        });
    });


    $('#addImage').on('change', function (event) {
        var files = event.target.files;
        $('#images').empty();

        $.each(files, function (i, file) {
            if (file.type.startsWith('image/')) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    var img = `<img style="height: 150px; width: 150px;" class="img-thumbnail" src="${e.target.result}"/>`;
                    $('#images').append(img);
                };

                reader.readAsDataURL(file);
            }
        });
    });

    $('#editImage').on('change', function (event) {
        var files = event.target.files;
        $('#edit-image').empty();

        $.each(files, function (i, file) {
            if (file.type.startsWith('image/')) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    var img = `<img style="height: 150px; width: 150px;" class="img-thumbnail" src="${e.target.result}"/>`;
                    $('#edit-image').append(img);
                };

                reader.readAsDataURL(file);
            }
        });
    });

    $("#editRestaurantForm").on("submit", function (e) {
        e.preventDefault();

        var formData = new FormData(this);
        formData.append('title', $('#editResName').val());
        formData.append('subTitle', $('#editSubtitle').val());
        formData.append('description', $('#editDescription').val());
        if ($('#editImage')[0].files[0]) {
            formData.append('file', $('#editImage')[0].files[0]);
        } else if ($('#edit-image img')) {
            formData.append('file', $('#edit-image img').data('img'));
        }
        formData.append('isFreeship', $('#editFreeship').val() === 'true');
        formData.append('address', $('#editAddress').val());
        formData.append('openDate', $('#editOpenTime').val());

        $.ajax({
            url: 'http://localhost:8080/restaurant/' + $('#editId').val(),
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            data: formData,
            contentType: false,
            processData: false,
            success: function (response) {
                if (response.data) {
                    $('#restaurants').DataTable().ajax.reload(null, false);
                    $('#editRestaurantModal').modal('toggle');
                }
            },
            error: function (xhr, status, error) {
                alert("Error:", xhr.responseText);
            }
        });
    });


});
