package com.lx.cus.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.Customer;
import com.lx.cus.service.CustomerService;
import com.lx.cus.util.CacheContextUtil;
import com.lx.cus.util.ExcelUtil;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.ComboVo;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping(value = "/search")
	public DataGrid<Customer> search(DataGridRequest dataGridRequest, Customer customer) {
		return this.customerService.search(customer, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(Customer customer) {
		return this.customerService.save(customer);
	}
	
	@PostMapping(value = "/update")
	public Response update(Customer customer) {
		return this.customerService.update(customer);
	}
	
	@PostMapping(value = "/delete")
	public Response delete(@RequestParam(value = "ids[]") Integer[] ids) {
		return this.customerService.delete(ids);
	}
	
	@GetMapping(value = "/detail")
	public Customer detail(@RequestParam("id") Integer id) {
		return this.customerService.get(id);
	}
	
	@GetMapping(value = "/listShares")
	public List<ComboVo> listShares() {
		List<ComboVo> vos = new ArrayList<>(2);
		vos.add(new ComboVo(0, "否"));
		vos.add(new ComboVo(1, "是"));
		return vos;
	}
	
	@GetMapping(value = "/export")
	public Response export(Customer customer, @RequestParam("headers") String headers) throws Exception {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_EXPORT);
		DataGrid<Customer> dataGrid = this.customerService.search(customer, 1, Integer.MAX_VALUE);
		List<Customer> rows = dataGrid.getRows();
		if (rows == null || rows.isEmpty()) {
			return new Response(-1, "没有数据可以导出", null);
		}
		LinkedMap<String, String> allKeyAndHeaders = new LinkedMap<>();
	    allKeyAndHeaders.put("name", "客户");
	    allKeyAndHeaders.put("mobilePhone", "手机号码");
	    allKeyAndHeaders.put("telePhone", "座机号码");
	    allKeyAndHeaders.put("qq", "QQ");
	    allKeyAndHeaders.put("wechat", "微信");
	    allKeyAndHeaders.put("tradeName", "客户行业");
	    allKeyAndHeaders.put("levelName", "客户级别");
	    allKeyAndHeaders.put("company", "公司名称");
	    allKeyAndHeaders.put("createTimeStr", "添加时间");
	    
	    String[] keys = headers.split(",");
	    LinkedMap<String, String> keyAndHeaders = new LinkedMap<>();
	    for (String k : keys) {
	    	if (allKeyAndHeaders.containsKey(k)) {
	    		keyAndHeaders.put(k, allKeyAndHeaders.get(k));
	    	}
	    }
	    
	    byte[] excelBytes = ExcelUtil.toExcelBytes(rows, keyAndHeaders, "客户信息");
	    String code = CacheContextUtil.setValue(excelBytes);
	    
	    Map<String, Object> data = new HashMap<>();
	    data.put("code", code);
	    data.put("name", "客户信息" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".xlsx");
	    return new Response(0, "", data);
	}

}
