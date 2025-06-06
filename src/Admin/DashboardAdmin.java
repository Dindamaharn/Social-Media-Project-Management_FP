/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin;

import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import Database.DatabaseConnection;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 *
 * @author NOVA
 */
public class DashboardAdmin extends javax.swing.JFrame {
    private int adminId;
    private JPanel activityPanelDetails;
//    private JTable tableTopUser;
    private DefaultTableModel topUserModel;
   
    /**
     * Creates new form DashboardAdmin
     */
    public DashboardAdmin(int adminId) {
        this.adminId = adminId;
        
        initComponents();
         // Pengaturan efek hover dan kursor pada label sidebar
        setupSidebarLabel(TxtDashboard);
        setupSidebarLabel(TxtUser);
        setupSidebarLabel(TxtProject);
        setupSidebarLabel(TxtTask);
        setupSidebarLabel(TxtLogout);
        
        loadTaskSummary();
        loadProjectSummary(); // ← ini ditambahkan
        loadUserSummary();       // Tambahkan
        loadTaskCount();         // Tambahkan
        loadTopUserTable(); // ← Tambahkan ini
        loadOverdueTasks();
        loadProjectData();
    }
    
    private void loadUserSummary() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String query = "SELECT COUNT(*) AS total FROM assignees"; // ganti 'users' sesuai nama tabel kamu
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int total = rs.getInt("total");
            lblTotalUser.setText(String.valueOf(total));
        }
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data user: " + e.getMessage());
    }
}
private void loadTaskCount() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String query = "SELECT COUNT(*) AS total FROM tasks"; // ganti 'task' sesuai nama tabel kamu
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int total = rs.getInt("total");
            lblTotalTask.setText(String.valueOf(total));
        }
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data task: " + e.getMessage());
    }
}

    private void loadProjectSummary() {
    try (Connection conn = DatabaseConnection.getConnection()) {
    String query = "SELECT COUNT(*) AS total FROM projects";
    PreparedStatement stmt = conn.prepareStatement(query);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
    int total = rs.getInt("total");
    lblTotalProject.setText(String.valueOf(total));
    }
    rs.close();
    stmt.close();
    } catch (SQLException e) {
    JOptionPane.showMessageDialog(this, "Gagal memuat data project: " + e.getMessage());
    }
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
    
     private void loadTaskSummary() {
        try (Connection conn = DatabaseConnection.getConnection()) {
        
        //nama user header
        String queryUserHeaderName = "SELECT name FROM admins WHERE id = ? ";
        PreparedStatement stmtUserHeaderName = conn.prepareStatement(queryUserHeaderName);
        stmtUserHeaderName.setInt(1, adminId);
        ResultSet rsUserHeaderName = stmtUserHeaderName.executeQuery();
        if (rsUserHeaderName.next()){
            String username = rsUserHeaderName.getString("name");
            greeting.setText("Hello, " + username +" Here's what happening today");
        }
        rsUserHeaderName.close();
        stmtUserHeaderName.close();
        } catch (SQLException ex) {
        ex.printStackTrace(); // atau tampilkan pesan error ke user
        }
    }
    
