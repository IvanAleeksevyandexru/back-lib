package ru.gosuslugi.pgu.common.esia.search.service.impl;

import ru.atc.carcass.common.cache.CacheControlService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс необходимый для инициализации контекста работы RestRequest,
 * по идее здесь нужно использовать кэш окружения, а не изобретать своё.
 *
 * Но старая реализация EhCacheControlServiceImpl не завелась Spring Boot 2, в то же время кэш не кеширует
 */
public class CacheImpl implements CacheControlService {

    Map<String, Object> map = new ConcurrentHashMap<>();

    @Override
    public void put(String s1, String s2, Object o) {
        map.put(makeKey(s1, s2), o);
    }

    @Override
    public Object get(String s1, String s2) {
        return map.get(makeKey(s1, s2));
    }

    @Override
    public void delete(String s1, String s2) {
        map.remove(makeKey(s1, s2));
    }

    @Override
    public void clear(String s) {
        map.clear();
    }

    @Override
    public void deleteCacheByKeyPart(String s1, String s2) {
        map.remove(makeKey(s1, s2));
    }

    @Override
    public void deleteCacheByKeyPart(String s) {
        for (String key : map.keySet()) {
            if (key.startsWith(s+"..") || key.endsWith(".."+s)) {
                map.remove(key);
            }
        }
    }

    private String makeKey(String s1, String s2) {
        return s1+".."+s2;
    }

}
