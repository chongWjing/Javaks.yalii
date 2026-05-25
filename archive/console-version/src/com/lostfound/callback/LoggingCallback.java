package com.lostfound.callback;

import com.lostfound.api.DatabaseCallback;
import java.util.logging.Logger;

/**
 * 日志回调类 - 实现DatabaseCallback接口，展示接口回调的具体实现
 * 用于记录数据库操作的日志
 */
public class LoggingCallback<T> implements DatabaseCallback<T> {
    private static final Logger logger = Logger.getLogger(LoggingCallback.class.getName());
    private String operationName;

    public LoggingCallback(String operationName) {
        this.operationName = operationName;
    }

    @Override
    public void onSuccess(T result) {
        logger.info(String.format("[%s] 操作成功: %s", operationName, result));
        System.out.println("[日志] " + operationName + " 操作成功");
    }

    @Override
    public void onError(String errorMessage) {
        logger.warning(String.format("[%s] 操作失败: %s", operationName, errorMessage));
        System.err.println("[日志] " + operationName + " 操作失败: " + errorMessage);
    }
}
