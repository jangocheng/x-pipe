package com.ctrip.xpipe.redis.core.redis.operation.parser;

import com.ctrip.xpipe.redis.core.redis.operation.*;
import com.ctrip.xpipe.redis.core.redis.operation.op.RedisOpSetEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lishanglin
 * date 2022/2/19
 */
@Component
public class RedisOpSetExParser extends AbstractRedisOpParser implements RedisOpParser {

    @Autowired
    public RedisOpSetExParser(RedisOpParserManager redisOpParserManager) {
        redisOpParserManager.registerParser(RedisOpType.SETEX, this);
    }

    @Override
    public RedisOp parse(byte[][] args) {
        return new RedisOpSetEx(args, new RedisKey(args[1]), args[3]);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
