/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin;

import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.JTableHeader;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * @author dinda
 */
public class DetailProject extends javax.swing.JFrame {
    private int projectId;
    private int adminId;
    /**
     * Creates new form DetailProject
     */
    public DetailProject(int adminId, int projectId) {
        this.adminId = adminId;
        this.projectId = projectId;
        initComponents();
        
        setLocationRelativeTo(null);
        
        loadProjectDetails(projectId);
        
        // Pengaturan efek hover dan kursor pada label sidebar
        setupSidebarLabel(TxtDashboard);
        setupSidebarLabel(TxtUser);
        setupSidebarLabel(TxtProject);
        setupSidebarLabel(TxtTask);
        setupSidebarLabel(TxtLogout);
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

    
    // Method untuk load data detail project
    public void loadProjectDetails(int projectId) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Ambil nama & deskripsi project
            String queryProject = "SELECT name, `desc` FROM projects WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(queryProject);
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                FieldProjectName.setText(rs.getString("name"));
                AreaDesc.setText(rs.getString("desc"));
            }

            // Ambil nama assignee, point, dan task completed
            String queryAssigneeTask = """
                    SELECT a.name AS assignee_name,
                         SUM(CASE WHEN st.status = 'completed' THEN t.point ELSE 0 END) AS completed_point,
                            (
                              SELECT COUNT(DISTINCT t2.id) 
                              FROM tasks t2 
                              WHERE t2.assignees_id = a.id AND t2.projects_id = ?
                            ) AS total_tasks,
                           (
                              SELECT COUNT(DISTINCT t2.id) 
                              FROM tasks t2
                              JOIN status_tracks st2 ON t2.id = st2.tasks_id
                              WHERE t2.assignees_id = a.id AND t2.projects_id = ? AND st2.status = 'completed'
                            ) AS completed_tasks
                    FROM assignees a
                    JOIN tasks t ON a.id = t.assignees_id
                    LEFT JOIN status_tracks st ON t.id = st.tasks_id AND st.status = 'completed'
                    WHERE t.projects_id = ?
                    GROUP BY a.id
            """;

            PreparedStatement taskStmt = conn.prepareStatement(queryAssigneeTask);
            taskStmt.setInt(1, projectId); // for total_tasks subquery
            taskStmt.setInt(2, projectId); // for completed_tasks subquery
            taskStmt.setInt(3, projectId); // for main WHERE clause
            ResultSet taskRs = taskStmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new Object[]{"Assignee Name", "Point", "Task"}, 0);
            TblDetailProject.setModel(model); // Tetapkan model baru dengan kolom

            while (taskRs.next()) {
            String assigneeName = taskRs.getString("assignee_name");
            int completedPoint = taskRs.getInt("completed_point");
            int completedTasks = taskRs.getInt("completed_tasks");
            int totalTasks = taskRs.getInt("total_tasks");
            String tasks = completedTasks + "/" + totalTasks;

            model.addRow(new Object[]{assigneeName, completedPoint, tasks});
            }


            // Styling header tabel
            JTableHeader header = TblDetailProject.getTableHeader();
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40)); // Atur tinggi header
            header.setFont(header.getFont().deriveFont(Font.BOLD)); // Buat tulisan tebal
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER); // Rata tengah
            
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading project details: " + e.getMessage());
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
        TxtDetailProject = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        TxtProjectName = new javax.swing.JLabel();
        FieldProjectName = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TblDetailProject = new javax.swing.JTable();
        TxtDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AreaDesc = new javax.swing.JTextArea();
        BtnBack = new javax.swing.JButton();

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

        TxtDetailProject.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        TxtDetailProject.setText("DETAIL PROJECT");

        jPanel2.setBackground(new java.awt.Color(214, 201, 197));
        jPanel2.setPreferredSize(new java.awt.Dimension(972, 631));

        TxtProjectName.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtProjectName.setText("Project Name");

        FieldProjectName.setEditable(false);
        FieldProjectName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldProjectName.setText("Project Name Here");
        FieldProjectName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldProjectNameActionPerformed(evt);
            }
        });

        TblDetailProject.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Assignee Name", "Point", "Task"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TblDetailProject.setRowHeight(40);
        jScrollPane2.setViewportView(TblDetailProject);

        TxtDescription.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TxtDescription.setText("Description");
        TxtDescription.setToolTipText("");

        AreaDesc.setEditable(false);
        AreaDesc.setColumns(20);
        AreaDesc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AreaDesc.setRows(5);
        AreaDesc.setText("Description Project Here");
        jScrollPane1.setViewportView(AreaDesc);

        BtnBack.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        BtnBack.setText("Back");
        BtnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BtnBack)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(FieldProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TxtProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(166, 166, 166)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TxtDescription)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtProjectName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TxtDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FieldProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(BtnBack)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SidebarPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtDetailProject, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1041, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 55, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SidebarPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(TxtDetailProject)
                .addGap(27, 27, 27)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void FieldProjectNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldProjectNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldProjectNameActionPerformed

    private void BtnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBackActionPerformed
        this.dispose();       
    }//GEN-LAST:event_BtnBackActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        
        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetailProject().setVisible(true);
            }
        });*/
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AreaDesc;
    private javax.swing.JButton BtnBack;
    private javax.swing.JTextField FieldProjectName;
    private javax.swing.JSeparator LineSidebar2;
    private javax.swing.JSeparator LineSidebar3;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel SidebarPanel1;
    private javax.swing.JTable TblDetailProject;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtDescription;
    private javax.swing.JLabel TxtDetailProject;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtProjectName;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JLabel TxtUser;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
