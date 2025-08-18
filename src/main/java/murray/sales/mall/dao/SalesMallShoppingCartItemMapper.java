package murray.sales.mall.dao;

import murray.sales.mall.entity.SalesMallShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalesMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(SalesMallShoppingCartItem record);

    int insertSelective(SalesMallShoppingCartItem record);

    SalesMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    SalesMallShoppingCartItem selectByUserIdAndGoodsId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("goodsId") Long goodsId);

    List<SalesMallShoppingCartItem> selectByUserId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("number") int number);

    int selectCountByUserId(Long newBeeMallUserId);

    int updateByPrimaryKeySelective(SalesMallShoppingCartItem record);

    int updateByPrimaryKey(SalesMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}