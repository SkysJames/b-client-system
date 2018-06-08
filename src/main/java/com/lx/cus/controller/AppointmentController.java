package com.lx.cus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lx.cus.entity.Appointment;
import com.lx.cus.entity.AppointmentTip;
import com.lx.cus.service.AppointmentService;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.DataGridRequest;
import com.lx.cus.vo.Response;

@RestController
@RequestMapping(value = "/appointment")
public class AppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@GetMapping(value = "/search")
	public DataGrid<Appointment> search(DataGridRequest dataGridRequest, Appointment appointment) {
		return this.appointmentService.search(appointment, dataGridRequest.getPage(), dataGridRequest.getRows());
	}
	
	@PostMapping(value = "/save")
	public Response save(Appointment appointment) {
		return this.appointmentService.save(appointment);
	}
	
	@PostMapping(value = "/update")
	public Response update(Appointment appointment) {
		return this.appointmentService.update(appointment);
	}
	
	@PostMapping(value = "/delete")
	public Response delete(@RequestParam(value = "ids[]") Integer[] ids) {
		return this.appointmentService.delete(ids);
	}
	
	@GetMapping(value = "/detail")
	public Appointment detail(@RequestParam("id") Integer id) {
		return this.appointmentService.get(id);
	}
	
	@GetMapping(value = "/listTips")
	public List<AppointmentTip> listTips(@RequestParam("id") Integer id) {
		return this.appointmentService.listTips(id);
	}
	
	@RequestMapping(value = "/listTipsGrid")
	public DataGrid<AppointmentTip> listTipsGrid(@RequestParam("id") Integer id) {
		return this.appointmentService.listTipsGrid(id);
	}
	
	@PostMapping(value = "/addTip")
	public Response addTip(AppointmentTip appointmentTip) {
		return this.appointmentService.addTip(appointmentTip);
	}

}
