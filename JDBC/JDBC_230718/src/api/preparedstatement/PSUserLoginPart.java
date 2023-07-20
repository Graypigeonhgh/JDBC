package api.preparedstatement;

import java.sql.*;
import java.util.Scanner;

/**
 * @description: 使用预编译的statement完成用户登录
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-19-14:29
 **/
public class PSUserLoginPart {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //1.收集用户账号和密码信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号：");
        String account = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();

        //2.ps的数据库流程
        //2.1注册驱动（反射）
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2.2获取连接（一个参数)
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu?user=root&password = a1234567");

        //2.3编写SQL语句的结果
        String sql = "SELECT * FROM t_user WHERE account = ? AND PASSWORD = ?;";

        /*
        statement:
        1.创建statement
        2.拼接SQL语句
        3.发送SQL 语句，并且获取返回结果
        preparedstatement
        不包含动态值部分的语句，动态值部分使用占位符 ? 替代 注意: ? 只能替代动态值
        1.编写SQL语句结果
        2.创建preparedStatement，并且传入动态值
        3.动态值 占位符 值 ? 单独赋值即可
        4.发送SQL语句即可，并获取返回结果
        */

        //2.4创建preparedStatement
        //TODO 需要传入SQL语句结构
        //TODO 要的是SQL语句结构，动态值的部分使用 ? ,  占位符！
        //TODO ?  不能加 '?'  ? 只能替代值，不能替代关键字和容器名

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //2.5单独的占位符进行赋值
        /**
         *  参数1： index 占位符的下角标（位置），从左到右，从1开始
         *  参数2： object 占位符的值，可以设置任何类型的数据，避免了我们的拼接和类型更加丰富
         */
        preparedStatement.setObject(1,account);
        preparedStatement.setObject(2,password);

        //2.6发送SQL语句，并获取返回结果(DDL)
        ResultSet resultSet = preparedStatement.executeQuery();

        //2.7结果集解析
        if (resultSet.next()) {
            System.out.println("登录成功");
        }else{
            System.out.println("登录失败");
        }

        //2.8关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }
}
