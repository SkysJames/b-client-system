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
import com.lx.cus.entity.SaleOrder;
import com.lx.cus.service.SaleOrderService;
import com.lx.cus.util.CacheContextUtil;
import com.lx.cus.util.ExcelUtil;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.ComboVo;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/order")
public class SaleOrderController {
	
	@Autowired
	private SaleOrderService saleOrderService;
	
	@GetMapping(value = "/search")
	public DataGrid<SaleOrder> search(DataGridRequest dataGridRequest, SaleOrder saleOrder) {
		return this.saleOrderService.search(saleOrder, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(SaleOrder saleOrder) {
		return this.saleOrderService.save(saleOrder);
	}
	
	@PostMapping(value = "/updateStatus")
	public Response updateStatus(@RequestParam("ids[]") Integer[] ids, @RequestParam("status") String status) {
		return this.saleOrderService.updateStatus(ids, status);
	}
	
	@GetMapping(value = "/listTypes")
	public List<ComboVo> listTypes() {
		List<ComboVo> comboVos = new ArrayList<>(2);
		comboVos.add(new ComboVo(SaleOrder.Type.ADVERTISING, SaleOrder.Type.ADVERTISING));
		comboVos.add(new ComboVo(SaleOrder.Type.REFUND, SaleOrder.Type.REFUND));
		return comboVos;
	}
	
	@GetMapping(value = "/listStatus")
	public List<ComboVo> listStatus() {
		List<ComboVo> comboVos = new ArrayList<>(2);
		comboVos.add(new ComboVo(SaleOrder.Status.PAYMENT_NON, SaleOrder.Status.PAYMENT_NON));
		comboVos.add(new ComboVo(SaleOrder.Status.PAYMENT_BEEN, SaleOrder.Status.PAYMENT_BEEN));
		comboVos.add(new ComboVo(SaleOrder.Status.PAYMENT_FINISH, SaleOrder.Status.PAYMENT_FINISH));
		comboVos.add(new ComboVo(SaleOrder.Status.AGENT_REFUND, SaleOrder.Status.AGENT_REFUND));
		comboVos.add(new ComboVo(SaleOrder.Status.CUSTOMER_REFUND, SaleOrder.Status.CUSTOMER_REFUND));
		comboVos.add(new ComboVo(SaleOrder.Status.ORDER_INVALID, SaleOrder.Status.ORDER_INVALID));
		return comboVos;
	}
	
	@GetMapping(value = "/getStatisticsInfo")
	public Response getStatisticsInfo(SaleOrder saleOrder) {
		return this.saleOrderService.getStatisticsInfo(saleOrder);
	}
	
	@GetMapping(value = "/export")
	public Response export(SaleOrder saleOrder, @RequestParam("headers") String headers) throws Exception {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SALES_EXPORT);
		DataGrid<SaleOrder> dataGrid = this.saleOrderService.search(saleOrder, 1, Integer.MAX_VALUE);
		List<SaleOrder> rows = dataGrid.getRows();
		if (rows == null || rows.isEmpty()) {
			return new Response(-1, "没有数据可以导出", null);
		}
		LinkedMap<String, String> allKeyAndHeaders = new LinkedMap<>();
	    allKeyAndHeaders.put("type", "订单类型");
	    allKeyAndHeaders.put("code", "订单号");
	    allKeyAndHeaders.put("createTimeStr", "录单日期");
	    allKeyAndHeaders.put("userName", "录单人");
	    allKeyAndHeaders.put("customerName", "客户");
	    allKeyAndHeaders.put("customerMobilePhone", "手机号码");
	    allKeyAndHeaders.put("account", "账号");
	    allKeyAndHeaders.put("productName", "产品");
	    allKeyAndHeaders.put("customerPay", "客户应付金额");
	    allKeyAndHeaders.put("costPrice", "成本价");
	    allKeyAndHeaders.put("grossProfit", "毛利");
	    allKeyAndHeaders.put("status", "付款状态");
	    
	    String[] keys = headers.split(",");
	    LinkedMap<String, String> keyAndHeaders = new LinkedMap<>();
	    for (String k : keys) {
	    	if (allKeyAndHeaders.containsKey(k)) {
	    		keyAndHeaders.put(k, allKeyAndHeaders.get(k));
	    	}
	    }
	    
	    byte[] excelBytes = ExcelUtil.toExcelBytes(rows, keyAndHeaders, "销售单信息");
	    String code = CacheContextUtil.setValue(excelBytes);
	    
	    Map<String, Object> data = new HashMap<>();
	    data.put("code", code);
	    data.put("name", "销售单信息" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".xlsx");
	    return new Response(0, "", data);
	}
	
	@GetMapping(value = "/getTypeCount")
	public List<Map<String, Object>> getTypeCount() {
		return this.saleOrderService.getTypeCount();
	}

}
