/**
 * Problem: Search in Rotated Sorted Array (LeetCode #33)
 * Difficulty: Medium
 * Source: LeetCode
 * 
 * Problem Description:
 * There is an integer array nums sorted in ascending order (with distinct values).
 * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length).
 * For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
 * Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.
 * You must write an algorithm with O(log n) runtime complexity.
 * 
 * Example:
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 * 
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 */
public class SearchRotatedArray_LeetCode_Medium {
    
    /**
     * Binary search solution for rotated array
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public static int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // Check if left half is sorted
            if (nums[left] <= nums[mid]) {
                // Check if target is in left half
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // Right half is sorted
            else {
                // Check if target is in right half
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Alternative solution that first finds pivot then performs regular binary search
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public static int searchWithPivot(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        // Find pivot index (smallest element)
        int pivot = findPivot(nums);
        
        // If array is not rotated
        if (pivot == 0) {
            return binarySearch(nums, 0, nums.length - 1, target);
        }
        
        // If target is greater than or equal to first element,
        // search in first half
        if (target >= nums[0]) {
            return binarySearch(nums, 0, pivot - 1, target);
        }
        
        // Otherwise, search in second half
        return binarySearch(nums, pivot, nums.length - 1, target);
    }
    
    /**
     * Helper method to find pivot (index of minimum element)
     */
    private static int findPivot(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    /**
     * Standard binary search implementation
     */
    private static int binarySearch(int[] nums, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
    
    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        // Test Case 1: Standard rotated array
        int[] nums1 = {4, 5, 6, 7, 0, 1, 2};
        int target1 = 0;
        System.out.println("Test Case 1:");
        System.out.println("Array: " + java.util.Arrays.toString(nums1) + ", Target: " + target1);
        System.out.println("Result (Direct): " + search(nums1, target1));
        System.out.println("Result (With Pivot): " + searchWithPivot(nums1, target1));
        
        // Test Case 2: Not rotated array
        int[] nums2 = {1, 2, 3, 4, 5};
        int target2 = 3;
        System.out.println("\nTest Case 2:");
        System.out.println("Array: " + java.util.Arrays.toString(nums2) + ", Target: " + target2);
        System.out.println("Result (Direct): " + search(nums2, target2));
        System.out.println("Result (With Pivot): " + searchWithPivot(nums2, target2));
        
        // Test Case 3: Target not found
        int[] nums3 = {4, 5, 6, 7, 0, 1, 2};
        int target3 = 3;
        System.out.println("\nTest Case 3:");
        System.out.println("Array: " + java.util.Arrays.toString(nums3) + ", Target: " + target3);
        System.out.println("Result (Direct): " + search(nums3, target3));
        System.out.println("Result (With Pivot): " + searchWithPivot(nums3, target3));
        
        // Test Case 4: Single element array
        int[] nums4 = {1};
        int target4 = 1;
        System.out.println("\nTest Case 4:");
        System.out.println("Array: " + java.util.Arrays.toString(nums4) + ", Target: " + target4);
        System.out.println("Result (Direct): " + search(nums4, target4));
        System.out.println("Result (With Pivot): " + searchWithPivot(nums4, target4));
    }
} 