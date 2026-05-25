package com.lostfound.service;

import com.lostfound.api.ItemMatcher;
import com.lostfound.api.MatchResult;
import com.lostfound.model.*;
import com.lostfound.db.DataManager;
import java.util.List;

/**
 * 物品匹配服务 - 使用策略模式进行匹配
 */
public class ItemMatchService {
    private ItemMatcher matcher;
    private DataManager dataManager;

    public ItemMatchService() {
        this.dataManager = DataManager.getInstance();
        this.matcher = new SimpleItemMatcher(); // 默认使用简单匹配策略
    }

    public ItemMatchService(ItemMatcher matcher) {
        this.dataManager = DataManager.getInstance();
        this.matcher = matcher;
    }

    /**
     * 设置匹配策略 - 展示策略模式的动态切换
     */
    public void setMatcher(ItemMatcher matcher) {
        this.matcher = matcher;
        System.out.println("[匹配服务] 切换到策略: " + matcher.getStrategyName());
    }

    /**
     * 执行匹配
     */
    public List<MatchResult> performMatch() {
        System.out.println("[匹配服务] 开始执行匹配，策略: " + matcher.getStrategyName());

        List<Item> lostItems = dataManager.getItemsByType("失物");
        List<Item> foundItems = dataManager.getItemsByType("招领");

        System.out.println("[匹配服务] 找到 " + lostItems.size() + " 个失物, " +
                foundItems.size() + " 个招领物品");

        List<MatchResult> results = matcher.findMatches(lostItems, foundItems);
        System.out.println("[匹配服务] 匹配完成，找到 " + results.size() + " 个潜在匹配");

        return results;
    }

    /**
     * 打印匹配结果
     */
    public void printMatchResults(List<MatchResult> results) {
        if (results.isEmpty()) {
            System.out.println("未找到匹配结果");
            return;
        }

        System.out.println("\n========== 匹配结果 ==========");
        for (int i = 0; i < results.size(); i++) {
            MatchResult result = results.get(i);
            System.out.printf("匹配 %d: 相似度 %.1f%%%n", i + 1, result.getSimilarity() * 100);
            System.out.println("  失物: " + result.getLostItem());
            System.out.println("  招领: " + result.getFoundItem());
            System.out.println("  详情: " + result.getMatchDescription());
            System.out.println();
        }
        System.out.println("================================");
    }
}
