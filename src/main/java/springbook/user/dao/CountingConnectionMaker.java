package springbook.user.dao;

import springbook.user.ConnectionMaker;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

    int counter = 0;

    private final ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(){
        this.realConnectionMaker = new DConnectionMaker();
    }

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    @Override
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        this.counter++;
        System.out.println("connection Called : " + this.getCounter());
        return realConnectionMaker.getConnection();
    }

    public int getCounter() {
        return counter;
    }
}
