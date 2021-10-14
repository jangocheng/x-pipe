package com.ctrip.xpipe.redis.integratedtest.console.spring.console;

import com.ctrip.xpipe.api.foundation.FoundationService;
import com.ctrip.xpipe.api.sso.LogoutHandler;
import com.ctrip.xpipe.api.sso.UserInfoHolder;
import com.ctrip.xpipe.redis.checker.PersistenceCache;
import com.ctrip.xpipe.redis.checker.config.CheckerConfig;
import com.ctrip.xpipe.redis.checker.healthcheck.actions.ping.PingService;
import com.ctrip.xpipe.redis.checker.impl.TestMetaCache;
import com.ctrip.xpipe.redis.checker.spring.ConsoleServerMode;
import com.ctrip.xpipe.redis.checker.spring.ConsoleServerModeCondition;
import com.ctrip.xpipe.redis.console.cluster.ConsoleLeaderElector;
import com.ctrip.xpipe.redis.console.config.ConsoleConfig;
import com.ctrip.xpipe.redis.console.config.ConsoleDbConfig;
import com.ctrip.xpipe.redis.console.config.impl.DefaultConsoleDbConfig;
import com.ctrip.xpipe.redis.console.dao.ClusterDao;
import com.ctrip.xpipe.redis.console.dao.ConfigDao;
import com.ctrip.xpipe.redis.console.dao.RedisDao;
import com.ctrip.xpipe.redis.console.healthcheck.nonredis.cluster.ClusterHealthMonitorManager;
import com.ctrip.xpipe.redis.console.healthcheck.nonredis.cluster.impl.DefaultClusterHealthMonitorManager;
import com.ctrip.xpipe.redis.console.resources.DefaultMetaCache;
import com.ctrip.xpipe.redis.console.resources.DefaultPersistenceCache;
import com.ctrip.xpipe.redis.console.service.DcClusterShardService;
import com.ctrip.xpipe.redis.console.service.RedisInfoService;
import com.ctrip.xpipe.redis.console.service.impl.AlertEventService;
import com.ctrip.xpipe.redis.console.service.impl.ConsoleCachedPingService;
import com.ctrip.xpipe.redis.console.service.impl.ConsoleRedisInfoService;
import com.ctrip.xpipe.redis.console.service.impl.DefaultCrossMasterDelayService;
import com.ctrip.xpipe.redis.console.sso.UserAccessFilter;
import com.ctrip.xpipe.redis.console.util.DefaultMetaServerConsoleServiceManagerWrapper;
import com.ctrip.xpipe.redis.core.meta.MetaCache;
import com.ctrip.xpipe.redis.integratedtest.console.config.SpringEnvConsoleConfig;
import com.ctrip.xpipe.redis.integratedtest.console.config.TestFoundationService;
import com.ctrip.xpipe.spring.AbstractProfile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.*;

import java.util.concurrent.ScheduledExecutorService;

import static com.ctrip.xpipe.spring.AbstractSpringConfigContext.SCHEDULED_EXECUTOR;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.ctrip.xpipe.service.sso", "com.ctrip.xpipe.redis.console", "com.ctrip.xpipe.redis.checker.alert"})
@ServletComponentScan("com.ctrip.framework.fireman")
@ConsoleServerMode(ConsoleServerModeCondition.SERVER_MODE.CONSOLE)
public class TestConsoleContextConfig {

    @Bean
    public ConsoleConfig consoleConfig() {
        return new SpringEnvConsoleConfig();
    }

    @Bean
    public DefaultMetaServerConsoleServiceManagerWrapper getMetaServerConsoleServiceManagerWraper() {
        return new DefaultMetaServerConsoleServiceManagerWrapper();
    }

    @Bean
    public UserInfoHolder userInfoHolder(){
        return UserInfoHolder.DEFAULT;
    }

    @Bean
    public LogoutHandler logoutHandler(){
        return LogoutHandler.DEFAULT;
    }

    @Bean
    @Profile(AbstractProfile.PROFILE_NAME_PRODUCTION)
    public FilterRegistrationBean userAccessFilter(ConsoleConfig consoleConfig) {

        FilterRegistrationBean userAccessFilter = new FilterRegistrationBean();

        userAccessFilter.setFilter(new UserAccessFilter(UserInfoHolder.DEFAULT, consoleConfig));
        userAccessFilter.addUrlPatterns("/*");

        return userAccessFilter;
    }

    @Bean
    @Lazy
    @Profile(AbstractProfile.PROFILE_NAME_PRODUCTION)
    public MetaCache metaCache() {
        return new DefaultMetaCache();
    }

    @Bean
    @Lazy
    @Profile(AbstractProfile.PROFILE_NAME_TEST)
    public MetaCache testMetaCache() {
        return new TestMetaCache();
    }

//    @Bean
//    public ConsoleConfig consoleConfig() {
//        return new DefaultConsoleConfig();
//    }

    @Bean
    public ConsoleDbConfig consoleDbConfig() {
        return new DefaultConsoleDbConfig();
    }

    @Bean
    @Profile(AbstractProfile.PROFILE_NAME_PRODUCTION)
    public ConsoleLeaderElector consoleLeaderElector() {
        return new ConsoleLeaderElector();
    }

    @Bean
    public PingService pingService() {
        return new ConsoleCachedPingService();
    }

    @Bean
    public RedisInfoService redisInfoService() {
        return new ConsoleRedisInfoService();
    }

    @Lazy
    @Bean
    @Profile(AbstractProfile.PROFILE_NAME_PRODUCTION)
    public ClusterHealthMonitorManager clusterHealthManager() {
        return new DefaultClusterHealthMonitorManager();
    }

    @Bean
    public DefaultCrossMasterDelayService defaultCrossMasterDelayService(FoundationService foundationService) {
        return new DefaultCrossMasterDelayService(foundationService.getDataCenter());
    }

    @Bean
    public PersistenceCache persistenceCache3(CheckerConfig config,
                                              @Qualifier(value = SCHEDULED_EXECUTOR) ScheduledExecutorService scheduled,
                                              AlertEventService alertEventService,
                                              ConfigDao configDao,
                                              DcClusterShardService dcClusterShardService,
                                              RedisDao redisDao,
                                              ClusterDao clusterDao) {
        return new DefaultPersistenceCache(
                config,
                scheduled,
                alertEventService,
                configDao,
                dcClusterShardService,
                redisDao,
                clusterDao);
    }

    @Bean
    public FoundationService foundationService() {
        return new TestFoundationService();
    }
    
}

