/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import Database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dinda
 */
public class CRUDTask extends javax.swing.JFrame {

    /**
     * Creates new form CRUDTask
     */
    public CRUDTask() {
        initComponents();
        
        // Pengaturan efek hover dan kursor pada label sidebar
        setupSidebarLabel(TxtDashboard);
        setupSidebarLabel(TxtProject);
        setupSidebarLabel(TxtTask);
        setupSidebarLabel(TxtLogout);
        // Untuk menampilkan data
        loadTaskData();
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
    
    // Method untuk loaddata tabel
    private void loadTaskData() {
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5; 
        }
    };

    model.setRowCount(0); 
    model.setColumnIdentifiers(new Object[]{
        "Task Name", "Project", "Deadline", "Assignee", "Point", "Action"
    });

    String query = """
        SELECT 
            tasks.id,
            tasks.name AS task_name,
            tasks.desc,
            tasks.point,
            tasks.deadline,
            projects.name AS project_name,
            assignees.name AS assignee_name
        FROM 
            tasks
        JOIN 
            projects ON tasks.projects_id = projects.id
        JOIN 
            assignees ON tasks.assignees_id = assignees.id
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            Object[] row = {
                rs.getString("task_name"),
                rs.getString("project_name"),
                rs.getString("deadline"),
                rs.getString("assignee_name"),
                rs.getInt("point"),
                "Edit/Delete"
            };
            model.addRow(row);
        }

        TabelCRUDTask.setModel(model);
        
        // Atur warna teks semua sel
        TabelCRUDTask.setForeground(Color.BLACK); 

        // Atur warna latar belakang baris biasa
        TabelCRUDTask.setBackground(Color.WHITE); // Latar belakang umum

        // Atur warna baris yang dipilih
        TabelCRUDTask.setSelectionBackground(new Color(171, 203, 202)); 
        TabelCRUDTask.setSelectionForeground(Color.BLACK); // Warna teks saat dipilih

        // Atur warna header tabel
        TabelCRUDTask.getTableHeader().setForeground(Color.WHITE);
       
        // Atur font seluruh tabel
        TabelCRUDTask.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Font isi tabel
        
        //Atur Header
        TabelCRUDTask.getTableHeader().setPreferredSize(new Dimension(TabelCRUDTask.getWidth(),40));
        DefaultTableCellRenderer centerHeaderRenderer = new DefaultTableCellRenderer();
        centerHeaderRenderer.setHorizontalAlignment(SwingConstants.CENTER); 
        centerHeaderRenderer.setForeground(Color.BLACK);                    
        centerHeaderRenderer.setBackground(Color.LIGHT_GRAY);                
        centerHeaderRenderer.setFont(new Font("Segoe UI", Font.BOLD, 36));  

        for (int i = 0; i < TabelCRUDTask.getColumnModel().getColumnCount(); i++) {
            TabelCRUDTask.getColumnModel().getColumn(i).setHeaderRenderer(centerHeaderRenderer);
        }
        
        // Atur tinggi baris 
        TabelCRUDTask.setRowHeight(40);

        // Atur lebar kolom
        TabelCRUDTask.getColumnModel().getColumn(0).setPreferredWidth(150);
        TabelCRUDTask.getColumnModel().getColumn(1).setPreferredWidth(100);
        TabelCRUDTask.getColumnModel().getColumn(2).setPreferredWidth(100);
        TabelCRUDTask.getColumnModel().getColumn(3).setPreferredWidth(120);
        TabelCRUDTask.getColumnModel().getColumn(4).setPreferredWidth(50);
        TabelCRUDTask.getColumnModel().getColumn(5).setPreferredWidth(120);

        TabelCRUDTask.getColumn("Action").setCellRenderer(new ButtonRendererTask());
        TabelCRUDTask.getColumn("Action").setCellEditor(new ButtonEditorTask(new JCheckBox(), this));
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to load tasks: " + e.getMessage());
    }
}
class ButtonRendererTask extends JPanel implements TableCellRenderer {
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");

    public ButtonRendererTask() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(editButton);
        add(deleteButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
class ButtonEditorTask extends DefaultCellEditor {
    protected JPanel panel;
    protected JButton editButton;
    protected JButton deleteButton;
    private CRUDTask parent;
    private JTable table;

    public ButtonEditorTask(JCheckBox checkBox, CRUDTask parent) {
        super(checkBox);
        this.parent = parent;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        panel.add(editButton);
        panel.add(deleteButton);

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String taskName = table.getValueAt(row, 0).toString();
                new EditTask(taskName).setVisible(true); 
                parent.dispose(); 
            }
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String taskName = table.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete this task? \"" + taskName + "\"?", "Confirmation:", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE name = ?");
                        stmt.setString(1, taskName);
                        stmt.executeUpdate();
                        conn.close();

                        ((DefaultTableModel) table.getModel()).removeRow(row);
                        JOptionPane.showMessageDialog(null, "Task was successfully deleted");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Failed to delete: " + ex.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                 int row, int column) {
        this.table = table;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
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
        Task = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelCRUDTask = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 768));

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

        Task.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Task.setForeground(new java.awt.Color(12, 44, 71));
        Task.setText("TASK");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Add Task");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        TabelCRUDTask.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Project Name", "Deadline", "Assignee", "Point", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TabelCRUDTask);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Task)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1070, Short.MAX_VALUE))
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Task))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TxtDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtDashboardMouseClicked
       DashboardAdmin dashboard = new DashboardAdmin();
       dashboard.setVisible(true);
       this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtDashboardMouseClicked

    private void TxtProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtProjectMouseClicked
        CRUDProject project = new CRUDProject();
        project.setVisible(true);  
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtProjectMouseClicked

    private void TxtTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtTaskMouseClicked
        CRUDTask task = new CRUDTask();
        task.setVisible(true);  
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtTaskMouseClicked

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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CRUDTask.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CRUDTask.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CRUDTask.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CRUDTask.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CRUDTask().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator LineSidebar;
    private javax.swing.JSeparator LineSidebar1;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel SidebarPanel;
    private javax.swing.JTable TabelCRUDTask;
    private javax.swing.JLabel Task;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
