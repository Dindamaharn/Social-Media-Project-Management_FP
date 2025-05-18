/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dinda
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/arasaka";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() {
    try {
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        System.out.println("Koneksi berhasil!");
        return conn;
    } catch (SQLException e) {
        System.out.println("Koneksi gagal: " + e.getMessage());
        return null;
    }
  }
}
