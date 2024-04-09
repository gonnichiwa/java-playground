import com.sun.org.glassfish.gmbal.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static java.util.Collections.binarySearch;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BinarySearchTest {

    ArrayList<Integer> arr;
    int wantFind = 143534;

    @Before
    public void insertValues(){
        arr = new ArrayList<>();
        for(int i = 0; i <= 200000; i++){
            arr.add(i);
        }
    }

    @Test
    @Description("0부터 끝까지 찾기")
    public void findFrom0() {
        boolean finded = false;
        for(Integer i: arr){
            if(i==wantFind){
                System.out.println("searched!");
                finded = true;
            }
        }
        assertThat(finded, is(true));
    }

    @Test
    public void binSearchTest() {
        boolean finded = binSearch(arr, wantFind, 0, arr.size()-1);
        assertThat(finded, is(true));
    }

    @Test
    public void binSearchTest2() {
        int finded = binarySearch(arr, (Integer) wantFind);
        assertThat(finded, is(wantFind));
    }

    private boolean binSearch(ArrayList<Integer> arr, int wantFind, int lIdx, int hIdx) {
        if(lIdx <= hIdx) {
            int middle = (lIdx + hIdx) / 2;
            if (wantFind < arr.get(middle)) {
                return binSearch(arr, wantFind, lIdx, middle-1);
            } else if (arr.get(middle) < wantFind){
                return binSearch(arr, wantFind, middle+1, hIdx);
            } else {
                return true;
            }
        }
        return false;
    }

    @Test
    public void binSearchTestBin() {
        boolean finded = search(arr, wantFind, 0, arr.size()-1);
        assertThat(finded, is(true));
    }

    private boolean search(ArrayList<Integer> sorted, int value,
                           int leftIndex, int rightIndex) {

        // 1. index check
        if (leftIndex > rightIndex) {
            return false;
        }

        // 2. middle index
        int middle = (rightIndex + leftIndex) / 2;

        // 3. recursive invoke
        if (sorted.get(middle) > value) {
            return search(sorted, value, leftIndex, middle - 1);
        } else if (sorted.get(middle) < value) {
            return search(sorted, value, middle + 1, rightIndex);
        } else {
            return true;
        }
    }


}
