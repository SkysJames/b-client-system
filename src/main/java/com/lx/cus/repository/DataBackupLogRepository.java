package com.lx.cus.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.lx.cus.common.ApplicationException;
import com.lx.cus.entity.DataBackupLog;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.vo.DataGrid;

@Repository
public class DataBackupLogRepository extends BaseEntityRepository<DataBackupLog, Integer> {
	
	private final Logger loger = LoggerFactory.getLogger(getClass());
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;

	public void backup(String tableName, String path) {
		String systemName = System.getProperty("os.name").toLowerCase();
		if (systemName.indexOf("linux") >= 0) {
			backupForLinux(tableName, path);
		} else if (systemName.indexOf("window") >= 0) {
			backupForWindows(tableName, path);
		} else {
			throw new ApplicationException("该操作系统不支持备份");
		}
		
	}
	
	private void backupForLinux(String tableName, String path) {
		String baseDir = getBaseDir();
		String tableSchame = getTableSchame();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseDir).append("bin/mysqldump ")
			.append(" -u").append(username).append(" -p").append(password)
			.append(" ").append(tableSchame).append(" ").append(tableName);
		//String[] cmd = new String[]{"/bin/sh ", "-c ", stringBuilder.toString()};
        try {  
            Process process = Runtime.getRuntime().exec(stringBuilder.toString());  
           /* if (process.waitFor() == 0) {// 0 表示线程正常终止。  
                loger.info("数据库备份成功");  
            }*/
            InputStream in = process.getInputStream();// 控制台的输出信息作为输入流
            
            InputStreamReader xx = new InputStreamReader(in, "utf-8");
            // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
 
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            // 组合控制台输出信息字符串
            BufferedReader br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
 
            // 要用来做导入用的sql目标文件：
            File file = new File(path);
            if (!file.exists()) {
            	file.getParentFile().mkdirs();
            	file.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf-8");
            writer.write(outStr);
            writer.flush();
            in.close();
            xx.close();
            br.close();
            writer.close();
            fout.close();
        } catch (Exception e) {  
            loger.info("数据库备份异常", e);
            e.printStackTrace();
            throw new ApplicationException("数据库备份异常");
        } 
	}
	
	private void backupForWindows(String tableName, String path) {
		String baseDir = getBaseDir();
		String tableSchame = getTableSchame();
		if (!baseDir.endsWith(File.separator)) {  
			baseDir = baseDir + File.separator;  
        } 
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseDir).append("bin\\mysqldump.exe ")
			.append(" -u").append(username).append(" -p").append(password)
			.append(" ").append(tableSchame).append(" ").append(tableName);
			//.append(" --result-file=").append(path);
        try {  
            Process process = Runtime.getRuntime().exec(stringBuilder.toString());  
           /* if (process.waitFor() == 0) {// 0 表示线程正常终止。  
                loger.info("数据库备份成功");  
            }*/
            InputStream in = process.getInputStream();// 控制台的输出信息作为输入流
            
            InputStreamReader xx = new InputStreamReader(in, "utf-8");
            // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
 
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            // 组合控制台输出信息字符串
            BufferedReader br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
 
            // 要用来做导入用的sql目标文件：
            File file = new File(path);
            if (!file.exists()) {
            	file.getParentFile().mkdirs();
            	file.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf-8");
            writer.write(outStr);
            writer.flush();
            in.close();
            xx.close();
            br.close();
            writer.close();
            fout.close();
        } catch (Exception e) {  
            loger.info("数据库备份异常", e);
            e.printStackTrace();
            throw new ApplicationException("数据库备份异常");
        } 
	}
	
	public String getBaseDir() {
		String sql = "select @@basedir";
		return this.getEntityManager().createNativeQuery(sql).getSingleResult().toString();
	}
	
	public String getTableSchame() {
		String sql = "select DATABASE()";
		return this.getEntityManager().createNativeQuery(sql).getSingleResult().toString();
	}

	public DataGrid<DataBackupLog> listTableGrid(int page, int rows) {
		String sql = "select t.TABLE_NAME tableName, t.TABLE_COMMENT remark from information_schema.`TABLES` t where t.TABLE_SCHEMA = DATABASE()";
		return this.listByNativeSqlPage2(sql, null, page, rows);
	}

}
