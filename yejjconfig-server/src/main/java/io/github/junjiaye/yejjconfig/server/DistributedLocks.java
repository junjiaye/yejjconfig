package io.github.junjiaye.yejjconfig.server;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: yejjconfig
 * @ClassName: DistributedLocks
 * @description:
 * @author: yejj
 * @create: 2024-05-26 21:20
 */
@Component
public class DistributedLocks {

    //注入datasource，手动操作数据库，控制数据库连接、事务等
    @Autowired
    DataSource dataSource;

    Connection connection;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Getter
    private AtomicBoolean locked = new AtomicBoolean(false);

    @PostConstruct
    public void init(){
        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        executor.scheduleWithFixedDelay(this::tryLock,1000,5000, TimeUnit.MILLISECONDS);
    }
    @PreDestroy
    public void close(){
        try {
            if (connection != null && !connection.isClosed()){
                connection.rollback();
                connection.close();
            }
        } catch (Exception e) {
            System.out.println("ignore this close exception");
        }
    }

    public boolean lock() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        connection.createStatement().execute("select app from locks where id = 1 for update");
        if (locked.get()){
            System.out.println("=====>>>> reenter this dist lock(分布式锁)");
        }else {
            System.out.println("=====>>>> get a dist lock(分布式锁).");
        }
        return true;
    }

    //
    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            System.out.println("lock faild ...");
            locked.set(false);
        }
    }
}
