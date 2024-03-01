package springbook.user;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.dao.UserDao;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException {

//        UserDao dao = new UserDao(); // 이래 쓰지 말고
//        ConnectionMaker connectionMaker = new NConnectionMaker(); // L9, 10과 같이 클라이언트에서 인터페이스 받아 뭐 쓸지 결정
//        UserDao dao = new UserDao(connectionMaker); // UserDao의 생성자 파라미터로 인터페이스를 던진다.
                                                      // 그리고 이런식으로 쓴걸 전략패턴이라고 한다.
        /*
        * 근데, 결국 위 9L 코드도 얼렁뚱땅 NConnectionMaker()에 의존해버린거 아닌가?
        * 클래스 이름이 UserDaoTest인데 이 테스트 클래스는 UserDao가 잘 동작하는지
        * 여부를 테스트 하는 역할일뿐 NConnectionMaker에 의존까지 해야하는 책임은
        * 없도록 하고싶음.
        *
        * 이걸 '팩토리'로 해결해보자
        * '팩토리'는 추상 팩토리 패턴, 팩토리 메소드 패턴의 팩토리와 다름.
        * '팩토리'는 단지 오브젝트 생성하는쪽, 오브젝트 사용하는쪽의 역할과 책임 분리목적
        * 어떻게 만들지 || 어떻게 사용할지 는 분명 다른 관심사임.
        * */
        // 그래서 아래처럼 만들었다.
        // 이럼 DaoFactory가 어떤 ConnectionMaker를 쓸지 책임을 가짐.
//        UserDao dao = new DaoFactory().userDao();

        /*
        * 이제 제어의 역전(IoC)를 얘기할 때가 됐는데,
        * 역전 되기 전의 제어(asis)는 뭐였길래 제어를 역전시킨다는건가?
        * 프로그램의 일반적인 제어 흐름은 메인메소드에 순차적으로 돌아가는 구조 생각해보자
        * 1 사용할 오브젝트 결정
        * -> 2 결정할 오브젝트 생성
        * -> 3 만들어진 오브젝트의 메소드 호출
        * -> 4 메소드안에 사용할 오브젝트 결정....
        * 이런식인데
        *
        * 본 경로 libs/ 에 라이브러리들 추가했으니 스프링 애플리케이션 컨텍스트로 쓰면
        * 아래와 같다.
        * */
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        // add()
        User user = new User();
        user.setId("111");
        user.setName("jjjjjj");
        user.setPassword("passwordddd");
        dao.add(user);
        System.out.println(user.getId() + " 등록 성공!");

        // getAll();
        ArrayList<User> users = dao.getAll();
        for (User u : users) {
            System.out.println(u.getId() + " | " + u.getName());
        }

        // get(id)
        User user2 = dao.get("1");
        System.out.println(user2.getId() + " | "
                + user2.getName() + " | "
                + user2.getPassword());
    }
}