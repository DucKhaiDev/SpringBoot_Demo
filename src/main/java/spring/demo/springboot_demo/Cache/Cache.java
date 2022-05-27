package spring.demo.springboot_demo.Cache;

import java.util.Collection;

public interface Cache {
    Object getItem(String key, Class<?> type);

    Object setItem(String key, Object item);

    void removeItem(String key);

    Collection<Object> getList(String key, Class<?> type);

    Collection<Object> addItemToList(String key, Object item);

    Collection<Object> removeItemFromList(String key, Object item);
}
