package springbook.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.user.dao.*;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    public DaoFactory() {}

    @Bean
    public UserDao userDao() {
        ConnectionMaker connectionMaker = getConnectionMaker();
        UserDao dao = new UserDao();
        dao.setConnectionMaker(connectionMaker);
        dao.setJdbcContext(new JdbcContext(dataSource()));
        dao.setDataSource(dataSource());
        dao.setJdbcTemplate(new JdbcTemplate(dataSource()));
        return dao;
    }

    // 4장에서 bean class 변경할 경우
//    @Bean
//    public IUserDao userDao(){
//        ConnectionMaker connectionMaker = getConnectionMaker();
//        IUserDaoJdbc dao = new IUserDaoJdbc();
//        dao.setConnectionMaker(connectionMaker);
//        dao.setJdbcContext(new JdbcContext(dataSource()));
//        dao.setDataSource(dataSource());
//        dao.setJdbcTemplate(new JdbcTemplate(dataSource()));
//        return dao;
//    }

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
