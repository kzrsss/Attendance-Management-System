import javax.swing.*;

import dao.DatabaseHelper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePanel extends JPanel {
    private JTextField studentNameField;
    private JTextField studentIDField;
    private JTextField dateField;
    private JTextField lessonField;
    private JTextField courseField;
    private JComboBox<String> absenceTypeComboBox;
    private JButton submitButton;
    private JButton clearButton;

    public UpdatePanel() {
        setLayout(new GridLayout(0, 2));

        JLabel studentName = new JLabel("学生姓名:");
        add(studentName);
        studentName.setHorizontalAlignment(SwingConstants.CENTER);
        studentNameField = new JTextField();
        add(studentNameField);

        JLabel studentId = new JLabel("学生学号:");
        add(studentId);
        studentId.setHorizontalAlignment(SwingConstants.CENTER);
        studentIDField = new JTextField();
        add(studentIDField);

        JLabel date = new JLabel("考勤日期:");
        add(date);
        date.setHorizontalAlignment(SwingConstants.CENTER);
        dateField = new JTextField();
        add(dateField);

        JLabel lesson = new JLabel("考勤课时:");
        add(lesson);
        lesson.setHorizontalAlignment(SwingConstants.CENTER);
        lessonField = new JTextField();
        add(lessonField);

        JLabel subject = new JLabel("考勤科目:");
        add(subject);
        subject.setHorizontalAlignment(SwingConstants.CENTER);
        courseField = new JTextField();
        add(courseField);

        JLabel type = new JLabel("考勤类型:");
        add(type);
        type.setHorizontalAlignment(SwingConstants.CENTER);
        String[] Types = {"迟到", "早退", "正常", "缺席"};
        absenceTypeComboBox = new JComboBox<>(Types);
        add(absenceTypeComboBox);

        submitButton = new JButton("修改");
        add(submitButton);

        clearButton = new JButton("清空");
        add(clearButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取输入信息
                String studentName = studentNameField.getText();
                String studentID = studentIDField.getText();
                String date = dateField.getText();
                String lesson = lessonField.getText();
                String course = courseField.getText();
                String absenceType = (String) absenceTypeComboBox.getSelectedItem();

                // 校验输入
                if (studentName.isEmpty() || studentID.isEmpty() || date.isEmpty() || lesson.isEmpty() || course.isEmpty() || absenceType.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "所有字段都是必填的");
                    return;
                }
                saveToDatabase(studentName, studentID, date, lesson, course, absenceType);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 清空输入框
                studentNameField.setText("");
                studentIDField.setText("");
                dateField.setText("");
                lessonField.setText("");
                courseField.setText("");
            }
        });
    }

    private void saveToDatabase(String studentName, String studentID, String date, String lesson, String course, String absenceType) {
        // 在这里添加将数据保存到数据库的代码
        // 例如使用 JDBC 连接数据库并执行相应的 SQL 语句
        DatabaseHelper databaseHelper = new DatabaseHelper();
        boolean success = databaseHelper.updateDatabase(studentName, studentID, date, lesson, course, absenceType);
        if (success) {
            JOptionPane.showMessageDialog(this, "更新数据成功");
        } else {
            JOptionPane.showMessageDialog(this, "查无此人或更新数据失败");
        }
    }
}