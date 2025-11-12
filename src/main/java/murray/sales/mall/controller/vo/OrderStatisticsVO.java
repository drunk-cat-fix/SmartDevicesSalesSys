package murray.sales.mall.controller.vo;

import java.math.BigDecimal;

/**
 * Order Statistics DTO
 */
public class OrderStatisticsVO {
    private Integer totalOrders;
    private Integer pendingOrders;
    private Integer paidOrders;
    private Integer shippedOrders;
    private Integer completedOrders;
    private Integer closedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;

    public OrderStatisticsVO() {
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Integer getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Integer pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Integer getPaidOrders() {
        return paidOrders;
    }

    public void setPaidOrders(Integer paidOrders) {
        this.paidOrders = paidOrders;
    }

    public Integer getShippedOrders() {
        return shippedOrders;
    }

    public void setShippedOrders(Integer shippedOrders) {
        this.shippedOrders = shippedOrders;
    }

    public Integer getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Integer completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Integer getClosedOrders() {
        return closedOrders;
    }

    public void setClosedOrders(Integer closedOrders) {
        this.closedOrders = closedOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(BigDecimal todayRevenue) {
        this.todayRevenue = todayRevenue;
    }
}