package murray.sales.mall.dao;

import murray.sales.mall.entity.SalesMallOrder;
import murray.sales.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalesMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(SalesMallOrder record);

    int insertSelective(SalesMallOrder record);

    SalesMallOrder selectByPrimaryKey(Long orderId);

    SalesMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(SalesMallOrder record);

    int updateByPrimaryKey(SalesMallOrder record);

    List<SalesMallOrder> findNewBeeMallOrderList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallOrders(PageQueryUtil pageUtil);

    List<SalesMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}