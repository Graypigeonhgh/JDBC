package api.utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 封装简化非DQL语句
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-20-16:38
 **/
public class BaseDao {
    /**
    封装简化非DQL语句
    @param sql 带占位符的SQL语句
    @param params 占位符的值 注意。传入占位符的值，必须等于SQL语句?位置!
    @return 执行影响的行数
    */

    public int executeUpdate(String sql,Object... params) throws SQLException {
        //Object... params:传入多个参数


        //获取连接
        Connection connection = JdbcUtilisV2.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //5.占位符赋值
        //可变参数可以当成数组使用
        for(int i = 1; i <= params.length; i++){
            preparedStatement.setObject(i,params[i-1]);
        }

        //6.方式SQL语句
        //DML类型
        int rows = preparedStatement.executeUpdate();

        preparedStatement.close();

        //是否回收连接，需考虑是不是事务（事务中有connection.setAutoCommit(false)）
        if (connection.getAutoCommit()) {
            //没有开启事务，则正常回收链接
            JdbcUtilisV2.freeConnection();
        }

        //connection.setAutoCommit(false):开启了事务，回收连接由业务层处理
        return  rows;

    }


    /**
     封装简化DQL语句
     @param clazz 要接值的实体类集合的实体类的模版对象
     @param sql 带占位符的SQL语句，要求列名或者别名等于实体类的属性名
     @param params 占位符的值 注意。传入占位符的值，必须等于SQL语句?位置!
     @param <T> 声明结果的泛型
     @return 查询的实体类的集合
     */
    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object...params) throws Exception{

        //2.获取连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql:///atguigu","root","a1234567");

        //4.创建preparedStatement，并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //5.占位符赋值
        if (params == null && params.length != 0) {
            for (int i = 1; i <= params.length; i++){
                preparedStatement.setObject(i,params[i-1]);
            }
        }

        //6.发送SQL语句(DDL)
        ResultSet resultSet = preparedStatement.executeQuery();

        //7.结果集解析（查询才有），创建一个集合
        List<T> list = new ArrayList<>();

        //8.获取列信息对象
        // metaData 装的当前结果集列的信息对象!(他可以获取列的名称根据下角标,可以获取列的数量)
        ResultSetMetaData  metaData= resultSet.getMetaData();

        //水平遍历获取列数
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {

           T t = clazz.newInstance();   //调用类的无参构造函数实例化对象

            //自动遍历列。注意：要从1开始，因为数据库使用的索引是从1开始的
            for (int i = 1; i <= columnCount; i++){

                //获取指定的列的值！resultSet
                Object value = resultSet.getObject(i);

                //获取指定列索引的列名称：ResultSetMetaDAta
                //getColumnLabel:会先获取列的别名，无别名时获取名称 ； getColumnName:只会获取列的名称
                String propertyName = metaData.getColumnLabel(i);

                //反射，给对象的属性值t赋值
                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);  //属性值可设置，打破private的修饰限制

                /*
                 * 参数1: 要要赋值的对象 如果属性是静态，第一个参数 可以为nulL!
                 * 参数2: 具体的属性值
                 **/
                field.set(t,value);

            }

            //一行数据的所有列全部存储到map中
            //再将map存储到集合中
            list.add(t);
        }


        //8.关闭资源close
        //是否回收连接，需考虑是不是事务（事务中有connection.setAutoCommit(false)）
        if (connection.getAutoCommit()) {
            //没有开启事务，则正常回收链接
            JdbcUtilisV2.freeConnection();
        }
        return list;

    }

}
