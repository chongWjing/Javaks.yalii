package com.lostfound.api;

import com.lostfound.model.Item;
import java.util.List;

/**
 * 物品匹配策略接口 - 展示策略模式和接口的使用
 * 定义物品匹配的策略，不同的实现类提供不同的匹配算法
 */
public interface ItemMatcher {
    /**
     * 匹配失物和招领物品
     * @param lostItems 失物列表
     * @param foundItems 招领物品列表
     * @return 匹配结果列表，每个结果包含匹配的失物和招领物品对
     */
    List<MatchResult> findMatches(List<? extends Item> lostItems, List<? extends Item> foundItems);

    /**
     * 获取匹配策略名称
     * @return 策略名称
     */
    String getStrategyName();
}
