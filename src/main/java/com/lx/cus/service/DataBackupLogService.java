package com.lx.cus.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.common.ApplicationException;
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
	
	private final ExecutorService executorService = Executors.newFixedThreadPool(10);

	public DataGrid<DataBackupLog> search(int page, int rows) {
		return this.dataBackupLogRepository.listByPage(page, rows);
	}

	public Response save(DataBackupLog dataBackupLog) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_DATA);
		Integer userId = SecurityUtils.getCurrentUser().getId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(new Date());
		
		String[] tableNames = dataBackupLog.getTableName().split(",");
		String[] remarks = dataBackupLog.getRemark().split(",");
		
		List<Callable<DataBackupLog>> tasks = new ArrayList<>(tableNames.length);
		for (int i = 0; i < tableNames.length; ++i) {
			String tableName = tableNames[i];
			String remark = remarks[i];
			tasks.add(() -> {
				DataBackupLog log = new DataBackupLog();
				String path = backPath + "/" + tableName + dateStr + ".sql";
				Date startTime = new Date();
				dataBackupLogRepository.backup(tableName, path);
				Date endTime = new Date();
				log.setEndTime(endTime);
				log.setStartTime(startTime);
				log.setRemark(remark);
				log.setCreateUserId(userId);
				log.setBackupPath(path);
				log.setTableName(tableName);
				return log;
			});
		}
		
		try {
			List<Future<DataBackupLog>> futures = this.executorService.invokeAll(tasks);
			for (Future<DataBackupLog> ft : futures) {
				DataBackupLog log = ft.get();
				this.dataBackupLogRepository.save(log);
			}
		} catch (InterruptedException e) {
			throw new ApplicationException("备份出错");
		} catch (ExecutionException e) {
			throw new ApplicationException("备份出错");
		}
		
		return new Response(0, "备份成功", null);
	}

	public DataGrid<DataBackupLog> listTableGrid(int page, int rows) {
		return this.dataBackupLogRepository.listTableGrid(page, rows);
	}

}
