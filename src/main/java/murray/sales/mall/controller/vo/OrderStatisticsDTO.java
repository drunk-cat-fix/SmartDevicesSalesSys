package murray.sales.mall.controller.vo;

import java.io.Serializable;

/**
 * Order Statistics DTO
 */
public class OrderStatisticsDTO implements Serializable {

    private Integer totalOrders;
    private Integer pendingOrders;
    private Integer paidOrders;
    private Integer shippedOrders;
    private Integer completedOrders;
    private Integer cancelledOrders;
    private Double totalRevenue;

    public OrderStatisticsDTO() {
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

    public Integer getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(Integer cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}