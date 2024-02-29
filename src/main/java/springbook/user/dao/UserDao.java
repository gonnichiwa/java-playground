package springbook.user.dao;

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
        User user = new User();

        try {
            c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

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
}
