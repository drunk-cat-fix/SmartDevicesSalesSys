package murray.sales.mall.dao;

import murray.sales.mall.entity.SalesMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalesMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(SalesMallOrderItem record);

    int insertSelective(SalesMallOrderItem record);

    SalesMallOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<SalesMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<SalesMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<SalesMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(SalesMallOrderItem record);

    int updateByPrimaryKey(SalesMallOrderItem record);
}