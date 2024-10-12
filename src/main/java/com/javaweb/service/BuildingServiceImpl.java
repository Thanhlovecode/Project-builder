package com.javaweb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.BuildingConvertDTO;
import com.javaweb.api.BuildingDTO;
import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.converto.BuildingSearchBuilderConverto;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;

import entity.BuildingEntity;
import entity.DistrictEntity; 
@Service
public class BuildingServiceImpl implements BuildingService {
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired DistrictRepository districtRepository;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired
	private BuildingSearchBuilderConverto buildingSearchBuilderConverto;
	
	@Override
	public List<BuildingDTO> findAll(BuildingConvertDTO buildingConvertDTO) {
		BuildingSearchBuilder buildingSearchBuilder= buildingSearchBuilderConverto.toBuildingSearchBuilder(buildingConvertDTO);
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(buildingSearchBuilder);
		List<BuildingDTO> result= new ArrayList<>();
		HashMap<String,BuildingDTO> hashMap = new HashMap<>();
	for(BuildingEntity item : buildingEntities){
		BuildingDTO building= modelMapper.map(item,BuildingDTO.class);
		if(!hashMap.containsKey(item.getName())){
			DistrictEntity districtEntity= districtRepository.findNameById(item.getDistrictId());
			building.setAddress(districtEntity.getName()+","+item.getWard()+","+item.getStreet());
			hashMap.put(item.getName(), building);
			result.add(building);
			}
		else {
			BuildingDTO buildingDTO = hashMap.get(item.getName());
			buildingDTO.appendRentArea(item.getRentArea());
		}
	      }
		return result;
	}

}
