
package murray.sales.mall.dao;

import murray.sales.mall.entity.ItemSimilarity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemSimilarityMapper {

    int insertBatch(List<ItemSimilarity> similarities);

    int deleteAll();

    List<ItemSimilarity> selectTopSimilarItems(@Param("goodsId") Long goodsId, @Param("limit") int limit);

    List<Map<String, Object>> getUserPurchaseHistory();

    List<Long> getRecommendedItemsForUser(@Param("userId") Long userId, @Param("limit") int limit);
}