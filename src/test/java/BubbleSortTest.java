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

    private void bubbleSort(int[] arr, int last) {
        if(last > 0){
            for(int i = 1; i < last; i++){
                if(arr[i-1] > arr[i]){
                    arr = swap(arr, i-1, i);
                }
            }
            bubbleSort(arr, --last);
        }
    }

    private int[] swap(int[] arr, int l, int r){
        int temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;
        return arr;
    }


}
