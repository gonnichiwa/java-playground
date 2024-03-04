import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import springbook.user.Level;
import springbook.user.User;
import springbook.user.dao.IUserDao;
import springbook.user.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class UserServiceTest {
    List<User> users;

    IUserDao userDao;

    UserService userService;

    @Before public void SetUp() {
        ApplicationContext context
                = new ClassPathXmlApplicationContext("applicationContext.xml");
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class); // DaoFactory.userDao() 생성자로 IUserDao 리턴하는걸로 바꿔줘야함.
        this.userDao = context.getBean("userDao", IUserDao.class);
        this.userService = context.getBean("userService", UserService.class);

        this.users = Arrays.asList(
                new User("1","n1","p1", Level.BASIC,49,0),
                new User("2","n2","p2", Level.BASIC,50,0),
                new User("3","n3","p3", Level.SILVER,60,29),
                new User("4","n4","p4", Level.SILVER,60,30),
                new User("5","n5","p5", Level.GOLD,100,100)
        );
    }

    @Test public void upgradeLevels(){
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        users = userDao.getAll2();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        assertEquals(user.getLevel(), level);
    }

    // 신규 가입 사용자의 레벨은 BASIC으로 한다면?
    // ++ 특별한 이유로 미리 레벨을 정해서 신규 생성해야한다면? (ex 새로운 관리자)
    @Test public void add(){
        userDao.deleteAll();

        // given
        User userWithLevel = users.get(4); // GOLD 레벨인 유저, 등록 후에도 GOLD 유지되는지 확인
        User userWITHOUTLevel = users.get(0); // newbie
        userWITHOUTLevel.setLevel(null); // 등록중 BASIC이 설정되어야 함.

        // when
        userService.add(userWithLevel);
        userService.add(userWITHOUTLevel);

        // then
        User userWithLevelRead = userDao.get2(userWithLevel.getId());
        User userWITHOUTLevelRead = userDao.get2(userWITHOUTLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevelRead.getLevel()));
        assertThat(userWITHOUTLevelRead.getLevel(), is(userWITHOUTLevelRead.getLevel()));

    }


}