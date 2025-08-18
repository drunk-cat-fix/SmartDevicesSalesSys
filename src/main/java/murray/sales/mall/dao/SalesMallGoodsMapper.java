/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
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