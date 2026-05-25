package com.lostfound.api;

/**
 * 操作监听器接口 - 展示接口回调和观察者模式
 * 用于监听系统操作事件
 */
public interface OperationListener {
    /**
     * 操作开始时的回调
     * @param operation 操作名称
     */
    void onOperationStart(String operation);

    /**
     * 操作完成时的回调
     * @param operation 操作名称
     * @param success 是否成功
     * @param message 结果消息
     */
    void onOperationComplete(String operation, boolean success, String message);

    /**
     * 数据更新时的回调
     * @param dataType 数据类型
     * @param count 更新数量
     */
    void onDataUpdate(String dataType, int count);
}
