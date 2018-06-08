$(function() {
	window.top.Modules.Customer = Customer = {
			
			Modules : window.top.Modules,
			
			//初始化
			init : function() {
				$('#customer_table').datagrid({
				    url : '/customer/search',
				    toolbar : '#customer_table_tb',
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
				    	params['name'] = $('#customer_name_term').val();
				    	params['mobilePhone'] = $('#customer_mobilephone_term').val();
				    	params['startTime'] = $('#customer_starttime_term').val();
				    	params['endTime'] = $('#customer_endtime_term').val();
				    	return true;
				    },
				    onLoadSuccess : function(data) {return true},
				    columns:[[
				        {field:'ck',checkbox:true},
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'name', title:'客户', width:100},
				        {field: 'mobilePhone', title:'手机号码', width:100},
				        {field: 'telePhone', title:'座机号码', width:100},
				        {field: 'qq', title:'QQ', width:100},
				        {field: 'wechat', title:'微信', width:100},
				        {field: 'tradeName', title:'客户行业', width:140},
				        {field: 'levelName', title:'客户级别', width:140},
				        {field: 'company', title:'公司名称', width:100},
				        {field: 'createTimeStr', title:'添加时间', width:180},
				        {field: 'operate', title:'操作', width:100, formatter: this.operate}
				    ]]
				});
				$('#customer_table').datagrid('getPager').pagination({  
	                beforePageText: '第',//页数文本框前显示的汉字   
	                afterPageText: '页    共 {pages} 页',  
	                displayMsg: '共 {total} 条记录',
	                onSelectPage: function(page, rows) {
	                	$('#customer_table').datagrid('gotoPage', page);
	                }
	            });
							
			},
			
			operate : function(value, row, index) {
				var auth = Customer.Modules.Index.Auth;
				
				var html = '<div>';
				//if (auth.isPermission(1)) {
					html += '<a title="删除" class="icon-cancel" style="text-decoration:none; cursor:pointer;" onclick="Customer.remove(' + row.id + ',\'' + row.name + '\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				//if (auth.isPermission(1)) {
					html += '<a title="修改" class="icon-edit" style="text-decoration:none; cursor:pointer;" onclick="Customer.edit(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
					
				html += '</div>';
				return html;
			},
			
			search : function() {
				$("#customer_table").datagrid('clearSelections').datagrid('clearChecked');
				$('#customer_table').datagrid('reload');
			},
			
			add : function() {
				/*var auth = Customer.Modules.Index.Auth;
				if (!auth.isPermission(6)) {
					$.messager.alert('您没有添加的权限');
					return;
				}
				$.messager.alert('您有添加的权限');*/
				var dialog = $('<div>');
				$('#customer_edit').empty();
				$('#customer_edit').append(dialog);
				$(dialog).dialog({
					title : '新增客户',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/customer_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#customer_edit_form').form('submit', {
							    url : '/customer/save',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		Customer.search();
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
				$('#customer_edit').empty();
				$('#customer_edit').append(dialog);
				$(dialog).dialog({
					title : '新增客户',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/customer_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#customer_edit_form').form('submit', {
							    url : '/customer/update',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		Customer.search();
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
						$('#customer_edit_form').form('load', '/customer/detail?id=' + id);
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
					msg : '确定删除客户(' + name + ')？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/customer/delete',
								data : {
									ids : [id]
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		Customer.search();
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
				var rows = $('#customer_table').datagrid('getChecked');
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
					msg : '确定删除多个客户？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/customer/delete',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		Customer.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}
				});
			}
	};
	Customer.init();
});

