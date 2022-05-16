package com.mininglamp.km.nebula.generator.ui;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author daiyi
 * @date 2022/5/8
 **/
public class Test {

    public static void main(String[] args) throws Exception {
        // gen();
        Class.forName("com.vesoft.nebula.jdbc.NebulaDriver");
        DriverManager.setLogWriter(new PrintWriter(System.out));
        Connection conn = DriverManager.getConnection("jdbc:nebula://localhost:9669/basketballplayer", "root", "123");
        System.out.println(conn);
    }
}
