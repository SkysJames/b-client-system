package com.lx.cus.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lx.cus.util.CacheContextUtil;

@Controller
@RequestMapping(value = "/download")
public class DownLoadController {
	
	@RequestMapping
	public void download(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
		Object val = CacheContextUtil.getValue(code);
		if (val != null) {
			byte[] bys = (byte[]) val;
			response.getOutputStream().write(bys, 0, bys.length);
			response.getOutputStream().close();
		}
	}

}
