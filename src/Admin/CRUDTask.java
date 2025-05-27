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
    private int adminId;
    
    /**
     * Creates new form CRUDTask
     */
    public CRUDTask(int adminId) {
        this.adminId = adminId;
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
            return column == 6; 
        }
    };

    model.setRowCount(0); 
    model.setColumnIdentifiers(new Object[]{
        "Task Name", "Project", "Deadline", "Assignee", "Point", "Status", "Action", "Task Id"
    });

    String query = """
        SELECT 
            t.id,
            t.name AS task_name,
            t.desc,
            t.point,
            t.deadline,
            p.name AS project_name,
            a.name AS assignee_name,
            st.status AS status_name
       FROM 
            tasks t
       JOIN 
            projects p ON t.projects_id = p.id
       JOIN 
            assignees a ON t.assignees_id = a.id
       LEFT JOIN 
            (
                SELECT st1.*
                FROM status_tracks st1
                INNER JOIN (
                        SELECT tasks_id, MAX(created_at) AS latest
                        FROM status_tracks
                        GROUP BY tasks_id
                ) st2 ON st1.tasks_id = st2.tasks_id AND st1.created_at = st2.latest
            ) st ON st.tasks_id = t.id;
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
                rs.getString("status_name"),
                "Edit/Delete",
                rs.getInt("id")
            };
            model.addRow(row);
        }

        TabelCRUDTask.setModel(model);
        
        // Sembunyikan kolom terakhir (Task ID)
        TabelCRUDTask.getColumnModel().getColumn(7).setMinWidth(0);
        TabelCRUDTask.getColumnModel().getColumn(7).setMaxWidth(0);
        TabelCRUDTask.getColumnModel().getColumn(7).setWidth(0);
        
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
        TabelCRUDTask.getColumnModel().getColumn(5).setPreferredWidth(100); 
        TabelCRUDTask.getColumnModel().getColumn(6).setPreferredWidth(120); 

        TabelCRUDTask.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        TabelCRUDTask.getColumn("Action").setCellRenderer(new ButtonRendererTask());
        TabelCRUDTask.getColumn("Action").setCellEditor(new ButtonEditorTask(new JCheckBox(), this));
        
        //hover kursor kolom status action
        TabelCRUDTask.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
        int row = TabelCRUDTask.rowAtPoint(e.getPoint());
        int col = TabelCRUDTask.columnAtPoint(e.getPoint());

        if (col == 6) { 
            TabelCRUDTask.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (col == 5) { 
            String status = TabelCRUDTask.getValueAt(row, col).toString();
            if (status.equalsIgnoreCase("under review")) {
                TabelCRUDTask.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return;
            }
        }
        TabelCRUDTask.setCursor(Cursor.getDefaultCursor());
        }
        });
        
        //kolom status
        TabelCRUDTask.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());

        //klik mouse hover
        TabelCRUDTask.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
        int row = TabelCRUDTask.rowAtPoint(e.getPoint());
        int col = TabelCRUDTask.columnAtPoint(e.getPoint());

        if (col == 6) {
            // Kolom "Action" selalu cursor tangan
            TabelCRUDTask.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (col == 5) {
            // Kolom "Status" cursor tangan hanya jika status "under review"
            String status = TabelCRUDTask.getValueAt(row, col).toString();
            if (status.equalsIgnoreCase("under review")) {
                TabelCRUDTask.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                TabelCRUDTask.setCursor(Cursor.getDefaultCursor());
            }
        } else {
            TabelCRUDTask.setCursor(Cursor.getDefaultCursor());
        }
        }
        });
        
        //klik mouse menuju file
        TabelCRUDTask.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
        int row = TabelCRUDTask.rowAtPoint(e.getPoint());
        int col = TabelCRUDTask.columnAtPoint(e.getPoint());

        if (col == 5) { // kolom status
            String status = TabelCRUDTask.getValueAt(row, col).toString();
            if (status.equalsIgnoreCase("under review")) {
                int taskId = Integer.parseInt(TabelCRUDTask.getValueAt(row, 7).toString());
                new ReviewTask(adminId, taskId).setVisible(true);
            }
        }
        }
        });
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to load tasks: " + e.getMessage());
    }
}
class ButtonRendererTask extends JPanel implements TableCellRenderer {
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");

    public ButtonRendererTask() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Style tombol Edit
        editButton.setBackground(new Color(30, 144, 255)); 
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorderPainted(false);

        // Style tombol Delete
        deleteButton.setBackground(new Color(220, 20, 60)); 
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        
        add(editButton);
        add(deleteButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
        setBackground(table.getSelectionBackground());
    } else {
        if (row % 2 == 0) {
            setBackground(new Color(245, 245, 245)); 
        } else {
            setBackground(new Color(230, 230, 230)); 
        }
    }
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
        
        editButton.setBackground(new Color(30, 144, 255));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorderPainted(false);

        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);

        panel.add(editButton);
        panel.add(deleteButton);

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int taskId = Integer.parseInt(table.getValueAt(row, 7).toString());
                EditTask editTaskWindow = new EditTask(adminId, taskId);
                editTaskWindow.setVisible(true);
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
        panel.setBackground(table.getBackground());
    if (isSelected) {
        panel.setBackground(table.getSelectionBackground());
    } else {
        if (row % 2 == 0) {
            panel.setBackground(new Color(245, 245, 245));
        } else {
            panel.setBackground(new Color(230, 230, 230));
        }
    }
        this.table = table;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}

class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = new JLabel(value.toString(), JLabel.CENTER);
        label.setOpaque(true);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);

        String status = value.toString().toLowerCase();
        switch (status) {
            case "pending" -> label.setBackground(new Color(255, 153, 51));      // Oranye
            case "ongoing" -> label.setBackground(new Color(51, 153, 255));     // Biru
            case "under review" -> label.setBackground(new Color(204, 153, 255)); // Ungu
            case "completed" -> label.setBackground(new Color(102, 204, 102));     // Hijau
            default -> label.setBackground(Color.GRAY);
        }

        if (isSelected) {
            label.setBackground(new Color(171, 203, 202)); // warna seleksi
            label.setForeground(Color.BLACK);
        }

        return label;
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

        Task = new javax.swing.JLabel();
        BtnAddTask = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelCRUDTask = new javax.swing.JTable();
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

        Task.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Task.setForeground(new java.awt.Color(12, 44, 71));
        Task.setText("TASK");

        BtnAddTask.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnAddTask.setText("Add Task");
        BtnAddTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAddTaskActionPerformed(evt);
            }
        });

        TabelCRUDTask.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Project Name", "Deadline", "Assignee", "Point", "Status", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TabelCRUDTask);

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Task)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnAddTask, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1070, Short.MAX_VALUE))
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BtnAddTask, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Task))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
            .addComponent(SidebarPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnAddTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAddTaskActionPerformed
        AddTask addTaskForm = new AddTask(adminId);
        addTaskForm.setVisible(true);
        this.dispose();       
    }//GEN-LAST:event_BtnAddTaskActionPerformed

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
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CRUDTask().setVisible(true);
            }
        });*/
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAddTask;
    private javax.swing.JSeparator LineSidebar2;
    private javax.swing.JSeparator LineSidebar3;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel SidebarPanel1;
    private javax.swing.JTable TabelCRUDTask;
    private javax.swing.JLabel Task;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JLabel TxtUser;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
