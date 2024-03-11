import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReflectionTest {

    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";

        // length()
        assertThat(name.length(), is(6));

        Method lengthMethod = String.class.getMethod("length");
        assertThat(lengthMethod.invoke(name), is(6));

        // charAt()
        assertThat(name.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat(charAtMethod.invoke(name,0), is('S'));
    }

    @Test
    public void simpleProxy() {
        // 단순 구현
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("jj"), is("Hello jj"));
        assertThat(hello.sayHi("jj"), is("Hi jj"));
        assertThat(hello.sayThankyou("jj"), is("Thank you jj"));

        // 데코레이터 패턴 적용(부가기능 객체HelloUppercase) 적용
        Hello helloDeco = new HelloUpperCase(new HelloTarget());
        assertThat(helloDeco.sayHello("jj"), is("HELLO JJ"));
        assertThat(helloDeco.sayHi("jj"), is("HI JJ"));
        assertThat(helloDeco.sayThankyou("jj"), is("THANK YOU JJ"));
        // BUT, 문제점 2개.
        // 1. 인터페이스의 모든 메소드 구현, 위임하도록 코드 만들어야함.
        // 2. 본 클래스의 부가기능(리턴을 대문자 변환)하는게 모든 메소드에 중복해서 나타남.


    }
}

interface Hello {
    String sayHello(String name);
    String sayHi(String name);
    String sayThankyou(String name);
}
class HelloTarget implements Hello {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
    @Override
    public String sayHi(String name) {
        return "Hi " + name;
    }
    @Override
    public String sayThankyou(String name) {
        return "Thank you " + name;
    }
}
// 데코레이터 패턴의 프록시 (부가기능 부여)
class HelloUpperCase implements Hello {
    Hello hello; // 위임할 타깃 오브젝트 (다른 프록시를 추가할 수도 있으므로 인터페이스로 접근함.
    public HelloUpperCase(Hello hello) {
        this.hello = hello;
    }
    @Override
    public String sayHello(String name) {
        return hello.sayHello(name).toUpperCase(); // 위임과 부가기능
    }
    @Override
    public String sayHi(String name) {
        return hello.sayHi(name).toUpperCase();
    }
    @Override
    public String sayThankyou(String name) {
        return hello.sayThankyou(name).toUpperCase();
    }
}
