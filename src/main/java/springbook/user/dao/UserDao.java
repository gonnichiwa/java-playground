package springbook.user.dao;

import org.mariadb.jdbc.internal.util.exceptions.MariaDbSqlException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.ConnectionMaker;
import springbook.user.User;
import springbook.user.exception.ApplicationRuntimeException;
import springbook.user.exception.DuplicateUserIdException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private ConnectionMaker connectionMaker;

    private DataSource dataSource;

    private JdbcContext jdbcContext;

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

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

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user) throws DuplicateUserIdException {
        try {
            this.jdbcTemplate.update("insert into users values (?,?,?)",
                    user.getId(),
                    user.getName(),
                    user.getPassword());
        }
        catch (Exception e) {
            if(e instanceof DataIntegrityViolationException) {
                if (e.getMessage().contains("PRIMARY"))
                    throw new DuplicateUserIdException(e);
            } else {
                throw new ApplicationRuntimeException(e);
            }
        }
    }

    public void deleteAll() {
//        this.jdbcContext.executeSql("delete from users");
        this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("delete from users");
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

    public User get2(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{ id },
                this.userMapper);
    }
    public List<User> getAll2() {
        return this.jdbcTemplate.query("select * from users order by id asc", this.userMapper);
    }

    public int getCount2(){
        return this.jdbcTemplate.query(
                c -> c.prepareStatement("select count(*) as count from users"),
                rs -> {
                    rs.next();
                    return rs.getInt(1);
                });
    }

    public int getCount3(){
        return this.jdbcTemplate.queryForInt("select count(*) as count from users");
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
