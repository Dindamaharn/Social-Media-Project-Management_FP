/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin;

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
public class EditTask extends javax.swing.JFrame {
private int adminId;
private int taskId;
private CRUDTask parentFrame;
    /**
     * Creates new form EditTask
     */
    public EditTask(int adminId, int taskId, CRUDTask parent) {
        this.adminId = adminId;
        this.taskId = taskId;
        this.parentFrame = parentFrame; 
        
        initComponents();
        setupSidebarLabel(TxtDashboard);
        setupSidebarLabel(TxtUser);
        setupSidebarLabel(TxtProject);
        setupSidebarLabel(TxtTask);
        setupSidebarLabel(TxtLogout);
        setLocationRelativeTo(null);
        
        loadTaskData();
        loadComboData();
        
    }

    //Method Sidebar
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
    
    private void loadComboData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Load assignees
            DefaultComboBoxModel<String> assigneeModel = new DefaultComboBoxModel<>();
            PreparedStatement stmt1 = conn.prepareStatement("SELECT id, name FROM assignees");
            ResultSet rs1 = stmt1.executeQuery();
            while (rs1.next()) {
                assigneeModel.addElement(rs1.getInt("id") + " - " + rs1.getString("name"));
            }
            ComboAssignee.setModel(assigneeModel);

