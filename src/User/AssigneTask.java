/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package User;

import Admin.ReviewTask;
import Database.DatabaseConnection;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dinda
 */
public class AssigneTask extends javax.swing.JFrame {

    private int assigneeId;
    private String userName;

    public AssigneTask(int assigneeId) {
        this.assigneeId = assigneeId;
        initComponents();
        fetchUserName(); // Ambil nama user berdasarkan userId
        welcomeLabel.setText("Welcome, " + userName);
        loadTaskData();  // Ganti ini dari loadTaskData(userId) menjadi loadTaskData()
        setupMenuHoverEffect();
    }
    
    private void hideSelf(){
        this.setVisible(false);
    }

    private void fetchUserName() {
        try {
            Connection conn = Database.DatabaseConnection.getConnection();
            String sql = "SELECT name FROM assignees WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assigneeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("name"); // Ganti dari "username" ke "name"
            } else {
                userName = "User";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            userName = "User";
        }
    }
//

    private void loadTaskData() {
        try {
            Connection conn = Database.DatabaseConnection.getConnection();
            String sql;
            sql = """
                WITH latest_status AS (
                                               SELECT st.*,
                                                      ROW_NUMBER() OVER (PARTITION BY tasks_id ORDER BY created_at DESC) AS rn
                                               FROM status_tracks st
                                           )
                                           
                                           SELECT 
                                               t.id,
                                               t.name AS task_name,
                                               t.desc,
                                               t.point,
                                               t.deadline,
                                               p.name AS project_name,
                                               a.name AS assignee_name,
                                               adm.name AS admin_name,
                                               ls.status AS status_name
                                           FROM tasks t
                                           JOIN projects p ON t.projects_id = p.id
                                           JOIN assignees a ON t.assignees_id = a.id
                                           JOIN admins adm ON t.admins_id = adm.id
                                           LEFT JOIN latest_status ls ON ls.tasks_id = t.id AND ls.rn = 1
                                           WHERE t.id = ?
                                       ORDER BY t.id ASC;
                  """;
//"SELECT id, name, 'desc' , point, deadline FROM tasks WHERE assignees_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assigneeId);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            model.addColumn("NAME");
            model.addColumn("Description");
            model.addColumn("Point");
            model.addColumn("Deadline");
            model.addColumn("Status");
            model.addColumn("ID");
//
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("task_name"),
                    rs.getString("desc"),
                    rs.getInt("point"),
                    rs.getDate("deadline"),
                    rs.getString("status_name"),
                    rs.getInt("id")
                });
            }

