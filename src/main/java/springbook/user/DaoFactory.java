package springbook.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
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
        dao.setDataSource(dataSource());
        return dao;
    }

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mariadb://localhost:3306/springbook");
        dataSource.setUsername("root");
        dataSource.setPassword("aidaboat24");
        return dataSource;
    }


    private ConnectionMaker getConnectionMaker(){
        return new CountingConnectionMaker(new DConnectionMaker());
    }
}
