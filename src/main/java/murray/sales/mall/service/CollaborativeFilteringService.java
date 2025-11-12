
package murray.sales.mall.service;

import murray.sales.mall.controller.vo.SalesMallSearchGoodsVO;

import java.util.List;

public interface CollaborativeFilteringService {

    /**
     * Calculate and update item similarity matrix
     */
    void calculateItemSimilarity();

    /**
     * Get recommended items for a specific user
     * @param userId User ID
     * @param limit Number of recommendations
     * @return List of recommended goods
     */
    List<SalesMallSearchGoodsVO> getRecommendationsForUser(Long userId, int limit);

    /**
     * Get similar items for a specific product
     * @param goodsId Product ID
     * @param limit Number of similar items
     * @return List of similar goods
     */
    List<SalesMallSearchGoodsVO> getSimilarItems(Long goodsId, int limit);
}