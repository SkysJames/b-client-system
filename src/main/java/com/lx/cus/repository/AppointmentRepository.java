package com.lx.cus.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lx.cus.entity.Appointment;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.vo.DataGrid;

@Repository
public class AppointmentRepository extends BaseEntityRepository<Appointment, Integer> {

	public DataGrid<Appointment> search(Appointment appointment, int page, int rows) {
		String sql = //
				"select a.id, a.remark, a.`status`, a.appointment_time appointmentTime,  " +
				"       DATE_FORMAT(a.create_time, '%Y-%m-%d %T') createTimeStr, " +
				"       p.`name` productName, c.`name` customerName, u.emp_no userEmpNo " +
				"from appointment a, sys_user u, product p, customer c " +
				"where a.update_user_id = u.id and a.product_id = p.id and a.customer_id = c.id ";
		Map<String, Object> params = new HashMap<>();
		
		if (StringUtils.hasText(appointment.getCustomerName())) {
			sql += " and c.name like :name ";
			params.put("name", "%" + appointment.getCustomerName() + "%");
		}
		
		if (StringUtils.hasText(appointment.getCustomerMobilePhone())) {
			sql += " and c.mobile_phone like :mobilePhone ";
			params.put("mobilePhone", "%" + appointment.getCustomerMobilePhone() + "%");
		}
		
		if (StringUtils.hasText(appointment.getStartTime()) && StringUtils.hasText(appointment.getEndTime())) {
			sql += " and a.create_time >= :startTime and a.create_time <= :endTime ";
			params.put("startTime", appointment.getStartTime());
			params.put("endTime", appointment.getEndTime());
		} else if (StringUtils.hasText(appointment.getStartTime())) {
			sql += " and a.create_time >= :startTime  ";
			params.put("startTime", appointment.getStartTime());
		} else if (StringUtils.hasText(appointment.getEndTime())) {
			sql += " and a.create_time <= :endTime ";
			params.put("endTime", appointment.getEndTime());
		}
		return this.listByNativeSqlPage2(sql, params, page, rows);
	}

}
