package com.lx.cus.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lx.cus.entity.AppointmentTip;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.util.BeanUtil;

@Repository
public class AppointmentTipRepository extends BaseEntityRepository<AppointmentTip, Integer> {

	public void deleteByAppointmentId(Integer appoinmentId) {
		Map<String, Object> params = new HashMap<>();
		params.put("appoinmentId", appoinmentId);
		this.executeByNativeSql("delete from appointment_tip where appointment_id = :appoinmentId ", params);
	}

	public List<AppointmentTip> listByAppointmentId(Integer appoinmentId) {
		String sql = "select DATE_FORMAT(t.create_time, '%Y-%m-%d %T') createTimeStr, u.emp_no userEmpNo, t.tip " + 
				     "from appointment_tip t, sys_user u where t.appointment_id = :appoinmentId and t.create_user_id = u.id " +
				     "order by t.create_time asc";
		Map<String, Object> params = new HashMap<>();
		params.put("appoinmentId", appoinmentId);
		List<Map<String, Object>> rows = this.listByNativeSql(sql, params);
		return rows != null && !rows.isEmpty() ? BeanUtil.mapToBean(rows, AppointmentTip.class) : new ArrayList<>(0);
	}

}
