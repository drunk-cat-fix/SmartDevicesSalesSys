/**
 * jqGrid English Translation
 *
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 **/
/*global jQuery, define */
(function( factory ) {
    "use strict";
    if ( typeof define === "function" && define.amd ) {
        // AMD. Register as an anonymous module.
        define([
            "jquery",
            "../grid.base"
        ], factory );
    } else {
        // Browser globals
        factory( jQuery );
    }
}(function( $ ) {

    $.jgrid = $.jgrid || {};
    if(!$.jgrid.hasOwnProperty("regional")) {
        $.jgrid.regional = [];
    }
    $.jgrid.regional["en"] = {
        defaults : {
            recordtext: "View {0} - {1} of {2}",
            emptyrecords: "No records to view",
            loadtext: "Loading...",
            savetext: "Saving...",
            pgtext : "Page {0} of {1}",
            pgfirst : "First Page",
            pglast : "Last Page",
            pgnext : "Next Page",
            pgprev : "Previous Page",
            pgrecs : "Records per Page",
            showhide: "Toggle Expand Collapse Grid",
            // mobile
            pagerCaption : "Grid::Page Settings",
            pageText : "Page:",
            recordPage : "Records per Page",
            nomorerecs : "No more records...",
            scrollPullup: "Load more...",
            scrollPulldown : "Refresh...",
            scrollRefresh : "Scroll to refresh..."
        },
        search : {
            caption: "Search...",
            Find: "Find",
            Reset: "Reset",
            odata: [{ oper:'eq', text:'equal\u3000\u3000'},{ oper:'ne', text:'not equal\u3000'},{ oper:'lt', text:'less\u3000\u3000'},{ oper:'le', text:'less or equal'},{ oper:'gt', text:'greater\u3000\u3000'},{ oper:'ge', text:'greater or equal'},{ oper:'bw', text:'begins with'},{ oper:'bn', text:'does not begin with'},{ oper:'in', text:'is in\u3000\u3000'},{ oper:'ni', text:'is not in'},{ oper:'ew', text:'ends with'},{ oper:'en', text:'does not end with'},{ oper:'cn', text:'contains\u3000\u3000'},{ oper:'nc', text:'does not contain'},{ oper:'nu', text:'is null'},{ oper:'nn', text:'is not null'}, {oper:'bt', text:'between'}],
            groupOps: [ { op: "AND", text: "all" },    { op: "OR",  text: "any" } ],
            operandTitle : "Click to choose search operation.",
            resetTitle : "Reset Search Value",
            addsubgrup : "Add subgroup",
            addrule : "Add rule",
            delgroup : "Delete group",
            delrule : "Delete rule",
            Close : "Close",
            Operand : "Operand : ",
            Operation : "Oper : "
        },
        edit : {
            addCaption: "Add Record",
            editCaption: "Edit Record",
            bSubmit: "Submit",
            bCancel: "Cancel",
            bClose: "Close",
            saveData: "Data has been changed! Save changes?",
            bYes : "Yes",
            bNo : "No",
            bExit : "Cancel",
            msg: {
                required:"Field is required",
                number:"Please enter a valid number",
                minValue:"Value must be greater than or equal to ",
                maxValue:"Value must be less than or equal to ",
                email: "This is not a valid e-mail address",
                integer: "Please enter a valid integer",
                date: "Please enter a valid date",
                url: "This is not a valid URL. Prefix required ('http://' or 'https://')",
                nodefined : " is not defined!",
                novalue : " return value is required!",
                customarray : "Custom function should return array!",
                customfcheck : "Custom function should be present!"
            }
        },
        view : {
            caption: "View Record",
            bClose: "Close"
        },
        del : {
            caption: "Delete",
            msg: "Delete selected record(s)?",
            bSubmit: "Delete",
            bCancel: "Cancel"
        },
        nav : {
            edittext: "",
            edittitle: "Edit selected row",
            addtext:"",
            addtitle: "Add new row",
            deltext: "",
            deltitle: "Delete selected row",
            searchtext: "",
            searchtitle: "Find records",
            refreshtext: "",
            refreshtitle: "Reload Grid",
            alertcap: "Warning",
            alerttext: "Please, select a row",
            viewtext: "",
            viewtitle: "View selected row",
            savetext: "",
            savetitle : "Save row",
            canceltext: "",
            canceltitle : "Cancel row editing",
            selectcaption : "Actions..."
        },
        col : {
            caption: "Select columns",
            bSubmit: "Ok",
            bCancel: "Cancel"
        },
        errors : {
            errcap : "Error",
            nourl : "No url is set",
            norecords: "No records to process",
            model : "Length of colNames and colModel does not match!"
        },
        formatter : {
            integer : {thousandsSeparator: ",", defaultValue: '0'},
            number : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, defaultValue: '0.00'},
            currency : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "", suffix:"", defaultValue: '0.00'},
            date : {
                dayNames:   [
                    "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
                    "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
                ],
                monthNames: [
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                    "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
                ],
                AmPm : ["am","pm","AM","PM"],
                S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th';},
                srcformat: 'Y-m-d',
                newformat: 'Y-m-d',
                parseRe : /[#%\\\/:_;.,\t\s-]/,
                masks : {
                    ISO8601Long:"Y-m-d H:i:s",
                    ISO8601Short:"Y-m-d",
                    ShortDate: "n/j/Y",
                    LongDate: "l, F d, Y",
                    FullDateTime: "l, F d, Y g:i:s A",
                    MonthDay: "F d",
                    ShortTime: "g:i A",
                    LongTime: "g:i:s A",
                    SortableDateTime: "Y-m-d\\TH:i:s",
                    UniversalSortableDateTime: "Y-m-d H:i:sO",
                    YearMonth: "F, Y"
                },
                reformatAfterEdit : false,
                userLocalTime : false
            },
            baseLinkUrl: '',
            showAction: '',
            target: '',
            checkbox : {disabled:true},
            idName : 'id'
        },
        colmenu : {
            sortasc : "Sort Ascending",
            sortdesc : "Sort Descending",
            columns : "Columns",
            filter : "Filter",
            grouping : "Group By",
            ungrouping : "Ungroup",
            searchTitle : "Get items with value that:",
            freeze : "Freeze",
            unfreeze : "Unfreeze",
            reorder : "Move to reorder",
            hovermenu: "Click for column quick actions"
        }
    };
}));