private void loadTopUserTable() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT assignees.name, SUM(tasks.point) AS total_point " +
                     "FROM tasks " +
                     "JOIN assignees ON tasks.assignees_id = assignees.id " +
                     "GROUP BY assignees_id " +
                     "ORDER BY total_point DESC " +
                     "LIMIT 5";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        // Set kolom
        topUserModel = new DefaultTableModel();
        topUserModel.setColumnIdentifiers(new String[] {"No", "Nama Assignee", "Total Point"});

        int no = 1;
        while (rs.next()) {
            String nama = rs.getString("name");
            int point = rs.getInt("total_point");
            topUserModel.addRow(new Object[]{no++, nama, point});
        }

        tableTopUser.setModel(topUserModel);

        rs.close();
        stmt.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data top user: " + e.getMessage());
    }
}

    private void loadOverdueTasks() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT t.id AS no, t.name AS task_name, p.name AS project_name " +
                         "FROM tasks t " +
                         "JOIN projects p ON t.projects_id = p.id " +
                         "WHERE t.deadline < CURDATE()";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            
            DefaultTableModel overdueTaskModel = new DefaultTableModel();
            overdueTaskModel.setColumnIdentifiers(new String[] {"No", "Task Name", "Project Name"});

            int no = 1;
            while (rs.next()) {
                String taskName = rs.getString("task_name");
                String projectName = rs.getString("project_name");
                overdueTaskModel.addRow(new Object[]{no++, taskName, projectName});
            }

         
            tableOverdueTask.setModel(overdueTaskModel);

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data overdue tasks: " + e.getMessage());
        }
    }

    private void loadProjectData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT p.id AS project_id, p.name AS project_name, " +
                         "       (SELECT COUNT(*) FROM status_tracks st " +
                         "        WHERE st.tasks_id IN (SELECT t.id FROM tasks t WHERE t.projects_id = p.id) " +
                         "        AND st.status = 'completed') AS completed_tasks, " +
                         "       (SELECT COUNT(*) FROM tasks t WHERE t.projects_id = p.id) AS total_tasks " +
                         "FROM projects p";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel projectModel = new DefaultTableModel();
            projectModel.setColumnIdentifiers(new String[] {"No", "Project Name", "Progress", "Task Count"});       

            int no = 1;
            while (rs.next()) {
                String projectName = rs.getString("project_name");
                int completedTasks = rs.getInt("completed_tasks");
                int totalTasks = rs.getInt("total_tasks");

                double progress = (totalTasks == 0) ? 0 : (double) completedTasks / totalTasks * 100;
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setValue((int) progress);
                progressBar.setStringPainted(true);

                projectModel.addRow(new Object[]{no++, projectName, progressBar, totalTasks});
            }
            tableAllUser.setModel(projectModel);

            tableAllUser.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    return (JProgressBar) value;
                }
            });
    
            tableAllUser.getColumnModel().getColumn(2).setCellEditor(null);


            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data project: " + e.getMessage());
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

        greeting = new javax.swing.JLabel();
        MainContent = new javax.swing.JPanel();
        TotalUser = new javax.swing.JPanel();
        lblTotalUser = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        TotalTask = new javax.swing.JPanel();
        lblTotalTask = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        TotalProject = new javax.swing.JPanel();
        lblTotalProject = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableAllUser = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableOverdueTask = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableTopUser = new javax.swing.JTable();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        greeting.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        greeting.setText("Hello, Bella! Here's what happening today");

        MainContent.setBackground(new java.awt.Color(12, 44, 71));

        TotalUser.setBackground(new java.awt.Color(214, 201, 197));
        TotalUser.setPreferredSize(new java.awt.Dimension(270, 130));

        lblTotalUser.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTotalUser.setText("30");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel14.setText("TOTAL");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel15.setText("USER");

        javax.swing.GroupLayout TotalUserLayout = new javax.swing.GroupLayout(TotalUser);
        TotalUser.setLayout(TotalUserLayout);
        TotalUserLayout.setHorizontalGroup(
            TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalUserLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                .addComponent(lblTotalUser)
                .addGap(45, 45, 45))
            .addGroup(TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TotalUserLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jLabel14)
                    .addContainerGap(186, Short.MAX_VALUE)))
        );
        TotalUserLayout.setVerticalGroup(
            TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalUserLayout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTotalUser)
                    .addComponent(jLabel15))
                .addGap(42, 42, 42))
            .addGroup(TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TotalUserLayout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(jLabel14)
                    .addContainerGap(66, Short.MAX_VALUE)))
        );

        TotalTask.setBackground(new java.awt.Color(214, 201, 197));

        lblTotalTask.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTotalTask.setText("5");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel24.setText("TOTAL");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel25.setText("TASK");

        javax.swing.GroupLayout TotalTaskLayout = new javax.swing.GroupLayout(TotalTask);
        TotalTask.setLayout(TotalTaskLayout);
        TotalTaskLayout.setHorizontalGroup(
            TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalTaskLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(lblTotalTask)
                .addGap(45, 45, 45))
            .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TotalTaskLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jLabel24)
                    .addContainerGap(186, Short.MAX_VALUE)))
        );
        TotalTaskLayout.setVerticalGroup(
            TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalTaskLayout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTotalTask)
                    .addComponent(jLabel25))
                .addGap(42, 42, 42))
            .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TotalTaskLayout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(jLabel24)
                    .addContainerGap(66, Short.MAX_VALUE)))
        );

        TotalProject.setBackground(new java.awt.Color(214, 201, 197));

        lblTotalProject.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTotalProject.setText("4");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel27.setText("TOTAL");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel28.setText("PROJECT");

        javax.swing.GroupLayout TotalProjectLayout = new javax.swing.GroupLayout(TotalProject);
        TotalProject.setLayout(TotalProjectLayout);
        TotalProjectLayout.setHorizontalGroup(
            TotalProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalProjectLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(lblTotalProject)
                .addGap(44, 44, 44))
            .addGroup(TotalProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TotalProjectLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jLabel27)
                    .addContainerGap(186, Short.MAX_VALUE)))
        );
        TotalProjectLayout.setVerticalGroup(
            TotalProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalProjectLayout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(TotalProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTotalProject)
                    .addComponent(jLabel28))
                .addGap(42, 42, 42))
            .addGroup(TotalProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TotalProjectLayout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(jLabel27)
                    .addContainerGap(66, Short.MAX_VALUE)))
        );

        tableAllUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tableAllUser);

        tableOverdueTask.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(tableOverdueTask);

        tableTopUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane4.setViewportView(tableTopUser);

        javax.swing.GroupLayout MainContentLayout = new javax.swing.GroupLayout(MainContent);
        MainContent.setLayout(MainContentLayout);
        MainContentLayout.setHorizontalGroup(
            MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContentLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainContentLayout.createSequentialGroup()
                        .addComponent(TotalUser, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TotalProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TotalTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(MainContentLayout.createSequentialGroup()
                        .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(MainContentLayout.createSequentialGroup()
                                .addGap(464, 464, 464)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3))
                        .addGap(0, 42, Short.MAX_VALUE))))
        );
        MainContentLayout.setVerticalGroup(
            MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContentLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TotalUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(407, Short.MAX_VALUE))
        );

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LineSidebar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxtLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SidebarPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(greeting)
                    .addComponent(MainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(greeting)
                .addGap(18, 18, 18)
                .addComponent(MainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(SidebarPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
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

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        

        /* Create and display the form */
        /* java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardAdmin().setVisible(true);
            }
        });
    }*/


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator LineSidebar2;
    private javax.swing.JSeparator LineSidebar3;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel MainContent;
    private javax.swing.JPanel SidebarPanel1;
    private javax.swing.JPanel TotalProject;
    private javax.swing.JPanel TotalTask;
    private javax.swing.JPanel TotalUser;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JLabel TxtUser;
    private javax.swing.JLabel greeting;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblTotalProject;
    private javax.swing.JLabel lblTotalTask;
    private javax.swing.JLabel lblTotalUser;
    private javax.swing.JTable tableAllUser;
    private javax.swing.JTable tableOverdueTask;
    private javax.swing.JTable tableTopUser;
    // End of variables declaration//GEN-END:variables
}
