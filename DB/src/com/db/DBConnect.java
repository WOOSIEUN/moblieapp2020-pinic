package com.db;

import java.sql.*;

public class DBConnect {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = "";
	Double dist = 0.05388;
	String returnString;

	// �̸� ������ �� AWS RDS DB �ν��Ͻ��� ����
	String driver = "com.mysql.cj.jdbc.Driver";
	String dbURL = "jdbc:mysql://moappdb.co0p7vaxsheo.us-east-2.rds.amazonaws.com:3306/";
	String dbName = "MoApp";
	String user = "admin";
	String pass = "dlgud123"; // commit�� �ش�κ� �׻� ������ ��

	private static DBConnect instance = new DBConnect();

	public static DBConnect getInstance() {
		return instance;
	}

	public DBConnect() {

	}

	public String DBConnect_SQL(String DBTable, String modeStr, String latitudeStr, String longitudeStr) {
		if (DBTable == null || modeStr == null || latitudeStr == null || longitudeStr == null) {
			return null;
		}
		try {
			Double latitude = Double.valueOf(latitudeStr);
			Double longitude = Double.valueOf(longitudeStr);
			// Connect
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL + dbName, user, pass);
			System.out.println("Connection Success");

			// SQL�� ����

			if (DBTable.equals("FESTIVAL")) {
				sql = "SELECT id, fstvlNm, latitude, longitude FROM " + DBTable + " WHERE latitude <= "
						+ (double) (latitude + dist) + " AND latitude >= " + (double) (latitude - dist)
						+ " AND longitude >= " + (double) (longitude - dist) + " AND longitude <= "
						+ (double) (longitude + dist) + ";";
			} else if (DBTable.equals("TOUR")) {
				sql = "SELECT id, trrsrtNm, latitude, longitude FROM " + DBTable + " WHERE latitude <= "
						+ (double) (latitude + dist) + " AND latitude >= " + (double) (latitude - dist)
						+ " AND longitude >= " + (double) (longitude - dist) + " AND longitude <= "
						+ (double) (longitude + dist) + ";";
			} else if (DBTable.equals("HERITAGE")) {
				sql = "SELECT id, ccbaMnm1, latitude, longitude FROM " + DBTable + " WHERE latitude <= "
						+ (double) (latitude + dist) + " AND latitude >= " + (double) (latitude - dist)
						+ " AND longitude >= " + (double) (longitude - dist) + " AND longitude <= "
						+ (double) (longitude + dist) + ";";
			}
			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			returnString = "";
			while (rs.next()) {
				returnString += rs.getString(1) + "#" + rs.getString(2) + "#" + rs.getString(3) + "#" + rs.getString(4)
						+ "#";
			}

			// ------------ �ٸ� ��� �߰��� ���⿡ �߰� �ʿ� ---------------

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
		return returnString;
	}
}