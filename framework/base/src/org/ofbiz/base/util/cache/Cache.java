package org.ofbiz.base.util.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Cache<K, V> {

    /** Getter for the name of the Cache instance.
     * @return The name of the instance
     */
    public String getName();

    public void setMaxInMemory(int newInMemory);

    public int getMaxInMemory();

    public void setSizeLimit(int newSizeLimit);

    public int getSizeLimit();

    /** Sets the expire time for the cache elements.
     * If 0, elements never expire.
     * @param expireTimeMillis The expire time for the cache elements
     */
    public void setExpireTime(long expireTimeMillis);

    /** return the current expire time for the cache elements
     * @return The expire time for the cache elements
     */
    public long getExpireTime();

    /** Set whether or not the cache lines should use a soft reference to the data */
    public void setUseSoftReference(boolean useSoftReference);

    /** Return whether or not the cache lines should use a soft reference to the data */
    public boolean getUseSoftReference();

    public boolean getUseFileSystemStore();

    /** Returns the number of elements currently in the cache
     * @return The number of elements currently in the cache
     */
    public long size();

    /** Returns a boolean specifying whether or not an element with the specified key is in the cache.
     * @param key The key for the element, used to reference it in the hashtables and LRU linked list
     * @return True is the cache contains an element corresponding to the specified key, otherwise false
     */
    public boolean containsKey(Object key);

    /**
     * NOTE: this returns an unmodifiable copy of the keySet, so removing from here won't have an effect,
     * and calling a remove while iterating through the set will not cause a concurrent modification exception.
     * This behavior is necessary for now for the persisted cache feature.
     */
    public Set<? extends K> getCacheLineKeys();

    public Collection<? extends Map<String, Object>> getLineInfos();

    /** Adds an event listener for key removals */
    public void addListener(CacheListener<K, V> listener);

    /** Removes an event listener for key removals */
    public void removeListener(CacheListener<K, V> listener);

    /** Puts or loads the passed element into the cache
     * @param key The key for the element, used to reference it in the hashtables and LRU linked list
     * @param value The value of the element
     */
    public V put(K key, V value);

    public V putIfAbsent(K key, V value);

    public V putIfAbsentAndGet(K key, V value);

    /** Gets an element from the cache according to the specified key.
     * @param key The key for the element, used to reference it in the hashtables and LRU linked list
     * @return The value of the element specified by the key
     */
    public V get(Object key);

    /** Removes an element from the cache according to the specified key
     * @param key The key for the element, used to reference it in the hashtables and LRU linked list
     * @return The value of the removed element specified by the key
     */
    public V remove(Object key);

    public Collection<V> values();

    public void clear();

    /** Removes all elements from this cache */
    public void erase();

    public boolean isEmpty();

    /** Returns the number of successful hits on the cache
     * @return The number of successful cache hits
     */
    public long getHitCount();

    public long getSizeInBytes();

    /** Returns the number of cache misses from entries that are not found in the cache
     * @return The number of cache misses
     */
    public long getMissCountNotFound();

    /** Returns the number of cache misses from entries that are expired
     * @return The number of cache misses
     */
    public long getMissCountExpired();

    /** Returns the number of cache misses from entries that are have had the soft reference cleared out (by garbage collector and such)
     * @return The number of cache misses
     */
    public long getMissCountSoftRef();

    /** Returns the number of cache misses caused by any reason
     * @return The number of cache misses
     */
    public long getMissCountTotal();

    public long getRemoveHitCount();

    public long getRemoveMissCount();

    /** Clears the hit and miss counters
     */
    public void clearCounters();

}