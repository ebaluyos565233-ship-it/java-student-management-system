package baluyos.pkg;

import java.awt.Font;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Separate Model Class demonstrating Encapsulation (Private fields + Getters/Setters)
class Student {
    private String name;
    private String age;
    private String course;
    private String gender;
    private String nationality;

    public Student(String name, String age, String course, String gender, String nationality) {
        this.name = name;
        this.age = age;
        this.course = course;
        this.gender = gender;
        this.nationality = nationality;
    }

    // Encapsulated Getters
    public String getName() { return name; }
    public String getAge() { return age; }
    public String getCourse() { return course; }
    public String getGender() { return gender; }
    public String getNationality() { return nationality; }
}

public class StudentManager extends JFrame { 

    // Private fields (Encapsulation)
    private DefaultTableModel model;
    private JTable table;

    private JTextField txtName, txtAge, txtCourse, txtNationality;
    private JRadioButton buttonMale, buttonFemale;
    private ButtonGroup genderGroup;

    public StudentManager() {
        // Window setup
        setTitle("Student Information Management System");
        setSize(820, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Header Label
        JLabel lblTitle = new JLabel("STUDENT DETAILS");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setBounds(80, 10, 180, 30);
        add(lblTitle);

        // Form Inputs
        JLabel lblName = new JLabel("Enter Name:");
        lblName.setBounds(20, 50, 100, 20);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(130, 50, 140, 22);
        add(txtName);

        JLabel lblAge = new JLabel("Enter Age:");
        lblAge.setBounds(20, 85, 100, 20);
        add(lblAge);

        txtAge = new JTextField();
        txtAge.setBounds(130, 85, 140, 22);
        add(txtAge);

        JLabel lblCourse = new JLabel("Enter Course:");
        lblCourse.setBounds(20, 120, 100, 20);
        add(lblCourse);

        txtCourse = new JTextField();
        txtCourse.setBounds(130, 120, 140, 22);
        add(txtCourse);

        // --- NEW: NATIONALITY FIELD ---
        JLabel lblNationality = new JLabel("Nationality:");
        lblNationality.setBounds(20, 155, 100, 20);
        add(lblNationality);

        txtNationality = new JTextField();
        txtNationality.setBounds(130, 155, 140, 22);
        add(txtNationality);

        // Gender Selection
        JLabel lblGender = new JLabel("Select Gender:");
        lblGender.setBounds(20, 190, 100, 20);
        add(lblGender);

        buttonMale = new JRadioButton("Male");
        buttonMale.setBounds(130, 190, 60, 20);
        buttonFemale = new JRadioButton("Female");
        buttonFemale.setBounds(195, 190, 80, 20);

        genderGroup = new ButtonGroup();
        genderGroup.add(buttonMale);
        genderGroup.add(buttonFemale);
        add(buttonMale);
        add(buttonFemale);

        // --- BUTTON ACTIONS ---

        // Record Button
        JButton btnRecord = new JButton("Record");
        btnRecord.setBounds(30, 235, 100, 25);
        add(btnRecord);

        btnRecord.addActionListener(e -> {
            String name = txtName.getText().trim();
            String age = txtAge.getText().trim();
            String course = txtCourse.getText().trim();
            String nationality = txtNationality.getText().trim();
            String gender = buttonMale.isSelected() ? "Male" : (buttonFemale.isSelected() ? "Female" : "");

            if (name.isEmpty() || age.isEmpty() || course.isEmpty() || nationality.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Using the encapsulated Student object
            Student student = new Student(name, age, course, gender, nationality);

            model.addRow(new Object[]{
                student.getName(), student.getAge(), student.getCourse(), student.getGender(), student.getNationality()
            });

            saveRecord(student);
            JOptionPane.showMessageDialog(this, "Successfully Recorded!");
            clearFields();
        });

        // Update Button
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(150, 235, 100, 25);
        add(btnUpdate);

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String gender = buttonMale.isSelected() ? "Male" : (buttonFemale.isSelected() ? "Female" : "");
                
                model.setValueAt(txtName.getText(), selectedRow, 0);
                model.setValueAt(txtAge.getText(), selectedRow, 1);
                model.setValueAt(txtCourse.getText(), selectedRow, 2);
                if (!gender.isEmpty()) model.setValueAt(gender, selectedRow, 3);
                model.setValueAt(txtNationality.getText(), selectedRow, 4);

                JOptionPane.showMessageDialog(this, "Row Updated Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row from the table to update.");
            }
        });

        // Delete Button
        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(30, 275, 100, 25);
        add(btnDelete);

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                model.removeRow(selectedRow);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        });

        // Clear Table View Button
        JButton btnClear = new JButton("Clear Table");
        btnClear.setBounds(150, 275, 100, 25);
        add(btnClear);

        btnClear.addActionListener(e -> {
            model.setRowCount(0);
            clearFields();
        });

        // Table & ScrollPane Setup
        String[] columns = {"Name", "Age", "Course", "Gender", "Nationality"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(290, 30, 490, 370);
        add(scrollPane);

        // Populate form inputs when a row is clicked
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                txtName.setText(model.getValueAt(selectedRow, 0).toString());
                txtAge.setText(model.getValueAt(selectedRow, 1).toString());
                txtCourse.setText(model.getValueAt(selectedRow, 2).toString());
                
                String genderVal = model.getValueAt(selectedRow, 3).toString();
                if (genderVal.equalsIgnoreCase("Male")) buttonMale.setSelected(true);
                else if (genderVal.equalsIgnoreCase("Female")) buttonFemale.setSelected(true);

                txtNationality.setText(model.getValueAt(selectedRow, 4).toString());
            }
        });

        // Load existing records from data.txt
        readFile();
        setVisible(true);
    }

    private void clearFields() {
        txtName.setText("");
        txtAge.setText("");
        txtCourse.setText("");
        txtNationality.setText("");
        genderGroup.clearSelection();
    }

    // Encapsulated File Writing
    private void saveRecord(Student s) {
        try (BufferedWriter write = new BufferedWriter(new FileWriter("data.txt", true))) {
            write.write(s.getName() + "#" + s.getAge() + "#" + s.getCourse() + "#" + s.getGender() + "#" + s.getNationality());
            write.newLine();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // Encapsulated File Reading
    private void readFile() {
        File file = new File("data.txt");
        if (!file.exists()) return;

        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = read.readLine()) != null) {
                String[] row = line.split("#");
                if (row.length == 5) {
                    model.addRow(row);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManager());
    }
}