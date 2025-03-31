package sourse.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 6379);
        configuration.setPassword("root");

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    // ✅ Kiểm tra kết nối Redis ngay khi Spring Boot khởi động
    @Bean
    public CommandLineRunner testRedisConnection(RedisTemplate<String, String> redisTemplate) {
        return args -> {
            try {
                redisTemplate.opsForValue().set("testKey", "Hello, World!");

                String value = redisTemplate.opsForValue().get("testKey");

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
