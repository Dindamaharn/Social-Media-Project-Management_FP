/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Database.DatabaseConnection;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author fikra
 */


public class ProjectDetailsUI extends JFrame {
    private JLabel projectName;
    private JLabel descLabel;
    private JLabel taskDoneCount;
    private JLabel totalTaskCount;
    private JProgressBar progressBar;
    private JLabel progressStatus;
    private int projectId;
    private JPanel taskListPanel;
    private int assigneeIdProjects;
    private JLabel userLabel;
    
    public ProjectDetailsUI(int projectId, int assigneeIdProjects) {
//        System.out.println("DEBUG: Received projectId = " + projectId);
        this.projectId = projectId;
        this.assigneeIdProjects = assigneeIdProjects;
//        System.out.println("DEBUG: Constructor - this.projectId = " + this.projectId);
        setTitle("Project Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // SIDEBAR 
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(255, 153, 51)); // orange
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        //  MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);

        JLabel logo = new JLabel("<html><div style='text-align:center; padding-left:25px;'>Arasaka Social Media<br>Project Management</div></html>");
        logo.setFont(new Font("Arial", Font.BOLD, 14));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton dashboardButton = createSidebarButton("DASHBOARD");
        dashboardButton.addActionListener(e -> {
            dispose();
            new DashboardUserUI(assigneeIdProjects).setVisible(true);
        });
        sidebar.add(dashboardButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton projectButton = createSidebarButton("PROJECT");
        projectButton.addActionListener(e -> {
            dispose();
            new AssigneProjects(assigneeIdProjects).setVisible(true);
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

        // Header user
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userLabel = new JLabel("{user}");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel detailLabel = new JLabel("Project Details");
        detailLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(userLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(detailLabel);
        headerPanel.setBackground(Color.LIGHT_GRAY);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // CONTENT
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(255, 140, 0));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Project name & task count
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.setBackground(new Color(255, 140, 0));
        topPanel.setPreferredSize(new Dimension(0, 45));
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JPanel projectNamePanel = new JPanel(new BorderLayout());
        projectNamePanel.setBackground(new Color(200, 255, 100));
        JLabel projectTitle = new JLabel("Project Name", JLabel.CENTER);
        projectTitle.setFont(new Font("Arial", Font.BOLD, 16));
        projectName = new JLabel("Loading...", JLabel.CENTER);
        projectName.setFont(new Font("Arial", Font.PLAIN, 16));
        projectNamePanel.add(projectTitle, BorderLayout.NORTH);
        projectNamePanel.add(projectName, BorderLayout.CENTER);

        JPanel taskDonePanel = new JPanel(new BorderLayout());
        taskDonePanel.setBackground(new Color(200, 255, 100));
        JLabel taskDoneTitle = new JLabel("TASK DONE", JLabel.CENTER);
        taskDoneCount = new JLabel("20", JLabel.CENTER);
        taskDonePanel.add(taskDoneTitle, BorderLayout.NORTH);
        taskDonePanel.add(taskDoneCount, BorderLayout.CENTER);

        JPanel totalTaskPanel = new JPanel(new BorderLayout());
        totalTaskPanel.setBackground(new Color(200, 255, 100));
        JLabel totalTaskTitle = new JLabel("TOTAL TASK", JLabel.CENTER);
        totalTaskCount = new JLabel("40", JLabel.CENTER);
        totalTaskPanel.add(totalTaskTitle, BorderLayout.NORTH);
        totalTaskPanel.add(totalTaskCount, BorderLayout.CENTER);

        topPanel.add(projectNamePanel);
        topPanel.add(taskDonePanel);
        topPanel.add(totalTaskPanel);

        // Deskripsi dan progress
        JPanel descProgressPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        descProgressPanel.setBackground(new Color(255, 140, 0));
        descProgressPanel.setPreferredSize(new Dimension(0, 90));
        descProgressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        

        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(new Color(200, 255, 100));
        descPanel.add(new JLabel("DESKRIPSI PROJECT", JLabel.LEFT), BorderLayout.NORTH);
        descPanel.setBorder(new EmptyBorder(10, 15, 0, 0));
        descLabel = new JLabel("Loading...", JLabel.LEFT);
        descPanel.add(descLabel, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBackground(new Color(200, 255, 100));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        JLabel progressLabel = new JLabel("PROGRESS");
        progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel barWithStatusPanel = new JPanel();
        barWithStatusPanel.setLayout(new BoxLayout(barWithStatusPanel, BoxLayout.X_AXIS));
        barWithStatusPanel.setOpaque(false);
        barWithStatusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        progressBar = new JProgressBar();
        progressBar.setValue(50);
        progressBar.setStringPainted(true);
        
        progressBar.setPreferredSize(new Dimension(150, 25));
        progressBar.setMaximumSize(new Dimension(150, 25));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        progressStatus = new JLabel("OnGoing");
        progressStatus.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        barWithStatusPanel.add(progressBar);
        barWithStatusPanel.add(progressStatus);

        progressPanel.add(Box.createVerticalStrut(10));
        progressPanel.add(progressLabel);
        progressPanel.add(Box.createVerticalStrut(10));
        progressPanel.add(barWithStatusPanel);

        descProgressPanel.add(descPanel);
        descProgressPanel.add(progressPanel);

        // Task List
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(new Color(200, 255, 100));
        taskListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        taskListPanel.setPreferredSize(new Dimension(0, 400)); 
        taskListPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        taskListPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        addTaskListHeader();

        loadTasks(taskListPanel);
        contentPanel.add(topPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(descProgressPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(taskListPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        loadProjectData();
    }
    
    private void addTaskListHeader() {
         JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 4));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        JLabel nameHeader = new JLabel("Task Name");
        nameHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameHeader.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel progressHeaderPanel = new JPanel(new BorderLayout());
        progressHeaderPanel.setOpaque(false);
        JLabel progressHeader = new JLabel("Due Date", SwingConstants.CENTER);
        progressHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        progressHeaderPanel.add(progressHeader, BorderLayout.CENTER);

        JLabel remainingHeader = new JLabel("Status", SwingConstants.CENTER);
        remainingHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        remainingHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel actionHeader = new JLabel(""); 
        actionHeader.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(nameHeader);
        headerPanel.add(progressHeaderPanel);
        headerPanel.add(remainingHeader);
        headerPanel.add(actionHeader);

        taskListPanel.add(headerPanel);
        taskListPanel.add(Box.createVerticalStrut(10));
    }
    
    private void loadProjectData() {
    try (Connection conn = DatabaseConnection.getConnection()){
        String queryUserHeaderName = "SELECT name FROM assignees WHERE id = ? ";
        PreparedStatement stmtUserHeaderName = conn.prepareStatement(queryUserHeaderName);
        stmtUserHeaderName.setInt(1, assigneeIdProjects);
        ResultSet rsUserHeaderName = stmtUserHeaderName.executeQuery();
        if (rsUserHeaderName.next()){
            String username = rsUserHeaderName.getString("name");
            userLabel.setText(username);
                
        }
        rsUserHeaderName.close();
        stmtUserHeaderName.close();
        
        String query = "SELECT name, `desc` FROM projects WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, projectId);
        ResultSet rs = stmt.executeQuery();
        System.out.println("DEBUG: projectId yang diterima = " + projectId);

        if (rs.next()) {
            String name = rs.getString("name");
            String desc = rs.getString("desc");
            
            // update UI
            projectName.setText(name);
            descLabel.setText(desc);
        }

        rs.close();
        stmt.close();
        
        String queryTotalTask = "SELECT COUNT(*) AS total FROM tasks WHERE projects_id = ?";
        PreparedStatement stmtTotal = conn.prepareStatement(queryTotalTask);
        stmtTotal.setInt(1, projectId);
        ResultSet rsTotal = stmtTotal.executeQuery();

        int totalTasks = 0;
        if (rsTotal.next()) {
            totalTasks = rsTotal.getInt("total");
            totalTaskCount.setText(String.valueOf(totalTasks));
        }

        rsTotal.close();
        stmtTotal.close();
        
        String queryDone = "SELECT COUNT(*) AS done FROM status_tracks st " +
                           "JOIN tasks t ON st.tasks_id = t.id " +
                           "WHERE t.projects_id = ? AND st.status = 'completed'";
        PreparedStatement stmtDone = conn.prepareStatement(queryDone);
        stmtDone.setInt(1, projectId);
        ResultSet rsDone = stmtDone.executeQuery();

        int doneTasks = 0;
        if (rsDone.next()) {
            doneTasks = rsDone.getInt("done");
            taskDoneCount.setText(String.valueOf(doneTasks));
        }

        rsDone.close();
        stmtDone.close();

        // Set progress bar
        int progress = totalTasks > 0 ? (doneTasks * 100 / totalTasks) : 0;
        if (progress == 100) {
            progressStatus.setText("Completed");
        } else if (progress > 0) {
            progressStatus.setText("On Going");
        } else {
            progressStatus.setText("Not Started");
        }
     
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    private void loadTasks(JPanel taskListPanel) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            String query = "SELECT t.id AS task_id, t.name, t.deadline, st.status " +
                           "FROM tasks t " +
                           "LEFT JOIN status_tracks st ON t.id = st.tasks_id " +
                           "WHERE t.projects_id = ? AND t.assignees_id = ? ";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, projectId);
            stmt.setInt(2, assigneeIdProjects);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                String name = rs.getString("name");
                String deadline = rs.getString("deadline");
                String status = rs.getString("status");

                JPanel row = new JPanel(new GridLayout(1, 4));
                row.setOpaque(false);
                row.setPreferredSize(new Dimension(0, 30));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

                row.add(new JLabel(name));
                row.add(new JLabel(deadline));
                row.add(new JLabel(status != null ? status : "Belum Ada Status"));

                JButton btn = new JButton("Details");
                btn.addActionListener(e -> {
//                    new UserTaskDetails(taskId).setVisible(true);
                });
                row.add(btn);

                taskListPanel.add(row);
                taskListPanel.add(Box.createVerticalStrut(5));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    
 }



