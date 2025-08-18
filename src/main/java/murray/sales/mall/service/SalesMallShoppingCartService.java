package murray.sales.mall.service;

import murray.sales.mall.controller.vo.SalesMallShoppingCartItemVO;
import murray.sales.mall.entity.SalesMallShoppingCartItem;

import java.util.List;

public interface SalesMallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param salesMallShoppingCartItem
     * @return
     */
    String saveNewBeeMallCartItem(SalesMallShoppingCartItem salesMallShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param salesMallShoppingCartItem
     * @return
     */
    String updateNewBeeMallCartItem(SalesMallShoppingCartItem salesMallShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param newBeeMallShoppingCartItemId
     * @return
     */
    SalesMallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     *
     * @param shoppingCartItemId
     * @param userId
     * @return
     */
    Boolean deleteById(Long shoppingCartItemId, Long userId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param newBeeMallUserId
     * @return
     */
    List<SalesMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId);
}
