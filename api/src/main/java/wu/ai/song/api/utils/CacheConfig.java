package wu.ai.song.api.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liubujun
 * @Date: 2022/2/5 15:21
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * CacheManager为一个接口，RedisCacheManager为该接口的实现
     * redisConnectionFactory 连接工厂
     * cacheDefaults 默认配置
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig(10000))
                .withInitialCacheConfigurations(initCacheConfigMap())
                .transactionAware()
                .build();
        return redisCacheManager;
    }

    /**
     * 默认配置中进行了序列化的配置
     *
     * @param second
     * @return
     */
    private RedisCacheConfiguration defaultCacheConfig(Integer second) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //配置序列化 解决乱码的问题
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(second))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
        return config;
    }

    /**
     * 针对不同的redis的key 有不同的过期时间
     *
     * @return
     */
    private Map<String, RedisCacheConfiguration> initCacheConfigMap() {
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(4);
        configMap.put("User", this.defaultCacheConfig(1000));
        configMap.put("User1", this.defaultCacheConfig(1000));
        configMap.put("User2", this.defaultCacheConfig(1000));
        configMap.put("User3", this.defaultCacheConfig(1000));
        return configMap;
    }

}
