import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private JSlider slider;
    private Connection connection;
    private String id;

    public Student(String id, JFrame parent) {
        this.id = id;
        setTitle("考勤系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        initDatabaseConnection();
        initComponents();
    }

    private void initComponents() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("考勤时间");
        model.addColumn("考勤课程");
        model.addColumn("考勤课时");
        model.addColumn("考勤结果");

        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton viewAttendance = new JButton("查看个人考勤情况");
        JButton viewPersonalInfo = new JButton("查看个人信息");
        JButton modifyPassword = new JButton("修改个人密码");
        JButton backButton = new JButton("回退到登录界面"); 

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.add(viewAttendance);
        buttonPanel.add(viewPersonalInfo);
        buttonPanel.add(modifyPassword);
        buttonPanel.add(backButton); // 将回退按钮添加到面板

        initSlider();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel, scrollPane);
        splitPane.setDividerLocation(200);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(slider, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(sliderPanel, BorderLayout.EAST);

        getContentPane().add(mainPanel);

        viewAttendance.addActionListener(e -> loadAttendanceRecords());
        viewPersonalInfo.addActionListener(e -> viewPersonalInfo());
        modifyPassword.addActionListener(e -> modifyPassword());
        backButton.addActionListener(e -> goBack()); // 为回退按钮添加事件监听器


        setVisible(true);
    }

    private void initSlider() {
        slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            int maximum = scrollPane.getVerticalScrollBar().getMaximum();
            int extent = scrollPane.getVerticalScrollBar().getVisibleAmount();
            int newPosition = (maximum * value) / 100;
            scrollPane.getVerticalScrollBar().setValue(newPosition);
        });
    }

    private void initDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/student?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            String user = "root";
            String password = "kzr13883288366";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAttendanceRecords() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM attendance_records WHERE student_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                model.addRow(new Object[]{
                		resultSet.getDate("date").toString(),
                        resultSet.getString("course"),
                        resultSet.getString("lesson"),
                        resultSet.getString("type")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewPersonalInfo() {
        try {
            String sql = "SELECT * FROM student_users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String major = resultSet.getString("major");
                String className = resultSet.getString("class");
                JOptionPane.showMessageDialog(this, "姓名: " + name + "\n" +
                        "专业: " + major + "\n" +
                        "班级: " + className);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyPassword() {
        // 创建一个面板来包含旧密码和新密码的输入框
        JPanel passwordPanel = new JPanel(new GridLayout(2, 2));
        JLabel oldPasswordLabel = new JLabel("旧密码:");
        JPasswordField oldPasswordField = new JPasswordField(15);
        JLabel newPasswordLabel = new JLabel("新密码:");
        JPasswordField newPasswordField = new JPasswordField(15);

        passwordPanel.add(oldPasswordLabel);
        passwordPanel.add(oldPasswordField);
        passwordPanel.add(newPasswordLabel);
        passwordPanel.add(newPasswordField);

        // 显示对话框并获取用户响应
        int option = JOptionPane.showConfirmDialog(null, passwordPanel, "修改密码", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            if (oldPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "旧密码不能为空");
                return;
            }

            try {
                String sql = "SELECT * FROM student_users WHERE id = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                statement.setString(2, oldPassword);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String newPassword = new String(newPasswordField.getPassword());
                    if (!newPassword.isEmpty()) {
                        sql = "UPDATE student_users SET password = ? WHERE id = ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, newPassword);
                        statement.setString(2, id);
                        int updated = statement.executeUpdate();
                        if (updated > 0) {
                            JOptionPane.showMessageDialog(this, "密码修改成功！");
                        } else {
                            JOptionPane.showMessageDialog(this, "密码修改失败！");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "新密码不能为空！");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "旧密码错误！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "数据库操作错误！");
            }
        }
    }
    
    private void goBack() {
        this.setVisible(false); // 隐藏当前窗口
        new Login(); // 打开登录窗口
    }
}