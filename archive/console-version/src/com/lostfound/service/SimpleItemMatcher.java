package com.lostfound.service;

import com.lostfound.api.ItemMatcher;
import com.lostfound.model.*;
import com.lostfound.api.MatchResult;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单匹配策略 - 实现ItemMatcher接口，展示策略模式
 * 基于物品名称、类别和地点进行匹配
 */
public class SimpleItemMatcher implements ItemMatcher {

    @Override
    public List<MatchResult> findMatches(List<? extends Item> lostItems, List<? extends Item> foundItems) {
        List<MatchResult> matches = new ArrayList<>();

        for (Item lost : lostItems) {
            if (!lost.isMatchable()) continue;

            for (Item found : foundItems) {
                if (!found.isMatchable()) continue;

                double similarity = calculateSimilarity(lost, found);
                if (similarity > 0.3) { // 相似度超过30%才认为是潜在匹配
                    String description = generateMatchDescription(lost, found, similarity);
                    matches.add(new MatchResult(lost, found, similarity, description));
                }
            }
        }

        // 按相似度降序排序
        matches.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));
        return matches;
    }

    private double calculateSimilarity(Item item1, Item item2) {
        double score = 0;
        int factors = 0;

        // 名称相似度（40%权重）
        double nameSim = calculateStringSimilarity(item1.getName(), item2.getName());
        score += nameSim * 0.4;
        factors++;

        // 类别相同（30%权重）
        if (item1.getCategory().equals(item2.getCategory())) {
            score += 0.3;
        }
        factors++;

        // 地点相似度（30%权重）
        double locationSim = calculateStringSimilarity(item1.getLocation(), item2.getLocation());
        score += locationSim * 0.3;
        factors++;

        return score;
    }

    private double calculateStringSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        if (s1.equals(s2)) return 1.0;
        if (s1.contains(s2) || s2.contains(s1)) return 0.8;

        // 简单的字符匹配计算
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;

        int matches = 0;
        for (char c : s1.toCharArray()) {
            if (s2.indexOf(c) >= 0) matches++;
        }

        return (double) matches / maxLen;
    }

    private String generateMatchDescription(Item lost, Item found, double similarity) {
        StringBuilder desc = new StringBuilder();
        desc.append(String.format("名称匹配度: %.0f%%",
                calculateStringSimilarity(lost.getName(), found.getName()) * 100));

        if (lost.getCategory().equals(found.getCategory())) {
            desc.append(", 类别相同: ").append(lost.getCategory());
        }

        double locationSim = calculateStringSimilarity(lost.getLocation(), found.getLocation());
        if (locationSim > 0.5) {
            desc.append(String.format(", 地点接近: %.0f%%", locationSim * 100));
        }

        return desc.toString();
    }

    @Override
    public String getStrategyName() {
        return "简单匹配策略";
    }
}
