package api.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description:
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-20-15:13
 **/
/*
v1.@版本工具类
内部包含一个连接池对象，并且对外提供获取连接和回收连接的方法!
        小建议:
        工具类的方法，推荐写成静态，外部调用会更加方便!
        实现:
            属性 连接池对象 [实例化一次]
                单例模式I
                 static{全局调用一次}
        方法
            对外提供连接的方法,回收外部传入连接方法
*/

public class JdbcUtilis {

    private static DataSource dataSource = null;    //连接池对象

    static{
        //初始化连接池对象
        Properties properties = new Properties();
        InputStream ips =JdbcUtilis.class.getClassLoader().getResourceAsStream("druid.properties");

        try {
            properties.load(ips);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 对外提供连接的方法
     */
    public static Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }

    public static void freeConnection(Connection connection) throws SQLException{
        //回收连接
        connection.close();
    }

}
