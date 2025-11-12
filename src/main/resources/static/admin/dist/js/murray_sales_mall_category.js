$(function () {
    var categoryLevel = $("#categoryLevel").val();
    var parentId = $("#parentId").val();

    $("#jqGrid").jqGrid({
        url: '/admin/categories/list?categoryLevel=' + categoryLevel + '&parentId=' + parentId,
        datatype: "json",
        colModel: [
            {label: 'id', name: 'categoryId', index: 'categoryId', width: 50, key: true, hidden: true},
            {label: 'Category Name', name: 'categoryName', index: 'categoryName', width: 240},
            {label: 'Sort Order', name: 'categoryRank', index: 'categoryRank', width: 120},
            {label: 'Create Time', name: 'createTime', index: 'createTime', width: 120}
        ],
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: 'Loading information...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            // Hide grid bottom scrollbar
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

/**
 * jqGrid reload
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function categoryAdd() {
    reset();
    $('.modal-title').html('Add Category');
    $('#categoryModal').modal('show');
}

/**
 * Manage subcategories
 */
function categoryManage() {
    var categoryLevel = parseInt($("#categoryLevel").val());
    var parentId = $("#parentId").val();
    var id = getSelectedRow();
    if (id == undefined || id < 0) {
        return false;
    }
    if (categoryLevel == 1 || categoryLevel == 2) {
        categoryLevel = categoryLevel + 1;
        window.location.href = '/admin/categories?categoryLevel=' + categoryLevel + '&parentId=' + id + '&backParentId=' + parentId;
    } else {
        Swal.fire({
            text: "No subcategories",
            icon: "warning",
            iconColor: "#dea32c",
        });
    }
}

/**
 * Return to previous level
 */
function categoryBack() {
    var categoryLevel = parseInt($("#categoryLevel").val());
    var backParentId = $("#backParentId").val();
    if (categoryLevel == 2 || categoryLevel == 3) {
        categoryLevel = categoryLevel - 1;
        window.location.href = '/admin/categories?categoryLevel=' + categoryLevel + '&parentId=' + backParentId + '&backParentId=0';
    } else {
        Swal.fire({
            text: "No parent category",
            icon: "warning",
            iconColor: "#dea32c",
        });
    }
}

// Bind save button on modal
$('#saveButton').click(function () {
    var categoryName = $("#categoryName").val();
    var categoryLevel = $("#categoryLevel").val();
    var parentId = $("#parentId").val();
    var categoryRank = $("#categoryRank").val();
    if (!validCN_ENString2_18(categoryName)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("Please enter a valid category name!");
    } else {
        var data = {
            "categoryName": categoryName,
            "categoryLevel": categoryLevel,
            "parentId": parentId,
            "categoryRank": categoryRank
        };
        var url = '/admin/categories/save';
        var id = getSelectedRowWithoutAlert();
        if (id != null) {
            url = '/admin/categories/update';
            data = {
                "categoryId": id,
                "categoryName": categoryName,
                "categoryLevel": categoryLevel,
                "parentId": parentId,
                "categoryRank": categoryRank
            };
        }
        $.ajax({
            type: 'POST',
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                if (result.resultCode == 200) {
                    $('#categoryModal').modal('hide');
                    Swal.fire({
                        text: "Save successful",
                        icon: "success",
                        iconColor: "#1d953f",
                    });
                    reload();
                } else {
                    $('#categoryModal').modal('hide');
                    Swal.fire({
                        text: result.message,
                        icon: "error",
                        iconColor: "#f05b72",
                    });
                }
            },
            error: function () {
                Swal.fire({
                    text: "Operation failed",
                    icon: "error",
                    iconColor: "#f05b72",
                });
            }
        });
    }
});

function categoryEdit() {
    reset();
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    var rowData = $("#jqGrid").jqGrid("getRowData", id);
    $('.modal-title').html('Edit Category');
    $('#categoryModal').modal('show');
    $("#categoryId").val(id);
    $("#categoryName").val(rowData.categoryName);
    $("#categoryRank").val(rowData.categoryRank);
}

/**
 * Deleting categories will affect multi-level category modifications and product data modifications.
 * Please be cautious when deleting category data.
 * If you don't want to display related categories on the mall page, you can adjust the display order by modifying the rank value.
 * However, I have written part of the code. If you want to keep the delete function, you can modify it based on this code.
 */
function deleteCagegory() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    Swal.fire({
        title: "Confirm",
        text: "Are you sure you want to delete the selected data?",
        icon: "warning",
        iconColor: "#dea32c",
        showCancelButton: true,
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel'
    }).then((flag) => {
        if (flag.value) {
            $.ajax({
                type: "POST",
                url: "/admin/categories/delete",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function (r) {
                    if (r.resultCode == 200) {
                        Swal.fire({
                            text: "Delete successful",
                            icon: "success",
                            iconColor: "#1d953f",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        Swal.fire({
                            text: r.message,
                            icon: "error",
                            iconColor: "#f05b72",
                        });
                    }
                }
            });
        }
    });
}

function reset() {
    $("#categoryName").val('');
    $("#categoryRank").val(0);
    $('#edit-error-msg').css("display", "none");
}