/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin;

import java.io.File;
import javax.swing.JOptionPane;
import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Statement;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author dinda
 */
public class ReviewTask extends javax.swing.JFrame {
    private int adminId;
    private int taskId;
    /**
     * Creates new form ReviewTask
     */
    public ReviewTask(int adminId, int taskId) {
        this.adminId = adminId;
        this.taskId = taskId;
        initComponents();
        setLocationRelativeTo(null);
        
        // Pengaturan efek hover dan kursor pada label sidebar
        setupSidebarLabel(TxtDashboard);
        setupSidebarLabel(TxtProject);
        setupSidebarLabel(TxtTask);
        setupSidebarLabel(TxtLogout);
        
        loadTaskUnderReview(taskId);
    }

    // Method untuk mengatur efek hover dan kursor label sidebar agar tidak mengulang kode
    private void setupSidebarLabel(javax.swing.JLabel label) {
        label.setOpaque(true);
        label.setBackground(new java.awt.Color(211, 211, 211));
        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label.setBackground(new java.awt.Color(191, 191, 191));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                label.setBackground(new java.awt.Color(211, 211, 211));
            }
        });
    }
    
    //method load data
    public void loadTaskUnderReview(int taskId) {
    try {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT t.name AS task_name, t.deadline, t.point, t.image, t.comment, " +
                       "p.name AS project_name, a.name AS assignee_name, s.status " +
                       "FROM tasks t " +
                       "JOIN projects p ON t.projects_id = p.id " +
                       "JOIN assignees a ON t.assignees_id = a.id " +
                       "JOIN (SELECT tasks_id, status FROM status_tracks WHERE status = 'under review' ORDER BY created_at DESC) s " +
                       "ON t.id = s.tasks_id " +
                       "WHERE t.id = ? LIMIT 1";

        PreparedStatement pst = conn.prepareStatement(query);
        pst.setLong(1, taskId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
        FieldTaskName.setText(rs.getString("task_name"));
        FieldDeadline.setText(rs.getString("deadline"));
        FieldPoint.setText(String.valueOf(rs.getInt("point")));

        String imageFileName = rs.getString("image");
        String imagePath = "src" + File.separator + "Images" + File.separator + "uploads" + File.separator + imageFileName;

        System.out.println("Image path: " + imagePath);

        File imgFile = new File(imagePath);
        System.out.println("Absolute path: " + imgFile.getAbsolutePath());
        System.out.println("File exists: " + imgFile.exists());
    
 
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
            LabelImagePreview.setIcon(new ImageIcon(img));
        } else {
            System.out.println("Gambar tidak ditemukan di path: " + imagePath);
            LabelImagePreview.setIcon(null);
        }

        FieldImages.setText(imagePath);

        FieldProjectName.setText(rs.getString("project_name"));
        FieldAssignee.setText(rs.getString("assignee_name"));
        FieldStatus.setText(rs.getString("status"));
        } else {
            JOptionPane.showMessageDialog(null, "Task dengan status 'under review' tidak ditemukan.");
        }

        rs.close();
        pst.close();
        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error saat mengambil data task: " + e.getMessage());
    }
    }
    
    //method konfimasi status
    private void updateTaskStatus(int taskId, String newStatus) {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
        conn = DatabaseConnection.getConnection();
        String sql = "UPDATE status_tracks SET status = ? WHERE tasks_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, newStatus);
        stmt.setInt(2, taskId);

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Status task berhasil diupdate menjadi: " + newStatus);
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengupdate status task.");
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error saat update status: " + ex.getMessage());
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SidebarPanel1 = new javax.swing.JPanel();
        TxtDashboard = new javax.swing.JLabel();
        TxtProject = new javax.swing.JLabel();
        TxtLogout = new javax.swing.JLabel();
        TxtTask = new javax.swing.JLabel();
        LineSidebar2 = new javax.swing.JSeparator();
        LineSidebar3 = new javax.swing.JSeparator();
        TxtArasaka = new javax.swing.JLabel();
        TxtSocialMedia = new javax.swing.JLabel();
        TxtProjectManagement = new javax.swing.JLabel();
        LogoArasaka = new javax.swing.JLabel();
        TxtUser = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        BtnAccept = new javax.swing.JButton();
        TxtProjectName1 = new javax.swing.JLabel();
        TxtTaskName = new javax.swing.JLabel();
        TxtDeadline = new javax.swing.JLabel();
        TxtPoint = new javax.swing.JLabel();
        TxtStatus = new javax.swing.JLabel();
        TxtAssignee = new javax.swing.JLabel();
        BtnCancel = new javax.swing.JButton();
        FieldAssignee = new javax.swing.JTextField();
        FieldStatus = new javax.swing.JTextField();
        FieldProjectName = new javax.swing.JTextField();
        FieldDeadline = new javax.swing.JTextField();
        FieldTaskName = new javax.swing.JTextField();
        FieldPoint = new javax.swing.JTextField();
        TxtStatus1 = new javax.swing.JLabel();
        LabelImagePreview = new javax.swing.JLabel();
        FieldImages = new javax.swing.JTextField();
        BtnDecline = new javax.swing.JButton();
        Task = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        SidebarPanel1.setBackground(new java.awt.Color(211, 211, 211));
        SidebarPanel1.setPreferredSize(new java.awt.Dimension(220, 420));

        TxtDashboard.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TxtDashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icondashboard.png"))); // NOI18N
        TxtDashboard.setText("DASHBOARD");
        TxtDashboard.setIconTextGap(15);
        TxtDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TxtDashboardMouseClicked(evt);
            }
        });

        TxtProject.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TxtProject.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconproject.png"))); // NOI18N
        TxtProject.setText("PROJECT");
        TxtProject.setIconTextGap(15);
        TxtProject.setInheritsPopupMenu(false);
        TxtProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TxtProjectMouseClicked(evt);
            }
        });

        TxtLogout.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TxtLogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconlogout.png"))); // NOI18N
        TxtLogout.setText("LOGOUT");
        TxtLogout.setIconTextGap(15);
        TxtLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TxtLogoutMouseClicked(evt);
            }
        });

        TxtTask.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TxtTask.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icontask.png"))); // NOI18N
        TxtTask.setText("TASK");
        TxtTask.setIconTextGap(15);
        TxtTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TxtTaskMouseClicked(evt);
            }
        });

        LineSidebar2.setForeground(new java.awt.Color(0, 0, 0));

        LineSidebar3.setForeground(new java.awt.Color(0, 0, 0));

        TxtArasaka.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtArasaka.setText("Arasaka");

        TxtSocialMedia.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtSocialMedia.setText("Social Media ");

        TxtProjectManagement.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtProjectManagement.setText("Project Management");

        LogoArasaka.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconarasaka.png"))); // NOI18N

        TxtUser.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TxtUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconuser.png"))); // NOI18N
        TxtUser.setText("USER");
        TxtUser.setIconTextGap(15);
        TxtUser.setInheritsPopupMenu(false);
        TxtUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TxtUserMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SidebarPanel1Layout = new javax.swing.GroupLayout(SidebarPanel1);
        SidebarPanel1.setLayout(SidebarPanel1Layout);
        SidebarPanel1Layout.setHorizontalGroup(
            SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarPanel1Layout.createSequentialGroup()
                .addGroup(SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SidebarPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LineSidebar2)
                            .addComponent(LineSidebar3)))
                    .addGroup(SidebarPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SidebarPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(TxtProjectManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(SidebarPanel1Layout.createSequentialGroup()
                                .addComponent(LogoArasaka)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(SidebarPanel1Layout.createSequentialGroup()
                                        .addComponent(TxtArasaka, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidebarPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(TxtSocialMedia, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(SidebarPanel1Layout.createSequentialGroup()
                        .addGroup(SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SidebarPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(TxtDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(SidebarPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TxtProject, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TxtTask, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(SidebarPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(TxtLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        SidebarPanel1Layout.setVerticalGroup(
            SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarPanel1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(SidebarPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LogoArasaka)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidebarPanel1Layout.createSequentialGroup()
                        .addComponent(TxtArasaka)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtSocialMedia)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtProjectManagement)
                .addGap(76, 76, 76)
                .addComponent(TxtDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LineSidebar2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxtProject, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxtTask, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addComponent(LineSidebar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxtLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        jPanel2.setBackground(new java.awt.Color(214, 201, 197));
        jPanel2.setForeground(new java.awt.Color(12, 44, 71));
        jPanel2.setPreferredSize(new java.awt.Dimension(972, 631));

        BtnAccept.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        BtnAccept.setForeground(new java.awt.Color(0, 0, 255));
        BtnAccept.setText("Accept");
        BtnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAcceptActionPerformed(evt);
            }
        });

        TxtProjectName1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtProjectName1.setText("Project Name");

        TxtTaskName.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtTaskName.setText("Task Name");

        TxtDeadline.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtDeadline.setText("Deadline");

        TxtPoint.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtPoint.setText("Point");

        TxtStatus.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtStatus.setText("Images");

        TxtAssignee.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtAssignee.setText("Assignee");

        BtnCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        BtnCancel.setText("Cancel");
        BtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelActionPerformed(evt);
            }
        });

        FieldAssignee.setEditable(false);
        FieldAssignee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldAssignee.setText("Assignee Name Here");
        FieldAssignee.setPreferredSize(new java.awt.Dimension(105, 30));
        FieldAssignee.setRequestFocusEnabled(false);
        FieldAssignee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldAssigneeActionPerformed(evt);
            }
        });

        FieldStatus.setEditable(false);
        FieldStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldStatus.setText("Status Here");
        FieldStatus.setPreferredSize(new java.awt.Dimension(105, 30));
        FieldStatus.setRequestFocusEnabled(false);
        FieldStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldStatusActionPerformed(evt);
            }
        });

        FieldProjectName.setEditable(false);
        FieldProjectName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldProjectName.setText("Project Name Here");
        FieldProjectName.setPreferredSize(new java.awt.Dimension(105, 30));
        FieldProjectName.setRequestFocusEnabled(false);
        FieldProjectName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldProjectNameActionPerformed(evt);
            }
        });

        FieldDeadline.setEditable(false);
        FieldDeadline.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldDeadline.setText("Deadline Here");
        FieldDeadline.setPreferredSize(new java.awt.Dimension(105, 30));
        FieldDeadline.setRequestFocusEnabled(false);
        FieldDeadline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldDeadlineActionPerformed(evt);
            }
        });

        FieldTaskName.setEditable(false);
        FieldTaskName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldTaskName.setText("Task Name Here");
        FieldTaskName.setPreferredSize(new java.awt.Dimension(105, 30));
        FieldTaskName.setRequestFocusEnabled(false);
        FieldTaskName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldTaskNameActionPerformed(evt);
            }
        });

        FieldPoint.setEditable(false);
        FieldPoint.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldPoint.setText("Point Here");
        FieldPoint.setPreferredSize(new java.awt.Dimension(105, 30));
        FieldPoint.setRequestFocusEnabled(false);
        FieldPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldPointActionPerformed(evt);
            }
        });

        TxtStatus1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtStatus1.setText("Status");

        FieldImages.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldImages.setText("Task Name Here");
        FieldImages.setPreferredSize(new java.awt.Dimension(105, 30));
        FieldImages.setRequestFocusEnabled(false);
        FieldImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldImagesActionPerformed(evt);
            }
        });

        BtnDecline.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        BtnDecline.setForeground(new java.awt.Color(255, 0, 51));
        BtnDecline.setText("Decline");
        BtnDecline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDeclineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(BtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 631, Short.MAX_VALUE)
                        .addComponent(BtnDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(FieldProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtProjectName1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FieldTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FieldDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FieldAssignee, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtAssignee, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(124, 124, 124)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(FieldImages, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FieldPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TxtPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(FieldStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TxtStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(LabelImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(12, 12, 12)
                .addComponent(BtnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtProjectName1)
                    .addComponent(TxtPoint))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtTaskName, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(TxtStatus1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtDeadline)
                    .addComponent(TxtStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldImages, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(TxtAssignee)
                        .addGap(18, 18, 18)
                        .addComponent(FieldAssignee, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(158, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(LabelImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtnDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))))
        );

        Task.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Task.setForeground(new java.awt.Color(12, 44, 71));
        Task.setText("REVIEW TASK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SidebarPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Task)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1035, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SidebarPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(Task)
                .addGap(26, 26, 26)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtDashboardMouseClicked
        DashboardAdmin dashboard = new DashboardAdmin(adminId);
        dashboard.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtDashboardMouseClicked

    private void TxtProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtProjectMouseClicked
        CRUDProject project = new CRUDProject(adminId);
        project.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtProjectMouseClicked

    private void TxtLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtLogoutMouseClicked
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure want to exit?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm ==JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_TxtLogoutMouseClicked

    private void TxtTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtTaskMouseClicked
        CRUDTask task = new CRUDTask(adminId);
        task.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtTaskMouseClicked

    private void TxtUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtUserMouseClicked
        CRUDUser task = new CRUDUser(adminId);
        task.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_TxtUserMouseClicked

    private void BtnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAcceptActionPerformed
     updateTaskStatus(taskId, "completed");
     CRUDTask task = new CRUDTask(adminId);
     task.setVisible(true);
     this.dispose();
    }//GEN-LAST:event_BtnAcceptActionPerformed

    private void BtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelActionPerformed
        CRUDTask task = new CRUDTask(adminId);
        task.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BtnCancelActionPerformed

    private void FieldAssigneeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldAssigneeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldAssigneeActionPerformed

    private void FieldStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldStatusActionPerformed

    private void FieldProjectNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldProjectNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldProjectNameActionPerformed

    private void FieldDeadlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldDeadlineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldDeadlineActionPerformed

    private void FieldTaskNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldTaskNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldTaskNameActionPerformed

    private void FieldPointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldPointActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldPointActionPerformed

    private void FieldImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldImagesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldImagesActionPerformed

    private void BtnDeclineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDeclineActionPerformed
     updateTaskStatus(taskId, "ongoing");
     CRUDTask task = new CRUDTask(adminId);
     task.setVisible(true);
     this.dispose();
    }//GEN-LAST:event_BtnDeclineActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReviewTask().setVisible(true);
            }
        });*/
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAccept;
    private javax.swing.JButton BtnCancel;
    private javax.swing.JButton BtnDecline;
    private javax.swing.JTextField FieldAssignee;
    private javax.swing.JTextField FieldDeadline;
    private javax.swing.JTextField FieldImages;
    private javax.swing.JTextField FieldPoint;
    private javax.swing.JTextField FieldProjectName;
    private javax.swing.JTextField FieldStatus;
    private javax.swing.JTextField FieldTaskName;
    private javax.swing.JLabel LabelImagePreview;
    private javax.swing.JSeparator LineSidebar2;
    private javax.swing.JSeparator LineSidebar3;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel SidebarPanel1;
    private javax.swing.JLabel Task;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtAssignee;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtDeadline;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtPoint;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtProjectName1;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtStatus;
    private javax.swing.JLabel TxtStatus1;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JLabel TxtTaskName;
    private javax.swing.JLabel TxtUser;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
