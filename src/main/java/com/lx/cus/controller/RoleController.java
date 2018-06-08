package com.lx.cus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.Role;
import com.lx.cus.service.RoleService;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping(value = "/search")
	public DataGrid<Role> search(DataGridRequest dataGridRequest, @RequestParam("term") String term) {
		return this.roleService.searchByNameAndRemark(term, term, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(Role role) {
		return this.roleService.save(role);
	}
	
	@PostMapping(value = "/update")
	public Response update(Role role) {
		return this.roleService.update(role);
	}
	
	@PostMapping(value = "/delete")
	public Response delete(@RequestParam(value = "ids[]") Integer[] ids) {
		return this.roleService.delete(ids);
	}
	
	@GetMapping(value = "/detail")
	public Role detail(@RequestParam("id") Integer id) {
		return this.roleService.get(id);
	}
	
	@PostMapping(value = "/authorize")
	public Response authorize(@RequestParam(value = "resIds[]", required = false) Integer[] resIds, @RequestParam("id") Integer id) {
		return this.roleService.authorize(resIds, id);
	}
	
	@GetMapping(value = "/listResIds")
	public List<Integer> listResoureIdByRoleId(@RequestParam("id") Integer id) {
		return this.roleService.listResoureIdByRoleId(id);
	}
	
	@GetMapping(value = "/listAllGrid")
	public DataGrid<Role> listAllGrid() {
		return this.roleService.listAllGrid();
	}

}
