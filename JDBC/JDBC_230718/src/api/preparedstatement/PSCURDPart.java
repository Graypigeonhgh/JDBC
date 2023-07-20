package api.preparedstatement;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 使用preparedstatement进行t_user表的CURD动作（增删改查）
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-19-15:27
 **/
public class PSCURDPart {

    @Test
    public void testInsert() throws ClassNotFoundException, SQLException {
        /**
         * 插入一条用户数据!
         * 账号: test
         * 密码: test
         * 昵称: 测试
         */
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "a1234567");

        //3.编写SQL语句结果，动态值的部分使用？代替
        //TODO: 切记, ? 只能代替 值!!!!!  不能代替关键字 特殊符号 容器名
        String sql = "insert into t_user(account,password,nickname) values (?,?,?);";

        //4.创建preparedStatement，并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //5.占位符赋值
        preparedStatement.setObject(1, "test"); //account:也可根据类型使用setString()等
        preparedStatement.setObject(2, "test"); //password
        preparedStatement.setObject(3, "测试"); //nickname

        //6.发送SQL语句(DML类型)
        int rows = preparedStatement.executeUpdate();

        //DDL:ResultSet resultSet = preparedStatement.executeQuery();

        //7.输出结果
        if (rows > 0) {
            System.out.println("数据插入成功");
        }else{
            System.out.println("数据插入失败");
        }

        //8.关闭资源close
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, SQLException {
        /**
         * 修改一条用户数据!
         * 修改账号: test的用户,将nickname改为tomcat
         */

        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu","root","a1234567");
        //3.编写SQL语句结果，动态值的部分使用？代替
        //TODO: 切记, ? 只能代替 值!!!!!  不能代替关键字 特殊符号 容器名
        String sql = "update t_user set nickname=? where id =?";
        //4.创建preparedStatement，并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        preparedStatement.setObject(1,"AA");
        preparedStatement.setObject(2,5);
        //6.发送SQL语句
        int i = preparedStatement.executeUpdate();
        //7.输出结果
        if (i > 0) {
            System.out.println("修改成功");
        }else{
            System.out.println("修改失败");
        }
        //8.关闭资源close
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testDelete() throws ClassNotFoundException, SQLException {
        /**
         * 删除一条id = 5用户数据!
         */
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu","root","a1234567");
        //3.编写SQL语句结果，动态值的部分使用？代替
        //TODO: 切记, ? 只能代替 值!!!!!  不能代替关键字 特殊符号 容器名
        String sql = "delete from t_user where id = ?";
        //4.创建preparedStatement，并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        preparedStatement.setObject(1,5);
        //6.发送SQL语句
        int i = preparedStatement.executeUpdate();
        //7.输出结果
        if (i > 0) {
            System.out.println("数据删除成功");
        }else{
            System.out.println("数据删除失败");
        }
        //8.关闭资源close
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testSelect() throws ClassNotFoundException, SQLException {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu","root","a1234567");
        //3.编写SQL语句结果，动态值的部分使用？代替
        //TODO: 切记, ? 只能代替 值!!!!!  不能代替关键字 特殊符号 容器名
        String sql = "select id,account,password,nickname from t_user";
        //4.创建preparedStatement，并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        //6.发送SQL语句(DDL)
        ResultSet resultSet = preparedStatement.executeQuery();
        //7.结果集解析（查询才有），创建一个集合
        List<Map> list = new ArrayList<>();

        //8.获取列信息对象
        // metaData 装的当前结果集列的信息对象!(他可以获取列的名称根据下角标,可以获取列的数量)
        ResultSetMetaData  metaData= resultSet.getMetaData();

        //水平遍历获取列数
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map map = new HashMap();
            //一行数据对应一个map
            //手动取值(不推荐)
            /*
            map.put("id",resultSet.getObject("id"));
            map.put("account",resultSet.getObject("account"));
            map.put("pasword",resultSet.getObject("password"));
            map.put("nickname",resultSet.getObject("nickname"));
            */

            //自动遍历列。注意：要从1开始，因为数据库使用的索引是从1开始的
            for (int i = 1; i <= columnCount; i++){
                //获取指定的列的值！resultSet
                Object value = resultSet.getObject(i);
                //获取指定列索引的列名称：ResultSetMetaDAta
                //getColumnLabel:会先获取列的别名，无别名时获取名称 ； getColumnName:只会获取列的名称
                String columnLabel = metaData.getColumnLabel(i);
                map.put(columnLabel,value);
            }

            //一行数据的所有列全部存储到map中
            //再将map存储到集合中
            list.add(map);
        }

        System.out.println("list = " + list);
        //8.关闭资源close
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
