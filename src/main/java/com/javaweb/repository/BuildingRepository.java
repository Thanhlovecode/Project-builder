package com.javaweb.repository;

import java.util.List;

import com.javaweb.builder.BuildingSearchBuilder;

import entity.BuildingEntity;

public interface BuildingRepository {
	List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder);
}
