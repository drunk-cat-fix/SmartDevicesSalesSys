package murray.sales.mall.service;

import murray.sales.mall.controller.vo.NewBeeMallIndexCarouselVO;
import murray.sales.mall.entity.Carousel;
import murray.sales.mall.util.PageQueryUtil;
import murray.sales.mall.util.PageResult;

import java.util.List;

public interface SalesMallCarouselService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number);
}
