package com.lx.cus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.DataBackupLog;
import com.lx.cus.service.DataBackupLogService;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/databackup")
public class DataBackupLogController {
	
	@Autowired
	private DataBackupLogService dataBackupLogService;
	
	@GetMapping(value = "/search")
	public DataGrid<DataBackupLog> search(DataGridRequest dataGridRequest) {
		return this.dataBackupLogService.search(dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(DataBackupLog dataBackupLog) {
		return this.dataBackupLogService.save(dataBackupLog);
	}
	
	@GetMapping(value = "/listTableGrid")
	public DataGrid<DataBackupLog> listTableGrid(DataGridRequest dataGridRequest) {
		return this.dataBackupLogService.listTableGrid(dataGridRequest.getPage(), dataGridRequest.getRows());
	}

}
