
package murray.sales.mall.entity;

import java.util.Date;

public class ItemSimilarity {
    private Long id;
    private Long goodsId1;
    private Long goodsId2;
    private Double similarity;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId1() {
        return goodsId1;
    }

    public void setGoodsId1(Long goodsId1) {
        this.goodsId1 = goodsId1;
    }

    public Long getGoodsId2() {
        return goodsId2;
    }

    public void setGoodsId2(Long goodsId2) {
        this.goodsId2 = goodsId2;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}