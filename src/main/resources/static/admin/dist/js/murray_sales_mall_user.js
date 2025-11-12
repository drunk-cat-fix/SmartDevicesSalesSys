$(function () {
    $("#jqGrid").jqGrid({
        url: '/admin/users/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'userId', index: 'userId', width: 50, key: true, hidden: true},
            {label: 'Nickname', name: 'nickName', index: 'nickName', width: 180},
            {label: 'Login Name', name: 'loginName', index: 'loginName', width: 120},
            {label: 'Status', name: 'lockedFlag', index: 'lockedFlag', width: 60, formatter: lockedFormatter},
            {label: 'Deleted', name: 'isDeleted', index: 'isDeleted', width: 60, formatter: deletedFormatter},
            {label: 'Registration Time', name: 'createTime', index: 'createTime', width: 120}
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

    function lockedFormatter(cellvalue) {
        if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">Locked</button>";
        } else if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">Normal</button>";
        }
    }

    function deletedFormatter(cellvalue) {
        if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">Deleted</button>";
        } else if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">Normal</button>";
        }
    }
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

function lockUser(lockStatus) {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    if (lockStatus != 0 && lockStatus != 1) {
        Swal.fire({
            text: 'Illegal operation',
            icon: "error",
            iconColor:"#f05b72",
        });
    }
    Swal.fire({
        title: "Confirm",
        text: "Are you sure you want to modify the account status?",
        icon: "warning",
        iconColor:"#dea32c",
        showCancelButton: true,
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel'
    }).then((flag) => {
        if (flag.value) {
            $.ajax({
                type: "POST",
                url: "/admin/users/lock/" + lockStatus,
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function (r) {
                    if (r.resultCode == 200) {
                        Swal.fire({
                            text: "Operation successful",
                            icon: "success",
                            iconColor:"#1d953f",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        Swal.fire({
                            text: r.message,
                            icon: "error",
                            iconColor:"#f05b72",
                        });
                    }
                }
            });
        }
    });
}