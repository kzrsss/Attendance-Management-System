package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
 
public class DatabaseHelper {
    // JDBC连接URL，用户名和密码
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/student?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kzr13883288366";
 
    public static Object[][] performDatabaseQuery(String selectedSubject, String selectedAbsenceType, String startDate) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Object[]> dataList = new ArrayList<>();
 
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
 
            String sql = "SELECT student_name, type, date FROM attendance_records " +
                    "WHERE course = ? AND type = ? AND date = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, selectedSubject);
            statement.setString(2, selectedAbsenceType);
            statement.setString(3, startDate);
       
 
            resultSet = statement.executeQuery();
 
            while (resultSet.next()) {
                String studentName = resultSet.getString("student_name");
                String Type = resultSet.getString("type");
                String absenceDate = resultSet.getString("date");
 
                Object[] row = { studentName, Type, absenceDate };
                dataList.add(row);
            }
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            // 可以根据需要处理异常
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
 
        // 转换为 Object[][] 返回
        Object[][] data = new Object[dataList.size()][3];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        return data;
    }
 
    public static String[] getSubjects() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
 
        String[] result = new String[20];
 
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            // 创建SQL语句
            String sql = "SELECT DISTINCT course FROM attendance_records";
            statement = connection.prepareStatement(sql);
 
            // 执行查询
            resultSet = statement.executeQuery();
 
            // 处理查询结果
            int i = 0;
            while (resultSet.next() && i < result.length) {
                result[i++] = resultSet.getString(1);
            }
 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭ResultSet、Statement和Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
 
    public static String[] getTimes() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
 
        String[] result = new String[200];
 
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
 
            // 创建SQL语句
            String sql = "SELECT DISTINCT date FROM attendance_records";
            statement = connection.prepareStatement(sql);
 
            // 执行查询
            resultSet = statement.executeQuery();
 
            // 处理查询结果
            int i = 0;
            while (resultSet.next() && i < result.length) {
                result[i++] = resultSet.getString(1);
            }
 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭ResultSet、Statement和Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
 
 
    public static void saveToDatabase(String studentName, String studentID, String date, String lesson, String course, String absenceType) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // 首先验证姓名和学号是否匹配
            String checkSql = "SELECT * FROM student_users WHERE id = ? AND name = ?";
            statement = connection.prepareStatement(checkSql);
            statement.setString(1, studentID);
            statement.setString(2, studentName);
            resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                // 如果不匹配，提示用户并返回
                JOptionPane.showMessageDialog(null, "姓名和学号不匹配，请重新录入");
                return;
            }

            // 如果匹配，创建SQL语句插入考勤记录
            String sql = "INSERT INTO attendance_records (student_name, student_id, date, lesson, course, type) VALUES (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            // 设置SQL语句参数
            statement.setString(1, studentName);
            statement.setString(2, studentID);
            statement.setString(3, date);
            statement.setString(4, lesson);
            statement.setString(5, course);
            statement.setString(6, absenceType);

            // 执行SQL语句
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // 如果插入成功，提示用户
                JOptionPane.showMessageDialog(null, "考勤记录保存成功！");
            } else {
                // 如果插入失败，提示用户
                JOptionPane.showMessageDialog(null, "考勤记录保存失败，请重试");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库操作错误！");
        } finally {
            // 关闭ResultSet、Statement和Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public String queryDatabase(String studentID) {
        Connection connection = null;
        PreparedStatement statement = null; 	
        ResultSet resultSet = null;
 
        StringBuilder result = new StringBuilder();
 
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
 
            // 创建SQL语句
            String sql = "SELECT * FROM attendance_records WHERE student_id = ?";
            statement = connection.prepareStatement(sql);
 
            // 设置SQL语句参数
            statement.setString(1, studentID);
 
            // 执行查询
            resultSet = statement.executeQuery();
 
            // 处理查询结果
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String lesson = resultSet.getString("lesson");
                String course = resultSet.getString("course");
                String Type = resultSet.getString("type");
 
                result.append("Student ID: ").append(studentID).append("\n");
                result.append("考勤日期: ").append(date).append("\n");
                result.append("考勤课时: ").append(lesson).append("\n");
                result.append("考勤学科: ").append(course).append("\n");
                result.append("考勤类型: ").append(Type).append("\n\n");
            }
            return result.toString();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭ResultSet、Statement和Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
 
        return result.toString();
    }
    public static boolean updateDatabase(String studentName, String studentID, String date, String lesson, String course, String absenceType) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // 首先检查学生ID是否存在
            String checkSql = "SELECT * FROM attendance_records WHERE student_id = ? AND date=? AND course=? AND student_name=?";
            statement = connection.prepareStatement(checkSql);
            statement.setString(1, studentID);
            statement.setString(2, date);
            statement.setString(3, course);
            statement.setString(4, studentName);
            resultSet = statement.executeQuery();

            // 如果查询结果为空，说明数据有误
            if (!resultSet.next()) {
                return false; // 学生ID不存在，返回false
            }

            // 如果学生ID存在，执行更新操作
            String sql = "UPDATE attendance_records SET  lesson = ?,  type = ? WHERE student_id = ? AND date = ? AND course = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, lesson);
            statement.setString(2, absenceType);
            statement.setString(3, studentID);
            statement.setString(4, date);
            statement.setString(5, course);

        
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // 返回更新的行数是否大于0
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 数据库操作错误，返回false
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}