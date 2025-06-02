/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package User;

import Database.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import javax.swing.UIManager;


/**
 *
 * @author fikra
 */
public class ProjectDetails extends javax.swing.JFrame {
    private int projectId;
    private int assigneeIdProjects;
    private JPanel headerPanel;
    private JPanel anonymousPanel;
    private JPanel anonymousPanelDetails;
    private JPanel anonymousPanelDetailsUtama;
    /**
     * Creates new form ProjectDetails
     */
    public ProjectDetails(int projectId, int assigneeIdProjects) {
        this.projectId = projectId;
        this.assigneeIdProjects = assigneeIdProjects;
        setTitle("Project Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        initComponents();
        
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 
       
        
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
        
        addTaskListHeader();
        loadTasks(taskListPanel);
        loadProjectData();
        
    }
    
    private void addTaskListHeader() {
        headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 4));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        anonymousPanel = new JPanel();
        anonymousPanel.setLayout(new GridLayout(1, 4));
        anonymousPanel.setPreferredSize(new Dimension(952, 40));
        anonymousPanel.setMaximumSize(new Dimension(952, 40));
        anonymousPanel.setMinimumSize(new Dimension(952, 40));
        anonymousPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        anonymousPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        anonymousPanel.setOpaque(false);

        JLabel nameHeader = new JLabel("Task Name");
        nameHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameHeader.setHorizontalAlignment(SwingConstants.LEFT);

//        JPanel progressHeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//        progressHeaderPanel.setOpaque(false);
        JLabel progressHeader = new JLabel("Due Date", SwingConstants.CENTER);
        progressHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        progressHeader.setHorizontalAlignment(SwingConstants.LEFT);
//        progressHeaderPanel.add(progressHeader, BorderLayout.CENTER);

        JLabel remainingHeader = new JLabel("Status", SwingConstants.CENTER);
        remainingHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        remainingHeader.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel actionHeader = new JLabel("     "); 
        actionHeader.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(nameHeader);
        headerPanel.add(progressHeader);
        headerPanel.add(remainingHeader);
        headerPanel.add(actionHeader);
        
