package org.geotools.Shelter;

/**
 * Created by jiao.xue on 2017/02/08.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jiao on 2017/02/07.
 */
public class DB_operation {
    public Connection getConn() {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost";
        String username = "shelters";
        String password = "2017";
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * @method insert(shelters)
     */
    public int insert(shelters shelter) {
        Connection conn = getConn();
        int i = 0;
        String sql = "insert into People (id,Name,Sex,Age) values(?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, shelter.getId());
            pstmt.setString(2, shelter.getName());
            pstmt.setDouble(3, shelter.getLon());
            pstmt.setDouble(4, shelter.getLat());
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * @method delete(shelters)
     */
    public int delete(String Name) {
        Connection conn = getConn();
        int i = 0;
        String sql = "delete from shelters where shelter's Name='" + Name + "'";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("result: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * @method update(shelters)
     */
    public int update(shelters shelter) {
        Connection conn = getConn();
        int i = 0;
        String sql = "update shelters set shelter's belong to='" + shelter.getId() + "' where shelter's Name='" + shelter.getName() + "'";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("result: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * @method Integer getAll()
     * @return Integer
     */
    public String getAll() {
        Connection conn = getConn();
        String sql = "select * from shelters";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
            }
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

