package com.lx.cus.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.User;
import com.lx.cus.service.UserService;
import com.lx.cus.vo.ComboVo;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/search")
	public DataGrid<User> search(DataGridRequest dataGridRequest, User user) {
		return this.userService.search(user, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(User user) {
		return this.userService.save(user);
	}
	
	@PostMapping(value = "/update")
	public Response update(User user) {
		return this.userService.update(user);
	}
	
	@GetMapping(value = "/detail")
	public User detail(@RequestParam("id") Integer id) {
		return this.userService.get(id);
	}
	
	@PostMapping(value = "/login")
	public Response login(String username, String password) {
		return this.userService.login(username, password);
	}
	
	@GetMapping(value = "/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect("/login.html");
	}
	
	@GetMapping(value = "/getCurrentUser")
	public User getCurrentUser() {
		return this.userService.getCurrentUser();
	}
	
	@GetMapping(value = "/listStatus")
	public List<ComboVo> listStatus() {
		List<ComboVo> status = new ArrayList<>();
		status.add(new ComboVo("在职", "在职"));
		status.add(new ComboVo("锁定", "锁定"));
		status.add(new ComboVo("离职", "离职"));
		return status;
	}
	
	@GetMapping(value = "/listSex")
	public List<ComboVo> listSex() {
		List<ComboVo> sexs = new ArrayList<>();
		sexs.add(new ComboVo("男", "男"));
		sexs.add(new ComboVo("女", "女"));
		sexs.add(new ComboVo("未知", "未知"));
		return sexs;
	}
	
	@PostMapping(value = "/authorize")
	public Response authorize(@RequestParam(value = "roleIds[]", required = false) Integer[] roleIds, @RequestParam("id") Integer userId) {
		return this.userService.authorize(roleIds, userId);
	}
	
	@GetMapping(value = "/listRoleIds")
	public List<Integer> listRoleIds(@RequestParam("id") Integer userId) {
		return this.userService.listRoleIdsByUserId(userId);
	}
	
	@GetMapping(value = "/listAll")
	public List<User> listAll() {
		return this.userService.listAll();
	}
	
	@PostMapping(value = "/changePassword")
	public Response changePassword(User user) {
		return this.userService.changePassword(user);
	}
	
	@GetMapping(value = "/getStatusCount")
	public List<Map<String, Object>> getStatusCount() {
		return this.userService.getStatusCount();
	}

}
