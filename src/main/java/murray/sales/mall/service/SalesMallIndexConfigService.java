package murray.sales.mall.service;

import murray.sales.mall.controller.vo.SalesMallIndexConfigGoodsVO;
import murray.sales.mall.entity.IndexConfig;
import murray.sales.mall.util.PageQueryUtil;
import murray.sales.mall.util.PageResult;

import java.util.List;

public interface SalesMallIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<SalesMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
