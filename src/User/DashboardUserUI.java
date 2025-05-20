/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import Database.DatabaseConnection;

/**
 *
 * @author fikra
 */
public class DashboardUserUI extends JFrame {
    
    public DashboardUserUI(){
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardUserUI().setVisible(true));
    }
}
