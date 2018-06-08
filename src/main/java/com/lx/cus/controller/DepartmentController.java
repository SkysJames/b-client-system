package com.lx.cus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.Department;
import com.lx.cus.service.DepartmentService;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/department")
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	@GetMapping(value = "/search")
	public DataGrid<Department> search(DataGridRequest dataGridRequest, @RequestParam("term") String term) {
		return this.departmentService.searchByNameAndRemark(term, term, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(Department department) {
		return this.departmentService.save(department);
	}
	
	@PostMapping(value = "/update")
	public Response update(Department department) {
		return this.departmentService.update(department);
	}
	
	@PostMapping(value = "/delete")
	public Response delete(@RequestParam(value = "ids[]") Integer[] ids) {
		return this.departmentService.delete(ids);
	}
	
	@GetMapping(value = "/detail")
	public Department detail(@RequestParam("id") Integer id) {
		return this.departmentService.get(id);
	}
	
	@GetMapping(value = "/listAll")
	public List<Department> listAll() {
		return this.departmentService.listAll();
	}

}
