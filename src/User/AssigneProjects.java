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
public class AssigneProjects extends JFrame {
    private JPanel projectPanel;
    private int assigneeId;
    private JLabel headerLabel;

    public AssigneProjects(int assigneeId) { 
        this.assigneeId = assigneeId;
        setTitle("Assignee Projects");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
//         //TxtDahboard
//        TxtDashboard.setOpaque(true);
//        TxtDashboard.setBackground(new java.awt.Color(211, 211, 211));
//        TxtDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
//
//        TxtDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
//        public void mouseEntered(java.awt.event.MouseEvent evt) {
//            TxtDashboard.setBackground(new java.awt.Color(191, 191, 191));
//        }
//        
//        public void mouseExited(java.awt.event.MouseEvent evt) {
//        TxtDashboard.setBackground(new java.awt.Color(211, 211, 211));
//        }
//        });
//        
//        // TxtProject
//        TxtProject.setOpaque(true);
//        TxtProject.setBackground(new java.awt.Color(211, 211, 211));
//        TxtProject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
//        TxtProject.addMouseListener(new java.awt.event.MouseAdapter() {
//        public void mouseEntered(java.awt.event.MouseEvent evt) {
//        TxtProject.setBackground(new java.awt.Color(191, 191, 191));
//        }
//        public void mouseExited(java.awt.event.MouseEvent evt) {
//        TxtProject.setBackground(new java.awt.Color(211, 211, 211));
//        }
//        });
//
//        // TxtTask
//        TxtTask.setOpaque(true);
//        TxtTask.setBackground(new java.awt.Color(211, 211, 211));
//        TxtTask.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
//        TxtTask.addMouseListener(new java.awt.event.MouseAdapter() {
//        public void mouseEntered(java.awt.event.MouseEvent evt) {
//        TxtTask.setBackground(new java.awt.Color(191, 191, 191));
//        }
//        public void mouseExited(java.awt.event.MouseEvent evt) {
//        TxtTask.setBackground(new java.awt.Color(211, 211, 211));
//        }
//        });
//        
//        // TxtLogout
//        TxtLogout.setOpaque(true);
//        TxtLogout.setBackground(new java.awt.Color(211, 211, 211));
//        TxtLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
//        TxtLogout.addMouseListener(new java.awt.event.MouseAdapter() {
//        public void mouseEntered(java.awt.event.MouseEvent evt) {
//        TxtLogout.setBackground(new java.awt.Color(191, 191, 191));
//        }
//        public void mouseExited(java.awt.event.MouseEvent evt) {
//        TxtLogout.setBackground(new java.awt.Color(211, 211, 211));
//        }
//        });
        // Sidebar kiri
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(255, 153, 51)); // orange
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel logo = new JLabel("<html><div style='text-align:center; padding-left:25px;'>Arasaka Social Media<br>Project Management</div></html>");
        logo.setFont(new Font("Arial", Font.BOLD, 14));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton dashboardButton = createSidebarButton("DASHBOARD");
        dashboardButton.addActionListener(e -> {
            dispose();
            new DashboardUserUI(assigneeId).setVisible(true);
        });
        sidebar.add(dashboardButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton projectButton = createSidebarButton("PROJECT");
        projectButton.setEnabled(false);
        sidebar.add(projectButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton taskButton = createSidebarButton("TASK");
        taskButton.addActionListener(e -> {
            dispose();
//            new AssigneTasks().setVisible(true); // belum ada assignee task
        });
        sidebar.add(taskButton);
        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = createSidebarButton("LOGOUT");
//        logoutButton.addActionListener(e -> {
//            dispose(); 
//            new LoginPage().setVisible(true); 
//        });
        sidebar.add(logoutButton);
        add(sidebar, BorderLayout.WEST);

        // Panel konten utama
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        headerLabel = new JLabel("{user} Assignee Project"); //nanti diganti namanya
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(headerLabel, BorderLayout.NORTH);

        // Panel daftar project
        projectPanel = new JPanel();
        projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.Y_AXIS));
        projectPanel.setBackground(new Color(240, 255, 200)); 

