# Cache Replacement Policies

Cache replacement policies determine which items to evict from a cache when it is full and a new item needs to be inserted. The choice of policy can significantly impact cache hit rates and system performance.

## Common Policies

### 1. LRU (Least Recently Used)
- **Definition:** Evicts the item that has not been used for the longest time.
- **How it works:** Maintains access order; evicts the least recently accessed item.
- **Java Example:**
```java
import java.util.LinkedHashMap;
import java.util.Map;

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
```

### 2. MRU (Most Recently Used)
- **Definition:** Evicts the most recently used item first.
- **How it works:** Removes the most recently accessed item when full.
- **Java Example:**
```java
// MRU is less common; here is a simple conceptual example
import java.util.*;
class MRUCache<K, V> {
    private final int capacity;
    private final Deque<K> stack = new ArrayDeque<>();
    private final Map<K, V> map = new HashMap<>();
    public MRUCache(int capacity) { this.capacity = capacity; }
    public void put(K key, V value) {
        if (map.size() == capacity) {
            K mru = stack.removeLast();
            map.remove(mru);
        }
        stack.addLast(key);
        map.put(key, value);
    }
    public V get(K key) {
        if (!map.containsKey(key)) return null;
        stack.remove(key);
        stack.addLast(key);
        return map.get(key);
    }
}
```

### 3. LFU (Least Frequently Used)
- **Definition:** Evicts the item with the lowest access frequency.
- **How it works:** Tracks access counts; evicts the least frequently accessed item.
- **Java Example:**
```java
// Simplified LFU cache using two maps
import java.util.*;
class LFUCache<K, V> {
    private final int capacity;
    private final Map<K, V> values = new HashMap<>();
    private final Map<K, Integer> counts = new HashMap<>();
    public LFUCache(int capacity) { this.capacity = capacity; }
    public void put(K key, V value) {
        if (values.size() == capacity) {
            K lfu = Collections.min(counts.entrySet(), Map.Entry.comparingByValue()).getKey();
            values.remove(lfu);
            counts.remove(lfu);
        }
        values.put(key, value);
        counts.put(key, 1);
    }
    public V get(K key) {
        if (!values.containsKey(key)) return null;
        counts.put(key, counts.get(key) + 1);
        return values.get(key);
    }
}
```

### 4. Pseudo-LRU (PLRU)
- **Definition:** An approximation of LRU, often used in hardware caches.
- **How it works:** Uses bits or a tree to track usage, not exact order.
- **Java Example:**
```java
// PLRU is hardware-oriented; here is a conceptual bit-based example for 4 items
class PLRUCache {
    private final int[] bits = new int[3]; // For 4-way
    // ... implement access and eviction logic using bits ...
}
```

## Best Practices
- Choose the policy that matches your workload
- Monitor cache performance and adjust as needed
- Use efficient data structures for O(1) operations
- Consider hybrid policies (e.g., LRU-K, ARC) 