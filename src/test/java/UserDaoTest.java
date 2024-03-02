import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.DaoFactory;
import springbook.user.User;
import springbook.user.dao.UserDao;
import springbook.user.exception.DuplicateUserIdException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao dao;

    // fixture : 테스트에 필요한 정보나 오브젝트
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
//        ApplicationContext context
//                = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = context.getBean("userDao", UserDao.class);

        // 즉석에서 테스트환경 DB 쓰면서 dataSource수정하고 싶으면
//        DataSource dataSource = new SingleConnectionDataSource(
//                "org.mariadb.jdbc.Driver",
//                "jdbc:mariadb://localhost:3306/springbookTestDB",
//                "root",
//                "aidaboat24",
//                true
//        );
//        dao.setDataSource(dataSource);

        this.user1 = new User("1","aa","p123");
        this.user2 = new User("2","bb","p124");
        this.user3 = new User("3","cc","p125");
    }

    @Test
    public void addAndGet() throws SQLException {
//        ApplicationContext context
//                = new ClassPathXmlApplicationContext("applicationContext.xml");
//        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

//        User user1 = new User("1","aa","pp1");
//        User user2 = new User("2","bb","pp2");

        dao.add(user1);
        dao.add(user2);
        dao.add(user3);
        assertThat(dao.getCount(), is(3));
        
        User getuser1 = dao.get(user1.getId());
        assertThat(getuser1.getName(), is(user1.getName()));
        assertThat(getuser1.getPassword(), is(user1.getPassword()));
        
        User getuser2 = dao.get2(user2.getId());
        assertThat(getuser2.getName(), is(user2.getName()));
        assertThat(getuser2.getPassword(), is(user2.getPassword()));
        
        User getuser3 = dao.get2(user3.getId());
        assertThat(getuser3.getName(), is(user3.getName()));
        assertThat(getuser3.getPassword(), is(user3.getPassword()));
    }

    @Test(expected = DuplicateUserIdException.class)
    public void getUserIdDuplicateFailure() throws DuplicateUserIdException, SQLException {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user1);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
//        ApplicationContext context
//                = new ClassPathXmlApplicationContext("applicationContext.xml");
//        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");

    }

    @Test
    public void count() throws SQLException {
//        ApplicationContext context
//                = new ClassPathXmlApplicationContext("applicationContext.xml");
//        UserDao dao = context.getBean("userDao", UserDao.class);

//        User user1 = new User("1","aa","p123");
//        User user2 = new User("2","bb","p124");
//        User user3 = new User("3","cc","p125");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount2(), is(1));

        dao.add(user2);
        assertThat(dao.getCount2(), is(2));

        dao.add(user3);
        assertThat(dao.getCount3(), is(3));
        
    }
    
    @Test public void getAll2() throws SQLException {
        dao.deleteAll();

        List<User> users0 = dao.getAll2();
        assertThat(users0.size(), is(0));
        
        dao.add(user3); // id : 3
        List<User> users3 = dao.getAll2();
        assertThat(users3.size(), is(1));
        checkSameUser(user3, users3.get(0));
        
        dao.add(user2); // id : 2
        List<User> users2 = dao.getAll2();
        assertThat(users2.size(), is(2));
        checkSameUser(user2, users2.get(0));
        checkSameUser(user3, users2.get(1));


        dao.add(user1);
        List<User> users1 = dao.getAll2();
        assertThat(users1.size(), is(3));
        checkSameUser(user3, users1.get(2));
        checkSameUser(user2, users1.get(1));
        checkSameUser(user1, users1.get(0));
    }

    private void checkSameUser(User source, User target) {
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getName(), target.getName());
        assertEquals(source.getPassword(), target.getPassword());
    }
}
