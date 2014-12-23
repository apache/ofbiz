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

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.ofbiz.base.util.Debug;

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
public class OFBizCacheManager {

    public static final String module = OFBizCacheManager.class.getName();

    /** A static Map to keep track of all of the UtilCache instances. */
    private final ConcurrentHashMap<String, OFBizCache<?, ?>> ofbizCacheTable = new ConcurrentHashMap<String, OFBizCache<?, ?>>();

    /** An index number appended to utilCacheTable names when there are conflicts. */
    private final ConcurrentHashMap<String, AtomicInteger> defaultIndices = new ConcurrentHashMap<String, AtomicInteger>();

    /** The name of the UtilCache instance, is also the key for the instance in utilCacheTable. */
    private final String name;

    // weak ref on this
    private static final ConcurrentMap<String, JdbmRecordManager> fileManagers = new ConcurrentHashMap<String, JdbmRecordManager>();

    //TODO: Totally wrong description
    /** Constructor which specifies the cacheName as well as the sizeLimit, expireTime and useSoftReference.
     * The passed sizeLimit, expireTime and useSoftReference will be overridden by values from cache.properties if found.
     * @param sizeLimit The sizeLimit member is set to this value
     * @param expireTime The expireTime member is set to this value
     * @param cacheName The name of the cache.
     * @param useSoftReference Specifies whether or not to use soft references for this cache.
     */
    public OFBizCacheManager(String name) {
        this.name = name;
    }

    public JdbmRecordManager getJdbmRecordManager(String fileStore) {
        // create the manager the first time it is needed
        JdbmRecordManager jdbmMgr = fileManagers.get(fileStore);
        if (jdbmMgr == null) {
            Debug.logImportant("Creating file system cache store for cache manager with name: " + this.name, module);
            try {
                String ofbizHome = System.getProperty("ofbiz.home");
                if (ofbizHome == null) {
                    Debug.logError("No ofbiz.home property set in environment", module);
                } else {
                    jdbmMgr = new JdbmRecordManager(ofbizHome + "/" + fileStore);
                }
            } catch (IOException e) {
                Debug.logError(e, "Error creating file system cache store for cache manager with name: " + this.name, module);
            }
            fileManagers.putIfAbsent(fileStore, jdbmMgr);
        }
        return fileManagers.get(fileStore);
    }

    private String getNextDefaultIndex(String cacheName) {
        AtomicInteger curInd = defaultIndices.get(cacheName);
        if (curInd == null) {
            defaultIndices.putIfAbsent(cacheName, new AtomicInteger(0));
            curInd = defaultIndices.get(cacheName);
        }
        int i = curInd.getAndIncrement();
        return i == 0 ? "" : Integer.toString(i);
    }


    /** Removes all elements from this cache */
    public void clearAllCaches() {
        // We make a copy since clear may take time
        for (OFBizCache<?,?> cache : ofbizCacheTable.values()) {
            cache.clear();
        }
    }

    public Set<String> getUtilCacheTableKeySet() {
        Set<String> set = new HashSet<String>(ofbizCacheTable.size());
        set.addAll(ofbizCacheTable.keySet());
        return set;
    }

    /** Getter for the name of the UtilCache instance.
     * @return The name of the instance
     */
    public String getName() {
        return this.name;
    }

    /** Checks for a non-expired key in a specific cache */
    public boolean validKey(String cacheName, Object key) {
        OFBizCache<?, ?> cache = findCache(cacheName);
        if (cache != null) {
            if (cache.containsKey(key))
                return true;
        }
        return false;
    }

    public void clearCachesThatStartWith(String startsWith) {
        for (Map.Entry<String, OFBizCache<?, ?>> entry: ofbizCacheTable.entrySet()) {
            String name = entry.getKey();
            if (name.startsWith(startsWith)) {
                OFBizCache<?, ?> cache = entry.getValue();
                cache.clear();
            }
        }
    }

    public void clearCache(String cacheName) {
        OFBizCache<?, ?> cache = findCache(cacheName);
        if (cache == null) return;
        cache.clear();
    }

    @SuppressWarnings("unchecked")
    public <K, V> OFBizCache<K, V> getOrCreateUtilCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore, String... names) {
        OFBizCache<K, V> existingCache = (OFBizCache<K, V>) ofbizCacheTable.get(name);
        if (existingCache != null) return existingCache;
        String cacheName = name + getNextDefaultIndex(name);
        OFBizCache<K, V> newCache = new OFBizCache<K, V>(this, cacheName, sizeLimit, maxInMemory, expireTime, useSoftReference, useFileSystemStore, name, names);
        ofbizCacheTable.putIfAbsent(name, newCache);
        return (OFBizCache<K, V>) ofbizCacheTable.get(name);
    }

    public <K, V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore, String... names) {
        String cacheName = name + getNextDefaultIndex(name);
        return storeCache(new OFBizCache<K, V>(this, cacheName, sizeLimit, maxInMemory, expireTime, useSoftReference, useFileSystemStore, name, names));
    }

    public <K, V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, int maxInMemory, long expireTime, boolean useSoftReference, boolean useFileSystemStore) {
        String cacheName = name + getNextDefaultIndex(name);
        return storeCache(new OFBizCache<K, V>(this, cacheName, sizeLimit, maxInMemory, expireTime, useSoftReference, useFileSystemStore, name));
    }

    public <K,V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, long expireTime, boolean useSoftReference) {
        String cacheName = name + getNextDefaultIndex(name);
        return storeCache(new OFBizCache<K, V>(this, cacheName, sizeLimit, sizeLimit, expireTime, useSoftReference, false, name));
    }

    public <K,V> OFBizCache<K, V> createUtilCache(String name, int sizeLimit, long expireTime) {
        String cacheName = name + getNextDefaultIndex(name);
        return storeCache(new OFBizCache<K, V>(this, cacheName, sizeLimit, sizeLimit, expireTime, false, false, name));
    }

    public <K,V> OFBizCache<K, V> createUtilCache(int sizeLimit, long expireTime) {
        String cacheName = "specified" + getNextDefaultIndex("specified");
        return storeCache(new OFBizCache<K, V>(this, cacheName, sizeLimit, sizeLimit, expireTime, false, false, "specified"));
    }

    public <K,V> OFBizCache<K, V> createUtilCache(String name, boolean useSoftReference) {
        String cacheName = name + getNextDefaultIndex(name);
        return storeCache(new OFBizCache<K, V>(this, cacheName, 0, 0, 0, useSoftReference, false, "default", name));
    }

    public <K,V> OFBizCache<K, V> createUtilCache(String name) {
        String cacheName = name + getNextDefaultIndex(name);
        return storeCache(new OFBizCache<K, V>(this, cacheName, 0, 0, 0, false, false, "default", name));
    }

    public <K,V> OFBizCache<K, V> createUtilCache() {
        String cacheName = "default" + getNextDefaultIndex("default");
        return storeCache(new OFBizCache<K, V>(this, cacheName, 0, 0, 0, false, false, "default"));
    }

    private <K, V> OFBizCache<K, V> storeCache(OFBizCache<K, V> cache) {
        ofbizCacheTable.put(cache.getName(), cache);
        return cache;
    }

    @SuppressWarnings("unchecked")
    public <K, V> OFBizCache<K, V> findCache(String cacheName) {
        return (OFBizCache<K, V>) ofbizCacheTable.get(cacheName);
    }

}
