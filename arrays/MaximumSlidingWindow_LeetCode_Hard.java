import java.util.*;

/**
 * Problem: Sliding Window Maximum (LeetCode #239)
 * Difficulty: Hard
 * Source: LeetCode
 * 
 * Problem Description:
 * You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right.
 * You can only see the k numbers in the window. Each time the sliding window moves right by one position.
 * Return the max sliding window.
 * 
 * Example:
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [3,3,5,5,6,7]
 * Explanation: 
 * Window position                Max
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7     3
 *  1 [3  -1  -3] 5  3  6  7     3
 *  1  3 [-1  -3  5] 3  6  7     5
 *  1  3  -1 [-3  5  3] 6  7     5
 *  1  3  -1  -3 [5  3  6] 7     6
 *  1  3  -1  -3  5 [3  6  7]    7
 * 
 * Time Complexity: O(n)
 * Space Complexity: O(k)
 */
public class MaximumSlidingWindow_LeetCode_Hard {
    
    /**
     * Optimized solution using Deque
     * Time Complexity: O(n)
     * Space Complexity: O(k)
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        int ri = 0;
        
        // Deque to store indices of potential maximum values
        Deque<Integer> deque = new ArrayDeque<>();
        
        for (int i = 0; i < nums.length; i++) {
            // Remove elements outside current window
            while (!deque.isEmpty() && deque.peek() < i - k + 1) {
                deque.poll();
            }
            
            // Remove elements smaller than current element from the back
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            // Add current element
            deque.offer(i);
            
            // Add to result if we have a full window
            if (i >= k - 1) {
                result[ri++] = nums[deque.peek()];
            }
        }
        
        return result;
    }
    
    /**
     * Brute force solution for comparison
     * Time Complexity: O(n*k)
     * Space Complexity: O(1)
     */
    public static int[] maxSlidingWindowBruteForce(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        
        for (int i = 0; i <= n - k; i++) {
            int max = nums[i];
            for (int j = 1; j < k; j++) {
                max = Math.max(max, nums[i + j]);
            }
            result[i] = max;
        }
        
        return result;
    }
    
    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        // Test Case 1: Example from problem description
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        System.out.println("Test Case 1:");
        System.out.println("Input: nums = " + Arrays.toString(nums1) + ", k = " + k1);
        System.out.println("Output (Optimized): " + Arrays.toString(maxSlidingWindow(nums1, k1)));
        System.out.println("Output (Brute Force): " + Arrays.toString(maxSlidingWindowBruteForce(nums1, k1)));
        
        // Test Case 2: Single element window
        int[] nums2 = {1, -1, 2, -2, 3, -3};
        int k2 = 1;
        System.out.println("\nTest Case 2:");
        System.out.println("Input: nums = " + Arrays.toString(nums2) + ", k = " + k2);
        System.out.println("Output (Optimized): " + Arrays.toString(maxSlidingWindow(nums2, k2)));
        System.out.println("Output (Brute Force): " + Arrays.toString(maxSlidingWindowBruteForce(nums2, k2)));
        
        // Test Case 3: Window size equals array length
        int[] nums3 = {1, 2, 3, 4, 5};
        int k3 = 5;
        System.out.println("\nTest Case 3:");
        System.out.println("Input: nums = " + Arrays.toString(nums3) + ", k = " + k3);
        System.out.println("Output (Optimized): " + Arrays.toString(maxSlidingWindow(nums3, k3)));
        System.out.println("Output (Brute Force): " + Arrays.toString(maxSlidingWindowBruteForce(nums3, k3)));
    }
} 