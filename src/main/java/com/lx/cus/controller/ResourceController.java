package com.lx.cus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.Resource;
import com.lx.cus.service.ResourceService;
import com.lx.cus.vo.DataGrid;

@RestController
@RequestMapping(value = "/auth")
public class ResourceController {
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/listGrid")
	public DataGrid<Resource> listAllForPropertyGrid() {
		return this.resourceService.listAllForPropertyGrid();
	}

}
