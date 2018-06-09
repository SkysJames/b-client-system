$(function() {
	window.top.Modules.Appointment = Appointment = {
			
			Modules : window.top.Modules,
			
			//初始化
			init : function() {
				$('#appointment_table').datagrid({
				    url:'/appointment/search',
				    toolbar : '#appointment_table_tb',
				    method : 'get',
				    idField : 'id',
				    loadMsg : '正在加载......',
				    emptyMsg : '没有记录',
				    pagination : true,
				    rownumbers : true,
	                pageNumber: 1,
	                pageSize: 10,
	                pageList: [10, 20, 30, 40, 50],  
				    fit : true,
				    queryParams : {},
				    onBeforeLoad : function(params) {
				    	params['customerName'] = $('#appointment_name_term').val();
				    	params['customerMobilePhone'] = $('#appointment_mobilephone_term').val();
				    	params['startTime'] = $('#appointment_starttime_term').val();
				    	params['endTime'] = $('#appointment_endtime_term').val();
				    	return true;
				    },
				    onLoadSuccess : function(data) {return true},
				    columns:[[
				        {field:'ck',checkbox:true},
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'customerName', title:'预约人', width:100},
				        {field: 'productName', title:'产品', width:100},
				        {field: 'appointmentTime', title:'预约时间', width:180},
				        {field: 'status', title:'预约状态', width:100},
				        {field: 'remark', title:'备注', width:100},
				        {field: 'userEmpNo', title:'操作员工', width:100},
				        {field: 'createTimeStr', title:'添加时间', width:180},
				        {field: 'operate', title:'操作', width:100, formatter: this.operate}
				    ]]
				});
				$('#appointment_table').datagrid('getPager').pagination({  
	                beforePageText: '第',//页数文本框前显示的汉字   
	                afterPageText: '页    共 {pages} 页',  
	                displayMsg: '共 {total} 条记录',
	                onSelectPage: function(page, rows) {
	                	$('#appointment_table').datagrid('gotoPage', page);
	                }
	            });
							
			},
			
			operate : function(value, row, index) {
				var auth = Appointment.Modules.Index.Auth;
				
				var html = '<div>';
				//if (auth.isPermission(1)) {
					html += '<a title="删除" class="icon-cancel" style="text-decoration:none; cursor:pointer;" onclick="Appointment.remove(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				//if (auth.isPermission(1)) {
					//html += '<a title="修改" class="icon-edit" style="text-decoration:none; cursor:pointer;" onclick="Appointment.edit(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				html += '<a title="回访跟进" class="icon-more" style="text-decoration:none; cursor:pointer;" onclick="Appointment.showTips(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				if (row.status != '已去电') {
					html += '<a title="确认去电" class="icon-ok" style="text-decoration:none; cursor:pointer;" onclick="Appointment.confirm(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				}
				html += '</div>';
				return html;
			},
			
			search : function() {
				$("#appointment_table").datagrid('clearSelections').datagrid('clearChecked');
				$('#appointment_table').datagrid('reload');
			},
			
			add : function() {
				/*var auth = Appointment.Modules.Index.Auth;
				if (!auth.isPermission(6)) {
					$.messager.alert('您没有添加的权限');
					return;
				}
				$.messager.alert('您有添加的权限');*/
				var dialog = $('<div>');
				$('#appointment_edit').empty();
				$('#appointment_edit').append(dialog);
				$(dialog).dialog({
					title : '新增预约信息',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 400,
				    collapsible : true,
				    modal : true,
					href: '../html/appointment_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#appointment_edit_form').form('submit', {
							    url:'/appointment/save',
							    onSubmit : function(){
							    	var check = $(this).form('enableValidation').form('validate');
							    	if (check) {
							    		load('正在执行...');
							    	}
							        return check;
							    },
							    success : function(data) {
							    	disLoad();
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		Appointment.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
							    }
							});
						}
					}, {
						text : '取消',
						handler : function() {
							$(dialog).dialog('destroy');
						}
					}],
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			},
			
			edit : function(id) {
				var dialog = $('<div>');
				$('#appointment_edit').empty();
				$('#appointment_edit').append(dialog);
				$(dialog).dialog({
					title : '新增预约信息',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 400,
				    collapsible : true,
				    modal : true,
					href: '../html/appointment_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#appointment_edit_form').form('submit', {
							    url:'/appointment/update',
							    onSubmit : function(){
							    	var check = $(this).form('enableValidation').form('validate');
							    	if (check) {
							    		load('正在执行...');
							    	}
							        return check;
							    },
							    success : function(data) {
							    	disLoad();
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		Appointment.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
							    }
							});
						}
					}, {
						text : '取消',
						handler : function() {
							$(dialog).dialog('destroy');
						}
					}],
					onLoad : function() {
						//$('#appointment_edit_form').form('load', '/appointment/detail?id=' + id);
						$.ajax({
							url : '/appointment/detail?id=' + id,
							type : 'get',
							dataType : 'json',
							success : function(data) {
								$.ajax({
									url : '/customer/detail',
									type : 'get',
									data : {
										id : data.customerId
									},
									dataType : 'json',
									success : function(cust) {
										$('#appointment_edit_form').form('load', data);
										$('#appointment_edit_form').find('#customerId').textbox('setValue', cust.id);
										$('#appointment_edit_form').find('#customerId').textbox('setText', cust.name);
									}
								});
							}
						});
					},
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			},
			
			remove : function(id) {
				$.messager.confirm({
					title : '警告',
					msg : '确定删除预约信息？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							load('正在执行...');
							$.ajax({
								url : '/appointment/delete',
								data : {
									ids : [id]
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		Appointment.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}
				});
			},
			
			removeAll : function() {
				var rows = $('#appointment_table').datagrid('getChecked');
				if (!rows || rows.length == 0) {
					$.messager.alert('提示', '请选择要删除的记录');
					return;
				};
				var ids = [];
				for (var i = 0; i < rows.length; ++i) {
					ids.push(rows[i].id);
				}
				$.messager.confirm({
					title : '警告',
					msg : '确定删除多个预约信息？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							load('正在执行...');
							$.ajax({
								url : '/appointment/delete',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		Appointment.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}
				});
			},
			
			confirm : function(id) {
				var rw = $('#appointment_table').datagrid('selectRecord', id);
				$.messager.confirm({
					title : '警告',
					msg : '确定预约已去电？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							load('正在执行...');
							$.ajax({
								url : '/appointment/update',
								data : {
									id : id,
									status : '已去电'
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		Appointment.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}
				});
			},
			
			showTips : function(id) {
				var dialog = $('<div>');
				$('#appointment_edit').empty();
				$('#appointment_edit').append(dialog);
				$(dialog).dialog({
					title : '跟进回访信息',
					resizable : true,
					maximizable : true,
					width: 600,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/appointment_tips.html',
					buttons : [{
						text : '保存',
						handler : function() {
							var tip = $('#appointment_tip_form').find('#tip').val();
							if (!tip) {
								$.messager.alert('提示', '请输入回访信息');
								return;
							}
							load('正在执行...');
							$.ajax({
								url : '/appointment/addTip',
								type : 'post',
								data : {
									appointmentId : id,
									tip : tip
								},
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
										$('#appointment_tip_form').find('#tip').textbox('setValue', '');
										$('#appointment_tips_table').propertygrid('reload');
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}, {
						text : '取消',
						handler : function() {
							$(dialog).dialog('destroy');
						}
					}],
					onLoad : function() {
						$('#appointment_tips_table').propertygrid({
						    url: '/appointment/listTipsGrid?id=' + id,
						    showGroup: true,
						    groupField: 'group',
						    scrollbarSize: 0,
						    fit : true,
						    idField : 'id',
						    singleSelect : false,
						    columns:[[
						        {field:'id', title:'ID', width:100, hidden : true},
						        {field: 'tip', title:'回访信息', width:100}
						    ]]
						});
					},
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			}
			
	};
	Appointment.init();
});

