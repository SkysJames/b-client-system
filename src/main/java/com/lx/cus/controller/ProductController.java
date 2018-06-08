package com.lx.cus.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.lx.cus.entity.Product;
import com.lx.cus.service.ProductService;
import com.lx.cus.util.CacheContextUtil;
import com.lx.cus.util.ExcelUtil;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping(value = "/search")
	public DataGrid<Product> search(DataGridRequest dataGridRequest, @RequestParam("term") String term) {
		return this.productService.searchByNameAndRemark(term, term, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(Product product) {
		return this.productService.save(product);
	}
	
	@PostMapping(value = "/update")
	public Response update(Product product) {
		return this.productService.update(product);
	}
	
	@PostMapping(value = "/delete")
	public Response delete(@RequestParam(value = "ids[]") Integer[] ids) {
		return this.productService.delete(ids);
	}
	
	@GetMapping(value = "/detail")
	public Product detail(@RequestParam("id") Integer id) {
		return this.productService.get(id);
	}
	
	@GetMapping(value = "/listAll")
	public List<Product> listAll() {
		return this.productService.listAll();
	}
	
	@GetMapping(value = "/export")
	public Response export(@RequestParam("term") String term, @RequestParam("headers") String headers) throws Exception {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.PRODUCT_EXPORT);
		DataGrid<Product> dataGrid = this.productService.searchByNameAndRemark(term, term, 1, Integer.MAX_VALUE);
		List<Product> rows = dataGrid.getRows();
		if (rows == null || rows.isEmpty()) {
			return new Response(-1, "没有数据可以导出", null);
		}
		LinkedMap<String, String> allKeyAndHeaders = new LinkedMap<>();
	    allKeyAndHeaders.put("name", "产品名称");
	    allKeyAndHeaders.put("remark", "备注");
	    
	    String[] keys = headers.split(",");
	    LinkedMap<String, String> keyAndHeaders = new LinkedMap<>();
	    for (String k : keys) {
	    	if (allKeyAndHeaders.containsKey(k)) {
	    		keyAndHeaders.put(k, allKeyAndHeaders.get(k));
	    	}
	    }
	    
	    byte[] excelBytes = ExcelUtil.toExcelBytes(rows, keyAndHeaders, "产品信息");
	    String code = CacheContextUtil.setValue(excelBytes);
	    
	    Map<String, Object> data = new HashMap<>();
	    data.put("code", code);
	    data.put("name", "产品信息" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".xlsx");
	    return new Response(0, "", data);
	}

}
