package murray.sales.mall.service;

import murray.sales.mall.entity.SalesMallGoods;
import murray.sales.mall.util.PageQueryUtil;
import murray.sales.mall.util.PageResult;

import java.util.List;

public interface SalesMallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveNewBeeMallGoods(SalesMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param salesMallGoodsList
     * @return
     */
    void batchSaveNewBeeMallGoods(List<SalesMallGoods> salesMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateNewBeeMallGoods(SalesMallGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    SalesMallGoods getNewBeeMallGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);
}
