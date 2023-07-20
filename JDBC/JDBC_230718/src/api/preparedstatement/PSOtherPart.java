package api.preparedstatement;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.junit.Test;

import java.sql.*;

/**
 * @description:    扩展：练习ps的特殊使用情况
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-19-22:47
 **/
public class PSOtherPart {

    /**
     *TODO: 主键回显和主键值获取:t_user插入一条数据，并且获取数据库自增长的主键
     * 1.创建prepareStatement的时候,告知,携带回数据本自增长的主键 (sql,Statement,RETURN_GENERATED_KEYS)
     * 2.获取司机装主链值的结果集对象，一行一列,获取对应的数据即可 ResultSet resultSet = statement.getGeneratedKeys()
     */
    @Test
    public void returnPrimaryKey() throws ClassNotFoundException, SQLException {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu","root","a1234567");
        //3.编写SQL语句
        String sql = "insert into t_user(account,password,nickname) values(?,?,?)";
        //4.创建PreparedStatement
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //5.占位符（？）赋值
        statement.setObject(1,"test1");
        statement.setObject(2,"123456");
        statement.setObject(3,"HGH");
        //6.发送SQL语句，并获取结果
        int i = statement.executeUpdate();
        //7.结果解析
        if (i > 0) {
            System.out.println("数据插入成功");

            //获取回显的主键
            //获取装有主键的结果集对象，一行一列，id =  值
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();   //移动下光标
            int id = resultSet.getInt(1);
            System.out.println("id = " + id);

        }else{
            System.out.println("数据插入失败");
        }
        //8.关闭资源
        statement.close();
        connection.close();

    }


    /**
     *TODO: 使用普通的方式插入10000条数据
     */
    @Test
    public void testInsert() throws ClassNotFoundException, SQLException {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu","root","a1234567");
        //3.编写SQL语句
        String sql = "insert into t_user(account,password,nickname) values(?,?,?)";
        //4.创建PreparedStatement
        PreparedStatement statement = connection.prepareStatement(sql);
        //5.占位符（？）赋值

        long start = System.currentTimeMillis();    //计算执行时间

        for(int i = 0;i < 10000;i++){
            statement.setObject(1,"test2" + i);
            statement.setObject(2,"aa" + i);
            statement.setObject(3,"HGH" + i);

            //6.发送SQL语句，并获取结果
            statement.executeUpdate();
        }

        long end = System.currentTimeMillis();


        //7.结果解析
        System.out.println("执行10000次数据插入消耗的时间为：" + (end  - start)); //6635

        //8.关闭资源
        statement.close();
        connection.close();

    }


    /**
     *TODO: 使用批量插入的方式插入10000条数据、
     * 批量细节：
     *    1.url?rewriteBatchedStatements=true
     *    2.insert 语句必须使用 values
     *    3.values语句后面不能添加分号;结束
     *    4.语句不能直接执行，每次需要装货  addBatch() （先批量添加再批量执行）
     *    5.遍历添加完成后，统一批量执行 executeBatch();
     */
    @Test
    public void testBatchInsert() throws ClassNotFoundException, SQLException {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        //让MySQL允许批量插入，需在路径下添加固定参数：?rewriteBatchedStatements=true
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql:///atguigu?rewriteBatchedStatements=true","root","a1234567");
        //3.编写SQL语句
        String sql = "insert into t_user(account,password,nickname) values(?,?,?)";
        //4.创建PreparedStatement
        PreparedStatement statement = connection.prepareStatement(sql);
        //5.占位符（？）赋值

        long start = System.currentTimeMillis();    //计算执行时间

        for(int i = 0;i < 10000;i++){
            statement.setObject(1,"test3" + i);
            statement.setObject(2,"bb" + i);
            statement.setObject(3,"HGH" + i);

            //6.发送SQL语句，并获取结果
            //statement.executeUpdate();
            statement.addBatch();   //上式不执行，追加到SQL语句的values后面
        }

        statement.executeBatch();   //执行批量操作

        long end = System.currentTimeMillis();


        //7.结果解析
        System.out.println("执行10000次数据插入消耗的时间为：" + (end  - start)); //288

        //8.关闭资源
        statement.close();
        connection.close();

    }

    @Test
    public void test(){

    }

}
