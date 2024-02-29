import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.User;
import springbook.user.dao.UserDao;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context
                = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();;
        assertThat(dao.getCount(), is(0));

        User user1 = new User("1","aa","pp1");
        User user2 = new User("2","bb","pp2");

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
        ApplicationContext context
                = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");

    }

    @Test
    public void count() throws SQLException {
        ApplicationContext context
                = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("1","aa","p123");
        User user2 = new User("2","bb","p124");
        User user3 = new User("3","cc","p125");

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
