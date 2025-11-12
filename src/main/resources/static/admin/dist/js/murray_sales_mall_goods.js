$(function () {
    $("#jqGrid").jqGrid({
        url: '/admin/goods/list',
        datatype: "json",
        colModel: [
            {label: 'Product ID', name: 'goodsId', index: 'goodsId', width: 60, key: true},
            {label: 'Product Name', name: 'goodsName', index: 'goodsName', width: 120},
            {label: 'Product Description', name: 'goodsIntro', index: 'goodsIntro', width: 120},
            {label: 'Product Image', name: 'goodsCoverImg', index: 'goodsCoverImg', width: 120, formatter: coverImageFormatter},
            {label: 'Stock', name: 'stockNum', index: 'stockNum', width: 60},
            {label: 'Selling Price', name: 'sellingPrice', index: 'sellingPrice', width: 60},
            {
                label: 'Status',
                name: 'goodsSellStatus',
                index: 'goodsSellStatus',
                width: 80,
                formatter: goodsSellStatusFormatter
            },
            {label: 'Create Time', name: 'createTime', index: 'createTime', width: 60}
        ],
        height: 760,
        rowNum: 20,
        rowList: [20, 50, 80],
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

    function goodsSellStatusFormatter(cellvalue) {
        // Product status 0-On Sale 1-Off Shelf
        if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 80%;\">On Sale</button>";
        }
        if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 80%;\">Off Shelf</button>";
        }
    }

    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"80\" width=\"80\" alt='Product Image'/>";
    }

});

/**
 * jqGrid reload
 */
function reload() {
    initFlatPickr();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

/**
 * Add product
 */
function addGoods() {
    window.location.href = "/admin/goods/edit";
}

/**
 * Edit product
 */
function editGoods() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.location.href = "/admin/goods/edit/" + id;
}

/**
 * Put on sale
 */
function putUpGoods() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    Swal.fire({
        title: "Confirm",
        text: "Are you sure you want to put these products on sale?",
        icon: "warning",
        iconColor:"#dea32c",
        showCancelButton: true,
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel'
    }).then((flag) => {
        if (flag.value) {
            $.ajax({
                type: "PUT",
                url: "/admin/goods/status/0",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function (r) {
                    if (r.resultCode == 200) {
                        Swal.fire({
                            text: "Successfully put on sale",
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

/**
 * Take off shelf
 */
function putDownGoods() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    Swal.fire({
        title: "Confirm",
        text: "Are you sure you want to take these products off shelf?",
        icon: "warning",
        iconColor:"#dea32c",
        showCancelButton: true,
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel'
    }).then((flag) => {
        if (flag.value) {
            $.ajax({
                type: "PUT",
                url: "/admin/goods/status/1",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function (r) {
                    if (r.resultCode == 200) {
                        Swal.fire({
                            text: "Successfully taken off shelf",
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