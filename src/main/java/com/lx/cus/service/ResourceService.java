package com.lx.cus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lx.cus.entity.Resource;
import com.lx.cus.repository.ResourceRepository;
import com.lx.cus.vo.DataGrid;

@Service
public class ResourceService {
	
	@Autowired
	private ResourceRepository resourceRepository;

	public DataGrid<Resource> listAllForPropertyGrid() {
		List<Resource> rows = this.resourceRepository.listAllForPropertyGrid();
		DataGrid<Resource> dataGrid = new DataGrid<>();
		dataGrid.setTotal(rows.size());
		dataGrid.setRows(rows);
		return dataGrid;
	}
	
	

}