//            taskTable1.setModel(model);
            taskTable1.setModel(model);

            // Sembunyikan kolom terakhir (Task ID)
            taskTable1.getColumnModel().getColumn(5).setMinWidth(0);
            taskTable1.getColumnModel().getColumn(5).setMaxWidth(0);
            taskTable1.getColumnModel().getColumn(5).setWidth(0);

            // Atur warna teks semua sel
            taskTable1.setForeground(Color.BLACK);

            // Atur warna latar belakang baris biasa
            taskTable1.setBackground(Color.WHITE); // Latar belakang umum

            // Atur warna baris yang dipilih
            taskTable1.setSelectionBackground(new Color(171, 203, 202));
            taskTable1.setSelectionForeground(Color.BLACK); // Warna teks saat dipilih

            // Atur warna header tabel
            taskTable1.getTableHeader().setForeground(Color.WHITE);

            // Atur font seluruh tabel
            taskTable1.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Font isi tabel

            //Atur Header
            taskTable1.getTableHeader().setPreferredSize(new Dimension(taskTable1.getWidth(), 40));
            DefaultTableCellRenderer centerHeaderRenderer = new DefaultTableCellRenderer();
            centerHeaderRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            centerHeaderRenderer.setForeground(Color.BLACK);
            centerHeaderRenderer.setBackground(Color.LIGHT_GRAY);
            centerHeaderRenderer.setFont(new Font("Segoe UI", Font.BOLD, 36));

            for (int i = 0; i < taskTable1.getColumnModel().getColumnCount(); i++) {
                taskTable1.getColumnModel().getColumn(i).setHeaderRenderer(centerHeaderRenderer);
            }

            // Atur tinggi baris 
            taskTable1.setRowHeight(40);

            // Atur lebar kolom
            taskTable1.getColumnModel().getColumn(0).setPreferredWidth(150);
            taskTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            taskTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            taskTable1.getColumnModel().getColumn(3).setPreferredWidth(120);
            taskTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
            taskTable1.getColumnModel().getColumn(5).setPreferredWidth(50);

            taskTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (!isSelected) {
                        if (row % 2 == 0) {
                            c.setBackground(new Color(245, 245, 245));
                        } else {
                            c.setBackground(new Color(230, 230, 230));
                        }
                    } else {
                        c.setBackground(table.getSelectionBackground()); // Tetap pakai warna seleksi jika dipilih
                    }

                    c.setForeground(Color.BLACK);
                    return c;
                }
            });

            //kolom status
            taskTable1.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

            //klik mouse menuju detail
            taskTable1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = taskTable1.rowAtPoint(e.getPoint());
                    int col = taskTable1.columnAtPoint(e.getPoint());

                    if (col == 4) { // kolom status
//                        String status = taskTable1.getValueAt(row, col).toString();
//                        if (status.equalsIgnoreCase("under review")) {
                            int taskId = Integer.parseInt(taskTable1.getValueAt(row, 5).toString());
                            hideSelf();
                            new DetailTask(taskId, assigneeId).setVisible(true);
//                        }
                    }
                }
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    class StatusCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            String text = (value == null) ? "" : value.toString();  // Cek null dulu
            JLabel label = new JLabel(text, JLabel.CENTER);
            label.setOpaque(true);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setForeground(Color.WHITE);

            String status = text.toLowerCase();
            switch (status) {
                case "pending" ->
                    label.setBackground(new Color(255, 153, 51));      // Oranye
                case "ongoing" ->
                    label.setBackground(new Color(51, 153, 255));      // Biru
                case "under review" ->
                    label.setBackground(new Color(204, 153, 255)); // Ungu
                case "completed" ->
                    label.setBackground(new Color(102, 204, 102));   // Hijau
                default ->
                    label.setBackground(Color.GRAY);
            }

            if (isSelected) {
                label.setBackground(new Color(171, 203, 202)); // warna seleksi
                label.setForeground(Color.BLACK);
            }

            return label;
        }

    }

    private void setupMenuHoverEffect() {
        // TxtDashboard
        TxtDashboard.setOpaque(true);
        TxtDashboard.setBackground(new java.awt.Color(211, 211, 211));
        TxtDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TxtDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                TxtDashboard.setBackground(new java.awt.Color(191, 191, 191));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                TxtDashboard.setBackground(new java.awt.Color(211, 211, 211));
            }
        });

        // Ulangi untuk TxtProject, TxtTask, TxtLogout...
        // TxtProject
        TxtProject.setOpaque(true);
        TxtProject.setBackground(new java.awt.Color(211, 211, 211));
        TxtProject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TxtProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                TxtProject.setBackground(new java.awt.Color(191, 191, 191));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                TxtProject.setBackground(new java.awt.Color(211, 211, 211));
            }
        });
        
        // TxtTask
        TxtTask.setOpaque(true);
        TxtTask.setBackground(new java.awt.Color(211, 211, 211));
        TxtTask.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TxtTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                TxtTask.setBackground(new java.awt.Color(191, 191, 191));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                TxtTask.setBackground(new java.awt.Color(211, 211, 211));
            }
        });
        
        // TxtLogOut
        TxtLogout.setOpaque(true);
        TxtLogout.setBackground(new java.awt.Color(211, 211, 211));
        TxtLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TxtLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                TxtTask.setBackground(new java.awt.Color(191, 191, 191));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                TxtTask.setBackground(new java.awt.Color(211, 211, 211));
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SidebarPanel = new javax.swing.JPanel();
        TxtDashboard = new javax.swing.JLabel();
        TxtProject = new javax.swing.JLabel();
        TxtLogout = new javax.swing.JLabel();
        TxtTask = new javax.swing.JLabel();
        LineSidebar = new javax.swing.JSeparator();
        LineSidebar1 = new javax.swing.JSeparator();
        LogoArasaka = new javax.swing.JLabel();
        TxtArasaka = new javax.swing.JLabel();
        TxtSocialMedia = new javax.swing.JLabel();
        TxtProjectManagement = new javax.swing.JLabel();
        welcomeLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        SidebarPanel.setBackground(new java.awt.Color(211, 211, 211));
        SidebarPanel.setPreferredSize(new java.awt.Dimension(220, 420));

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

        LineSidebar.setForeground(new java.awt.Color(0, 0, 0));

        LineSidebar1.setForeground(new java.awt.Color(0, 0, 0));

        LogoArasaka.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconarasaka.png"))); // NOI18N

        TxtArasaka.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtArasaka.setText("Arasaka");

        TxtSocialMedia.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtSocialMedia.setText("Social Media ");

        TxtProjectManagement.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtProjectManagement.setText("Project Management");

        javax.swing.GroupLayout SidebarPanelLayout = new javax.swing.GroupLayout(SidebarPanel);
        SidebarPanel.setLayout(SidebarPanelLayout);
        SidebarPanelLayout.setHorizontalGroup(
            SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarPanelLayout.createSequentialGroup()
                .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LineSidebar)
                            .addComponent(LineSidebar1)))
                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtProject, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtTask, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(TxtLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SidebarPanelLayout.createSequentialGroup()
                                .addComponent(LogoArasaka)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                                        .addComponent(TxtArasaka, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidebarPanelLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(TxtSocialMedia, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(SidebarPanelLayout.createSequentialGroup()
                                .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(TxtProjectManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(TxtDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        SidebarPanelLayout.setVerticalGroup(
            SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarPanelLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LogoArasaka)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidebarPanelLayout.createSequentialGroup()
                        .addComponent(TxtArasaka)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtSocialMedia)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtProjectManagement)
                .addGap(76, 76, 76)
                .addComponent(TxtDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LineSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TxtProject, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxtTask, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
                .addComponent(LineSidebar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxtLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        welcomeLabel.setText("jLabel1");

        taskTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Project Name", "Deadline", "Point", "Status", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(taskTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(831, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1111, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtDashboardMouseClicked
        DashboardUser dashboard = new DashboardUser(assigneeId);
        dashboard.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtDashboardMouseClicked

    private void TxtProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtProjectMouseClicked
        AssigneProject project = new AssigneProject(assigneeId);
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

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_TxtLogoutMouseClicked

    private void TxtTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtTaskMouseClicked
        AssigneTask task = new AssigneTask(assigneeId);
        task.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtTaskMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AssigneTask.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                int userId = 1; // ⬅️ ganti dengan id user yang sesuai
                new AssigneTask(userId).setVisible(true); // ✅ constructor with userId
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator LineSidebar;
    private javax.swing.JSeparator LineSidebar1;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel SidebarPanel;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable taskTable1;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
