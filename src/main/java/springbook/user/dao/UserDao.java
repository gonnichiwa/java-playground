package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.ConnectionMaker;
import springbook.user.User;
import springbook.user.dao.statement.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private ConnectionMaker connectionMaker;

    private DataSource dataSource;

    private JdbcContext jdbcContext;

    public UserDao(){}

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        // 걍 인터페이스 받아서 바로 구현
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = null;
                String sql = "insert into users values (?,?,?)";
                ps = c.prepareStatement(sql);
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    public User get(String id){
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);

            rs = ps.executeQuery();

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
        } finally {
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(c != null) c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    public ArrayList<User> getAll(){
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select * from users");
            rs = ps.executeQuery();

            while(rs.next()){
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                    ps.close();
                    c.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.workWithStatementStrategy(c -> c.prepareStatement("delete from users"));
    }

    public int getCount(){
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) as count from users");
            rs = ps.executeQuery();

            rs.next();
            int count = rs.getInt("count");

            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                    ps.close();
                    c.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
