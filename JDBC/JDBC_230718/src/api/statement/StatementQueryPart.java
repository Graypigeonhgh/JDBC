package api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

/**
 * @description: 使用statement查询t_user表下的全部数据
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-18-15:37
 **/
public class StatementQueryPart {
    public static void main(String[] args) throws SQLException {
        //1.注册驱动
        DriverManager.registerDriver(new Driver());

        //2.获取连接
        //java.sql 接口 = 实现类
        /*
         * 参数1: urL
         * jdbc:数据库厂商名://ip地址:主机端口号/数据库名 -> jdbc:mysql://127.0.0.1:3306/atguigu
         * 参数2: username
         * 数据库软件的账号 root
         * 参数3: password
         * 数据库软件的密码 a1234567
         **/
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu","root","a1234567");

        //3.创建statement
        Statement statement = connection.createStatement();

        //4.发送SQL语句，并且获取返回结果
        String sql = "SELECT * FROM t_user;";
        ResultSet resultSet = statement.executeQuery(sql);

        //5.进行结果集解析
        //查看是否有下一行数据，有就获取
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String account = resultSet.getString("account");
            String password = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");
            System.out.println(id + "--" + account + "--"+ password + "--" + nickname);
        }

        //6.关闭资源
        resultSet.close();
        statement.close();
        connection.close();

    }

}
