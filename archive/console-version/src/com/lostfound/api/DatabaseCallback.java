package com.lostfound.api;

import java.util.List;

/**
 * 数据库回调接口 - 展示接口回调机制
 * 用于异步操作完成后的回调通知
 * @param <T> 返回数据类型
 */
public interface DatabaseCallback<T> {
    /**
     * 操作成功时的回调方法
     * @param result 操作结果
     */
    void onSuccess(T result);

    /**
     * 操作失败时的回调方法
     * @param errorMessage 错误信息
     */
    void onError(String errorMessage);
}

/**
 * 数据库操作结果回调接口 - 展示接口回调
 */
interface DatabaseOperationCallback {
    void onComplete(boolean success, String message);
}
