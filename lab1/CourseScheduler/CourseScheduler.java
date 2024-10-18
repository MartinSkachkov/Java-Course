mport java.util.Arrays;
 
public class CourseScheduler {
 
    public static void mergeSort(int[][] courses, int left, int right) {
        if(left < right) {
            int middle = (left + right) / 2;
 
            mergeSort(courses, left, middle);
            mergeSort(courses, middle + 1, right);
 
            merge(courses, left, middle, right);
        }
    }
 
    public static void merge(int[][] courses, int left, int middle, int right) {
        int leftArrLen = middle - left + 1;
        int rightArrLen = right - middle;
 
        int[][] leftArray = new int[leftArrLen][2];
        int[][] rightArray = new int[rightArrLen][2];
 
        for (int i = 0; i < leftArrLen; i++) {
            leftArray[i] = courses[left + i];
        }
        for (int j = 0; j < rightArrLen; j++) {
            rightArray[j] = courses[middle + j + 1];
        }
 
        int i = 0, j = 0, k = left;
        while (i < leftArrLen && j < rightArrLen) {
            if (leftArray[i][1] <= rightArray[j][1]) {
                courses[k] = leftArray[i];
                i++;
            } else {
                courses[k] = rightArray[j];
                j++;
            }
            k++;
        }
 
        while (i < leftArrLen) {
            courses[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < rightArrLen) {
            courses[k] = rightArray[j];
            j++;
            k++;
        }
    }
 
    public static int maxNonOverlappingCourses(int[][] courses) {
        mergeSort(courses, 0, courses.length - 1);
 
        int end = 0;
        int counter = 0;
 
        for (int[] course : courses) {
            int courseStart = course[0];
            int courseEnd = course[1];
 
            if (end <= courseStart) {
                end = courseEnd;
                counter++;
            }
        }
 
        return counter;
    }
 
    public static void main(String[] args) {
        System.out.println(maxNonOverlappingCourses(new int[][]{{9, 11}, {10, 12}, {11, 13}, {15, 16}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{13, 15}, {13, 17}, {11, 17}}));
    }
}