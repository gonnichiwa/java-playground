import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BubbleSortTest {
    @Test
    public void sortTestMain() {
        int[] arr = {2,5,4,3,1,3};
        System.out.println(Arrays.toString(arr));
        bubbleSort(arr, arr.length);
        System.out.println(Arrays.toString(arr));
        assertThat(arr, is(new int[]{1, 2, 3, 3, 4, 5}));
    }
    public int[] bubbleSort (int[] arr, int last){
        if(last > 0) {
            for (int i = 1; i < last; i++){
                if(arr[i] < arr[i-1] ){
                    arr = swap(arr, i, i-1);
                }
            }
            arr = bubbleSort(arr, last - 1);
        }
        return arr;
    }
    public int[] swap (int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
        return arr;
    }
}
