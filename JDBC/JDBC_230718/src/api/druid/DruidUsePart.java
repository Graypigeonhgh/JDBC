package api.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description: druid连接池使用类
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-20-14:09
 **/
public class DruidUsePart {
    /*
    1.创建一个druid连接池对象
    2.设置连接池参数[必须 | 非必须]
    3.获取连接[通用方法，所有连接池都一样]
    4.回收连接[通用方法，所有连接池都一样]
    */
    //硬连接方式
    public void testHard() throws SQLException {

        //1.连接池对象
        DruidDataSource dataSource = new DruidDataSource();

        //2.设置连接池参数
        //必须 连接数据库驱动类的全限定符[注册驱动]  urL | user | password
        dataSource.setUrl("jdbc:mysql://localhost:3306/atguigu");
        dataSource.setUsername("root");
        dataSource.setPassword("a1234567");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        //非必须 初始化连接数量，最大的连接数量
        dataSource.setInitialSize(5);   //初始化连接池数量
        dataSource.setMaxActive(10);    //最大数量

        //3.获取连接[通用方法，所有连接池都一样]
        Connection connection = dataSource.getConnection();

        //数据库curd

        //4.回收连接[通用方法，所有连接池都一样]
        connection.close();     //连接池提供的连接关闭，即回收连接

    }

    //软连接方式
    public void testSoft() throws Exception{

        //1.读取外部配置文件 properties
        Properties properties = new Properties();

        //src下的文件，可以使用类加载器提供的方法
        InputStream ips = DruidUsePart.class.getClassLoader().getResourceAsStream("druid.properties");
        properties.load(ips);

        //2.使用连接池的工具类的工程模式，创建连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        //3.获取连接[通用方法，所有连接池都一样]
        Connection connection = dataSource.getConnection();

        //数据库curd

        //4.回收连接[通用方法，所有连接池都一样]
        connection.close();

    }

}
