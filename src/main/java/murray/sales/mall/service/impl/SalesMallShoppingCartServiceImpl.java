package murray.sales.mall.service.impl;

import murray.sales.mall.common.Constants;
import murray.sales.mall.common.ServiceResultEnum;
import murray.sales.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import murray.sales.mall.dao.NewBeeMallGoodsMapper;
import murray.sales.mall.dao.NewBeeMallShoppingCartItemMapper;
import murray.sales.mall.entity.SalesMallGoods;
import murray.sales.mall.entity.SalesMallShoppingCartItem;
import murray.sales.mall.service.SalesMallShoppingCartService;
import murray.sales.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesMallShoppingCartServiceImpl implements SalesMallShoppingCartService {

    @Autowired
    private NewBeeMallShoppingCartItemMapper newBeeMallShoppingCartItemMapper;

    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Override
    public String saveNewBeeMallCartItem(SalesMallShoppingCartItem salesMallShoppingCartItem) {
        SalesMallShoppingCartItem temp = newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(salesMallShoppingCartItem.getUserId(), salesMallShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            temp.setGoodsCount(salesMallShoppingCartItem.getGoodsCount());
            return updateNewBeeMallCartItem(temp);
        }
        SalesMallGoods salesMallGoods = newBeeMallGoodsMapper.selectByPrimaryKey(salesMallShoppingCartItem.getGoodsId());
        //商品为空
        if (salesMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = newBeeMallShoppingCartItemMapper.selectCountByUserId(salesMallShoppingCartItem.getUserId()) + 1;
        //超出单个商品的最大数量
        if (salesMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (newBeeMallShoppingCartItemMapper.insertSelective(salesMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNewBeeMallCartItem(SalesMallShoppingCartItem salesMallShoppingCartItem) {
        SalesMallShoppingCartItem salesMallShoppingCartItemUpdate = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(salesMallShoppingCartItem.getCartItemId());
        if (salesMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if (salesMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //当前登录账号的userId与待修改的cartItem中userId不同，返回错误
        if (!salesMallShoppingCartItemUpdate.getUserId().equals(salesMallShoppingCartItem.getUserId())) {
            return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
        }
        //数值相同，则不执行数据操作
        if (salesMallShoppingCartItem.getGoodsCount().equals(salesMallShoppingCartItemUpdate.getGoodsCount())) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        salesMallShoppingCartItemUpdate.setGoodsCount(salesMallShoppingCartItem.getGoodsCount());
        salesMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (newBeeMallShoppingCartItemMapper.updateByPrimaryKeySelective(salesMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public SalesMallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId) {
        return newBeeMallShoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId, Long userId) {
        SalesMallShoppingCartItem salesMallShoppingCartItem = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
        if (salesMallShoppingCartItem == null) {
            return false;
        }
        //userId不同不能删除
        if (!userId.equals(salesMallShoppingCartItem.getUserId())) {
            return false;
        }
        return newBeeMallShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
    }

    @Override
    public List<NewBeeMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId) {
        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
        List<SalesMallShoppingCartItem> salesMallShoppingCartItems = newBeeMallShoppingCartItemMapper.selectByUserId(newBeeMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(salesMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> newBeeMallGoodsIds = salesMallShoppingCartItems.stream().map(SalesMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<SalesMallGoods> salesMallGoods = newBeeMallGoodsMapper.selectByPrimaryKeys(newBeeMallGoodsIds);
            Map<Long, SalesMallGoods> newBeeMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(salesMallGoods)) {
                newBeeMallGoodsMap = salesMallGoods.stream().collect(Collectors.toMap(SalesMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (SalesMallShoppingCartItem salesMallShoppingCartItem : salesMallShoppingCartItems) {
                NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO = new NewBeeMallShoppingCartItemVO();
                BeanUtil.copyProperties(salesMallShoppingCartItem, newBeeMallShoppingCartItemVO);
                if (newBeeMallGoodsMap.containsKey(salesMallShoppingCartItem.getGoodsId())) {
                    SalesMallGoods salesMallGoodsTemp = newBeeMallGoodsMap.get(salesMallShoppingCartItem.getGoodsId());
                    newBeeMallShoppingCartItemVO.setGoodsCoverImg(salesMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = salesMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    newBeeMallShoppingCartItemVO.setGoodsName(goodsName);
                    newBeeMallShoppingCartItemVO.setSellingPrice(salesMallGoodsTemp.getSellingPrice());
                    newBeeMallShoppingCartItemVOS.add(newBeeMallShoppingCartItemVO);
                }
            }
        }
        return newBeeMallShoppingCartItemVOS;
    }
}
