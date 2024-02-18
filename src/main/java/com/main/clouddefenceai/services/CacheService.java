package com.main.clouddefenceai.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CacheService {

    private ConcurrentMap<String, List<CacheEntry>> cache;

    @Value("${cache.expiration.duration}")
    private Long expirationDuration;

    @PostConstruct
    public void init() {
	cache = new ConcurrentHashMap<>();
    }

    public void putValue(String key, Object value) {
	cache.computeIfAbsent(key, k -> new ArrayList<>())
		.add(new CacheEntry(value, System.currentTimeMillis() + expirationDuration));
    }

    public List<String> getValue(String key) {
	List<String> cacheContent = new ArrayList<>();
	List<CacheEntry> entries = cache.get(key);
	if (entries != null && !entries.isEmpty()) {
	    synchronized (entries) {
		CacheEntry entry = entries.get(0);
		Long now = System.currentTimeMillis();
		if (entry.isValid(now)) {
		    cacheContent.add(entry.getValue().toString());
		}
	    }
	    return cacheContent;
	}
	return null;
    }

    @Scheduled(fixedDelayString = "${cache.cleanup.interval}")
    public void evictExpiredEntries() {
	long now = System.currentTimeMillis();
	cache.entrySet().forEach(entry -> {
	    synchronized (entry.getValue()) {
		entry.getValue().removeIf(e -> !e.isValid(now));
		if (entry.getValue().isEmpty()) {
		    cache.remove(entry.getKey());
		}
	    }
	});
    }

    public void printCacheContents() {
	cache.forEach((key, entriesList) -> {
	    System.out.println("Key: " + key);
	    entriesList.forEach(entry -> {
		long now = System.currentTimeMillis();
		if (entry.isValid(now)) {
		    System.out.println("  Value: " + entry.getValue() + ", Expires in: "
			    + (entry.expiryTime - now) / 1000 + " seconds");
		} else {
		    System.out.println("  [EXPIRED]");
		}
	    });
	});
    }

    public void clearCache(String currentUser) {
	cache.remove(currentUser);
    }

    private static class CacheEntry {
	private final Object value;
	private final Long expiryTime;

	CacheEntry(Object value, Long expiryTime) {
	    this.value = value;
	    this.expiryTime = expiryTime;
	}

	public boolean isValid(Long now) {
	    return now < expiryTime;
	}

	public Object getValue() {
	    return value;
	}
    }
}
