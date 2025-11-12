package murray.sales.mall.service.impl;

import murray.sales.mall.controller.vo.SalesMallSearchGoodsVO;
import murray.sales.mall.dao.ItemSimilarityMapper;
import murray.sales.mall.dao.SalesMallGoodsMapper;
import murray.sales.mall.entity.ItemSimilarity;
import murray.sales.mall.entity.SalesMallGoods;
import murray.sales.mall.service.CollaborativeFilteringService;
import murray.sales.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollaborativeFilteringServiceImpl implements CollaborativeFilteringService {

    @Autowired
    private ItemSimilarityMapper itemSimilarityMapper;

    @Autowired
    private SalesMallGoodsMapper goodsMapper;

    @Override
    @Transactional
    public void calculateItemSimilarity() {
        // Get user purchase history
        List<Map<String, Object>> purchaseHistory = itemSimilarityMapper.getUserPurchaseHistory();

        if (CollectionUtils.isEmpty(purchaseHistory)) {
            return;
        }

        // Build user-item matrix
        Map<Long, Set<Long>> userItemMap = new HashMap<>();
        Map<Long, Set<Long>> itemUserMap = new HashMap<>();

        for (Map<String, Object> record : purchaseHistory) {
            Long userId = ((Number) record.get("userId")).longValue();
            Long goodsId = ((Number) record.get("goodsId")).longValue();

            userItemMap.computeIfAbsent(userId, k -> new HashSet<>()).add(goodsId);
            itemUserMap.computeIfAbsent(goodsId, k -> new HashSet<>()).add(userId);
        }

        // Calculate item-item similarity using cosine similarity
        Map<String, Double> similarityMap = new HashMap<>();
        List<Long> items = new ArrayList<>(itemUserMap.keySet());

        for (int i = 0; i < items.size(); i++) {
            Long item1 = items.get(i);
            Set<Long> users1 = itemUserMap.get(item1);

            for (int j = i + 1; j < items.size(); j++) {
                Long item2 = items.get(j);
                Set<Long> users2 = itemUserMap.get(item2);

                // Calculate cosine similarity
                Set<Long> intersection = new HashSet<>(users1);
                intersection.retainAll(users2);

                if (!intersection.isEmpty()) {
                    double similarity = intersection.size() /
                            Math.sqrt(users1.size() * users2.size());

                    similarityMap.put(item1 + "-" + item2, similarity);
                }
            }
        }

        // Store similarities in database
        List<ItemSimilarity> similarities = new ArrayList<>();
        Date now = new Date();

        for (Map.Entry<String, Double> entry : similarityMap.entrySet()) {
            String[] items_pair = entry.getKey().split("-");
            Long goodsId1 = Long.parseLong(items_pair[0]);
            Long goodsId2 = Long.parseLong(items_pair[1]);
            Double similarity = entry.getValue();

            // Store both directions
            ItemSimilarity sim1 = new ItemSimilarity();
            sim1.setGoodsId1(goodsId1);
            sim1.setGoodsId2(goodsId2);
            sim1.setSimilarity(similarity);
            sim1.setUpdateTime(now);
            similarities.add(sim1);

            ItemSimilarity sim2 = new ItemSimilarity();
            sim2.setGoodsId1(goodsId2);
            sim2.setGoodsId2(goodsId1);
            sim2.setSimilarity(similarity);
            sim2.setUpdateTime(now);
            similarities.add(sim2);
        }

        if (!CollectionUtils.isEmpty(similarities)) {
            // Clear old data
            itemSimilarityMapper.deleteAll();

            // Insert new similarities in batches
            int batchSize = 500;
            for (int i = 0; i < similarities.size(); i += batchSize) {
                int end = Math.min(i + batchSize, similarities.size());
                itemSimilarityMapper.insertBatch(similarities.subList(i, end));
            }
        }
    }

    @Override
    public List<SalesMallSearchGoodsVO> getRecommendationsForUser(Long userId, int limit) {
        List<Long> recommendedGoodsIds = itemSimilarityMapper.getRecommendedItemsForUser(userId, limit);

        if (CollectionUtils.isEmpty(recommendedGoodsIds)) {
            return new ArrayList<>();
        }

        List<SalesMallGoods> goodsList = new ArrayList<>();
        for (Long goodsId : recommendedGoodsIds) {
            SalesMallGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            if (goods != null && goods.getGoodsSellStatus() == 0) {
                goodsList.add(goods);
            }
        }

        return convertToVOList(goodsList);
    }

    @Override
    public List<SalesMallSearchGoodsVO> getSimilarItems(Long goodsId, int limit) {
        List<ItemSimilarity> similarities = itemSimilarityMapper.selectTopSimilarItems(goodsId, limit);

        if (CollectionUtils.isEmpty(similarities)) {
            return new ArrayList<>();
        }

        List<Long> similarGoodsIds = similarities.stream()
                .map(ItemSimilarity::getGoodsId2)
                .collect(Collectors.toList());

        List<SalesMallGoods> goodsList = new ArrayList<>();
        for (Long similarGoodsId : similarGoodsIds) {
            SalesMallGoods goods = goodsMapper.selectByPrimaryKey(similarGoodsId);
            if (goods != null && goods.getGoodsSellStatus() == 0) {
                goodsList.add(goods);
            }
        }

        return convertToVOList(goodsList);
    }

    private List<SalesMallSearchGoodsVO> convertToVOList(List<SalesMallGoods> goodsList) {
        List<SalesMallSearchGoodsVO> voList = BeanUtil.copyList(goodsList, SalesMallSearchGoodsVO.class);

        for (SalesMallSearchGoodsVO vo : voList) {
            String goodsName = vo.getGoodsName();
            String goodsIntro = vo.getGoodsIntro();

            if (goodsName != null && goodsName.length() > 28) {
                goodsName = goodsName.substring(0, 28) + "...";
                vo.setGoodsName(goodsName);
            }
            if (goodsIntro != null && goodsIntro.length() > 30) {
                goodsIntro = goodsIntro.substring(0, 30) + "...";
                vo.setGoodsIntro(goodsIntro);
            }
        }

        return voList;
    }
}