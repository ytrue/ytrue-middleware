package com.ytrue.middleware.db.router;

/**
 * @author ytrue
 * @date 2022/9/21 17:53
 * @description DBRouterBase
 */
public class DbRouterBase {

    private String tbIdx;

    public String getTbIdx() {
        return DbContextHolder.getTBKey();
    }
}
