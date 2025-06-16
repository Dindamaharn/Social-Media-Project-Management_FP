# 🏢 Arasaka Project Management System

A desktop-based project management application built using **Java Swing**, designed for internal project tracking and task assignment at **Arasaka Corporation**.
This application allows employees to manage their assigned tasks, upload progress with images, and allows admins to oversee project workflows through an intuitive GUI.

---

## ✨ Features

- 🔐 Login & Register system
- 📁 Manage Projects and Tasks
- 👤 Assign tasks to specific employees
- 🚦 Task status flow: Assigned → In Progress → Under Review → Completed
- 🖼 Users upload screenshots/images as task proof during review
- 📅 Date picker integration using **JCalendar**
- 📊 Admin dashboard to monitor, review, accept, or reject tasks
- 🕒 Overdue task detection

---

## 📦 Built With

- **Java Swing** – Desktop GUI framework
- **MySQL** – Database backend
- **MySQL Connector/J** – JDBC driver to connect Java and MySQL
- **JCalendar** – Calendar/Date picker component for Swing

---

## ⚙️ How to Run

1. **Clone this repository**

```bash
git clone https://github.com/yourusername/arasaka-project-manager.git
```

2. Open in your Java IDE (e.g., IntelliJ IDEA, NetBeans, Eclipse)
3. Add JAR dependencies:
4. lib/mysql-connector-java-x.x.x.jar
5. lib/jcalendar-x.x.jar
6. Setup MySQL database:
7. Import the provided .sql file (if available)
8. Or create the necessary tables manually
9. Update database config in DatabaseConnection.java
10. Run the App
