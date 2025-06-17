# Array Problems

## Core Concepts
- Array traversal and manipulation
- Two-pointer technique
- Sliding window
- Prefix sum
- Kadane's algorithm
- Matrix operations

## Essential Methods and Data Structures
### Java Arrays Utility Methods
```java
import java.util.Arrays;

// Sorting
Arrays.sort(arr);                    // O(n log n)
Arrays.sort(arr, (a, b) -> a - b);  // Custom comparator

// Binary Search
Arrays.binarySearch(arr, key);       // O(log n)

// Filling
Arrays.fill(arr, value);             // O(n)

// Copying
Arrays.copyOf(arr, length);          // O(n)
Arrays.copyOfRange(arr, from, to);   // O(n)

// Comparison
Arrays.equals(arr1, arr2);           // O(n)
```

### ArrayList Operations
```java
import java.util.ArrayList;

ArrayList<Integer> list = new ArrayList<>();
list.add(element);           // O(1) amortized
list.get(index);            // O(1)
list.set(index, element);   // O(1)
list.remove(index);         // O(n)
list.size();               // O(1)
```

## Common Algorithm Patterns

### 1. Two Pointer Technique
Used for:
- Finding pairs in sorted array
- Partitioning
- Merging sorted arrays
- Finding subarrays with specific properties

Example Pattern:
```java
int left = 0, right = arr.length - 1;
while (left < right) {
    // Process elements at left and right
    // Move pointers based on condition
    if (condition) left++;
    else right--;
}
```

### 2. Sliding Window
Used for:
- Subarrays with specific sum
- Maximum/minimum subarray of fixed size
- String problems with window constraints

Example Pattern:
```java
int windowStart = 0;
for (int windowEnd = 0; windowEnd < arr.length; windowEnd++) {
    // Add element to window
    while (condition) {
        // Remove elements from window start
        windowStart++;
    }
    // Process current window
}
```

### 3. Prefix Sum
Used for:
- Range sum queries
- Cumulative computations
- Subarray sum problems

Example Pattern:
```java
int[] prefixSum = new int[n + 1];
for (int i = 0; i < n; i++) {
    prefixSum[i + 1] = prefixSum[i] + arr[i];
}
```

## Time/Space Complexity Patterns
- Linear Scan: O(n) time
- Nested Loops: O(n²) time
- Binary Search: O(log n) time
- Sorting Based: O(n log n) time
- Space Complexity: Usually O(1) or O(n)

## Java 17 Features Relevant to Arrays
- Pattern Matching for switch
- Records for immutable data
- Enhanced type inference
- Text blocks for better readability
- Sealed classes for type hierarchies

## Study Roadmap

### 1. Basic Operations (Start Here)
- Array traversal
- Basic operations (insert, delete, search)
- Simple sorting and searching

### 2. Common Techniques
- Two pointer approach
- Sliding window
- Prefix sum arrays
- Kadane's algorithm

### 3. Advanced Topics
- Matrix operations
- Multi-dimensional arrays
- Dynamic programming with arrays
- Advanced sorting techniques

### 4. Special Topics
- Bit manipulation with arrays
- Array-based data structures
- Complex array algorithms

## Problem Categories

### Easy
- Array traversal
- Basic searching
- Simple math operations
- Basic two-pointer problems

### Medium
- Sliding window problems
- Subarray problems
- Matrix operations
- Sorting-based problems

### Hard
- Complex array manipulations
- Advanced algorithmic problems
- Optimization problems
- Multi-dimensional array operations

## Best Practices
1. Always validate array bounds
2. Consider edge cases (empty array, single element)
3. Look for opportunities to optimize space
4. Use appropriate Java collections
5. Leverage built-in methods when possible

## Common Pitfalls
1. Array index out of bounds
2. Integer overflow in calculations
3. Modifying array while iterating
4. Incorrect initialization
5. Not handling edge cases

## Testing Strategies
1. Test with empty arrays
2. Test with single element
3. Test with duplicate elements
4. Test with negative numbers
5. Test with large arrays
6. Test edge cases 