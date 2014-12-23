/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.base.util.cache;

import java.util.Set;

/**
 * Generalized caching utility. Provides a number of caching features:
 * <ul>
 *   <li>Limited or unlimited element capacity
 *   <li>If limited, removes elements with the LRU (Least Recently Used) algorithm
 *   <li>Keeps track of when each element was loaded into the cache
 *   <li>Using the expireTime can report whether a given element has expired
 *   <li>Counts misses and hits
 * </ul>
 *
 */
@SuppressWarnings("serial")
public class UtilCache<K, V> {

    public static final String module = UtilCache.class.getName();

    private static final OFBizCacheManager cacheManager = new OFBizCacheManager("cache");

    public static Set<String> getUtilCacheTableKeySet() {
        return cacheManager.getUtilCacheTableKeySet();
    }

    /** Checks for a non-expired key in a specific cache */
    public static boolean validKey(String cacheName, Object key) {
        OFBizCache<?, ?> cache = findCache(cacheName);
        if (cache != null) {
            if (cache.containsKey(key))
                return true;
        }
        return false;
    }

    /** Removes all elements from this cache */
    public static void clearAllCaches() {
        cacheManager.clearAllCaches();
    }

    public static void clearCache(String cacheName) {
        cacheManager.clearCache(cacheName);
    }

    public static void clearCachesThatStartWith(String startsWith) {
        cacheManager.clearCachesThatStartWith(startsWith);
    }

    public static <K, V> OFBizCache<K, V> getOrCreateUtilCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore, String... names) {
        return cacheManager.getOrCreateUtilCache(name, sizeLimit, maxInMemory, expireTime, useSoftReference, useFileSystemStore, names);
    }

    public static <K, V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore, String... names) {
        return cacheManager.createUtilCache(name, sizeLimit, maxInMemory, expireTime, useSoftReference, useFileSystemStore, names);
    }

    public static <K, V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore) {
        return cacheManager.createUtilCache(name, sizeLimit, maxInMemory, expireTime, useSoftReference, useFileSystemStore);
    }

    public static <K,V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, long expireTime, boolean useSoftReference) {
        return cacheManager.createUtilCache(name, sizeLimit, expireTime, useSoftReference);
    }

    public static <K,V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, long expireTime) {
        return cacheManager.createUtilCache(name, sizeLimit, expireTime);
    }

    public static <K,V> OFBizCache<K, V> createUtilCache(int sizeLimit, long expireTime) {
        return cacheManager.createUtilCache(sizeLimit, expireTime);
    }

    public static <K,V> OFBizCache<K, V> createUtilCache(String name, boolean useSoftReference) {
        return cacheManager.createUtilCache(name, useSoftReference);
    }

    public static <K,V> OFBizCache<K, V> createUtilCache(String name) {
        return cacheManager.createUtilCache(name);
    }

    public static <K,V> OFBizCache<K, V> createUtilCache() {
        return cacheManager.createUtilCache();
    }

    public static <K, V> OFBizCache<K, V> findCache(String cacheName) {
        return cacheManager.findCache(cacheName);
    }

}
