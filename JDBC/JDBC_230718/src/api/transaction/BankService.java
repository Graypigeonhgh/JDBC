package api.transaction;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @description: 银行卡业务方法，调用dao方法
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-20-11:01
 **/

/*
数据库事务就是一种SQL语句执行的缓存机制,不会单条执行完毕就更新数据库数据,最终根据缓
存内的多条语句执行结果统一判定!一个事务内所有语句都成功及事务成功,我们可以触发Comnt提交事务来结束事务，
更新数据一个事务内任意一条语句失败,及事务失败我们可以触发rollback回滚结束事务数据回到事务之前状态!

// 事务类型
自动提交 : 每条语句自动存储一个事务中,执行成功自动提交,执行失败自动回滚!(HySQL)
手动提交: 手动开启事务，添加语句,手动提交或者手动回滚即可!
 */

public class BankService {

    @Test
    public void start() throws  Exception{

        transfer("jopsoda","asdfdfa",500);

    }

    /**
    事务添加是在业务方法中!
    利用try catch代码块，开启事务和提交事务，和事务回滚!
    将connection传入dao层即可! dao只负责使用，不要cLose()
     */

    public void transfer(String addAccount,String subAccount,int money) throws Exception {

        BankDao bankDao = new BankDao();

        //一个事务的最基本要求，必须是同一个连接对象 connection
        //一个转账方法，属于一个事务（加钱，减钱），事务的开启应在业务中开启

        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql:///atguigu?user=root&password=a1234567");
        //3.开启事务
        try {
            //关闭事务自动提交
            connection.setAutoCommit(false);

            //执行数据库动作,并传入连接对象connection（使它们使用的是同一个connection）
            bankDao.add(addAccount,money,connection);
            System.out.println("-------------------");
            bankDao.sub(subAccount,money,connection);

            //事务手动提交
            connection.commit();

        } catch (Exception e) {
            //事务回滚
            connection.rollback();
            //抛出异常
            throw e;
        }finally {
            connection.close();
        }

    }

}