        JScrollPane scrollPane = new JScrollPane(projectPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        addProjectHeader();
        loadProjects();
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(255, 153, 51));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    private void addProjectHeader() {
         JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 4));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        headerPanel.setBackground(new Color(210, 255, 100));
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));

        JLabel nameHeader = new JLabel("Project Name");
        nameHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameHeader.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel progressHeaderPanel = new JPanel(new BorderLayout());
        progressHeaderPanel.setOpaque(false);
        JLabel progressHeader = new JLabel("Progress", SwingConstants.CENTER);
        progressHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        progressHeaderPanel.add(progressHeader, BorderLayout.CENTER);

        JLabel remainingHeader = new JLabel("Remaining Time", SwingConstants.CENTER);
        remainingHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        remainingHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel actionHeader = new JLabel(""); 
        actionHeader.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(nameHeader);
        headerPanel.add(progressHeaderPanel);
        headerPanel.add(remainingHeader);
        headerPanel.add(actionHeader);

        projectPanel.add(headerPanel);
    }

    private void addProjectRow(String projectName, int progress, String remainingTime, int projectId, int assigneeId) {
    final int currentProjectId = projectId;
    int assigneeIdProjects = assigneeId;
    JPanel rowPanel = new JPanel(new GridLayout(1, 4));
    rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    rowPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
    rowPanel.setBackground(new Color(235, 255, 150));

    JLabel nameLabel = new JLabel(projectName);
    nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

    // Progress Panel
    JPanel progressPanel = new JPanel(new BorderLayout());
    progressPanel.setOpaque(false);
    
    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setValue(progress);
    progressBar.setStringPainted(false);
    
    JLabel progressLabel = new JLabel(progress + "%", SwingConstants.CENTER);
    progressPanel.add(progressBar, BorderLayout.CENTER);
    progressPanel.add(progressLabel, BorderLayout.SOUTH);

    JLabel remainingLabel = new JLabel(remainingTime, SwingConstants.CENTER);

    JButton detailsButton = new JButton("Details");
    detailsButton.addActionListener(e -> {
        dispose();
//        System.out.println("DEBUG: Mengklik project ID " + currentProjectId);
        new ProjectDetailsUI(currentProjectId, assigneeIdProjects).setVisible(true);
    });

    rowPanel.add(nameLabel);
    rowPanel.add(progressPanel);
    rowPanel.add(remainingLabel);
    rowPanel.add(detailsButton);

    projectPanel.add(Box.createVerticalStrut(5)); 
    projectPanel.add(rowPanel);
    }
    private void loadProjects() {
        try (Connection conn = DatabaseConnection.getConnection()){
            String query = """
            SELECT DISTINCT p.id, p.name
            FROM projects p
            JOIN tasks t ON p.id = t.projects_id
            WHERE t.assignees_id = ?
            """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, assigneeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                final int projectId = rs.getInt("id");
                String name = rs.getString("name");

                int remainingDays = getRemainingTimeForProject(projectId);
                String remainingStr = (remainingDays >= 0) ? remainingDays + " days" : "No deadline";

                int progress = getProjectProgress(projectId);

                addProjectRow(name, progress, remainingStr, projectId, this.assigneeId);
            }

            rs.close();
            ps.close();
            
            //nama user header
            String queryUserHeaderName = "SELECT name FROM assignees WHERE id = ? ";
            PreparedStatement stmtUserHeaderName = conn.prepareStatement(queryUserHeaderName);
            stmtUserHeaderName.setInt(1, assigneeId);
            ResultSet rsUserHeaderName = stmtUserHeaderName.executeQuery();
            if (rsUserHeaderName.next()){
                String username = rsUserHeaderName.getString("name");
                headerLabel.setText(username +" Assignee Project");
                
            }
            rsUserHeaderName.close();
            stmtUserHeaderName.close();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int getProjectProgress(int projectId) {
    int totalTasks = 0;
    int completedTasks = 0;

    try {
        Connection conn = DatabaseConnection.getConnection();

        String totalQuery = "SELECT COUNT(*) AS total FROM tasks WHERE projects_id = ?";
        PreparedStatement psTotal = conn.prepareStatement(totalQuery);
        psTotal.setInt(1, projectId);
        ResultSet rsTotal = psTotal.executeQuery();
        if (rsTotal.next()) {
            totalTasks = rsTotal.getInt("total");
        }
        rsTotal.close();
        psTotal.close();

        String completedQuery = """
            SELECT COUNT(*) AS completed 
            FROM tasks t 
            JOIN status_tracks s ON t.id = s.tasks_id 
            WHERE t.projects_id = ? AND s.status = 'completed'
        """;
        PreparedStatement psCompleted = conn.prepareStatement(completedQuery);
        psCompleted.setInt(1, projectId);
        ResultSet rsCompleted = psCompleted.executeQuery();
        if (rsCompleted.next()) {
            completedTasks = rsCompleted.getInt("completed");
        }
        rsCompleted.close();
        psCompleted.close();

        conn.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }

    if (totalTasks == 0) return 0;
    return (int) (((double) completedTasks / totalTasks) * 100);
    }

    private int getRemainingTimeForProject(int projectId) {
        int remainingDays = -1;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT MAX(deadline) AS max_deadline FROM tasks WHERE projects_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Date maxDeadline = rs.getDate("max_deadline");
                if (maxDeadline != null) {
                    LocalDate deadline = maxDeadline.toLocalDate();
                    LocalDate today = LocalDate.now();
                    remainingDays = (int) ChronoUnit.DAYS.between(today, deadline);
                }
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return remainingDays;
    }
}
