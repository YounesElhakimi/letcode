import java.util.Arrays;

/**
 * Problem: Rotate Image (LeetCode #48)
 * Difficulty: Medium
 * Source: LeetCode
 * 
 * Problem Description:
 * You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).
 * You have to rotate the image in-place, which means you have to modify the input 2D matrix directly.
 * DO NOT allocate another 2D matrix and do the rotation.
 * 
 * Example:
 * Input: matrix = [[1,2,3],
 *                  [4,5,6],
 *                  [7,8,9]]
 * Output:         [[7,4,1],
 *                  [8,5,2],
 *                  [9,6,3]]
 * 
 * Approaches:
 * 1. Transpose + Reverse (Optimal)
 * 2. Four-way swap
 * 
 * Time Complexity: O(n²)
 * Space Complexity: O(1)
 */
public class RotateMatrix_LeetCode_Medium {
    
    /**
     * Optimal solution using transpose and reverse
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     */
    public static void rotate(int[][] matrix) {
        int n = matrix.length;
        
        // Step 1: Transpose the matrix
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n/2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n-1-j];
                matrix[i][n-1-j] = temp;
            }
        }
    }
    
    /**
     * Alternative solution using four-way swaps
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     */
    public static void rotateFourWay(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[i][j];
                
                // Move values from right to top
                matrix[i][j] = matrix[n-1-j][i];
                // Move values from bottom to right
                matrix[n-1-j][i] = matrix[n-1-i][n-1-j];
                // Move values from left to bottom
                matrix[n-1-i][n-1-j] = matrix[j][n-1-i];
                // Move values from top to left
                matrix[j][n-1-i] = temp;
            }
        }
    }
    
    /**
     * Utility method to print matrix
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
    
    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        // Test Case 1: 3x3 matrix
        int[][] matrix1 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        System.out.println("Test Case 1 (3x3 matrix):");
        System.out.println("Original matrix:");
        printMatrix(matrix1);
        rotate(matrix1);
        System.out.println("After rotation:");
        printMatrix(matrix1);
        
        // Test Case 2: 4x4 matrix
        int[][] matrix2 = {
            {5, 1, 9, 11},
            {2, 4, 8, 10},
            {13, 3, 6, 7},
            {15, 14, 12, 16}
        };
        System.out.println("\nTest Case 2 (4x4 matrix):");
        System.out.println("Original matrix:");
        printMatrix(matrix2);
        rotateFourWay(matrix2);
        System.out.println("After rotation (using four-way swap):");
        printMatrix(matrix2);
        
        // Test Case 3: 2x2 matrix
        int[][] matrix3 = {
            {1, 2},
            {3, 4}
        };
        System.out.println("\nTest Case 3 (2x2 matrix):");
        System.out.println("Original matrix:");
        printMatrix(matrix3);
        rotate(matrix3);
        System.out.println("After rotation:");
        printMatrix(matrix3);
    }
} 