import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PredicateTest {

    @Test
    public void predicate(){
        // Creating predicate
        Predicate<Integer> lesserthan = i -> (i < 18);

        // Calling Predicate method
        System.out.println(lesserthan.test(18)); // false
        System.out.println(lesserthan.test(16)); // true

    }

    @Test
    public void predicateChaining() {
        // conditions..
        Predicate<Integer> greaterThanTen = (i) -> i > 10;
        Predicate<Integer> lowerThanTwenty = (i) -> i < 20;

        // Calling Predicate method
        // get result and conditions
        boolean result = greaterThanTen.and(lowerThanTwenty).test(15);
        System.out.println(result); // true
        boolean result2 = greaterThanTen.and(lowerThanTwenty).negate().test(15);
        System.out.println(result2); // false (negate)
        boolean result2_1 = greaterThanTen.and(lowerThanTwenty).negate().test(21);
        System.out.println(result2_1); // true

        // 한글 입력 여부 검사
        Predicate<String> isHangulIncluded = str -> {
            return str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
        };
        boolean result3 = isHangulIncluded.test("nameabc안");
        System.out.println(result3); // true
        boolean result3_1 = isHangulIncluded.negate().test("nameabc안");
        System.out.println(result3_1); // false
    }

    @Test(expected = IllegalArgumentException.class)
    public void existNumber(){
        pred(3, i -> i < 7); // pass
        pred(15, i -> i < 7); // IllegalArgumentException
    }

    private void pred(int number, Predicate<Integer> pred){
        if(pred.test(number)){
            System.out.println("Number : " + number);
        } else {
            throw new IllegalArgumentException("not matched number condition");
        }
    }

    @Test
    public void predicateCollection(){
        // User의 role이 ADMIN인 것들..

        // when
        List<User> users = new ArrayList<>();
        users.add(new User("a","ADMIN"));
        users.add(new User("b","USER"));
        users.add(new User("c","USER"));

        // then
        List<User> admins = filtering(users, (User u) -> u.getRole().equals("ADMIN"));

        assertThat(admins.size(), is(1));

        List<User> us = users.stream()
                .filter((User u) -> u.getRole().equals("USER"))
                .collect(Collectors.toList());
        assertThat(us.size(), is(2));
    }

    private List<User> filtering(List<User> users, Predicate<User> pred){
        List<User> l = new ArrayList<>();
        for(User u : users){
            if(pred.test(u)){
                l.add(u);
            }
        }
        return l;
    }
}

class User{
    String name;
    String role;

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
