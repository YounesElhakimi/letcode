import java.util.Arrays;

/**
 * Problem: Maximum Subarray (LeetCode #53)
 * Difficulty: Medium
 * Source: LeetCode
 * 
 * Problem Description:
 * Given an integer array nums, find the contiguous subarray (containing at least one number) 
 * which has the largest sum and return its sum.
 * 
 * Example:
 * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
 * Output: 6
 * Explanation: [4,-1,2,1] has the largest sum = 6
 * 
 * Approaches:
 * 1. Kadane's Algorithm (Optimal)
 * 2. Divide and Conquer
 * 3. Dynamic Programming
 * 
 * Time Complexity Analysis:
 * - Kadane's Algorithm: O(n)
 * - Divide and Conquer: O(n log n)
 * - Dynamic Programming: O(n)
 * 
 * Space Complexity Analysis:
 * - Kadane's Algorithm: O(1)
 * - Divide and Conquer: O(log n) due to recursion stack
 * - Dynamic Programming: O(1)
 */
public class MaximumSubarray_LeetCode_Medium {
    
    /**
     * Kadane's Algorithm solution
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static int maxSubArrayKadane(int[] nums) {
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
    
    /**
     * Divide and Conquer solution
     * Time Complexity: O(n log n)
     * Space Complexity: O(log n)
     */
    public static int maxSubArrayDivideConquer(int[] nums) {
        return maxSubArrayDivideConquerHelper(nums, 0, nums.length - 1);
    }
    
    private static int maxSubArrayDivideConquerHelper(int[] nums, int left, int right) {
        if (left == right) {
            return nums[left];
        }
        
        int mid = (left + right) / 2;
        
        // Find maximum sum crossing the midpoint
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftSum = Math.max(leftSum, sum);
        }
        
        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightSum = Math.max(rightSum, sum);
        }
        
        // Return maximum of:
        // 1. Maximum subarray sum in left half
        // 2. Maximum subarray sum in right half
        // 3. Maximum sum crossing midpoint
        return Math.max(
            Math.max(
                maxSubArrayDivideConquerHelper(nums, left, mid),
                maxSubArrayDivideConquerHelper(nums, mid + 1, right)
            ),
            leftSum + rightSum
        );
    }
    
    /**
     * Dynamic Programming solution
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static int maxSubArrayDP(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
    
    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        // Test Case 1: Mixed positive and negative numbers
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("Test Case 1:");
        System.out.println("Input: nums = " + Arrays.toString(nums1));
        System.out.println("Output (Kadane's): " + maxSubArrayKadane(nums1));
        System.out.println("Output (Divide & Conquer): " + maxSubArrayDivideConquer(nums1));
        System.out.println("Output (Dynamic Programming): " + maxSubArrayDP(nums1));
        
        // Test Case 2: All negative numbers
        int[] nums2 = {-2, -3, -1, -5};
        System.out.println("\nTest Case 2:");
        System.out.println("Input: nums = " + Arrays.toString(nums2));
        System.out.println("Output (Kadane's): " + maxSubArrayKadane(nums2));
        System.out.println("Output (Divide & Conquer): " + maxSubArrayDivideConquer(nums2));
        System.out.println("Output (Dynamic Programming): " + maxSubArrayDP(nums2));
        
        // Test Case 3: All positive numbers
        int[] nums3 = {1, 2, 3, 4};
        System.out.println("\nTest Case 3:");
        System.out.println("Input: nums = " + Arrays.toString(nums3));
        System.out.println("Output (Kadane's): " + maxSubArrayKadane(nums3));
        System.out.println("Output (Divide & Conquer): " + maxSubArrayDivideConquer(nums3));
        System.out.println("Output (Dynamic Programming): " + maxSubArrayDP(nums3));
    }
} 