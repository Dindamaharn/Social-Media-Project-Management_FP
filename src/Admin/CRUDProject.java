/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import Database.DatabaseConnection;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author NOVA
 */
public class CRUDProject extends javax.swing.JFrame {

    /**
     * Creates new form CRUDProject
     */
    public CRUDProject() {
        initComponents();
         loadProjectData();
        
        //TxtDahboard
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
        
        // TxtLogout
        TxtLogout.setOpaque(true);
        TxtLogout.setBackground(new java.awt.Color(211, 211, 211));
        TxtLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TxtLogout.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
        TxtLogout.setBackground(new java.awt.Color(191, 191, 191));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
        TxtLogout.setBackground(new java.awt.Color(211, 211, 211));
        }
        });
    }
    private void loadProjectData() {
    DefaultTableModel model = new DefaultTableModel() {
        public boolean isCellEditable(int row, int column) {
            return column == 3 || column == 4; // kolom edit dan delete bisa diklik
        }
    };

    model.addColumn("ID"); // hidden
    model.addColumn("Project Name");
    model.addColumn("Description");
    model.addColumn("Edit");
    model.addColumn("Delete");

    try {
        Connection conn = Database.DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM projects");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String desc = rs.getString("desc");

            model.addRow(new Object[]{id, name, desc, "Edit", "Delete"});
        }

        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + e.getMessage());
    }

    tableData.setModel(model);

    // Sembunyikan kolom ID
    tableData.getColumnModel().getColumn(0).setMinWidth(0);
    tableData.getColumnModel().getColumn(0).setMaxWidth(0);
    tableData.getColumnModel().getColumn(0).setWidth(0);

    // Render dan Editor tombol
    tableData.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
    tableData.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

    tableData.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
    tableData.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));
}
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean clicked;
    private int row;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, String labelType) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        this.label = labelType;

        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;
        button.setText(label);
        clicked = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (clicked) {
            int projectId = (int) table.getValueAt(row, 0); // ambil ID tersembunyi
            if (label.equals("Edit")) {
                new EditProject(projectId).setVisible(true); // pastikan ada constructor yg terima ID
            } else if (label.equals("Delete")) {
                int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus project ini?");
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteProject(projectId);
                }
            }
        }
        clicked = false;
        return label;
    }

    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    private void deleteProject(int projectId) {
        try {
            Connection conn = Database.DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM projects WHERE id = ?");
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Project berhasil dihapus!");
            ((DefaultTableModel) table.getModel()).removeRow(row); // hapus dari tabel
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus project: " + e.getMessage());
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

        PROJECT = new javax.swing.JLabel();
        MainContent = new javax.swing.JPanel();
        btnAddProject = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableData = new javax.swing.JTable();
        SidebarPanel = new javax.swing.JPanel();
        TxtDashboard = new javax.swing.JLabel();
        TxtProject = new javax.swing.JLabel();
        TxtLogout = new javax.swing.JLabel();
        TxtTask = new javax.swing.JLabel();
        LineSidebar = new javax.swing.JSeparator();
        LineSidebar1 = new javax.swing.JSeparator();
        TxtArasaka = new javax.swing.JLabel();
        TxtSocialMedia = new javax.swing.JLabel();
        TxtProjectManagement = new javax.swing.JLabel();
        LogoArasaka = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        PROJECT.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        PROJECT.setText("PROJECT");

        MainContent.setBackground(new java.awt.Color(255, 153, 51));

        btnAddProject.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnAddProject.setText("Add Project");
        btnAddProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProjectActionPerformed(evt);
            }
        });

        tableData.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tableData);

        javax.swing.GroupLayout MainContentLayout = new javax.swing.GroupLayout(MainContent);
        MainContent.setLayout(MainContentLayout);
        MainContentLayout.setHorizontalGroup(
            MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContentLayout.createSequentialGroup()
                .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainContentLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(btnAddProject, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MainContentLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        MainContentLayout.setVerticalGroup(
            MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContentLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnAddProject, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(148, Short.MAX_VALUE))
        );

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

        TxtArasaka.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtArasaka.setText("Arasaka");

        TxtSocialMedia.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtSocialMedia.setText("Social Media ");

        TxtProjectManagement.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        TxtProjectManagement.setText("Project Management");

        LogoArasaka.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconarasaka.png"))); // NOI18N

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
                        .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SidebarPanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(TxtDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(SidebarPanelLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TxtProject, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TxtTask, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SidebarPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(TxtProjectManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(SidebarPanelLayout.createSequentialGroup()
                                .addComponent(LogoArasaka)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(SidebarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(SidebarPanelLayout.createSequentialGroup()
                                        .addComponent(TxtArasaka, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidebarPanelLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(TxtSocialMedia, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
            .addGroup(SidebarPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(TxtLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LineSidebar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxtLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PROJECT)
                    .addComponent(MainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1254, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(PROJECT)
                .addGap(29, 29, 29)
                .addComponent(MainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(SidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        CRUDTask task = new CRUDTask();
        task.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtTaskMouseClicked

    private void btnAddProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProjectActionPerformed
        AddProject addproject = new AddProject(); // DetailPage adalah JFrame yang berisi halaman detail
        addproject.setVisible(true); // Menampilkan halaman detail
        this.dispose(); // Menutup frame saat ini jika diperlukan// TODO add your handling code here:/ TODO add your handling code here:
    }//GEN-LAST:event_btnAddProjectActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
         loadProjectData(); // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(CRUDProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CRUDProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CRUDProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CRUDProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CRUDProject().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator LineSidebar;
    private javax.swing.JSeparator LineSidebar1;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel MainContent;
    private javax.swing.JLabel PROJECT;
    private javax.swing.JPanel SidebarPanel;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JButton btnAddProject;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableData;
    // End of variables declaration//GEN-END:variables
}
