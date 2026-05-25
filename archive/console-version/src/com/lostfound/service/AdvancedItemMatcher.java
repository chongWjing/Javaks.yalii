package com.lostfound.service;

import com.lostfound.api.ItemMatcher;
import com.lostfound.model.*;
import com.lostfound.api.MatchResult;
import java.util.ArrayList;
import java.util.List;

/**
 * 高级匹配策略 - 实现ItemMatcher接口，展示策略模式
 * 使用更复杂的匹配算法，考虑更多因素
 */
public class AdvancedItemMatcher implements ItemMatcher {

    @Override
    public List<MatchResult> findMatches(List<? extends Item> lostItems, List<? extends Item> foundItems) {
        List<MatchResult> matches = new ArrayList<>();

        for (Item lost : lostItems) {
            if (!lost.isMatchable()) continue;

            for (Item found : foundItems) {
                if (!found.isMatchable()) continue;

                double similarity = calculateAdvancedSimilarity(lost, found);
                if (similarity > 0.4) {
                    String description = generateDetailedDescription(lost, found, similarity);
                    matches.add(new MatchResult(lost, found, similarity, description));
                }
            }
        }

        matches.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));
        return matches;
    }

    private double calculateAdvancedSimilarity(Item item1, Item item2) {
        double score = 0;

        // 名称相似度（35%权重）
        double nameSim = calculateJaccardSimilarity(item1.getName(), item2.getName());
        score += nameSim * 0.35;

        // 描述相似度（25%权重）
        double descSim = calculateJaccardSimilarity(item1.getDescription(), item2.getDescription());
        score += descSim * 0.25;

        // 类别相同（20%权重）
        if (item1.getCategory().equals(item2.getCategory())) {
            score += 0.2;
        }

        // 地点相似度（20%权重）
        double locationSim = calculateJaccardSimilarity(item1.getLocation(), item2.getLocation());
        score += locationSim * 0.2;

        return score;
    }

    private double calculateJaccardSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;
        s1 = s1.toLowerCase().trim();
        s2 = s2.toLowerCase().trim();

        if (s1.equals(s2)) return 1.0;

        String[] words1 = s1.split("\\s+");
        String[] words2 = s2.split("\\s+");

        int intersection = 0;
        int union = 0;

        java.util.Set<String> set1 = new java.util.HashSet<>();
        java.util.Set<String> set2 = new java.util.HashSet<>();

        for (String w : words1) set1.add(w);
        for (String w : words2) set2.add(w);

        for (String w : set1) {
            if (set2.contains(w)) intersection++;
        }

        union = set1.size() + set2.size() - intersection;

        return union == 0 ? 0 : (double) intersection / union;
    }

    private String generateDetailedDescription(Item lost, Item found, double similarity) {
        StringBuilder desc = new StringBuilder();

        double nameSim = calculateJaccardSimilarity(lost.getName(), found.getName());
        desc.append(String.format("名称相似度: %.0f%%", nameSim * 100));

        double descSim = calculateJaccardSimilarity(lost.getDescription(), found.getDescription());
        if (descSim > 0) {
            desc.append(String.format(", 描述相似度: %.0f%%", descSim * 100));
        }

        if (lost.getCategory().equals(found.getCategory())) {
            desc.append(", 类别一致");
        }

        double locationSim = calculateJaccardSimilarity(lost.getLocation(), found.getLocation());
        if (locationSim > 0.5) {
            desc.append(String.format(", 地点高度匹配: %.0f%%", locationSim * 100));
        }

        return desc.toString();
    }

    @Override
    public String getStrategyName() {
        return "高级匹配策略";
    }
}
