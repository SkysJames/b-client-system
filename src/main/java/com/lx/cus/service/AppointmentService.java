package com.lx.cus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.Appointment;
import com.lx.cus.entity.AppointmentTip;
import com.lx.cus.repository.AppointmentRepository;
import com.lx.cus.repository.AppointmentTipRepository;
import com.lx.cus.util.BeanUtil;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class AppointmentService {
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private AppointmentTipRepository appointmentTipRepository;

	public DataGrid<Appointment> search(Appointment appointment, int page, int rows) {
		return appointmentRepository.search(appointment, page, rows);
	}

	public Response save(Appointment appointment) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.BOOK_MANAGER);
		appointment.setCreateUserId(SecurityUtils.getCurrentUser().getId());
		appointment.setCreateTime(new Date());
		appointment.setUpdateTime(appointment.getCreateTime());
		appointment.setUpdateUserId(appointment.getCreateUserId());
		appointment.setStatus("未去电");
		this.appointmentRepository.save(appointment);
		return new Response(0, "添加成功", null);
	}

	public Response update(Appointment appointment) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.BOOK_MANAGER);
		appointment.setUpdateTime(new Date());
		appointment.setUpdateUserId(SecurityUtils.getCurrentUser().getId());
		Appointment old = this.appointmentRepository.get(appointment.getId());
		BeanUtil.copyPropertiesIgnoreNull(old, appointment);
		this.appointmentRepository.update(old);
		return new Response(0, "修改成功", null);
	}

	public Response delete(Integer[] ids) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.BOOK_MANAGER);
		for (Integer id : ids) {
			this.appointmentTipRepository.deleteByAppointmentId(id);
			this.appointmentRepository.delete(id);
		}
		return new Response(0, "删除成功", null);
	}

	public Appointment get(Integer id) {
		return this.appointmentRepository.get(id);
	}

	public List<AppointmentTip> listTips(Integer id) {
		return this.appointmentTipRepository.listByAppointmentId(id);
	}

	public DataGrid<AppointmentTip> listTipsGrid(Integer id) {
		List<AppointmentTip> tips = this.listTips(id);
		if (tips != null && !tips.isEmpty()) {
			tips.forEach(e -> e.setGroup(e.getUserEmpNo() + "    " + e.getCreateTimeStr()));
		}
		DataGrid<AppointmentTip> dataGrid = new DataGrid<>();
		if (tips == null) {
			dataGrid.setTotal(0);
			dataGrid.setRows(new ArrayList<>(0));
		} else {
			dataGrid.setTotal(tips.size());
			dataGrid.setRows(tips);
		}
		return dataGrid;
	}

	public Response addTip(AppointmentTip appointmentTip) {
		appointmentTip.setCreateTime(new Date());
		appointmentTip.setCreateUserId(SecurityUtils.getCurrentUser().getId());
		this.appointmentTipRepository.save(appointmentTip);
		return new Response(0, "添加成功", null);
	}

}
