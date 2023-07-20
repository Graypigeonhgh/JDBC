package transactionNew;

import api.utils.JdbcUtilisV2;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @description: bank表的数据库操作方法存储类
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-20-10:53
 **/
public class BankDao {

    /**
     * 加钱方法
     * @param account 加钱的行数
     * @param money 加钱的金额
     * connection 业务传递的connection和减钱是同一个! 才可以在一个事务中!
     * @return 影响行数
     */
    public void add(String account,int money) throws Exception {

        Connection connection = JdbcUtilisV2.getConnection();

        //3.编写SQL语句结果
        String sql =  "update t_bank set money = money + ? where account = ? ;";

        //4.创建statement
        PreparedStatement statement = connection.prepareStatement(sql);

        //5.占位符赋值

        statement.setObject(1,money);
        statement.setObject(2,account);

        //6.发送SQL语句
        statement.executeUpdate();

        //7.关闭资源
        statement.close();

        System.out.println("加钱成功");
    }

    /**
     * 减钱方法
     * @param account 减钱的行数
     * @param money 减钱的金额
     * connection 业务传递的connection和加钱是同一个! 才可以在一个事务中!
     * @return 影响行数
     */

    public void sub(String account,int money) throws Exception{

        Connection connection = JdbcUtilisV2.getConnection();

        //3.编写SQL语句结果
        String sql =  "update t_bank set money = money - ? where account = ? ;";

        //4.创建statement
        PreparedStatement statement = connection.prepareStatement(sql);

        //5.占位符赋值

        statement.setObject(1,money);
        statement.setObject(2,account);

        //6.发送SQL语句
        statement.executeUpdate();

        //7.关闭资源
        statement.close();

        System.out.println("减钱成功");

    }

}
