package springbook.user.dao;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.user.ConnectionMaker;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/springbook?user=root&password=aidaboat24");
    }
}
