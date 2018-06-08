package com.lx.cus.util;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static byte[] toExcelBytes(List<?> rows, LinkedMap<String, String> keyAndHeaders, String sheetName) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		String[] keys = keyAndHeaders.keySet().toArray(new String[keyAndHeaders.size()]);
		XSSFSheet sheet = workbook.createSheet(sheetName);
		XSSFRow row = sheet.createRow(0);
		int cols = keys.length;
		for (int i = 0; i < cols; ++i) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(keyAndHeaders.get(keys[i]));
		}
		int rowIndex = 1;
		for (Object obj : rows) {
			if (obj != null) {
				row = sheet.createRow(rowIndex++);
				for (int i = 0; i < cols; ++i) {
					String key = keys[i];
					String value = BeanUtils.getSimpleProperty(obj, key);
					XSSFCell cell = row.createCell(i);
					cell.setCellValue(value);
				}
			}
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 8);
		workbook.write(out);
		workbook.close();
		out.close();
		return out.toByteArray();
	}

}
