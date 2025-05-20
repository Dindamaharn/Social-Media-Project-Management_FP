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
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class DashboardUserUI extends JFrame {
    
    private int assigneeId;
    private JLabel taskAssignLabel;
    private JLabel taskInProgressLabel;
    private JLabel taskOverdueLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel totalPoints;
    private JLabel recentPoints;
    private JPanel activityPanel;
    private JLabel greeting;
    
    public DashboardUserUI(int assigneeId) {
        this.assigneeId = assigneeId;
        
        setTitle("Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLayout(new BorderLayout());

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
        dashboardButton.setEnabled(false);
        sidebar.add(dashboardButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton projectButton = createSidebarButton("PROJECT");
        projectButton.addActionListener(e -> {
            dispose();
            new AssigneProjects(assigneeId).setVisible(true);
        });
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


        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(255, 153, 51)); // orange

        // Header
        greeting = new JLabel("Hello, {user}! Here's what happening today");
        greeting.setFont(new Font("Arial", Font.BOLD, 20));
        greeting.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(greeting);

        // Top summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setBackground(new Color(255, 153, 51));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        String[] statuses = {"TASK ASSIGN", "TASK IN PROGRESS", "TASK OVERDUE"};
        taskAssignLabel = new JLabel("0");
        taskInProgressLabel = new JLabel("0");
        taskOverdueLabel = new JLabel("0");
        JLabel[] valueLabels = {taskAssignLabel, taskInProgressLabel, taskOverdueLabel};

        for (int i = 0; i < statuses.length; i++) {
            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.setBackground(new Color(200, 255, 100));
            box.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel title = new JLabel("<html><b>" + statuses[i] + "</b></html>");
            title.setFont(new Font("Arial", Font.BOLD, 16));
            
            JLabel value = valueLabels[i];
            value.setFont(new Font("Arial", Font.PLAIN, 24));
            
            box.add(title);
            box.add(Box.createVerticalStrut(10));
            box.add(value);
            summaryPanel.add(box);
        }

        mainPanel.add(summaryPanel);

        // Middle panels (user info + recent point)
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        middlePanel.setBackground(new Color(255, 153, 51));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(200, 255, 100));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        userPanel.add(new JLabel("<html><b>USERS</b></html>"));
        nameLabel = new JLabel("Name   : ");
        emailLabel = new JLabel("Email  : ");
        userPanel.add(nameLabel);
        userPanel.add(emailLabel);
       
        userPanel.add(new JLabel("Role   : assignee"));
        totalPoints = new JLabel("Total Points: ");
        userPanel.add(totalPoints);

        JPanel pointPanel = new JPanel();
        pointPanel.setLayout(new BoxLayout(pointPanel, BoxLayout.Y_AXIS));
        pointPanel.setBackground(new Color(200, 255, 100));
        pointPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pointPanel.add(new JLabel("<html><b>RECENT POINT</b></html>"));
        recentPoints = new JLabel("No Recent Point");
        pointPanel.add(recentPoints);

        middlePanel.add(userPanel);
        middlePanel.add(pointPanel);

        mainPanel.add(middlePanel);

        // Recent activity
        activityPanel = new JPanel();
        activityPanel.setLayout(new GridLayout(0, 3, 10, 5));
        activityPanel.setBackground(new Color(200, 255, 100));
        activityPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        activityPanel.add(new JLabel("<html><b>RECENT ACTIVITY</b></html>"));
        activityPanel.add(new JLabel(""));
        activityPanel.add(new JLabel(""));

        String[][] activities = {
                {"Project Name Here", "Task Name here", "Change to ..."},
                {"Project Name Here", "Task Name here", "Change to ..."},
                {"Project Name Here", "Task Name here", "Change to ..."},
        };

        for (String[] row : activities) {
            for (String col : row) {
                activityPanel.add(new JLabel(col));
            }
        }

        mainPanel.add(activityPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
        
        loadTaskSummary();
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
    
    private void loadTaskSummary() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            //nama user header
            String queryUserHeaderName = "SELECT name FROM assignees WHERE id = ? ";
            PreparedStatement stmtUserHeaderName = conn.prepareStatement(queryUserHeaderName);
            stmtUserHeaderName.setInt(1, assigneeId);
            ResultSet rsUserHeaderName = stmtUserHeaderName.executeQuery();
            if (rsUserHeaderName.next()){
                String username = rsUserHeaderName.getString("name");
                greeting.setText("Hello, " + username +" Here's what happening today");
                
            }
            rsUserHeaderName.close();
            stmtUserHeaderName.close();
            
            // Task Assign
            String queryAssign = "SELECT COUNT(*) FROM tasks WHERE assignees_id = ?";
            PreparedStatement stmtAssign = conn.prepareStatement(queryAssign);
            stmtAssign.setInt(1, assigneeId);
            ResultSet rsAssign = stmtAssign.executeQuery();
            if (rsAssign.next()) {
                int taskAssignCount = rsAssign.getInt(1);
                taskAssignLabel.setText(String.valueOf(taskAssignCount));
            }
            rsAssign.close();
            stmtAssign.close();

            // Task Overdue
            String queryOverdue = "SELECT COUNT(*) FROM tasks WHERE assignees_id = ? AND deadline < ?";
            PreparedStatement stmtOverdue = conn.prepareStatement(queryOverdue);
            stmtOverdue.setInt(1, assigneeId);
            stmtOverdue.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet rsOverdue = stmtOverdue.executeQuery();
            if (rsOverdue.next()) {
                int overdueCount = rsOverdue.getInt(1);
                taskOverdueLabel.setText(String.valueOf(overdueCount));
            }
            rsOverdue.close();
            stmtOverdue.close();

            // Task In Progress
            String queryInProgress = "SELECT COUNT(DISTINCT st.tasks_id) " +
                                     "FROM status_tracks st " +
                                     "JOIN tasks t ON st.tasks_id = t.id " +
                                     "WHERE t.assignees_id = ? AND st.status = 'in progress'";
            PreparedStatement stmtProgress = conn.prepareStatement(queryInProgress);
            stmtProgress.setInt(1, assigneeId);
            ResultSet rsProgress = stmtProgress.executeQuery();
            if (rsProgress.next()) {
                int inProgressCount = rsProgress.getInt(1);
                taskInProgressLabel.setText(String.valueOf(inProgressCount));
            }
            rsProgress.close();
            stmtProgress.close();
            
            //user name dan email
            String userInformation = "SELECT id, name, email FROM assignees WHERE id= ?";
            PreparedStatement stmtInformation = conn.prepareStatement(userInformation);
            stmtInformation.setInt(1, assigneeId);
            ResultSet rsInformation = stmtInformation.executeQuery();
            if (rsInformation.next()){
                String name = rsInformation.getString("name");
                String email = rsInformation.getString("email");

                nameLabel.setText("Name   : " + name);
                emailLabel.setText("Email  : " + email);
            }
            rsInformation.close();
            stmtInformation.close();
            
            //total pointnya
            String queryPoints = 
                    "SELECT SUM(t.point) AS total_points " +
                    "FROM tasks t " +
                    "JOIN ( " +
                    "    SELECT st.tasks_id " +
                    "    FROM status_tracks st " +
                    "    JOIN ( " +
                    "        SELECT tasks_id, MAX(created_at) AS latest_time " +
                    "        FROM status_tracks " +
                    "        GROUP BY tasks_id " +
                    "    ) latest ON st.tasks_id = latest.tasks_id AND st.created_at = latest.latest_time " +
                    "    WHERE st.status = 'completed' " +
                    ") completed_tasks ON t.id = completed_tasks.tasks_id " +
                    "WHERE t.assignees_id = ?";

            PreparedStatement stmtPoints = conn.prepareStatement(queryPoints);
            stmtPoints.setInt(1, assigneeId);
            ResultSet rsPoints = stmtPoints.executeQuery();
            if (rsPoints.next()) {
                int totalPointsQuery = rsPoints.getInt("total_points");
                totalPoints.setText("Total Points : " + totalPointsQuery);
            }
            rsPoints.close();
            stmtPoints.close();
            
            //recent pointnnya
            String queryRecentPoints = 
                "SELECT * FROM ( " +
                "    SELECT t.name, t.point, recent_completed.created_at " +
                "    FROM tasks t " +
                "    JOIN ( " +
                "        SELECT st.tasks_id, st.created_at " +
                "        FROM status_tracks st " +
                "        JOIN ( " +
                "            SELECT tasks_id, MAX(created_at) AS latest_time " +
                "            FROM status_tracks " +
                "            GROUP BY tasks_id " +
                "        ) latest ON st.tasks_id = latest.tasks_id AND st.created_at = latest.latest_time " +
                "        WHERE st.status = 'completed' AND st.created_at >= ? " +
                "    ) recent_completed ON t.id = recent_completed.tasks_id " +
                "    WHERE t.assignees_id = ? " +
                "    ORDER BY recent_completed.created_at DESC " +
                ") AS recent_limited " +
                "LIMIT 5";

            PreparedStatement stmtRecent = conn.prepareStatement(queryRecentPoints);
            stmtRecent.setDate(1, java.sql.Date.valueOf(LocalDate.now().minusDays(7)));
            stmtRecent.setInt(2, assigneeId);
            ResultSet rsRecent = stmtRecent.executeQuery();

            StringBuilder recentText = new StringBuilder("<html>");
            boolean hasRecent = false;
            while (rsRecent.next()) {
                hasRecent = true;
                String taskName = rsRecent.getString("name");
                int point = rsRecent.getInt("point");
                LocalDateTime completedAt = rsRecent.getTimestamp("created_at").toLocalDateTime();
                recentText.append(taskName).append(" (+").append(point).append(" pts)").append("<br>");
            }
            recentText.append("</html>");
            recentPoints.setText(hasRecent ? recentText.toString() : "No Recent Points");
            rsRecent.close();
            stmtRecent.close();
            
            // RECENT ACTIVITY
            String queryRecentActivity = 
                "SELECT p.name AS project_name, t.name AS task_name, st.status, st.updated_at " +
                "FROM status_tracks st " +
                "JOIN tasks t ON st.tasks_id = t.id " +
                "JOIN projects p ON t.projects_id = p.id " +
                "JOIN ( " +
                "    SELECT tasks_id, MAX(updated_at) AS latest_time " +
                "    FROM status_tracks " +
                "    GROUP BY tasks_id " +
                ") latest ON st.tasks_id = latest.tasks_id AND st.updated_at = latest.latest_time " +
                "WHERE t.assignees_id = ? " +
                "ORDER BY st.updated_at DESC " +
                "LIMIT 6";

            PreparedStatement stmtRecentActivity = conn.prepareStatement(queryRecentActivity);
            stmtRecentActivity.setInt(1, assigneeId);
            ResultSet rsActivity = stmtRecentActivity.executeQuery();

            Component[] components = activityPanel.getComponents();
            // hapus row sebelunya (header - index 0 to 2)
            for (int i = components.length - 1; i >= 3; i--) {
                activityPanel.remove(components[i]);
            }

            while (rsActivity.next()) {
                String projectName = rsActivity.getString("project_name");
                String taskName = rsActivity.getString("task_name");
                String status = rsActivity.getString("status");

                activityPanel.add(new JLabel(projectName));
                activityPanel.add(new JLabel(taskName));
                activityPanel.add(new JLabel("Changed to " + status));
            }

            activityPanel.revalidate();
            activityPanel.repaint();

            rsActivity.close();
            stmtRecentActivity.close();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
       }
    }
    public static void main(String[] args) {
        int testUserId = 3; // buat ngetest 
        SwingUtilities.invokeLater(() -> new DashboardUserUI(testUserId).setVisible(true));
    }
}

