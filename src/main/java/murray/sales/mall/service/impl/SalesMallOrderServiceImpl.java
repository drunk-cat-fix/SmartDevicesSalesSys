package murray.sales.mall.service.impl;

import murray.sales.mall.common.*;
import murray.sales.mall.controller.vo.*;
import murray.sales.mall.dao.SalesMallGoodsMapper;
import murray.sales.mall.dao.SalesMallOrderItemMapper;
import murray.sales.mall.dao.SalesMallOrderMapper;
import murray.sales.mall.dao.SalesMallShoppingCartItemMapper;
import murray.sales.mall.entity.SalesMallGoods;
import murray.sales.mall.entity.SalesMallOrder;
import murray.sales.mall.entity.SalesMallOrderItem;
import murray.sales.mall.entity.StockNumDTO;
import murray.sales.mall.service.SalesMallOrderService;
import murray.sales.mall.util.BeanUtil;
import murray.sales.mall.util.NumberUtil;
import murray.sales.mall.util.PageQueryUtil;
import murray.sales.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class SalesMallOrderServiceImpl implements SalesMallOrderService {

    @Autowired
    private SalesMallOrderMapper salesMallOrderMapper;
    @Autowired
    private SalesMallOrderItemMapper salesMallOrderItemMapper;
    @Autowired
    private SalesMallShoppingCartItemMapper salesMallShoppingCartItemMapper;
    @Autowired
    private SalesMallGoodsMapper salesMallGoodsMapper;

    @Override
    public PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil) {
        List<SalesMallOrder> salesMallOrders = salesMallOrderMapper.findNewBeeMallOrderList(pageUtil);
        int total = salesMallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
        PageResult pageResult = new PageResult(salesMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(SalesMallOrder salesMallOrder) {
        SalesMallOrder temp = salesMallOrderMapper.selectByPrimaryKey(salesMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(salesMallOrder.getTotalPrice());
            temp.setUserAddress(salesMallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (salesMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SalesMallOrder> orders = salesMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SalesMallOrder salesMallOrder : orders) {
                if (salesMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += salesMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (salesMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += salesMallOrder.getOrderNo() + " ";
                }
            }
            if (!StringUtils.hasText(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (salesMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SalesMallOrder> orders = salesMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SalesMallOrder salesMallOrder : orders) {
                if (salesMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += salesMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (salesMallOrder.getOrderStatus() != 1 && salesMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += salesMallOrder.getOrderNo() + " ";
                }
            }
            if (!StringUtils.hasText(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (salesMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SalesMallOrder> orders = salesMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SalesMallOrder salesMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (salesMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += salesMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (salesMallOrder.getOrderStatus() == 4 || salesMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += salesMallOrder.getOrderNo() + " ";
                }
            }
            if (!StringUtils.hasText(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间&&恢复库存
                if (salesMallOrderMapper.closeOrder(Arrays.asList(ids), SalesMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0 && recoverStockNum(Arrays.asList(ids))) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(SalesMallUserVO user, List<SalesMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(SalesMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(SalesMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<SalesMallGoods> salesMallGoods = salesMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<SalesMallGoods> goodsListNotSelling = salesMallGoods.stream()
                .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            SalesSystemException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, SalesMallGoods> newBeeMallGoodsMap = salesMallGoods.stream().collect(Collectors.toMap(SalesMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (SalesMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!newBeeMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                SalesSystemException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > newBeeMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                SalesSystemException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(salesMallGoods)) {
            if (salesMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = salesMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    SalesSystemException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                SalesMallOrder salesMallOrder = new SalesMallOrder();
                salesMallOrder.setOrderNo(orderNo);
                salesMallOrder.setUserId(user.getUserId());
                salesMallOrder.setUserAddress(user.getAddress());
                //总价
                for (SalesMallShoppingCartItemVO salesMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += salesMallShoppingCartItemVO.getGoodsCount() * salesMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    SalesSystemException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                salesMallOrder.setTotalPrice(priceTotal);
                //订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                salesMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (salesMallOrderMapper.insertSelective(salesMallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<SalesMallOrderItem> salesMallOrderItems = new ArrayList<>();
                    for (SalesMallShoppingCartItemVO salesMallShoppingCartItemVO : myShoppingCartItems) {
                        SalesMallOrderItem salesMallOrderItem = new SalesMallOrderItem();
                        //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
                        BeanUtil.copyProperties(salesMallShoppingCartItemVO, salesMallOrderItem);
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        salesMallOrderItem.setOrderId(salesMallOrder.getOrderId());
                        salesMallOrderItems.add(salesMallOrderItem);
                    }
                    //保存至数据库
                    if (salesMallOrderItemMapper.insertBatch(salesMallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    SalesSystemException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                SalesSystemException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            SalesSystemException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        SalesSystemException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public SalesMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        SalesMallOrder salesMallOrder = salesMallOrderMapper.selectByOrderNo(orderNo);
        if (salesMallOrder == null) {
            SalesSystemException.fail(ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult());
        }
        //验证是否是当前userId下的订单，否则报错
        if (!userId.equals(salesMallOrder.getUserId())) {
            SalesSystemException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
        }
        List<SalesMallOrderItem> orderItems = salesMallOrderItemMapper.selectByOrderId(salesMallOrder.getOrderId());
        //获取订单项数据
        if (CollectionUtils.isEmpty(orderItems)) {
            SalesSystemException.fail(ServiceResultEnum.ORDER_ITEM_NOT_EXIST_ERROR.getResult());
        }
        List<SalesMallOrderItemVO> salesMallOrderItemVOS = BeanUtil.copyList(orderItems, SalesMallOrderItemVO.class);
        SalesMallOrderDetailVO salesMallOrderDetailVO = new SalesMallOrderDetailVO();
        BeanUtil.copyProperties(salesMallOrder, salesMallOrderDetailVO);
        salesMallOrderDetailVO.setOrderStatusString(SalesMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(salesMallOrderDetailVO.getOrderStatus()).getName());
        salesMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(salesMallOrderDetailVO.getPayType()).getName());
        salesMallOrderDetailVO.setNewBeeMallOrderItemVOS(salesMallOrderItemVOS);
        return salesMallOrderDetailVO;
    }

    @Override
    public SalesMallOrder getNewBeeMallOrderByOrderNo(String orderNo) {
        return salesMallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = salesMallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
        List<SalesMallOrder> salesMallOrders = salesMallOrderMapper.findNewBeeMallOrderList(pageUtil);
        List<SalesMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(salesMallOrders, SalesMallOrderListVO.class);
            //设置订单状态中文显示值
            for (SalesMallOrderListVO salesMallOrderListVO : orderListVOS) {
                salesMallOrderListVO.setOrderStatusString(SalesMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(salesMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = salesMallOrders.stream().map(SalesMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<SalesMallOrderItem> orderItems = salesMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<SalesMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(SalesMallOrderItem::getOrderId));
                for (SalesMallOrderListVO salesMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(salesMallOrderListVO.getOrderId())) {
                        List<SalesMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(salesMallOrderListVO.getOrderId());
                        //将NewBeeMallOrderItem对象列表转换成NewBeeMallOrderItemVO对象列表
                        List<SalesMallOrderItemVO> salesMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, SalesMallOrderItemVO.class);
                        salesMallOrderListVO.setNewBeeMallOrderItemVOS(salesMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String cancelOrder(String orderNo, Long userId) {
        SalesMallOrder salesMallOrder = salesMallOrderMapper.selectByOrderNo(orderNo);
        if (salesMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(salesMallOrder.getUserId())) {
                SalesSystemException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //订单状态判断
            if (salesMallOrder.getOrderStatus().intValue() == SalesMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || salesMallOrder.getOrderStatus().intValue() == SalesMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || salesMallOrder.getOrderStatus().intValue() == SalesMallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || salesMallOrder.getOrderStatus().intValue() == SalesMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            //修改订单状态&&恢复库存
            if (salesMallOrderMapper.closeOrder(Collections.singletonList(salesMallOrder.getOrderId()), SalesMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0 && recoverStockNum(Collections.singletonList(salesMallOrder.getOrderId()))) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        SalesMallOrder salesMallOrder = salesMallOrderMapper.selectByOrderNo(orderNo);
        if (salesMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(salesMallOrder.getUserId())) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //订单状态判断 非出库状态下不进行修改操作
            if (salesMallOrder.getOrderStatus().intValue() != SalesMallOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            salesMallOrder.setOrderStatus((byte) SalesMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            salesMallOrder.setUpdateTime(new Date());
            if (salesMallOrderMapper.updateByPrimaryKeySelective(salesMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        SalesMallOrder salesMallOrder = salesMallOrderMapper.selectByOrderNo(orderNo);
        if (salesMallOrder != null) {
            //订单状态判断 非待支付状态下不进行修改操作
            if (salesMallOrder.getOrderStatus().intValue() != SalesMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            salesMallOrder.setOrderStatus((byte) SalesMallOrderStatusEnum.ORDER_PAID.getOrderStatus());
            salesMallOrder.setPayType((byte) payType);
            salesMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            salesMallOrder.setPayTime(new Date());
            salesMallOrder.setUpdateTime(new Date());
            if (salesMallOrderMapper.updateByPrimaryKeySelective(salesMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<SalesMallOrderItemVO> getOrderItems(Long id) {
        SalesMallOrder salesMallOrder = salesMallOrderMapper.selectByPrimaryKey(id);
        if (salesMallOrder != null) {
            List<SalesMallOrderItem> orderItems = salesMallOrderItemMapper.selectByOrderId(salesMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<SalesMallOrderItemVO> salesMallOrderItemVOS = BeanUtil.copyList(orderItems, SalesMallOrderItemVO.class);
                return salesMallOrderItemVOS;
            }
        }
        return null;
    }

    @Override
    public OrderStatisticsDTO getOrderStatistics() {
        OrderStatisticsDTO statistics = new OrderStatisticsDTO();

        // Total orders
        statistics.setTotalOrders(salesMallOrderMapper.countTotalOrders());

        // Order status counts (0: pending, 1: paid, 3: shipped, 4: completed, -1: cancelled, -3: closed)
        statistics.setPendingOrders(salesMallOrderMapper.countOrdersByStatus((byte) 0));
        statistics.setPaidOrders(salesMallOrderMapper.countOrdersByStatus((byte) 1));
        statistics.setShippedOrders(salesMallOrderMapper.countOrdersByStatus((byte) 3));
        statistics.setCompletedOrders(salesMallOrderMapper.countOrdersByStatus((byte) 4));
        statistics.setCancelledOrders(
                salesMallOrderMapper.countOrdersByStatus((byte) -1) +
                        salesMallOrderMapper.countOrdersByStatus((byte) -3)
        );

        // Total revenue (from completed orders)
        statistics.setTotalRevenue(salesMallOrderMapper.sumTotalRevenue());

        return statistics;
    }

    /**
     * 恢复库存
     * @param orderIds
     * @return
     */
    public Boolean recoverStockNum(List<Long> orderIds) {
        //查询对应的订单项
        List<SalesMallOrderItem> salesMallOrderItems = salesMallOrderItemMapper.selectByOrderIds(orderIds);
        //获取对应的商品id和商品数量并赋值到StockNumDTO对象中
        List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(salesMallOrderItems, StockNumDTO.class);
        //执行恢复库存的操作
        int updateStockNumResult = salesMallGoodsMapper.recoverStockNum(stockNumDTOS);
        if (updateStockNumResult < 1) {
            SalesSystemException.fail(ServiceResultEnum.CLOSE_ORDER_ERROR.getResult());
            return false;
        } else {
            return true;
        }
    }
}