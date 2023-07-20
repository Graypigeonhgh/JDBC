package api.statement;

import com.mysql.cj.jdbc.Driver;
import netscape.javascript.JSUtil;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @description: 模拟用户登录
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-18-16:46
 **/
public class StatementUserLoginPart {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //1.键盘输入事件，收集账号和密码信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号：");
        String account = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();

        //2.注册驱动

/*
方案1:
DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver())注意:8+ com.mysql.cj.jdbc.Driver
5+ com.mysql.jdbc.Driver
问题: 会注册两次驱动，消耗性能
1.DriverManager.registerDriver() 方法本身会注册一次!
2.Driver.static{ DriverManager.registerDriver() } 静态代码块，也会注册一次!
解决: 只想注册一次驱动
      只触发静态代码块即可! Driver
触发静态代码块:
类加载机制：类加载的时刻，会触发静态代码块!
    加载 [class文件 -> jvm虚拟机的cLass对象]
    连接[验证(检查文件类型) -> 准备(静态变默认值) -> 解析(发静态代码块)]
    初始化(静态属性赋真实值)
触发类加载:
    1.new 关键字
    2.调用静态方法
    3.调用静态属性
    4.接口 18 default默认实现
    5.反射
    6.子类触发父类
    7.程序的入口main
 */

        //方案一：
        //DriverManager.registerDriver(new Driver());

        //方案二：mysql新版本的驱动
        //new Driver();

        //方案三：字符串 ->提取到外部的配置文件 -> 可以在不改变代码的情况下，完成对数据库驱动的切换
        Class.forName("com.mysql.cj.jdbc.Driver");  //触发类加载，触发静态代码块的调用

        //3.获取连接
        /*一.getConnection()方法有三个参数：
         * 参数1: urL
         * jdbc:数据库厂商名://ip地址:port端口号/数据库名 -> jdbc:mysql://127.0.0.1:3306/atguigu
         * 或(jdbc:数据库厂商名://ip地址:port端口号/数据库名?key = value 可选信息)
         * 或简写（jdbc:数据库厂商名:///数据库名），本机信息可省略
         * 参数2: username
         * 数据库软件的账号 root
         * 参数3: password
         * 数据库软件的密码 a1234567
         *
         *二.getConnection()方法有二个参数：
         *String url: 此urL和三个参数的urL的作用一样! 数据ip，端口号，具体的数据库和可选信息存储账号和密码
         * Properties info:
         * Properties类似于 Map 只不过key = value 都是字符串形式的!
         * key user : 账号信息
         * key password:密码信息

         * 三.getConnection()方法有一个参数：
         * 数据库ip，端口号，具体的数据库 可选信息(账号密码)
         * jdbc:数据库软件名://ip:port/数据库?key=value&key=value&key=value
         * jdbc:mysql://Localhost:3306/atguigu?user=root&password=root
         * 携带固定的参数名 user password 传递账号和密码信息!
         **/
        //方式一：三个参数
        Connection connection = DriverManager.
                getConnection("jdbc:mysql:///atguigu","root","a1234567");
        //方式二：两个参数(简写)
//        Properties properties = new Properties();
//        properties.put("user","root");
//        properties.put("password","root");
//        Connection connection1 = DriverManager.getConnection("jdbc:mysql:///atguigu",properties);
        //方式三：一个参数（简写）
//        Connection connection2 = DriverManager.
//                getConnection("jdbc:mysql:///atguigu?user=root&password=a1234567");

        //4.创建statement
        //statement 可以发送SQL语句到数据库，并且获取返回结果！
        Statement statement = connection.createStatement();

        //5。发送查询SQL 语句，并获取返回结果
        String sql = "SELECT * FROM t_user WHERE account = '" + account + "' AND PASSWORD = '"+password + "';";
        ResultSet resultSet = statement.executeQuery(sql);

        //6.进行结果集解析
        //查看是否有下一行数据，有就获取
//        while (resultSet.next()){
//            //指定当前行
//            int id = resultSet.getInt(1);
//            String account1 = resultSet.getString("account");
//            String password1 = resultSet.getString(3);
//            String nickname = resultSet.getString("nickname");
//            System.out.println(id + "--" + account + "--"+ password1 + "--" + nickname);
//        }

        //结果判断,显示登录成功还是失败
        if (resultSet.next()) {
            System.out.println("登录成功");
        }else{
            System.out.println("登录失败");
        }

        //8.关闭资源
        resultSet.close();
        statement.close();
        connection.close();

    }

}
