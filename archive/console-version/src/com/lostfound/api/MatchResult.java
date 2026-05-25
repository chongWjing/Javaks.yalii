package com.lostfound.api;

import com.lostfound.model.Item;

/**
 * 匹配结果类 - 封装匹配结果
 */
public class MatchResult {
    private Item lostItem;
    private Item foundItem;
    private double similarity;
    private String matchDescription;

    public MatchResult(Item lostItem, Item foundItem, double similarity, String matchDescription) {
        this.lostItem = lostItem;
        this.foundItem = foundItem;
        this.similarity = similarity;
        this.matchDescription = matchDescription;
    }

    public Item getLostItem() { return lostItem; }
    public Item getFoundItem() { return foundItem; }
    public double getSimilarity() { return similarity; }
    public String getMatchDescription() { return matchDescription; }

    @Override
    public String toString() {
        return String.format("匹配结果: [失物]%s <-> [招领]%s (相似度:%.2f%%) - %s",
                lostItem.getName(), foundItem.getName(), similarity * 100, matchDescription);
    }
}