        anonymousPanel.add(headerPanel);
        taskListPanel.add(anonymousPanel);
        taskListPanel.add(Box.createVerticalStrut(10));
    }
    
    private void loadProjectData() {
    try (Connection conn = DatabaseConnection.getConnection()){
        
        //ambil name
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
        
        //ambil name dan desc
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
        
        
        //ambil total project
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
        
        //ambil yang done
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
        progressPercentage.setText(progress + "%");
        progressBar.setValue(progress);
        progressBar.setForeground(new Color(214,201,197)); 
        progressBar.setBackground(new Color(12,44,71)); 
        
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
                
                anonymousPanelDetailsUtama = new JPanel();
                anonymousPanelDetailsUtama.setLayout(new BoxLayout(anonymousPanelDetailsUtama, BoxLayout.Y_AXIS));
                anonymousPanelDetailsUtama.setBorder(new EmptyBorder(10, 10, 150, 10));  
                anonymousPanelDetailsUtama.setPreferredSize(new Dimension(952, 260));
                anonymousPanelDetailsUtama.setMaximumSize(new Dimension(952, 260));
                anonymousPanelDetailsUtama.setMinimumSize(new Dimension(952, 260));
                anonymousPanelDetailsUtama.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                anonymousPanelDetailsUtama.setOpaque(false);
                
                anonymousPanelDetails = new JPanel();
                anonymousPanelDetails.setLayout(new BoxLayout(anonymousPanelDetails, BoxLayout.Y_AXIS)); 
                anonymousPanelDetails.setPreferredSize(new Dimension(952, 40));
                anonymousPanelDetails.setMaximumSize(new Dimension(952, 40));
                anonymousPanelDetails.setMinimumSize(new Dimension(952, 40));
                anonymousPanelDetails.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                anonymousPanelDetails.setOpaque(false);
//                

                JPanel row = new JPanel(new GridLayout(1, 4));
                row.setOpaque(false);
                row.setPreferredSize(new Dimension(0, 30));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                row.setFont(new Font("SansSerif", Font.BOLD, 13));
                row.add(new JLabel(name));
                row.add(new JLabel(deadline));
                row.add(new JLabel(status != null ? status : "Belum Ada Status"));

                JButton btn = new JButton("Details");
                btn.addActionListener(e -> {
//                    new UserTaskDetails(taskId).setVisible(true);
                });
                row.add(btn);
                anonymousPanelDetails.add(row);
                anonymousPanelDetailsUtama.add(anonymousPanelDetails);
                taskListPanel.add(anonymousPanelDetailsUtama);
                taskListPanel.add(Box.createVerticalStrut(5));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
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

        MainContent = new javax.swing.JPanel();
        OverdueTask = new javax.swing.JPanel();
        txt = new javax.swing.JLabel();
        progressStatus = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        progressPercentage = new javax.swing.JLabel();
        TotalUser = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        projectName = new javax.swing.JLabel();
        TotalTask = new javax.swing.JPanel();
        taskDoneCount = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        totalTaskCount = new javax.swing.JLabel();
        garis3 = new javax.swing.JSeparator();
        taskListPanel = new javax.swing.JPanel();
        TopUser = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        descLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        Greeting = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MainContent.setBackground(new java.awt.Color(12, 44, 71));

        OverdueTask.setBackground(new java.awt.Color(171, 203, 202));

        txt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt.setText("PROGRESS");

        progressStatus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        progressStatus.setText("OnGoing");

        progressBar.setBackground(new java.awt.Color(255, 153, 0));
        progressBar.setValue(50);

        progressPercentage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        progressPercentage.setText("10%");

        javax.swing.GroupLayout OverdueTaskLayout = new javax.swing.GroupLayout(OverdueTask);
        OverdueTask.setLayout(OverdueTaskLayout);
        OverdueTaskLayout.setHorizontalGroup(
            OverdueTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OverdueTaskLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(OverdueTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OverdueTaskLayout.createSequentialGroup()
                        .addComponent(txt)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(OverdueTaskLayout.createSequentialGroup()
                        .addGroup(OverdueTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressPercentage)
                            .addGroup(OverdueTaskLayout.createSequentialGroup()
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(progressStatus)))
                        .addGap(0, 108, Short.MAX_VALUE))))
        );
        OverdueTaskLayout.setVerticalGroup(
            OverdueTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OverdueTaskLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progressPercentage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(OverdueTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressStatus, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(25, 25, 25))
        );

        TotalUser.setBackground(new java.awt.Color(214, 201, 197));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel14.setText("Project Name");

        projectName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        projectName.setText("Mamy Sehat");

        javax.swing.GroupLayout TotalUserLayout = new javax.swing.GroupLayout(TotalUser);
        TotalUser.setLayout(TotalUserLayout);
        TotalUserLayout.setHorizontalGroup(
            TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalUserLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(projectName))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        TotalUserLayout.setVerticalGroup(
            TotalUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalUserLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(26, 26, 26)
                .addComponent(projectName)
                .addGap(28, 28, 28))
        );

        TotalTask.setBackground(new java.awt.Color(214, 201, 197));

        taskDoneCount.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        taskDoneCount.setText("20");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel24.setText("TOTAL TASK");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel25.setText("TASK DONE");

        totalTaskCount.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalTaskCount.setText("40");

        garis3.setForeground(new java.awt.Color(0, 0, 0));
        garis3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout TotalTaskLayout = new javax.swing.GroupLayout(TotalTask);
        TotalTask.setLayout(TotalTaskLayout);
        TotalTaskLayout.setHorizontalGroup(
            TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalTaskLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalTaskLayout.createSequentialGroup()
                        .addComponent(taskDoneCount)
                        .addGap(59, 59, 59)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(garis3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TotalTaskLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24))
                    .addGroup(TotalTaskLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(totalTaskCount)))
                .addGap(31, 31, 31))
        );
        TotalTaskLayout.setVerticalGroup(
            TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalTaskLayout.createSequentialGroup()
                .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TotalTaskLayout.createSequentialGroup()
                        .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TotalTaskLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel24))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalTaskLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel25)))
                        .addGap(18, 18, 18)
                        .addGroup(TotalTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(taskDoneCount)
                            .addComponent(totalTaskCount))
                        .addGap(0, 25, Short.MAX_VALUE))
                    .addGroup(TotalTaskLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(garis3)))
                .addContainerGap())
        );

        taskListPanel.setBackground(new java.awt.Color(214, 201, 197));

        javax.swing.GroupLayout taskListPanelLayout = new javax.swing.GroupLayout(taskListPanel);
        taskListPanel.setLayout(taskListPanelLayout);
        taskListPanelLayout.setHorizontalGroup(
            taskListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 952, Short.MAX_VALUE)
        );
        taskListPanelLayout.setVerticalGroup(
            taskListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );

        TopUser.setBackground(new java.awt.Color(171, 203, 202));

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel43.setText("DESKRIPSI PROJECT");

        descLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        descLabel.setText("Deskripsi projectnya disini ya yahahahah");

        javax.swing.GroupLayout TopUserLayout = new javax.swing.GroupLayout(TopUser);
        TopUser.setLayout(TopUserLayout);
        TopUserLayout.setHorizontalGroup(
            TopUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopUserLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(TopUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(descLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        TopUserLayout.setVerticalGroup(
            TopUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopUserLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descLabel)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MainContentLayout = new javax.swing.GroupLayout(MainContent);
        MainContent.setLayout(MainContentLayout);
        MainContentLayout.setHorizontalGroup(
            MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContentLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(taskListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainContentLayout.createSequentialGroup()
                        .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(MainContentLayout.createSequentialGroup()
                                .addComponent(TopUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(24, 24, 24)
                                .addComponent(OverdueTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(MainContentLayout.createSequentialGroup()
                                .addComponent(TotalUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TotalTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        MainContentLayout.setVerticalGroup(
            MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContentLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TotalUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MainContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TopUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OverdueTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(taskListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        userLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        userLabel.setText("{user}");

        Greeting.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Greeting.setText("Project Details");

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
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Greeting)
                    .addComponent(userLabel)
                    .addComponent(MainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(userLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Greeting)
                .addGap(18, 18, 18)
                .addComponent(MainContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addComponent(SidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtDashboardMouseClicked
        DashboardUser dashboard = new DashboardUser(assigneeIdProjects);
        dashboard.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtDashboardMouseClicked

    private void TxtProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtProjectMouseClicked
        AssigneProject project = new AssigneProject(assigneeIdProjects);
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
        AssigneTask task = new AssigneTask();
        task.setVisible(true);
        this.dispose(); // Menutup form saat ini jika perlu
    }//GEN-LAST:event_TxtTaskMouseClicked
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ProjectDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ProjectDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ProjectDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ProjectDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ProjectDetails(1, 1).setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Greeting;
    private javax.swing.JSeparator LineSidebar;
    private javax.swing.JSeparator LineSidebar1;
    private javax.swing.JLabel LogoArasaka;
    private javax.swing.JPanel MainContent;
    private javax.swing.JPanel OverdueTask;
    private javax.swing.JPanel SidebarPanel;
    private javax.swing.JPanel TopUser;
    private javax.swing.JPanel TotalTask;
    private javax.swing.JPanel TotalUser;
    private javax.swing.JLabel TxtArasaka;
    private javax.swing.JLabel TxtDashboard;
    private javax.swing.JLabel TxtLogout;
    private javax.swing.JLabel TxtProject;
    private javax.swing.JLabel TxtProjectManagement;
    private javax.swing.JLabel TxtSocialMedia;
    private javax.swing.JLabel TxtTask;
    private javax.swing.JLabel descLabel;
    private javax.swing.JSeparator garis3;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressPercentage;
    private javax.swing.JLabel progressStatus;
    private javax.swing.JLabel projectName;
    private javax.swing.JLabel taskDoneCount;
    private javax.swing.JPanel taskListPanel;
    private javax.swing.JLabel totalTaskCount;
    private javax.swing.JLabel txt;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
}
