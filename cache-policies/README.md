# Cache Replacement Policies

## Overview
Cache replacement policies determine which items to evict from a cache when it is full and a new item needs to be inserted. The choice of policy can significantly impact cache hit rates and system performance.

## Common Policies

### 1. LRU (Least Recently Used)
- **Definition:** Evicts the item that has not been used for the longest time.
- **Architecture:** Typically implemented with a doubly-linked list and a hash map for O(1) access and update.
- **Best For:** General-purpose caching where recent access is a good predictor of future use.
- **Best Practices:**
  - Use efficient data structures (LinkedHashMap in Java)
  - Periodically monitor hit/miss rates
- **Avoid:**
  - Using LRU for workloads with cyclic or scan access patterns
- **What to do:**
  - Tune cache size based on workload
  - Use LRU for web caches, database buffers

### 2. MRU (Most Recently Used)
- **Definition:** Evicts the most recently used item first.
- **Architecture:** Similar to LRU but removes the head of the list.
- **Best For:** Workloads where recently accessed items are less likely to be reused soon (e.g., some stack-like access patterns).
- **Best Practices:**
  - Use when MRU fits the access pattern
- **Avoid:**
  - Using MRU for typical web or database caches
- **What to do:**
  - Analyze access patterns before choosing MRU

### 3. LFU (Least Frequently Used)
- **Definition:** Evicts the item with the lowest access frequency.
- **Architecture:** Uses a frequency counter for each item, often implemented with a min-heap or frequency list.
- **Best For:** Workloads with strong temporal locality and repeated access to a small set of items.
- **Best Practices:**
  - Use approximate LFU for large caches (to avoid high overhead)
  - Reset counters periodically to avoid stale data
- **Avoid:**
  - Using LFU for rapidly changing workloads
- **What to do:**
  - Use LFU for CDN caches, object stores

### 4. Pseudo-LRU (PLRU)
- **Definition:** An approximation of LRU that is less expensive to implement, often used in hardware caches.
- **Architecture:** Uses a binary tree or bit flags to track usage, not exact order.
- **Best For:** Hardware caches (CPU, GPU) where true LRU is too costly.
- **Best Practices:**
  - Use for set-associative caches
  - Accept some loss in accuracy for performance
- **Avoid:**
  - Using PLRU in software where true LRU is feasible
- **What to do:**
  - Use PLRU for CPU cache lines, embedded systems

## How the Algorithms Work

### LRU (Least Recently Used)
- **How it works:**
  - LRU keeps track of the order in which items are accessed.
  - When a cache miss occurs and the cache is full, it evicts the item that was accessed the longest time ago.
  - Every time an item is accessed (read or written), it is moved to the "most recently used" position.
- **Typical Implementation:**
  - **Doubly-linked list + Hash Map:**
    - The list maintains the order of usage (most recent at one end, least at the other).
    - The hash map provides O(1) access to nodes in the list.
    - On access: Move the node to the front.
    - On insert: Add to the front; if full, remove from the back.

### MRU (Most Recently Used)
- **How it works:**
  - MRU evicts the item that was accessed most recently.
  - This is the opposite of LRU and is useful for certain access patterns (e.g., stack-like).
- **Typical Implementation:**
  - **Stack or Doubly-linked list:**
    - The most recently used item is always at the top/front.
    - On access: Move the item to the top/front.
    - On insert: If full, remove the item at the top/front.

### LFU (Least Frequently Used)
- **How it works:**
  - LFU tracks how many times each item is accessed.
  - When eviction is needed, it removes the item with the lowest access count.
  - If multiple items have the same frequency, the oldest among them is evicted.
- **Typical Implementation:**
  - **Hash Map + Min-Heap or Frequency List:**
    - Each item has a counter for access frequency.
    - A min-heap or a list of frequency buckets allows quick access to the least frequently used items.
    - On access: Increment the counter and move the item to the appropriate bucket.
    - On insert: If full, remove an item from the lowest-frequency bucket.

### Pseudo-LRU (PLRU)
- **How it works:**
  - PLRU is an approximation of LRU, often used in hardware where true LRU is too expensive.
  - It uses a binary tree or a set of bits to track which items are "less recently used."
  - It does not maintain a strict order but provides a good-enough approximation.
- **Typical Implementation:**
  - **Binary Tree or Bit Flags:**
    - For a 4-way set associative cache, a 3-bit tree can represent which way to evict.
    - Each bit indicates which subtree was used more recently.
    - On access: Update the bits to reflect the path taken.
    - On eviction: Follow the bits to find a "less recently used" item.

---

## Visual Example: LRU

Suppose the cache can hold 3 items and the access sequence is: A, B, C, A, D

- After A, B, C: [A, B, C] (A is least recently used)
- Access A: [B, C, A] (B is now least recently used)
- Insert D (cache full): Evict B → [C, A, D]

---

## Summary Table

| Policy | Tracks | Evicts | Data Structure | Complexity |
|--------|--------|--------|---------------|------------|
| LRU    | Order of use | Least recently used | List + Hash Map | O(1) |
| MRU    | Order of use | Most recently used  | List/Stack      | O(1) |
| LFU    | Frequency    | Least frequently used | Hash Map + Heap/List | O(1)–O(log n) |
| PLRU   | Approx. order| Approx. LRU         | Bits/Tree       | O(1) |

## General Best Practices
- Choose the policy that matches your workload
- Monitor cache performance and adjust as needed
- Use efficient data structures for O(1) operations
- Consider hybrid policies (e.g., LRU-K, ARC)
- Document cache behavior and tuning parameters

## What to Avoid
- Using a one-size-fits-all policy
- Ignoring cache metrics
- Overcomplicating the cache logic
- Neglecting concurrency and thread safety

## What to Do
- Profile and benchmark cache performance
- Tune cache size and policy parameters
- Use built-in cache libraries when possible
- Test with real-world workloads

## Example Use Cases
- **LRU:** Web browser cache, database buffer pool
- **MRU:** Stack-based algorithms, some memory allocators
- **LFU:** CDN edge caches, object storage
- **PLRU:** CPU L1/L2/L3 caches

## References
- [Wikipedia: Cache Replacement Policies](https://en.wikipedia.org/wiki/Cache_replacement_policies)
- [Java LinkedHashMap LRU Example](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html)
- [LFU Cache Design](https://leetcode.com/problems/lfu-cache/)
- [PLRU in Hardware Caches](https://en.wikipedia.org/wiki/Pseudo-LRU)
