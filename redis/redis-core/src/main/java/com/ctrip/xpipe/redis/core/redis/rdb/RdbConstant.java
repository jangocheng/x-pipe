package com.ctrip.xpipe.redis.core.redis.rdb;

/**
 * @author lishanglin
 * date 2022/5/28
 */
public class RdbConstant {

    private RdbConstant() {

    }

    public static final int REDIS_RDB_VERSION = 9;
    public static final byte[] REDIS_RDB_MAGIC = new byte[] {'R', 'E', 'D', 'I', 'S'};

    public static final char REDIS_RDB_TYPE_STRING = 0;
    public static final char REDIS_RDB_TYPE_LIST = 1;
    public static final char REDIS_RDB_TYPE_SET = 2;
    public static final char REDIS_RDB_TYPE_ZSET = 3;
    public static final char REDIS_RDB_TYPE_HASH = 4;
    public static final char REDIS_RDB_TYPE_ZSET2 = 5;
    public static final char REDIS_RDB_TYPE_MODULE = 6;
    public static final char REDIS_RDB_TYPE_MODULE2 = 7;
    public static final char REDIS_RDB_TYPE_HASH_ZIPMAP = 9;
    public static final char REDIS_RDB_TYPE_LIST_ZIPLIST = 10;
    public static final char REDIS_RDB_TYPE_SET_INTSET = 11;
    public static final char REDIS_RDB_TYPE_ZSET_ZIPLIST = 12;
    public static final char REDIS_RDB_TYPE_HASH_ZIPLIST = 13;
    public static final char REDIS_RDB_TYPE_LIST_QUICKLIST = 14;
    public static final char REDIS_RDB_TYPE_STREAM_LISTPACKS = 15;

    public static final char REDIS_RDB_OP_CODE_MODULE_AUX = 247;
    public static final char REDIS_RDB_OP_CODE_IDLE = 248;
    public static final char REDIS_RDB_OP_CODE_FREQ = 249;
    public static final char REDIS_RDB_OP_CODE_AUX = 250;
    public static final char REDIS_RDB_OP_CODE_RESIZEDB = 251;
    public static final char REDIS_RDB_OP_CODE_EXPIRETIME_MS = 252;
    public static final char REDIS_RDB_OP_CODE_EXPIRETIME = 253;
    public static final char REDIS_RDB_OP_CODE_SELECTDB = 254;
    public static final char REDIS_RDB_OP_CODE_EOF = 255;

}
