import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BinaryTreeTest {

    IBinaryTree bt;

    @Before
    public void setTree(){
        /*
         * 아래 트리를 구현
         * */
        this.bt = new BinaryTree();
        /*
        *       6
        *    4     8
        *  3  5   7  9
        * */
        List<Integer> nums = Arrays.asList(6,4,8,3,5,7,9);
        for(int a : nums){
            bt.add(a);
        }
    }
    @Test
    public void linked(){
        assertThat(bt.isContainsNode(9), is(true));
        assertThat(bt.isContainsNode(7), is(true));
        assertThat(bt.isContainsNode(5), is(true));
        assertThat(bt.isContainsNode(3), is(true));
        assertThat(bt.isContainsNode(8), is(true));
        assertThat(bt.isContainsNode(4), is(true));
        assertThat(bt.isContainsNode(6), is(true));

        assertThat(bt.isContainsNode(12), is(false));
    }

}
interface IBinaryTree {
    void add(int value);
    boolean isContainsNode(int value);

}
class BinaryTree implements IBinaryTree {
    Node root;

    @Override
    public void add(int value){
        root = addRecursive(root, value);
    }

    @Override
    public boolean isContainsNode(int value) {
        return containsRecursive(root, value);
    }

    private boolean containsRecursive(Node current, int value) {
        if(current == null) return false;
        if(current.value == value) return true;
        return value < current.value ?
                containsRecursive(current.left, value)
                : containsRecursive(current.right, value);
    }

    private Node addRecursive(Node current, int value){
        if(current == null) {
            return new Node(value);
        }

        if(value < current.value ) {
            current.left = addRecursive(current.left, value);
        } else if (value > current.value) {
            current.right = addRecursive(current.right, value);
        } else {
            return current;
        }

        return current;
    }
}
class Node {
    Node left; // 왼쪽 노드 주소 (상위보다 작은값)
    Node right; // 오른쪽 노드 주소 (상위보다 큰값)
    int value;

    public Node(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

}
