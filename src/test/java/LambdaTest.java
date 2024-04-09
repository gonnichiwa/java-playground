import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LambdaTest {

    @Test
    public void lambdaeqTest() {
        Printer p = new Printer() {
            @Override
            public String print(String str, String str2) {
                return str + str2 + " good";
            }
        };
        assertThat(p.print("pc", "manner"), is("pcmanner good"));

        Printer p2 = (str, str2) -> str + str2 + " good";
        assertThat(p2.print("pc", "manner"), is("pcmanner good"));

        Printer p3 = (str, str2) -> {
            str += "123";
            return str + str2 + " good";
        };
        assertThat(p3.print("pc","manner"),
                is("pc123manner good"));

        Printer p4 = (String s, String r) -> s + r + " good";
        assertThat(p4.print("pc","manner"), is("pcmanner good"));
    }

}

//@FunctionalInterface // 람다식을 적용할 수 있는 인터페이스
interface Printer {
    String print(String str, String str2);
    // @FunctionalInterface 는 추상메소드가 하나여야만 한다.
    // 본 인터페이스로 람다식 구현되어 있다면 메소드 추가할 수 없다.
      // 바로 아래줄 주석 풀면 테스트 코드 컴파일 안됨.
//    void print2();
}