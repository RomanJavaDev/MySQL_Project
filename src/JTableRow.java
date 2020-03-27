
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JTableRow {

    public static void main(String[] args) throws ClassNotFoundException {

        String userName = "root";
        String password = "roma";
        String url = "jdbc:mysql://localhost:3306/students_db?serverTimezone=UTC&useSSL=false";
        Class.forName("com.mysql.jdbc.Driver");

        // create JFrame and JTable
        JFrame frame = new JFrame();
        JTable table = new JTable();

        // create a table model and set a Column Identifiers to this model
        Object[] columns = {"Id","Name","Mobile","Course"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        // set the model to the table
        table.setModel(model);

        // Change A JTable Background Color, Font Size, Font Color, Row Height
        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
        Font font = new Font("",1,14);
        table.setFont(font);
        table.setRowHeight(20);



        JLabel l1 = new JLabel("Имя");
        JLabel l2 = new JLabel("Телефон");
        JLabel l3 = new JLabel("Название курса");
        // create JTextFields
        JTextField textName = new JTextField();
        JTextField textMobile = new JTextField();
        JTextField textCourse = new JTextField();

        // create JButtons
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnUpdate = new JButton("Update");
        JButton btnClean = new JButton("Clean text");

        l1.setBounds(20, 250, 100, 25);
        l2.setBounds(20, 280, 100, 25);
        l3.setBounds(20, 310, 100, 25);

        textName.setBounds(170, 250, 100, 25);
        textMobile.setBounds(170, 280, 100, 25);
        textCourse.setBounds(170, 310, 100, 25);

        btnAdd.setBounds(320, 220, 100, 25);
        btnUpdate.setBounds(320, 265, 100, 25);
        btnDelete.setBounds(320, 310, 100, 25);
        btnClean.setBounds(320, 355, 100, 25);

        // create JScrollPane
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 880, 200);

        frame.setLayout(null);

        frame.add(pane);

        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        // add JTextFields to the jframe
        frame.add(textName);
        frame.add(textMobile);
        frame.add(textCourse);

        // add JButtons to the jframe
        frame.add(btnAdd);
        frame.add(btnDelete);
        frame.add(btnUpdate);
        frame.add(btnClean);

        // create an array of objects to set the row data
        Object[] row = new Object[4];

        try (Connection connection = DriverManager.getConnection(url, userName, password);
             Statement statement = connection.createStatement()) {
//                    statement.executeUpdate("INSERT INTO record VALUES (NULL, 'Человек-пук', 'Урюпинск')");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM record");
            int c = 0;
            ResultSetMetaData Rss = resultSet.getMetaData();
            c = Rss.getColumnCount();
//            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
//            dtm.setRowCount(0);
            while (resultSet.next()) {
//                    Vector v = new Vector();
                for (int i = 1; i <= c; i++) {
                    row[0] = resultSet.getInt("id");
                    row[1] = resultSet.getString("name");
                    row[2] = resultSet.getString("mobile");
                    row[3] = resultSet.getString("course");
                }
                // add row to the model
                model.addRow(row);


            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        // button add row
        btnAdd.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection connection = DriverManager.getConnection(url, userName, password);
                     Statement statement = connection.createStatement()) {
//                    statement.executeUpdate("INSERT INTO record VALUES (NULL, 'Человек-пук', 'Урюпинск')");

                    PreparedStatement ps = connection.prepareStatement("INSERT INTO record (name, mobile, course) VALUES (?,?,?)");
                    ps.setString(1, textName.getText());
                    ps.setString(2, textMobile.getText());
                    ps.setString(3, textCourse.getText());
                    ps.execute();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM record");
                    while (resultSet.next()) {

                        row[0] = resultSet.getInt("id");
                        row[1] = resultSet.getString("name");
                        row[2] = resultSet.getString("mobile");
                        row[3] = resultSet.getString("course");

                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // add row to the model
                model.addRow(row);
                JOptionPane.showMessageDialog(frame, "Student Added");

                textName.setText("");
                textMobile.setText("");
                textCourse.setText("");
                textName.requestFocus();

            }
        });

        // button remove row
        btnDelete.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();

                int i = table.getSelectedRow();
                int id = Integer.parseInt(table.getValueAt(i, 0).toString());

                try (Connection connection = DriverManager.getConnection(url, userName, password);
                     Statement statement = connection.createStatement()) {
//                    statement.executeUpdate("INSERT INTO record VALUES (NULL, 'Человек-пук', 'Урюпинск')");

                    PreparedStatement ps = connection.prepareStatement("DELETE FROM record WHERE id = ?");
                    ps.setInt(1, id);
                    ps.executeUpdate();








                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                model.removeRow(i);

                JOptionPane.showMessageDialog(frame, "Record Deleted");
                textName.setText("");
                textMobile.setText("");
                textCourse.setText("");
                textName.requestFocus();

            }
        });

        // get selected row data From table to textfields
        table.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){

                // i = the index of the selected row
                int i = table.getSelectedRow();


                textName.setText(model.getValueAt(i, 1).toString());
                textMobile.setText(model.getValueAt(i, 2).toString());
                textCourse.setText(model.getValueAt(i, 3).toString());

            }
        });

        // button update row
        btnUpdate.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();

                int i = table.getSelectedRow();
                int id = Integer.parseInt(table.getValueAt(i, 0).toString());

                try (Connection connection = DriverManager.getConnection(url, userName, password);
                     Statement statement = connection.createStatement()) {
//                    statement.executeUpdate("INSERT INTO record VALUES (NULL, 'Человек-пук', 'Урюпинск')");

                    PreparedStatement ps = connection.prepareStatement("UPDATE record SET name = ?, mobile = ?, course = ? WHERE id = ?");
                    ps.setString(1, textName.getText());
                    ps.setString(2, textMobile.getText());
                    ps.setString(3, textCourse.getText());
                    ps.setInt(4, id);
                    ps.executeUpdate();




                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


                model.setValueAt(textName.getText(), i, 1);
                model.setValueAt(textMobile.getText(), i, 2);
                model.setValueAt(textCourse.getText(), i, 3);

                JOptionPane.showMessageDialog(frame, "Record Updated");
                textName.setText("");
                textMobile.setText("");
                textCourse.setText("");
                textName.requestFocus();


            }
        });

        btnClean.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                textName.setText("");
                textMobile.setText("");
                textCourse.setText("");
                textName.requestFocus();


            }
        });

        frame.setSize(900,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}