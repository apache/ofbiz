package org.ofbiz.base.util.cache.impl;

import java.util.concurrent.atomic.AtomicReference;

import org.ofbiz.base.util.cache.CacheManager;
import org.ofbiz.base.util.cache.CacheManagerFactory;

public class OFBizCacheManagerFactory extends CacheManagerFactory {

    private static AtomicReference<CacheManager> singleton = new AtomicReference<CacheManager>();

    @Override
    public CacheManager getInstance(String cacheManagerName) {
        CacheManager cacheManager = singleton.get();
        if (cacheManager == null) {
            cacheManager = new OFBizCacheManager(cacheManagerName);
            singleton.compareAndSet(null, cacheManager);
        }
        return singleton.get();
    }

}
