package com.lx.cus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.CustomerLevel;
import com.lx.cus.service.CustomerLevelService;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/customer_level")
public class CustomerLevelController {
	
	@Autowired
	private CustomerLevelService customerLevelService;
	
	@GetMapping(value = "/search")
	public DataGrid<CustomerLevel> search(DataGridRequest dataGridRequest, @RequestParam("term") String term) {
		return this.customerLevelService.searchByNameAndRemark(term, term, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(CustomerLevel customerLevel) {
		return this.customerLevelService.save(customerLevel);
	}
	
	@PostMapping(value = "/update")
	public Response update(CustomerLevel customerLevel) {
		return this.customerLevelService.update(customerLevel);
	}
	
	@PostMapping(value = "/delete")
	public Response delete(@RequestParam(value = "ids[]") Integer[] ids) {
		return this.customerLevelService.delete(ids);
	}
	
	@GetMapping(value = "/detail")
	public CustomerLevel detail(@RequestParam("id") Integer id) {
		return this.customerLevelService.get(id);
	}
	
	@GetMapping(value = "/listAll")
	public List<CustomerLevel> listAll() {
		return this.customerLevelService.listAll();
	}

}
