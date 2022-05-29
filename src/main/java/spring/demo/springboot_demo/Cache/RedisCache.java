package spring.demo.springboot_demo.Cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
public class RedisCache implements Cache {
    private ObjectMapper objectMapper;
    private Jedis jedis;

    @Override
    public Object getItem(String key, Class<?> type) {
        String jsonObject = jedis.get(key);
        try {
            return objectMapper.readValue(jsonObject, type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Object setItem(String key, Object item) {
        try {
            String jsonItem = objectMapper.writeValueAsString(item);
            String out = jedis.set(key, jsonItem);
            return objectMapper.readValue(out, Object.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void removeItem(String key) {
        jedis.del(key);
    }

    @Override
    public Collection<Object> getList(String key, Class<?> type) {
        return getListFromJedis(key, type);
    }

    private Collection<Object> getListFromJedis(String key, Class<?> type) {
        Collection<Object> list = new ArrayList<>();
        jedis.smembers(key).forEach(jsonItem -> {
            try {
                list.add(objectMapper.readValue(jsonItem, type));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        return list;
    }

    @Override
    public Collection<Object> addItemToList(String key, Object item) {
        try {
            String jsonItem = objectMapper.writeValueAsString(item);
            jedis.sadd(key, jsonItem);

            return this.getListFromJedis(key, item.getClass());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<Object> removeItemFromList(String key, Object item) {
        getListFromJedis(key, item.getClass()).forEach(row -> {
            if (row.equals(item)) {
                try {
                    String jsonItem = objectMapper.writeValueAsString(row);
                    jedis.srem(key, jsonItem);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Can't find object in Redis list.");
            }
        });

        return getListFromJedis(key, item.getClass());
    }
}
