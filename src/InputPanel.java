import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.DatabaseHelper;

public class InputPanel extends JPanel {
	private JTextField studentNameField;
	private JTextField studentIDField;
	private JTextField dateField;
	private JTextField lessonField;
	private JTextField courseField;
	private JComboBox<String> absenceTypeComboBox;
	private JButton submitButton;
	private JButton clearButton;

	public InputPanel() {
		this.setLayout(new GridLayout(0, 2));
		JLabel studentName = new JLabel("学生姓名:");
		this.add(studentName);
		studentName.setHorizontalAlignment(0);
		this.studentNameField = new JTextField();
		this.add(this.studentNameField);
		JLabel studentId = new JLabel("学生学号:");
		this.add(studentId);
		studentId.setHorizontalAlignment(0);
		this.studentIDField = new JTextField();
		this.add(this.studentIDField);
		JLabel date = new JLabel("考勤日期:");
		this.add(date);
		date.setHorizontalAlignment(0);
		this.dateField = new JTextField();
		this.add(this.dateField);
		JLabel lesson = new JLabel("考勤课时:");
		this.add(lesson);
		lesson.setHorizontalAlignment(0);
		this.lessonField = new JTextField();
		this.add(this.lessonField);
		JLabel subject = new JLabel("考勤科目:");
		this.add(subject);
		subject.setHorizontalAlignment(0);
		this.courseField = new JTextField();
		this.add(this.courseField);
		JLabel type = new JLabel("考勤类型:");
		this.add(type);
		type.setHorizontalAlignment(0);
		String[] Types = new String[] { "迟到", "早退", "正常", "缺席" };
		this.absenceTypeComboBox = new JComboBox(Types);
		this.add(this.absenceTypeComboBox);
		this.submitButton = new JButton("录入");
		this.add(this.submitButton);
		this.clearButton = new JButton("清空");
		this.add(this.clearButton);
		this.submitButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String studentName = InputPanel.this.studentNameField.getText();
		        String studentID = InputPanel.this.studentIDField.getText();
		        String date = InputPanel.this.dateField.getText();
		        String lesson = InputPanel.this.lessonField.getText();
		        String course = InputPanel.this.courseField.getText();
		        String absenceType = (String) InputPanel.this.absenceTypeComboBox.getSelectedItem();
		        if (studentName.isEmpty() || studentID.isEmpty() || date.isEmpty() || lesson.isEmpty()
		                || course.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "所有字段都是必填的，请填写完整");
		            return;
		        }

		        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
		            JOptionPane.showMessageDialog(null, "日期格式应为 YYYY-MM-DD");
		            return;
		        }
		        // 调用 saveToDatabase 方法，但不在这里直接提示成功
		        InputPanel.this.saveToDatabase(studentName, studentID, date, lesson, course, absenceType);
		        // 成功提示已移至 saveToDatabase 方法中处理
		    }
		});
		this.clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputPanel.this.studentNameField.setText("");
				InputPanel.this.studentIDField.setText("");
				InputPanel.this.dateField.setText("");
				InputPanel.this.lessonField.setText("");
				InputPanel.this.courseField.setText("");
			}
		});
	}

	private void saveToDatabase(String studentName, String studentID, String date, String lesson, String course,
			String absenceType) {

		DatabaseHelper.saveToDatabase(studentName, studentID, date, lesson, course, absenceType);
	}
}