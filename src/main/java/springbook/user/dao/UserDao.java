package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.ConnectionMaker;
import springbook.user.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private ConnectionMaker connectionMaker;

    private DataSource dataSource;

    public UserDao(){}

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        Connection c = null;
        try {
            c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values (?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            ps.close();
            c.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User get(String id){
        Connection c = null;
        User user = null;

        try {
            c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            // 아래 주석해제 시 : Test/getUserFailure() 성공처리됨.
            if(user == null) throw new EmptyResultDataAccessException(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
    public ArrayList<User> getAll(){
        Connection c = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from users");

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }

            rs.close();
            ps.close();
            c.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void deleteAll() {
        try {
            Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("delete from users");
            ps.executeUpdate();

            ps.close();
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int getCount(){
        Connection c = null;
        try {
            c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("select count(*) as count from users");
            ResultSet rs = ps.executeQuery();

            rs.next();
            int count = rs.getInt("count");

            rs.close();
            ps.close();
            c.close();

            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
