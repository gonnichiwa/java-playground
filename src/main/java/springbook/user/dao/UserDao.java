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

//    public UserDao() {
//        connectionMaker = new NConnectionMaker(); // 이것 조차도 의존이다
//        // UserDao사용하고 싶은 아이는 클라이언트(여기서는 Userdao사용하는 main())다.
//        // 클라이언트가 UserDao쓸때, NConnectionMaker쓸지 DConnectionMaker쓸지 정해라.
//        // 어떻게?
//    }

    // 요렇게
    public UserDao(){}

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws SQLException {
        Connection c = null;
        try {
            c = connectionMaker.getConnection();
            PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values (?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            ps.close();
            c.close();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User get(String id){
        Connection c = null;
        User user = new User();

        try {
            c = connectionMaker.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
    public ArrayList<User> getAll(){
        Connection c = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            c = connectionMaker.getConnection();
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


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
