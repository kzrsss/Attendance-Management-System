import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener {
    private JTextField idField;
    private JPasswordField passwordField;
    private JComboBox<String> typeComboBox;
    private Connection connection;

    public Login() {
        // 初始化界面
        setTitle("登录界面");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // 创建背景面板并设置背景图片
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        getContentPane().add(backgroundPanel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("账号:");
        c.gridx = 0;
        c.gridy = 0;
        backgroundPanel.add(userLabel, c);

        idField = new JTextField(15);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        backgroundPanel.add(idField, c);

        JLabel passLabel = new JLabel("密码:");
        c.gridx = 0;
        c.gridy = 1;
        backgroundPanel.add(passLabel, c);

        passwordField = new JPasswordField(15);
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        backgroundPanel.add(passwordField, c);

        JLabel typeLabel = new JLabel("身份:");
        c.gridx = 0;
        c.gridy = 2;
        backgroundPanel.add(typeLabel, c);

        typeComboBox = new JComboBox<>(new String[]{"学生", "老师"});
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        backgroundPanel.add(typeComboBox, c);

        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(this);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.CENTER;
        backgroundPanel.add(loginButton, c);

        setLocationRelativeTo(null);
        setVisible(true);

        initializeDatabaseConnection();
    }

    private void initializeDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/student?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            String user = "root";
            String password = "kzr13883288366";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = idField.getText();
        String password = new String(passwordField.getPassword());
        String type = (String) typeComboBox.getSelectedItem();

        if (login(id, password, type)) {
            JOptionPane.showMessageDialog(this, "登录成功！");
            if ("学生".equals(type)) {
                new Student(id, this).setVisible(true);
                this.dispose();
            } else if ("老师".equals(type)) {
                new Teacher(id, this).setVisible(true);
                this.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "登录失败：用户名或密码错误。");
        }
    }

    private boolean login(String id, String password, String type) {
        if (connection == null) {
            return false;
        }
        String sql = "SELECT * FROM " + ("学生".equals(type) ? "student_users" : "teacher_users") + " WHERE id = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}