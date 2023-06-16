package KeCheng;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class AddressBook extends JFrame {
   // private static final long serialVersionUID = 1L;
    private static final String[] categories = {"", "Family", "Friend", "Colleague", "Other"};

    private ArrayList<Person> persons = new ArrayList<Person>();
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model);
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public AddressBook() {
        persons = FileIo.loadData();
        initUI();
    }

    private void initUI() {
        setTitle("地址簿管理器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 初始化表格
        model.addColumn("Name");
        model.addColumn("Phone Number");
        model.addColumn("E-Mail");
        model.addColumn("Category");
        for (Person person : persons) {
            Object[] row = new Object[4];
            row[0] = person.getName();
            row[1] = person.getPhoneNumber();
            row[2] = person.getEmail();
            row[3] = person.getCategory();
            model.addRow(row);
        }
       // DefaultTableCellRenderer r = new DefaultTableCellRenderer();
       // r.setHorizontalAlignment(JLabel.CENTER);
       // table.setDefaultRenderer(Object.class, r);
       // TableColumn column = null;

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 初始化按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        Font f = addButton.getFont().deriveFont(Font.BOLD);
        addButton.setFont(f);
        editButton.setFont(f);
        deleteButton.setFont(f);
        addButton.setPreferredSize(new Dimension(160, 40));
        editButton.setPreferredSize(new Dimension(160, 40));
        deleteButton.setPreferredSize(new Dimension(160, 40));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addPerson();
            }
        });
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                editPerson();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                deletePerson();
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 显示窗口
        setVisible(true);
    }

    private void addPerson() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(10);
        JTextField phoneField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JComboBox<String> categoryCombo = new JComboBox<String>(categories);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("E-Mail:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryCombo);
        panel.add(inputPanel, BorderLayout.CENTER);
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Person", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phoneNum = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            if (!name.isEmpty() && !phoneNum.isEmpty() && !email.isEmpty() && !category.isEmpty()) {// 添加到表格中
                Object[] row = new Object[4];
                row[0] = name;
                row[1] = phoneNum;
                row[2] = email;
                row[3] = category;
                model.addRow(row);
// 添加到数据列表
                persons.add(new Person(name, phoneNum, email, category));
// 保存到文件中
                FileIo.saveData(persons);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editPerson() {
        int rowIndex = table.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = (String) table.getValueAt(rowIndex, 0);
        String phoneNum = (String) table.getValueAt(rowIndex, 1);
        String email = (String) table.getValueAt(rowIndex, 2);
        String category = (String) table.getValueAt(rowIndex, 3);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(name, 10);
        JTextField phoneField = new JTextField(phoneNum, 10);
        JTextField emailField = new JTextField(email, 10);
        JComboBox<String> categoryCombo = new JComboBox<String>(categories);
        categoryCombo.setSelectedItem(category);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("E-Mail:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryCombo);
        panel.add(inputPanel, BorderLayout.CENTER);
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Person", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newPhoneNum = phoneField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newCategory = (String) categoryCombo.getSelectedItem();
            if (!newName.isEmpty() && !newPhoneNum.isEmpty() && !newEmail.isEmpty() && !newCategory.isEmpty()) {
                // 更新到表格中
                table.setValueAt(newName, rowIndex, 0);
                table.setValueAt(newPhoneNum, rowIndex, 1);
                table.setValueAt(newEmail, rowIndex, 2);
                table.setValueAt(newCategory, rowIndex, 3);
                // 更新到数据列表中
                Person person = persons.get(rowIndex);
                person.setName(newName);
                person.setPhoneNumber(newPhoneNum);
                person.setEmail(newEmail);
                person.setCategory(newCategory);
                // 保存到文件中
                FileIo.saveData(persons);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deletePerson() {
        int rowIndex = table.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = (String) table.getValueAt(rowIndex, 0);
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + name + "?", "Delete Person", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            // 从表格中删除
            model.removeRow(rowIndex);
            // 从数据列表中删除
            persons.remove(rowIndex);
            // 保存到文件中
            FileIo.saveData(persons);
        }
    }

    public static void main(String[] args) {
        new AddressBook();
    }
}
