package com.lx.cus.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.DataBackupLog;
import com.lx.cus.repository.DataBackupLogRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class DataBackupLogService {
	
	@Autowired
	private DataBackupLogRepository dataBackupLogRepository;
	
	@Value("${lx.data-backup-path}")
	private String backPath;

	public DataGrid<DataBackupLog> search(int page, int rows) {
		return this.dataBackupLogRepository.listByPage(page, rows);
	}

	public Response save(DataBackupLog dataBackupLog) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_DATA);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String path = backPath + "/" + dataBackupLog.getTableName() + dateFormat.format(new Date()) + ".sql";
		Date startTime = new Date();
		dataBackupLogRepository.backup(dataBackupLog.getTableName(), path);
		Date endTime = new Date();
		
		dataBackupLog.setId(null);
		dataBackupLog.setEndTime(endTime);
		dataBackupLog.setStartTime(startTime);
		dataBackupLog.setBackupPath(path);
		dataBackupLog.setCreateUserId(SecurityUtils.getCurrentUser().getId());
		
		this.dataBackupLogRepository.save(dataBackupLog);
		return new Response(0, "备份成功", null);
	}

	public DataGrid<DataBackupLog> listTableGrid(int page, int rows) {
		return this.dataBackupLogRepository.listTableGrid(page, rows);
	}

}
