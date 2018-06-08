$(function() {
	window.top.Modules.User = User = {
			
			Modules : window.top.Modules,
			
			//初始化
			init : function() {
				$('#user_table').datagrid({
				    url:'/user/search',
				    toolbar : '#user_table_tb',
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
				    	params['name'] = $('#user_name_term').val();
				    	params['empNo'] = $('#user_empno_term').val();
				    	params['departmentIds'] = $('#user_department_term').val();
				    	params['status'] = $('#user_status_term').val();
				    	return true;
				    },
				    onLoadSuccess : function(data) {return true},
				    columns:[[
				        {field:'ck',checkbox:true},
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'empNo', title:'工号', width:100},
				        {field: 'name', title:'姓名', width:100},
				        {field: 'departmentName', title:'部门', width:100},
				        {field: 'mobilePhone', title:'手机号码', width:100},
				        {field: 'telePhone', title:'座机号码', width:100},
				        {field: 'email', title:'邮件', width:100},
				        {field: 'lastLoginTimeStr', title:'最后登录时间', width:140},
				        {field: 'status', title:'状态', width:100},
				        {field: 'operate', title:'操作', width:100, formatter: this.operate}
				    ]]
				});
				$('#user_table').datagrid('getPager').pagination({  
	                beforePageText: '第',//页数文本框前显示的汉字   
	                afterPageText: '页    共 {pages} 页',  
	                displayMsg: '共 {total} 条记录',
	                onSelectPage: function(page, rows) {
	                	$('#user_table').datagrid('gotoPage', page);
	                }
	            });
							
			},
			
			operate : function(value, row, index) {
				var auth = User.Modules.Index.Auth;
				
				var html = '<div>';
				//if (auth.isPermission(1)) {
					//html += '<a title="删除" class="icon-cancel" style="text-decoration:none; cursor:pointer;" onclick="User.remove(' + row.id + ',\'' + row.name + '\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				//if (auth.isPermission(1)) {
					html += '<a title="修改" class="icon-edit" style="text-decoration:none; cursor:pointer;" onclick="User.edit(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
					
					html += '<a title="角色选择" class="icon-tip" style="text-decoration:none; cursor:pointer;" onclick="User.authorize(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>';
				html += '</div>';
				return html;
			},
			
			search : function() {
				$("#user_table").datagrid('clearSelections').datagrid('clearChecked');
				$('#user_table').datagrid('reload');
			},
			
			add : function() {
				/*var auth = User.Modules.Index.Auth;
				if (!auth.isPermission(6)) {
					$.messager.alert('您没有添加的权限');
					return;
				}
				$.messager.alert('您有添加的权限');*/
				var dialog = $('<div>');
				$('#user_edit').empty();
				$('#user_edit').append(dialog);
				$(dialog).dialog({
					title : '新增员工',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/user_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#user_edit_form').form('submit', {
							    url:'/user/save',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		User.search();
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
				$('#user_edit').empty();
				$('#user_edit').append(dialog);
				$(dialog).dialog({
					title : '新增员工',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/user_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#user_edit_form').form('submit', {
							    url:'/user/update',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		User.search();
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
						$('#user_edit_form').form('load', '/user/detail?id=' + id);
						$('#empNo').textbox('textbox').attr('readonly',true); 
						$('#empNo').textbox('textbox').attr('disabled', true);
					},
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			},
			
			remove : function(id, name) {
				$.messager.confirm({
					title : '警告',
					msg : '确定删除员工(' + name + ')？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/user/delete',
								data : {
									ids : [id]
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		User.search();
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
				var rows = $('#user_table').datagrid('getChecked');
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
					msg : '确定删除多个员工？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/user/delete',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		User.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}
				});
			},
			
			authorize : function(id) {
				var dialog = $('<div>');
				$('#user_edit').empty();
				$('#user_edit').append(dialog);
				$(dialog).dialog({
					title : '选择角色',
					resizable : true,
					maximizable : true,
					width: 700,
				    height: 400,
				    collapsible : true,
				    modal : true,
					href: '../html/role_check.html',
					buttons : [{
						text : '保存',
						handler : function() {
							var rows = $('#role_check_table').datagrid('getChecked');
							var ids = [];
							for (var i = 0; i < rows.length; ++i) {
								ids.push(rows[i].id);
							}
							$.ajax({
								url : '/user/authorize',
								data : {
									roleIds : ids,
									id : id
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
										$(dialog).dialog('destroy');
										$.messager.alert('提示', '角色绑定成功');
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
						//$('#user_edit_form').form('load', '/user/detail?id=' + id);
						$('#role_check_table').datagrid({
						    url: '/role/listAllGrid',
						    scrollbarSize: 0,
						    fit : true,
						    singleSelect : false,
						    method : 'get',
						    idField : 'id',
						    columns:[[
				                {field:'ck',checkbox:true},
						        {field:'id', title:'ID', width:100, hidden : true},
						        {field: 'name', title:'名称', width:100},
						        {field: 'remark', title:'备注', width:100}
						    ]],
						    onLoadSuccess : function(data) {
						    	$.ajax({
									url : '/user/listRoleIds',
									type : 'get',
									data : {
										id : id
									},
									dataType : 'json',
									success : function(data) {
										if (data && data.length) {
											for (var i = 0; i < data.length; ++i) {
												var rw = $('#role_check_table').datagrid('selectRecord', data[i]);
												var index = $('#role_check_table').datagrid('getRowIndex', rw);
												$('#role_check_table').datagrid('checkRow', index);
											}
										}
									}
								});
						    	return true;
						    }
						});
					},
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			}
	};
	User.init();
});