            // Load projects
            DefaultComboBoxModel<String> projectModel = new DefaultComboBoxModel<>();
            PreparedStatement stmt2 = conn.prepareStatement("SELECT id, name FROM projects");
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                projectModel.addElement(rs2.getInt("id") + " - " + rs2.getString("name"));
            }
            ComboProjectName.setModel(projectModel);

            // Load status options
            ComboStatus.setModel(new DefaultComboBoxModel<>(new String[] {"pending", "ongoing", "under review"}));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + e.getMessage());
        }
    }
    
    private void loadTaskData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT t.name, t.point, t.deadline, t.assignees_id, t.projects_id, s.status " +
                           "FROM tasks t " +
                           "JOIN status_tracks s ON t.id = s.tasks_id " +
                           "WHERE t.id = ? ORDER BY s.updated_at DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TaskNameData.setText(rs.getString("name"));
                PointData.setText(String.valueOf(rs.getInt("point")));
                jDateChooserDeadline.setDate(rs.getDate("deadline"));

                ComboAssignee.setSelectedItem(rs.getInt("assignees_id") + " - " + getAssigneeName(rs.getInt("assignees_id")));
                ComboProjectName.setSelectedItem(rs.getInt("projects_id") + " - " + getProjectName(rs.getInt("projects_id")));
                ComboStatus.setSelectedItem(rs.getString("status"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading task data: " + e.getMessage());
        }
    }

    private String getAssigneeName(int id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT name FROM assignees WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getString("name") : "";
    }

    private String getProjectName(int id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT name FROM projects WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getString("name") : "";
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
        TaskNameData = new javax.swing.JTextField();
        PointData = new javax.swing.JTextField();
        BtnSave = new javax.swing.JButton();
        TxtProjectName1 = new javax.swing.JLabel();
        TxtTaskName = new javax.swing.JLabel();
        TxtDeadline = new javax.swing.JLabel();
        TxtPoint = new javax.swing.JLabel();
        TxtStatus = new javax.swing.JLabel();
        jDateChooserDeadline = new com.toedter.calendar.JDateChooser();
        ComboAssignee = new javax.swing.JComboBox<>();
        ComboProjectName = new javax.swing.JComboBox<>();
        TxtAssignee = new javax.swing.JLabel();
        ComboStatus = new javax.swing.JComboBox<>();
        BtnCancel = new javax.swing.JButton();
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

        TaskNameData.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TaskNameData.setText("Task Name Here");
        TaskNameData.setPreferredSize(new java.awt.Dimension(105, 30));
        TaskNameData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskNameDataActionPerformed(evt);
            }
        });

        PointData.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PointData.setText("Point ");

        BtnSave.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        BtnSave.setText("Save");
        BtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSaveActionPerformed(evt);
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
        TxtStatus.setText("Status");

        ComboAssignee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ComboAssignee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ComboAssignee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboAssigneeActionPerformed(evt);
            }
        });

        ComboProjectName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ComboProjectName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ComboProjectName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboProjectNameActionPerformed(evt);
            }
        });

        TxtAssignee.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtAssignee.setText("Assignee");

        ComboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ComboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboStatusActionPerformed(evt);
            }
        });

        BtnCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        BtnCancel.setText("Cancel");
        BtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TxtProjectName1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtAssignee, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooserDeadline, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(ComboProjectName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ComboAssignee, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PointData, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(TxtTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(329, 329, 329))
                        .addComponent(ComboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BtnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(TxtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TaskNameData, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TxtTaskName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TxtProjectName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ComboProjectName)
                    .addComponent(TaskNameData, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtDeadline)
                    .addComponent(TxtPoint))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooserDeadline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PointData, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtAssignee, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TxtStatus, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboAssignee, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BtnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BtnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addGap(37, 37, 37))
        );

        Task.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Task.setForeground(new java.awt.Color(12, 44, 71));
        Task.setText("EDIT TASK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SidebarPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Task)
                        .addContainerGap(961, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(55, 55, 55))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SidebarPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(Task)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
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

    private void BtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelActionPerformed
        CRUDTask task = new CRUDTask(adminId);
        task.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BtnCancelActionPerformed

    private void BtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSaveActionPerformed
        String taskName = TaskNameData.getText();
        int point = Integer.parseInt(PointData.getText());
        Date deadline = jDateChooserDeadline.getDate();
        int assigneeId = Integer.parseInt(ComboAssignee.getSelectedItem().toString().split(" - ")[0]);
        int projectId = Integer.parseInt(ComboProjectName.getSelectedItem().toString().split(" - ")[0]);
        String status = ComboStatus.getSelectedItem().toString();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE tasks SET name = ?, point = ?, deadline = ?, assignees_id = ?, projects_id = ?, updated_at = NOW() WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, taskName);
            stmt.setInt(2, point);
            stmt.setDate(3, new java.sql.Date(deadline.getTime()));
            stmt.setInt(4, assigneeId);
            stmt.setInt(5, projectId);
            stmt.setInt(6, taskId);
            stmt.executeUpdate();

            String insertStatus = "INSERT INTO status_tracks (tasks_id, status, created_at, updated_at) VALUES (?, ?, NOW(), NOW())";
            PreparedStatement statusStmt = conn.prepareStatement(insertStatus);
            statusStmt.setInt(1, taskId);
            statusStmt.setString(2, status);
            statusStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Task updated successfully!");
            parentFrame.loadTaskData();
            this.dispose(); // close window
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating task: " + e.getMessage());
        }
    }//GEN-LAST:event_BtnSaveActionPerformed

    private void TaskNameDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskNameDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TaskNameDataActionPerformed

    private void ComboAssigneeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboAssigneeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboAssigneeActionPerformed

    private void ComboStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboStatusActionPerformed

    private void ComboProjectNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboProjectNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboProjectNameActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        
        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditTask().setVisible(true);
            }
        });*/
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCancel;
    private javax.swing.JButton BtnSave;
    private javax.swing.JComboBox<String> ComboAssignee;
    private javax.swing.JComboBox<String> ComboProjectName;
    private javax.swing.JComboBox<String> ComboStatus;
    private javax.swing.JSeparator LineSidebar2;
    private javax.swing.JSeparator LineSidebar3;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JTextField PointData;
    private javax.swing.JPanel SidebarPanel1;
    private javax.swing.JLabel Task;
    private javax.swing.JTextField TaskNameData;
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
    private javax.swing.JLabel TxtTask;
    private javax.swing.JLabel TxtTaskName;
    private javax.swing.JLabel TxtUser;
    private com.toedter.calendar.JDateChooser jDateChooserDeadline;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
