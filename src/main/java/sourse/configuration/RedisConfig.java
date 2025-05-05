package sourse.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("quiet-ant-13751.upstash.io", 6379);
        configuration.setPassword("ATW3AAIjcDE2OTc5MWI4MzNhNjU0YjNhYWFmYjc5MzRkMzFjYTZkZXAxMA");  // Đảm bảo mật khẩu đúng nếu có
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .useSsl()
                .build();
        return new LettuceConnectionFactory(configuration, clientConfig);
    }

    // Cấu hình ObjectMapper với JavaTimeModule
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    // Cấu hình RedisTemplate cho Object
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public CommandLineRunner testRedisConnection(RedisTemplate<String, Object> redisTemplate) {
        return args -> {
            try {
                redisTemplate.opsForValue().set("testKey", "Hello, World!");  // Lưu giá trị vào Redis

                String value = (String) redisTemplate.opsForValue().get("testKey");

                if ("Hello, World!".equals(value)) {
                    System.out.println("✅ Redis connected successfully! Value: " + value);
                } else {
                    System.out.println("⚠ Redis connected but value mismatch!");
                }
            } catch (Exception e) {
                System.err.println("❌ Redis connection failed: " + e.getMessage());
            }
        };
    }
}