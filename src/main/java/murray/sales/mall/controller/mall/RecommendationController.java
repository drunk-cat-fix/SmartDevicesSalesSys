package murray.sales.mall.controller.mall;

import murray.sales.mall.common.Constants;
import murray.sales.mall.common.ServiceResultEnum;
import murray.sales.mall.controller.vo.SalesMallSearchGoodsVO;
import murray.sales.mall.controller.vo.SalesMallUserVO;
import murray.sales.mall.service.CollaborativeFilteringService;
import murray.sales.mall.util.Result;
import murray.sales.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class RecommendationController {

    @Autowired
    private CollaborativeFilteringService collaborativeFilteringService;

    @GetMapping("/recommendations")
    public String recommendationsPage(HttpSession session) {
        SalesMallUserVO user = (SalesMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return "mall/login";
        }
        return "mall/recommendations";
    }

    @GetMapping("/api/recommendations")
    @ResponseBody
    public Result getRecommendations(HttpSession session,
                                     @RequestParam(defaultValue = "12") Integer limit) {
        SalesMallUserVO user = (SalesMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);

        if (user == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.USER_NOT_LOGIN.getResult());
        }

        List<SalesMallSearchGoodsVO> recommendations =
                collaborativeFilteringService.getRecommendationsForUser(user.getUserId(), limit);

        if (CollectionUtils.isEmpty(recommendations)) {
            return ResultGenerator.genFailResult("No recommendations available yet");
        }

        return ResultGenerator.genSuccessResult(recommendations);
    }

    @GetMapping("/api/similar-items/{goodsId}")
    @ResponseBody
    public Result getSimilarItems(@PathVariable Long goodsId,
                                  @RequestParam(defaultValue = "6") Integer limit) {
        List<SalesMallSearchGoodsVO> similarItems =
                collaborativeFilteringService.getSimilarItems(goodsId, limit);

        return ResultGenerator.genSuccessResult(similarItems);
    }
}