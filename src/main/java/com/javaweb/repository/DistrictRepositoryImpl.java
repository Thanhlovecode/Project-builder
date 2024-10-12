package com.javaweb.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import entity.DistrictEntity;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository{
	static final String URL="jdbc:mysql://localhost:3306/estatebasic";
	static final String user="root";
	static final String password="Thanh0962578061@";
	@Override
	public DistrictEntity findNameById(Integer number) {
		 String sql= "select a.name from district a where a.id = "+number+ ";";
		 DistrictEntity districtEntity= new DistrictEntity();
		try {
			Connection connection=DriverManager.getConnection(URL,user,password);
			Statement statement =connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				districtEntity.setName(result.getString("name"));
			}
		}
			catch(SQLException ex) {
				ex.printStackTrace();
			}
		return districtEntity;
	}
	

}
