import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BinaryTreeTest {

    IBinaryTree bt;

    @Before
    public void setTree(){
        /*
         * 아래 트리를 구현
         *         6
         *    4         8
         *  3  5     7    9
         *                  11
         *                10  15
         * 이진트리 원칙 1 : 왼쪽자식노드 < 부모노드값 < 오른쪽자식노드
         * 이후 추가되는 값에 따라 노드 위치 정해짐
         * 삭제시 : 아래 deleteRecursive() 참조
         * */
        this.bt = new BinaryTree();
        List<Integer> nums = Arrays.asList(6,4,8,3,5,7,9,11,15,10);
        for(int a : nums){
            bt.add(a);
        }
    }
    @Test
    public void isContains(){
        assertThat(bt.isContainsNode(9), is(true));
        assertThat(bt.isContainsNode(7), is(true));
        assertThat(bt.isContainsNode(5), is(true));
        assertThat(bt.isContainsNode(3), is(true));
        assertThat(bt.isContainsNode(11), is(true));
        assertThat(bt.isContainsNode(15), is(true));
        assertThat(bt.isContainsNode(8), is(true));
        assertThat(bt.isContainsNode(4), is(true));
        assertThat(bt.isContainsNode(6), is(true));

        assertThat(bt.isContainsNode(12), is(false));
    }

    @Test
    public void deleteTest(){
        assertTrue(bt.isContainsNode(11));
        bt.delete(11);
        assertFalse(bt.isContainsNode(11));
    }

    @Test
    public void traverseInOrderTest(){
        bt.traverseInOrder(bt.getRootNode());
    }

    @Test
    public void traversePreOrderTest(){
        bt.traversePreOrder(bt.getRootNode());
    }

    @Test
    public void traversePostOrderTest(){
        bt.traversePostOrder(bt.getRootNode());
    }

    @Test
    public void traverseLevelOrderTest(){
        bt.traverseLevelOrder(bt.getRootNode());
    }

}
interface IBinaryTree {
    void add(int value);
    boolean isContainsNode(int value);
    void delete(int value);
    void traverseInOrder(Node node);
    void traversePreOrder(Node node);
    Node getRootNode();
    void traversePostOrder(Node rootNode);
    void traverseLevelOrder(Node rootNode);
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

    @Override
    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    @Override
    public void traverseInOrder(Node node) {
        if(node != null){
            traverseInOrder(node.left);
            System.out.print(" " + node.value);
            traverseInOrder(node.right);
        }
    }

    @Override
    public void traversePreOrder(Node node) {
        if(node != null){
            System.out.print(" " + node.value);
            traversePreOrder(node.left);
            traversePreOrder(node.right);
        }
    }

    @Override
    public void traversePostOrder(Node node) {
        if(node != null){
            traversePostOrder(node.left);
            traversePostOrder(node.right);
            System.out.print(" " + node.value);
        }
    }

    @Override
    public void traverseLevelOrder(Node current) {
        if(current != null){
            Queue<Node> q = new LinkedList<>();
            q.add(current);
            while(!q.isEmpty()){
                Node n = q.remove();
                System.out.print(" " + n.value);

                if(n.left != null){
                    q.add(n.left);
                }

                if(n.right != null){
                    q.add(n.right);
                }
            }
        }
    }

    @Override
    public Node getRootNode() {
        return root;
    }

    private Node deleteRecursive(Node current, int value) {
        if(current == null) return null;

        if(current.value == value){
            // node has no child
            if(current.left == null && current.right == null){
                return null;
            }
            // node has one child
            if(current.right == null) return current.left;
            if(current.left == null) return current.right;

            // node has 2 child
            int smallestValue = findSmallestValue(current.right, value);
            current.value = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue);
            return current;
        }

        if(value < current.value){
            return deleteRecursive(current.left, value);
        }
        current.right = deleteRecursive(current.right, value);
        return current;

    }

    private int findSmallestValue(Node current, int value) {
        return current.left == null ? current.value : findSmallestValue(current.right, value);
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
