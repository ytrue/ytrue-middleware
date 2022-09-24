package com.ytrue.middleware.db.router;

/**
 * @author ytrue
 * @date 2022/9/21 17:49
 * @description DBContextHolder
 */
public class DbContextHolder {

    private static final ThreadLocal<String> DB_KEY = new ThreadLocal<>();
    private static final ThreadLocal<String> TB_KEY = new ThreadLocal<>();

    public static void setDBKey(String dbKeyIdx) {
        DB_KEY.set(dbKeyIdx);
    }

    public static String getDBKey() {
        return DB_KEY.get();
    }

    public static void setTBKey(String tbKeyIdx) {
        TB_KEY.set(tbKeyIdx);
    }

    public static String getTBKey() {
        return TB_KEY.get();
    }

    public static void clearDBKey() {
        DB_KEY.remove();
    }

    public static void clearTBKey() {
        TB_KEY.remove();
    }

}
