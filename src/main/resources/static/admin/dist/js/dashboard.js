$(function () {
    // Fetch order statistics
    $.ajax({
        url: '/admin/api/order-statistics',
        type: 'GET',
        success: function (result) {
            if (result.resultCode == 200) {
                updateDashboard(result.data);
            } else {
                swal('Error', result.message, 'error');
            }
        },
        error: function () {
            swal('Error', 'Failed to load statistics', 'error');
        }
    });
});

function updateDashboard(data) {
    // Update statistics cards
    $('#totalOrders').text(data.totalOrders || 0);
    $('#completedOrders').text(data.completedOrders || 0);
    $('#pendingOrders').text(data.pendingOrders || 0);
    $('#totalRevenue').text('RM ' + (data.totalRevenue || 0).toFixed(2));

    // Create Order Status Distribution Pie Chart
    const statusCtx = document.getElementById('orderStatusChart').getContext('2d');
    new Chart(statusCtx, {
        type: 'doughnut',
        data: {
            labels: ['Pending', 'Paid', 'Shipped', 'Completed', 'Cancelled'],
            datasets: [{
                data: [
                    data.pendingOrders || 0,
                    data.paidOrders || 0,
                    data.shippedOrders || 0,
                    data.completedOrders || 0,
                    data.cancelledOrders || 0
                ],
                backgroundColor: [
                    'rgba(255, 206, 86, 0.8)',
                    'rgba(54, 162, 235, 0.8)',
                    'rgba(153, 102, 255, 0.8)',
                    'rgba(75, 192, 192, 0.8)',
                    'rgba(255, 99, 132, 0.8)'
                ],
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.parsed || 0;
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0;
                            return label + ': ' + value + ' (' + percentage + '%)';
                        }
                    }
                }
            }
        }
    });

    // Create Order Processing Status Bar Chart
    const processingCtx = document.getElementById('orderProcessingChart').getContext('2d');
    new Chart(processingCtx, {
        type: 'bar',
        data: {
            labels: ['Pending', 'Paid', 'Shipped', 'Completed', 'Cancelled'],
            datasets: [{
                label: 'Number of Orders',
                data: [
                    data.pendingOrders || 0,
                    data.paidOrders || 0,
                    data.shippedOrders || 0,
                    data.completedOrders || 0,
                    data.cancelledOrders || 0
                ],
                backgroundColor: [
                    'rgba(255, 206, 86, 0.8)',
                    'rgba(54, 162, 235, 0.8)',
                    'rgba(153, 102, 255, 0.8)',
                    'rgba(75, 192, 192, 0.8)',
                    'rgba(255, 99, 132, 0.8)'
                ],
                borderColor: [
                    'rgba(255, 206, 86, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(255, 99, 132, 1)'
                ],
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
}