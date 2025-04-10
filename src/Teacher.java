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

public class Teacher extends JFrame {
	private JTable table;
    private JScrollPane scrollPane;
    private JSlider slider;
    private Connection connection;
    private String id;

    public Teacher(String id, JFrame parent) {
        this.id = id;
        setTitle("考勤系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600); // 增加宽度以适应新的布局
        initDatabaseConnection();
        initComponents();
    }

    private void initComponents() {
       
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 初始化滑块
        initSlider();

        // 初始化按钮
        JButton viewPersonalInfo = new JButton("查看个人信息");
        JButton modifyPassword = new JButton("修改个人密码");
        JButton backButton = new JButton("回退到登录界面"); 
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.add(viewPersonalInfo);
        buttonPanel.add(modifyPassword);
        buttonPanel.add(backButton); // 将回退按钮添加到面板

        // 初始化选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("录入考勤记录", new InputPanel());
        tabbedPane.addTab("修改考勤记录", new UpdatePanel());
        tabbedPane.addTab("查询考勤记录", new QueryPanel());
        tabbedPane.addTab("统计考勤记录", new StatisticsPanel());

        // 创建分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel, scrollPane);
        splitPane.setDividerLocation(200);

        // 将分割面板和选项卡面板组合
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(tabbedPane, BorderLayout.CENTER);

        // 创建主面板并设置布局
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST); // 将选项卡面板添加到右侧

        // 添加主面板到窗口
        getContentPane().add(mainPanel);

        // 添加事件监听器
        viewPersonalInfo.addActionListener(e -> viewPersonalInfo());
        modifyPassword.addActionListener(e -> modifyPassword());
        backButton.addActionListener(e -> goBack()); // 为回退按钮添加事件监听器

        // 显示窗口
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

    
    private void viewPersonalInfo() {
        try {
            String sql = "SELECT * FROM teacher_users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String major = resultSet.getString("major");
                String degree = resultSet.getString("degree");
                JOptionPane.showMessageDialog(this, "姓名: " + name + "\n" +
                        "专业: " + major + "\n" +
                        "学位: " + degree);
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
                String sql = "SELECT * FROM teacher_users WHERE id = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                statement.setString(2, oldPassword);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String newPassword = new String(newPasswordField.getPassword());
                    if (!newPassword.isEmpty()) {
                        sql = "UPDATE teacher_users SET password = ? WHERE id = ?";
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