import com.sun.org.glassfish.gmbal.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static java.util.Collections.binarySearch;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BinarySearchTest {

    ArrayList<Integer> arr;
    int wantFind = 15252;

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

    /**
     * middle - 1, middle + 1의 의미 ::
     * 1. 찾고자 하는 값(wantFind)이 middle idx 값 과의 비교가 이미 끝났으므로
     *   wantFind < middle 이면 middle - 1
     *   middle < wantFind 이면 middle + 1
     * 2. 찾고자 하는 값(watnFind) 가 없을때, middle +- 1 안주면..
     *   stackoverflow 발생함.
     *   왜? middle은 / 2 해서 얻어지므로 끝인덱스에 완전히 도달할 수 없음
     *   ex) (lIdx)199999 + (hIdx)200000 / 2 = 199999 (199999.5 에서 버림 처리)
     * */
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
