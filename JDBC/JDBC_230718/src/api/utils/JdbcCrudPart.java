package api.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description:  基于工具类的crud
 * @authod: GreyPigeon mail:2371849349@qq.com
 * @date: 2023-07-20-15:27
 **/
public class JdbcCrudPart {

    public void testInsert() throws SQLException{
        Connection connection = JdbcUtilis.getConnection();

        //数据库curd动作
        JdbcUtilis.freeConnection(connection);
    }

}
