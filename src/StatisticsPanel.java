import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.DatabaseHelper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
public class StatisticsPanel extends JPanel {
    private JComboBox<String> absenceTypeComboBox;
    private JComboBox<String> subjectComboBox;
    private JComboBox<String> dateStartComboBox;
    private String[] subjects;
    private String[] times;
    private static JTable content;
    private static final String[] columns = {"学生姓名", "考勤类型", "日期"};
 
    public StatisticsPanel() {
        setLayout(new BorderLayout());
 
        subjects = DatabaseHelper.getSubjects(); 
        times = DatabaseHelper.getTimes();
 
        JPanel inputPanel = new JPanel(new GridLayout(2, 4));
 
        JLabel subjectLabel = new JLabel("考勤科目:");
        inputPanel.add(subjectLabel);
        subjectComboBox = new JComboBox<>(subjects);
        inputPanel.add(subjectComboBox);
 
        JLabel absenceTypeLabel = new JLabel("考勤类别:");
        inputPanel.add(absenceTypeLabel);
        String[] absenceTypes = {"迟到", "早退", "正常", "缺席"};
        absenceTypeComboBox = new JComboBox<>(absenceTypes);
        inputPanel.add(absenceTypeComboBox);
 
        JLabel TimeLabel = new JLabel("考勤时间:");
        inputPanel.add(TimeLabel);
        dateStartComboBox = new JComboBox<>(times);
        inputPanel.add(dateStartComboBox);
 
       
 
        JButton searchButton = new JButton("统计");
        inputPanel.add(searchButton);
 
        add(inputPanel, BorderLayout.NORTH);
 
        content = new JTable(new DefaultTableModel(columns, 0)); // Initialize empty table model
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(scrollPane, BorderLayout.CENTER);
 
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSubject = (String) subjectComboBox.getSelectedItem();
                String selectedAbsenceType = (String) absenceTypeComboBox.getSelectedItem();
                String startDate = (String) dateStartComboBox.getSelectedItem();

 
                Object[][] data = DatabaseHelper.performDatabaseQuery(selectedSubject, selectedAbsenceType, startDate);         
                updateTableData(data);
            }
        });
    }
 
    public static void updateTableData(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) content.getModel();
        model.setRowCount(0); // Clear existing rows
 
        for (Object[] row : data) {
            model.addRow(row); // Add each row from data
        }
        model.fireTableDataChanged();
    }
   
}