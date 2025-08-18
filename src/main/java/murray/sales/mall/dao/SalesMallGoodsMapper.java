package murray.sales.mall.dao;

import murray.sales.mall.entity.SalesMallGoods;
import murray.sales.mall.entity.StockNumDTO;
import murray.sales.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalesMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(SalesMallGoods record);

    int insertSelective(SalesMallGoods record);

    SalesMallGoods selectByPrimaryKey(Long goodsId);

    SalesMallGoods selectByCategoryIdAndName(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Long goodsCategoryId);

    int updateByPrimaryKeySelective(SalesMallGoods record);

    int updateByPrimaryKeyWithBLOBs(SalesMallGoods record);

    int updateByPrimaryKey(SalesMallGoods record);

    List<SalesMallGoods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

    List<SalesMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<SalesMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("salesMallGoodsList") List<SalesMallGoods> salesMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int recoverStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}