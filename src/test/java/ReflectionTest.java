import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        assertThat(hello.hoho("jj"), is("jj"));

        // 데코레이터 패턴 적용(부가기능 객체HelloUppercase) 적용
        Hello helloDeco = new HelloUpperCase(new HelloTarget());
        assertThat(helloDeco.sayHello("jj"), is("HELLO JJ"));
        assertThat(helloDeco.sayHi("jj"), is("HI JJ"));
        assertThat(helloDeco.sayThankyou("jj"), is("THANK YOU JJ"));
        assertThat(helloDeco.hoho("jj"), is("jj"));
        // BUT, 문제점 2개.
        // 1. 인터페이스의 모든 메소드 구현, 위임하도록 코드 만들어야함.
        // 2. 본 클래스의 부가기능(리턴을 대문자 변환)하는게 모든 메소드에 중복해서 나타남.
        // 그래서 사용하는 다이내믹 프록시
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                UpperCaseHandler.class.getClassLoader(), // 다이내믹 프록시 (런타임에 인스턴스 생성하므로 클래스로더)
                new Class[] {Hello.class}, // 다이내믹 프록시가 구현해야할 인터페이스
                new UpperCaseHandler(new HelloTarget()) // 부가기능(UpperCaseHandler)와 위임공구리클래스(HelloTarget)
        );
        assertThat(proxiedHello.sayHello("jj"), is("HELLO JJ"));
        assertThat(proxiedHello.sayHi("jj"), is("HI JJ"));
        assertThat(proxiedHello.sayThankyou("jj"), is("THANK YOU JJ"));
        assertThat(proxiedHello.hoho("jj"), is(" hoho JJ hoho "));

        // 스프링에 DI 하고싶다면?
        // 다이내믹 프록시 패턴을 스프링에 적용하려면? ./FactoryBeanTest.java 참조할것.
    }

    @Test
    public void proxyFactoryBean(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("jj"), is("HELLO JJ"));
        assertThat(proxiedHello.sayHi("jj"), is("HI JJ"));
        assertThat(proxiedHello.sayThankyou("jj"), is("THANK YOU JJ"));
        assertThat(proxiedHello.hoho("jj"), is("JJ"));
    }

    // 포인트컷 : 부가기능(Advice) 적용 대상 메소드 선정방법(알고리즘)
    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*"); // 메소드이름비교조건.
        // Advisor = pointcut + advice
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("jj"), is("HELLO JJ"));
        assertThat(proxiedHello.sayHi("jj"), is("HI JJ"));
        assertThat(proxiedHello.sayThankyou("jj"), is("Thank you jj")); // pointcut.setMappedName에 맞지 않으므로 타겟 객체 구현대로 돈다.
        assertThat(proxiedHello.hoho("jj"), is("jj")); // pointcut.setMappedName에 맞지 않으므로 타겟 객체 구현대로 돈다.

    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}
interface Hello {
    String sayHello(String name);
    String sayHi(String name);
    String sayThankyou(String name);
    String hoho(String name);
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
    @Override
    public String hoho(String name) {
        return name;
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
    @Override
    public String hoho(String name) {
        return name;
    }
}
// 다이내믹 프록시
class UpperCaseHandler implements InvocationHandler {
    Object target; // 타깃으로 위임하기 위한 타깃 오브젝트 주입 방법
    public UpperCaseHandler(Object target) { // 어떤 오브젝트를 줘도 적용 가능하도록
        // 타깃으로 위임하기 위한 타깃 오브젝트 주입 방법
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 메소드 선택 기준의 알고리즘에 invoke가 의존하고 있음.
        Object ret = method.invoke(target, args);
        if(ret instanceof String && method.getName().startsWith("say") ){
            return ((String) ret).toUpperCase();
        } else if (ret instanceof String && method.getName().contains("hoho")) {
            return " hoho " + ((String) ret).toUpperCase() + " hoho ";
        } else {
          return ret;
        }
    }
}