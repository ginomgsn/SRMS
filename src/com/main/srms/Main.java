package com.main.srms;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Main {

	public static void main(String[] args) throws SQLException {
		JOptionPane.showMessageDialog(null, "Student Record Management System\nPresented by: Gino Ben Magsino",
				"Welcome!", JOptionPane.PLAIN_MESSAGE);

		while (true) {

			String[] options = { "Add", "Delete", "Change", "Search", "Clear", "Exit" };
			int method = JOptionPane.showOptionDialog(null, showRecord(), "Student Records", JOptionPane.PLAIN_MESSAGE,
					JOptionPane.DEFAULT_OPTION, null, options, options[0]);
			switch (method) {
			case 0:
				addStudent();
				break;
			case 1:
				deleteStudent();
				break;
			case 2:
				changeGrade();
				break;
			case 3:
				searchStudent();
				break;
			case 4:
				clear();
				break;
			case 5:
				exit();
				break;
			}
		}

		// METHODS

	}

	public static JScrollPane showRecord() throws SQLException {
		Connection con = DBconnect.getConnection();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM student_records ORDER BY student_no ASC");

		String[] field = { "Student ID", "Name", "Grade" };
		String[][] data = new String[5][3];

		int count = 0;

		while (rs.next()) {
			data[count][0] = rs.getString(2);
			data[count][1] = rs.getString(3);
			data[count][2] = String.valueOf(rs.getInt(4));
			count++;
		}

		JTable student_record = new JTable(data, field);
		student_record.setVisible(true);
		student_record.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		student_record.getColumn(field[0]).setMaxWidth(80);
		student_record.getColumn(field[2]).setMaxWidth(50);
		student_record.getColumn(field[0]);
		student_record.getColumn(field[1]);
		student_record.getColumn(field[2]);
		
		JScrollPane js = new JScrollPane(student_record);
		js.setPreferredSize(new Dimension(400,103));
		return js;
	}

	public static void addStudent() throws SQLException {
		Connection con = DBconnect.getConnection();
		Statement stmt = con.createStatement();
		ResultSet max = stmt.executeQuery("SELECT MAX(student_no) FROM student_records");
		
		int id_no = 0;

		while (max.next()) {
			id_no = max.getInt(1);
		}

		if (countRow() < 5) {
			String name;
			int grade;
			
			 name = JOptionPane.showInputDialog(null, "Enter Student Name", "Add Record",
					JOptionPane.PLAIN_MESSAGE);
			 if(name != null) {
				 
				 try {
					 grade = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Student Grade", "Add Record", JOptionPane.PLAIN_MESSAGE));
				 }
				 catch(NumberFormatException e) {
					 return;
				 }
					
					++id_no;

					if (id_no == 1) {
						String query = "ALTER TABLE student_records AUTO_INCREMENT = 1";
						PreparedStatement AI = con.prepareStatement(query);
						AI.execute();
					}

					String.valueOf(id_no);
					String query = "INSERT INTO student_records (student_id, student_name, student_grade) VALUES (?, ?, ?)";
					PreparedStatement add = con.prepareStatement(query);
					add.setString(1, "K11" + id_no);
					add.setString(2, name);
					add.setInt(3, grade);
					add.execute();
					JOptionPane.showMessageDialog(null, "Student Record added successfully!", null,
							JOptionPane.INFORMATION_MESSAGE, null);
			 } 
		}	
			else if ((countRow() >= 5)) JOptionPane.showMessageDialog(null, "Slot is Empty!", "Error", JOptionPane.WARNING_MESSAGE, null);
	}

	public static void deleteStudent() throws SQLException {
		Connection con = DBconnect.getConnection();

		String ID = JOptionPane.showInputDialog(null, "Enter Student ID", "Delete", JOptionPane.PLAIN_MESSAGE);
		
		if(ID != null) {
			if (scanRecords(ID)) {
				String query = "DELETE FROM student_records WHERE student_id = ?";
				PreparedStatement delete = con.prepareStatement(query);
				delete.setString(1, ID);
				delete.execute();
				JOptionPane.showMessageDialog(null, "Student Record deleted successfully!", null,
						JOptionPane.INFORMATION_MESSAGE, null);
			} else
				JOptionPane.showMessageDialog(null, "Student ID not found!", "Error", JOptionPane.WARNING_MESSAGE, null);
		}
		else return;
	}

	public static void searchStudent() throws SQLException {
		Connection con = DBconnect.getConnection();
		String ID = JOptionPane.showInputDialog(null, "Enter Student ID", "Search", JOptionPane.PLAIN_MESSAGE);
		
		if(ID != null) {
			if (scanRecords(ID)) {
				String query = "SELECT * FROM student_records WHERE student_id = ?";
				PreparedStatement search = con.prepareStatement(query);
				search.setString(1, ID);
				ResultSet rs = search.executeQuery();

				String[] field = { "Student ID", "Name", "Grade" };
				String[][] data = new String[1][3];

				int count = 0;

				while (rs.next()) {
					data[count][0] = rs.getString(2);
					data[count][1] = rs.getString(3);
					data[count][2] = String.valueOf(rs.getInt(4));
					count++;
				}

				JTable student_record = new JTable(data, field);
				student_record.setVisible(true);
				student_record.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				student_record.getColumn(field[0]).setMaxWidth(80);
				student_record.getColumn(field[2]).setMaxWidth(50);
				student_record.getColumn(field[0]);
				student_record.getColumn(field[1]);
				student_record.getColumn(field[2]);
				
				JScrollPane js = new JScrollPane(student_record);
				js.setPreferredSize(new Dimension(400,103));
				
				JOptionPane.showMessageDialog(null, js, "Student Records",
						JOptionPane.PLAIN_MESSAGE);

			} else
				JOptionPane.showMessageDialog(null, "Student ID not found!", "Error", JOptionPane.WARNING_MESSAGE, null);
		}
		else return;
	}
 
	public static void changeGrade() throws SQLException {
		Connection con = DBconnect.getConnection();

		String ID = JOptionPane.showInputDialog(null, "Enter Student ID", "Change", JOptionPane.PLAIN_MESSAGE);
		
		if(ID != null) {
			if (scanRecords(ID)) {
				String query_ID = "SELECT * FROM student_records WHERE student_id = ?";
				PreparedStatement search = con.prepareStatement(query_ID);
				search.setString(1, ID);
				ResultSet rs = search.executeQuery();

				String[] field = { "Student ID", "Name", "Grade" };
				String[][] data = new String[1][3];

				int count = 0;

				while (rs.next()) {
					data[count][0] = rs.getString(2);
					data[count][1] = rs.getString(3);
					data[count][2] = String.valueOf(rs.getInt(4));
					count++;
				}

				JTable student_record = new JTable(data, field);
				student_record.setVisible(true);
				student_record.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				student_record.getColumn(field[0]).setMaxWidth(80);
				student_record.getColumn(field[2]).setMaxWidth(50);
				student_record.getColumn(field[0]);
				student_record.getColumn(field[1]);
				student_record.getColumn(field[2]);
				JScrollPane js = new JScrollPane(student_record);
				js.setPreferredSize(new Dimension(400,103));
				

				String[] options = { "Back", "Change Grade" };
				int change = JOptionPane.showOptionDialog(null, js, "Student Records",
						JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[1]);
				
				if (change == 1) {
					int grade;
					
					try {
						grade = Integer.parseInt(
								JOptionPane.showInputDialog(null, "Enter Grade", "Change Grade", JOptionPane.PLAIN_MESSAGE));
					}
					catch(NumberFormatException e) {
						return;
					}
					
						String query = "UPDATE student_records SET student_grade = ? where student_id = ?";
						PreparedStatement update = con.prepareStatement(query);
						update.setInt(1, grade);
						update.setString(2, ID);
						update.execute();
						JOptionPane.showMessageDialog(null, "Student Grade changed successfully!", "Change Grade",
								JOptionPane.INFORMATION_MESSAGE, null);
				}
			} else
				JOptionPane.showMessageDialog(null, "Student ID not found!", "Error", JOptionPane.WARNING_MESSAGE, null);
		}
		else return;
	}

	public static boolean scanRecords(String ID) throws SQLException {
		Connection con = DBconnect.getConnection();

		boolean student_found = false;
		String query_find = "SELECT * FROM student_records";
		PreparedStatement find_student = con.prepareStatement(query_find);
		ResultSet start_finding = find_student.executeQuery();

		while (start_finding.next()) {
			if (ID.equalsIgnoreCase(start_finding.getString(2)))
				student_found = true;
		}

		con.close();
		return student_found;
	}

	public static void exit() throws SQLException {
		Connection con = DBconnect.getConnection();

		int exit = JOptionPane.showConfirmDialog(null, "Are you sure?", "Exit", JOptionPane.YES_NO_OPTION);

		if (exit == 0) {
			con.close();
			System.exit(0);
		}
	}

	public static int countRow() throws SQLException {
		int count = 0;
		Connection con = DBconnect.getConnection();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM student_records");

		while (rs.next()) {
			++count;
		}

		return count;
	}

	public static void clear() throws SQLException {
		Connection con = DBconnect.getConnection();

		int clear = JOptionPane.showConfirmDialog(null, "Are you sure?", "Clear", JOptionPane.YES_NO_OPTION);

		if (clear == 0) {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM student_records");
			stmt.execute();
		}
	}

}
