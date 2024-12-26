package com.github.mirko0.discordio.utils;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiringCache<K, V> {
    private final Map<K, CacheEntry<V>> cache;
    private final int expiringTime;
    private final ScheduledExecutorService executorService;
    private final ExpireAction<V> expireAction;

    private static class CacheEntry<V> {
        private final V entry;
        private final Instant expiresAt;

        private CacheEntry(V entry, Instant expiresAt) {
            this.entry = entry;
            this.expiresAt = expiresAt;
        }

        public V getEntry() {
            return entry;
        }

        public Instant expiresAt() {
            return expiresAt;
        }
    }

    public ExpiringCache(int expiringTimeInSec) {
        this.cache = new ConcurrentHashMap<>();
        this.expiringTime = expiringTimeInSec;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(this::removeExpiredEntries, 0, expiringTimeInSec, TimeUnit.SECONDS);
        this.expireAction = null;
    }

    private void removeExpiredEntries() {
        Iterator<CacheEntry<V>> iterator = cache.values().iterator();
        while (iterator.hasNext()) {
            CacheEntry<V> entry = iterator.next();
            if (entry.expiresAt.isBefore(Instant.now())) {
                if (expireAction != null) {
                    expireAction.onExpired(entry.getEntry());
                }
                iterator.remove();
            }
        }
    }

    public ExpiringCache(int expiringTimeInSec, ExpireAction<V> expireAction) {
        this.cache = new ConcurrentHashMap<>();
        this.expiringTime = expiringTimeInSec;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(this::removeExpiredEntries, 0, expiringTimeInSec, TimeUnit.SECONDS);
        this.expireAction = expireAction;
    }

    public void put(K key, V entry) {
        cache.put(key, new CacheEntry<>(entry, Instant.now().plusSeconds(expiringTime)));
    }

    public V get(K key) {
        CacheEntry<V> cacheEntry = cache.get(key);
        if (cacheEntry == null || cacheEntry.expiresAt.isBefore(Instant.now())) {
            return null;
        }
        return cacheEntry.getEntry();
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public boolean containsValue(V entry) {
        return cache.values().stream().anyMatch(cacheEntry -> cacheEntry.getEntry().equals(entry));
    }

    public Set<K> keySet() {
        return cache.keySet();
    }

    @FunctionalInterface
    public interface ExpireAction<V> {
        void onExpired(V value);
    }
}
