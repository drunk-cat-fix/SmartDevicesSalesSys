package murray.sales.mall.controller.mall;

import murray.sales.mall.common.Constants;
import murray.sales.mall.common.SalesSystemException;
import murray.sales.mall.common.ServiceResultEnum;
import murray.sales.mall.controller.vo.SalesMallShoppingCartItemVO;
import murray.sales.mall.controller.vo.SalesMallUserVO;
import murray.sales.mall.entity.SalesMallShoppingCartItem;
import murray.sales.mall.service.SalesMallShoppingCartService;
import murray.sales.mall.util.Result;
import murray.sales.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private SalesMallShoppingCartService salesMallShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        SalesMallUserVO user = (SalesMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<SalesMallShoppingCartItemVO> myShoppingCartItems = salesMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(SalesMallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                SalesSystemException.fail("The shopping items cannot be empty");
            }
            //总价
            for (SalesMallShoppingCartItemVO salesMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += salesMallShoppingCartItemVO.getGoodsCount() * salesMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                SalesSystemException.fail("The price of shopping items is abnormal");
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveNewBeeMallShoppingCartItem(@RequestBody SalesMallShoppingCartItem salesMallShoppingCartItem,
                                                 HttpSession httpSession) {
        SalesMallUserVO user = (SalesMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        salesMallShoppingCartItem.setUserId(user.getUserId());
        String saveResult = salesMallShoppingCartService.saveNewBeeMallCartItem(salesMallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateNewBeeMallShoppingCartItem(@RequestBody SalesMallShoppingCartItem salesMallShoppingCartItem,
                                                   HttpSession httpSession) {
        SalesMallUserVO user = (SalesMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        salesMallShoppingCartItem.setUserId(user.getUserId());
        String updateResult = salesMallShoppingCartService.updateNewBeeMallCartItem(salesMallShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
    @ResponseBody
    public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
                                                   HttpSession httpSession) {
        SalesMallUserVO user = (SalesMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = salesMallShoppingCartService.deleteById(newBeeMallShoppingCartItemId,user.getUserId());
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        SalesMallUserVO user = (SalesMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<SalesMallShoppingCartItemVO> myShoppingCartItems = salesMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (SalesMallShoppingCartItemVO salesMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += salesMallShoppingCartItemVO.getGoodsCount() * salesMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                SalesSystemException.fail("购物项价格异常");
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}
