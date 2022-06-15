package com.ctrip.xpipe.redis.core.redis.rdb;

import com.ctrip.xpipe.redis.core.redis.rdb.parser.DefaultRdbParserTest;
import com.ctrip.xpipe.redis.core.redis.rdb.parser.RdbAuxParserTest;
import com.ctrip.xpipe.redis.core.redis.rdb.parser.RdbSelectDbParserTest;
import com.ctrip.xpipe.redis.core.redis.rdb.parser.RdbStringParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author lishanglin
 * date 2022/6/5
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DefaultRdbParserTest.class,
        RdbAuxParserTest.class,
        RdbSelectDbParserTest.class,
        RdbStringParserTest.class
})
public class AllRdbTests {
}
