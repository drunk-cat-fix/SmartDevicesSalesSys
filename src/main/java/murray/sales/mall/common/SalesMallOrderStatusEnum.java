package murray.sales.mall.common;

/**
 * @author Murray
 * @email murray50325487@gmail.ocm
 * @apiNote 订单状态:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
 */
public enum SalesMallOrderStatusEnum {

    DEFAULT(-9, "ERROR"),
    ORDER_PRE_PAY(0, "Pending Payment"),
    ORDER_PAID(1, "Paid"),
    ORDER_PACKAGED(2, "Distribution Completed"),
    ORDER_EXPRESS(3, "Successful outbound"),
    ORDER_SUCCESS(4, "Transaction successful"),
    ORDER_CLOSED_BY_MALLUSER(-1, "Manual Close"),
    ORDER_CLOSED_BY_EXPIRED(-2, "Timeout Close"),
    ORDER_CLOSED_BY_JUDGE(-3, "Merchant Close");

    private int orderStatus;

    private String name;

    SalesMallOrderStatusEnum(int orderStatus, String name) {
        this.orderStatus = orderStatus;
        this.name = name;
    }

    public static SalesMallOrderStatusEnum getNewBeeMallOrderStatusEnumByStatus(int orderStatus) {
        for (SalesMallOrderStatusEnum salesMallOrderStatusEnum : SalesMallOrderStatusEnum.values()) {
            if (salesMallOrderStatusEnum.getOrderStatus() == orderStatus) {
                return salesMallOrderStatusEnum;
            }
        }
        return DEFAULT;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
