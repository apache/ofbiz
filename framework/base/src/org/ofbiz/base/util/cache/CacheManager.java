package org.ofbiz.base.util.cache;

import java.util.Set;

public interface CacheManager {

    /** Removes all elements from this cache */
    public void clearAllCaches();

    public Set<String> getUtilCacheTableKeySet();

    /** Getter for the name of the UtilCache instance.
     * @return The name of the instance
     */
    public String getName();

    /** Checks for a non-expired key in a specific cache */
    public boolean validKey(String cacheName, Object key);

    public void clearCachesThatStartWith(String startsWith);

    public void clearCache(String cacheName);

    public <K, V> Cache<K, V> getOrCreateCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore, String... names);

    public <K, V> Cache<K, V> createCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore, String... names);

    public <K, V> Cache<K, V> createCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore);

    public <K, V> Cache<K, V> createCache(String name, int sizeLimit, long expireTime, boolean useSoftReference);

    public <K, V> Cache<K, V> createCache(String name, int sizeLimit, long expireTime);

    public <K, V> Cache<K, V> createCache(int sizeLimit, long expireTime);

    public <K, V> Cache<K, V> createCache(String name, boolean useSoftReference);

    public <K, V> Cache<K, V> createCache(String name);

    public <K, V> Cache<K, V> createCache();

    public <K, V> Cache<K, V> findCache(String cacheName);

}