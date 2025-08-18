package murray.sales.mall.controller.mall;

import murray.sales.mall.common.Constants;
import murray.sales.mall.common.IndexConfigTypeEnum;
import murray.sales.mall.common.SalesSystemException;
import murray.sales.mall.controller.vo.SalesMallIndexCarouselVO;
import murray.sales.mall.controller.vo.SalesMallIndexCategoryVO;
import murray.sales.mall.controller.vo.SalesMallIndexConfigGoodsVO;
import murray.sales.mall.service.SalesMallCarouselService;
import murray.sales.mall.service.SalesMallCategoryService;
import murray.sales.mall.service.SalesMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private SalesMallCarouselService salesMallCarouselService;

    @Resource
    private SalesMallIndexConfigService salesMallIndexConfigService;

    @Resource
    private SalesMallCategoryService salesMallCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<SalesMallIndexCategoryVO> categories = salesMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            SalesSystemException.fail("Classification Data is NOT Completed");
        }
        List<SalesMallIndexCarouselVO> carousels = salesMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<SalesMallIndexConfigGoodsVO> hotGoodses = salesMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<SalesMallIndexConfigGoodsVO> newGoodses = salesMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<SalesMallIndexConfigGoodsVO> recommendGoodses = salesMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
    @GetMapping({"/test"})
    @ResponseBody
    public String test(HttpServletRequest request){
        return "test";
    }
}
