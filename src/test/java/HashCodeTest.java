import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HashCodeTest {
    @Test
    public void hashCodeTest() {
        String a = "Z@S.ME";
        String b = "Z@RN.E";
        assertThat(a.hashCode() == b.hashCode(), is(true));
    }
    @Test
    public void hashCodeOverride() {
        String abc = "abc";
        String abc2 = new String("abc");
        assertThat(abc.equals(abc2), is(true));

        Person a = new Person("abc");
        Person b = new Person("abc");

        assertThat(a.equals(b), is(true)); // (동등성 비교) 이름이 같을때 같게 주고 싶다면? equals 오버라이드
        assertThat(a.hashCode(), is(b.hashCode())); // (동일성 비교) 이름이 같을 때 같게 주고 싶다면? hashCode 오버라이드
    }
    @Test
    public void hashMapPut(){
        HashMap<Person, Integer> map = new HashMap<>();
        Person p1 = new Person("abc");
        Person p2 = new Person("abc");
        Person p3 = new Person("abcd");
        map.put(p1, 10);
        map.put(p2, 20);
        map.put(p3, 30);

        System.out.println(map.size());
        System.out.println(map.get(p2));
    }
}
class Person {
    String name;
    public Person(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object obj) {
        Person anotherPerson = (Person) obj;
        return this.name.equals(anotherPerson.name);
    }
    @Override
    public int hashCode() {
        int hashCode = 0;
        // String의 문자열 같으면 hashCode도 같음을 이용하여 객체 비교간 '같음'의 기준을 정의함.
        return 31 * hashCode + (this.name.isEmpty() ? 0 : name.hashCode());
    }
}