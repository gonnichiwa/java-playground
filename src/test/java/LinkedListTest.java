import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LinkedListTest {

    LinkedList<Integer> list;

    @Before
    public void setList(){
        list = new LinkedList<>();
        // 1 ~ 7 add
        for(int i = 1; i<=7; i++){
            list.add(i);
        }
    }

    @Test
    public void before수행_후_갯수(){
        int size = list.size();
        assertThat(size, is(7));
    }

    @Test
    public void 중간_인덱스_추가(){
        // 추가전
        System.out.println(list.get(2)); // 3
        System.out.println(list.get(3)); // 4

        list.add(2, 44);
        System.out.println(list.get(2)); // 44
        System.out.println(list.get(3)); // 3
        System.out.println(list.get(4)); // 4

    }
}
