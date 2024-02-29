package springbook.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.user.dao.CountingConnectionMaker;
import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.UserDao;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    public DaoFactory() {}

    @Bean
    public UserDao userDao() {
        ConnectionMaker connectionMaker = getConnectionMaker();
        UserDao dao = new UserDao();
        dao.setConnectionMaker(connectionMaker);
        return dao;
    }



    private ConnectionMaker getConnectionMaker(){
        return new CountingConnectionMaker(new DConnectionMaker());
    }
}
