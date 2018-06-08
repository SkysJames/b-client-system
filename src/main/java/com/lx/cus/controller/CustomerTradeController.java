package com.lx.cus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.CustomerTrade;
import com.lx.cus.service.CustomerTradeService;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/customer_trade")
public class CustomerTradeController {
	
	@Autowired
	private CustomerTradeService customerTradeService;
	
	@GetMapping(value = "/search")
	public DataGrid<CustomerTrade> search(DataGridRequest dataGridRequest, @RequestParam("term") String term) {
		return this.customerTradeService.searchByNameAndRemark(term, term, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(CustomerTrade customerTrade) {
		return this.customerTradeService.save(customerTrade);
	}
	
	@PostMapping(value = "/update")
	public Response update(CustomerTrade customerTrade) {
		return this.customerTradeService.update(customerTrade);
	}
	
	@PostMapping(value = "/delete")
	public Response delete(@RequestParam(value = "ids[]") Integer[] ids) {
		return this.customerTradeService.delete(ids);
	}
	
	@GetMapping(value = "/detail")
	public CustomerTrade detail(@RequestParam("id") Integer id) {
		return this.customerTradeService.get(id);
	}
	
	@GetMapping(value = "/listAll")
	public List<CustomerTrade> listAll() {
		return this.customerTradeService.listAll();
	}

}
