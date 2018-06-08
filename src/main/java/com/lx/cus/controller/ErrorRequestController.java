package com.lx.cus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lx.cus.common.ApplicationException;
import com.lx.cus.vo.Response;

@Controller
@ControllerAdvice
public class ErrorRequestController implements ErrorController {
	
	private static final String error = "/error";

	@Override
	public String getErrorPath() {
		return error;
	}
	
	/*@RequestMapping(value = error, produces = "html/text")
	public String htmlError() {
		return "/404.html";
	}*/
	
	@RequestMapping(value = error/*, produces = "application/json;charset=UTF-8"*/)
	@ResponseBody
	public Response jsonError(HttpServletRequest request, HttpServletResponse response) {
		int status = response.getStatus();
		response.setStatus(200);
		if (404 == status) {
			return new Response(-1, "您要的东西我们没有哦", null);
		}
		return new Response(-1, "系统出错", null);
	}
	
	@ExceptionHandler(value = ApplicationException.class)
	@ResponseBody
	public Response applicationError(ApplicationException applicationException) {
		return new Response(-1, applicationException.getMessage(), null);
	}

}
