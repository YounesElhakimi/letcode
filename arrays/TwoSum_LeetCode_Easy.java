import java.util.HashMap;
import java.util.Arrays;

/**
 * Problem: Two Sum (LeetCode #1)
 * Difficulty: Easy
 * Source: LeetCode
 * 
 * Problem Description:
 * Given an array of integers nums and an integer target, return indices of the two numbers 
 * in the array such that they add up to target. You may assume that each input would have 
 * exactly one solution, and you may not use the same element twice.
 * 
 * Example:
 * Input: nums = [2,7,11,15], target = 9
 * Output: [0,1]
 * Explanation: Because nums[0] + nums[1] == 9, we return [0, 1]
 * 
 * Time Complexity Analysis:
 * - Brute Force Approach: O(n²)
 * - Optimized Approach (HashMap): O(n)
 * 
 * Space Complexity Analysis:
 * - Brute Force Approach: O(1)
 * - Optimized Approach (HashMap): O(n)
 */
public class TwoSum_LeetCode_Easy {
    
    /**
     * Optimized solution using HashMap
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static int[] twoSumOptimized(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        
        return new int[] {}; // No solution found
    }
    
    /**
     * Brute force solution
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     */
    public static int[] twoSumBruteForce(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] { i, j };
                }
            }
        }
        
        return new int[] {}; // No solution found
    }
    
    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        // Test Case 1: Basic case
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        System.out.println("Test Case 1:");
        System.out.println("Input: nums = " + Arrays.toString(nums1) + ", target = " + target1);
        System.out.println("Output (Optimized): " + Arrays.toString(twoSumOptimized(nums1, target1)));
        System.out.println("Output (Brute Force): " + Arrays.toString(twoSumBruteForce(nums1, target1)));
        
        // Test Case 2: Numbers can be used only once
        int[] nums2 = {3, 3};
        int target2 = 6;
        System.out.println("\nTest Case 2:");
        System.out.println("Input: nums = " + Arrays.toString(nums2) + ", target = " + target2);
        System.out.println("Output (Optimized): " + Arrays.toString(twoSumOptimized(nums2, target2)));
        System.out.println("Output (Brute Force): " + Arrays.toString(twoSumBruteForce(nums2, target2)));
        
        // Test Case 3: Negative numbers
        int[] nums3 = {-1, -2, -3, -4, -5};
        int target3 = -8;
        System.out.println("\nTest Case 3:");
        System.out.println("Input: nums = " + Arrays.toString(nums3) + ", target = " + target3);
        System.out.println("Output (Optimized): " + Arrays.toString(twoSumOptimized(nums3, target3)));
        System.out.println("Output (Brute Force): " + Arrays.toString(twoSumBruteForce(nums3, target3)));
    }
} 