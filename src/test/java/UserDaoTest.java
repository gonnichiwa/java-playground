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

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DirtiesContext
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
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = context.getBean("userDao", UserDao.class);

        DataSource dataSource = new SingleConnectionDataSource(
                "org.mariadb.jdbc.Driver",
                "jdbc:mariadb://localhost:3306/springbookTestDB",
                "root",
                "aidaboat24",
                true
        );
        dao.setDataSource(dataSource);

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
        assertThat(dao.getCount(), is(2));
        
        User getuser1 = dao.get(user1.getId());
        assertThat(getuser1.getName(), is(user1.getName()));
        assertThat(getuser1.getPassword(), is(user1.getPassword()));
        
        User getuser2 = dao.get(user2.getId());
        assertThat(getuser2.getName(), is(user2.getName()));
        assertThat(getuser2.getPassword(), is(user2.getPassword()));
        
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() {
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
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
        
    }
}
