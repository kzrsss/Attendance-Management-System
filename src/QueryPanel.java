import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dao.DatabaseHelper;
 
public class QueryPanel extends JPanel {
    private JTextField studentNameField;
    private JTextField studentIDField;
    private JTextField dateField;
    private JTextField lessonField;
    private JTextField courseField;
    private JTextField absenceType;
    private JButton submitButton;
    private JButton clearButton;
    public JTextArea textArea;
    
    
    public QueryPanel() {
    	  this.dateField = new JTextField();
          this.lessonField = new JTextField();
          this.courseField = new JTextField();
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
        this.submitButton = new JButton("查询");
        this.add(this.submitButton);
        this.clearButton = new JButton("清空");
        this.add(this.clearButton);
        JLabel date = new JLabel("查询结果:");
        this.add(date);
        this.textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(this.textArea);
        this.add(scrollPane);
        this.submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentName = QueryPanel.this.studentNameField.getText();
                String studentID = QueryPanel.this.studentIDField.getText();
                if (studentID.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "学生学号不能为空");
                    return;
               }
 
                QueryPanel.this.query(studentName, studentID);
            }
        });
        this.clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QueryPanel.this.studentNameField.setText("");
                QueryPanel.this.studentIDField.setText("");
                QueryPanel.this.dateField.setText("");
                QueryPanel.this.lessonField.setText("");
                QueryPanel.this.courseField.setText("");
            }
        });
    }
 


	private void query(String studentName, String studentID) {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        String result = databaseHelper.queryDatabase(studentID);
        this.textArea.setText(result);
    }
}