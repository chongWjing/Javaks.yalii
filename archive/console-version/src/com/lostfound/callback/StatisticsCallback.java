package com.lostfound.callback;

import com.lostfound.api.DatabaseCallback;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计回调类 - 实现DatabaseCallback接口，展示接口回调
 * 用于收集和处理统计数据
 */
public class StatisticsCallback implements DatabaseCallback<Map<String, Object>> {
    private Map<String, Object> statistics;
    private String reportTitle;

    public StatisticsCallback(String reportTitle) {
        this.reportTitle = reportTitle;
        this.statistics = new HashMap<>();
    }

    @Override
    public void onSuccess(Map<String, Object> result) {
        statistics.putAll(result);
        statistics.put("reportTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date()));
        System.out.println("[统计] " + reportTitle + " 统计完成");
        printStatistics();
    }

    @Override
    public void onError(String errorMessage) {
        System.err.println("[统计] " + reportTitle + " 统计失败: " + errorMessage);
    }

    private void printStatistics() {
        System.out.println("========== " + reportTitle + " ==========");
        statistics.forEach((key, value) ->
                System.out.println(key + ": " + value));
        System.out.println("=====================================");
    }

    public Map<String, Object> getStatistics() {
        return statistics;
    }
}
