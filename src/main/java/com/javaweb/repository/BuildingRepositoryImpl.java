package com.javaweb.repository;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.utils.CheckUtil;

import entity.BuildingEntity;
@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	static final String URL="jdbc:mysql://localhost:3306/estatebasic";
	static final String user="root";
	static final String password="Thanh0962578061@";
	private StringBuilder sql = new StringBuilder("SELECT DISTINCT a.*, d.value FROM building a "
			+ " inner join rentarea d on d.buildingid=a.id ");
	public static void join(BuildingSearchBuilder buildingSearchBuilder,StringBuilder sql) {
		if(CheckUtil.checkNumber(buildingSearchBuilder.getStaffid())) {
			sql.append(" INNER JOIN assignmentbuilding c ON c.buildingid = a.id ");
		}
	}
	
	public static void queryNomal(BuildingSearchBuilder buildingSearchBuilder,StringBuilder where) {
		try {
			Field[] fields =BuildingSearchBuilder.class.getDeclaredFields();
			for(Field item:fields) {
				item.setAccessible(true);
				String fieldName=item.getName();
				if(!fieldName.equals("staffid") && !fieldName.endsWith("RentPrice") && !fieldName.endsWith("Area")) {
					Object value = item.get(buildingSearchBuilder);
					if(value!=null) {
						if(value instanceof Number) {
							where.append(" AND a." + fieldName + " = "+ value);
						}
						else if(value instanceof String && !((String)value).isEmpty()) {
							where.append(" AND a." + fieldName +" Like '%"+ value +"%' ");
						}
					}
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder,StringBuilder where) {
		Integer staffid=buildingSearchBuilder.getStaffid();
		if(staffid !=null) {
			where.append("And c.staffid = "+ staffid);
		}
		Integer startArea=buildingSearchBuilder.getStartArea();
		Integer endArea= buildingSearchBuilder.getEndArea();
		if(CheckUtil.checkNumber(endArea) || CheckUtil.checkNumber(startArea)) {
			if(CheckUtil.checkNumber(startArea)) {
				where.append(" And d.value >= "+ startArea);
			}
			if(CheckUtil.checkNumber(endArea)) {
				where.append(" And d.value <= "+ endArea);
			}
		}
		
		Integer startRentPrice= buildingSearchBuilder.getStartRentPrice();
		Integer endRentPrice = buildingSearchBuilder.getEndArea();
		if(CheckUtil.checkNumber(startRentPrice) || CheckUtil.checkNumber(endRentPrice)) {
			if(CheckUtil.checkNumber(startRentPrice)) {
				where.append(" And a.rentprice >= "+ startRentPrice);
			}
			if(CheckUtil.checkNumber(endRentPrice)) {
				where.append(" And a.rentprice <= "+ endRentPrice );
			}
		}
	}
	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {
		join(buildingSearchBuilder,sql);
		StringBuilder where = new StringBuilder(" Where 1=1 ");
		queryNomal(buildingSearchBuilder, where);
		querySpecial(buildingSearchBuilder, where);
		sql.append(where);
		List<BuildingEntity> answer=new ArrayList<>();
		try {
			Connection connection=DriverManager.getConnection(URL,user,password);
			Statement statement =connection.createStatement();
			ResultSet result = statement.executeQuery(sql.toString());
			while(result.next()) {
				BuildingEntity building = new BuildingEntity();
				building.setName(result.getString("name"));
				building.setDistrictId(result.getInt("districtid"));
				building.setWard(result.getString("ward"));
				building.setStreet(result.getString("street"));
				building.setFloorArea(result.getInt("floorarea"));
				building.setNumberOfBasement(result.getInt("numberofbasement"));
				building.setDirection(result.getString("direction"));
				building.setLevel(result.getString("level"));
				building.setRentPrice(result.getInt("rentprice"));
				building.setManagerName(result.getString("managername"));
				building.setManagerPhoneNumber(result.getString("managerphonenumber"));
				building.setRentArea(result.getInt("value"));
				building.setServiceFee(result.getString("servicefee"));
				answer.add(building);
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
	    }
		return answer;
	  }
}
