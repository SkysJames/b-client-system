$(function() {
	window.top.Modules.Order = Order = {
			
			Modules : window.top.Modules,
			
			//初始化
			init : function() {
				$('#order_table').datagrid({
				    url : '/order/search',
				    toolbar : '#order_table_tb',
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
				    	params['account'] = $('#order_account_term').val();
				    	params['customerMobilePhone'] = $('#order_mobilephone_term').val();
				    	params['type'] = $('#order_type_term').val();
				    	params['status'] = $('#order_status_term').val();
				    	params['startTime'] = $('#order_starttime_term').val();
				    	params['endTime'] = $('#order_endtime_term').val();
				    	return true;
				    },
				    onLoadSuccess : function(data) {return true},
				    columns:[[
				        {field:'ck',checkbox:true},
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'type', title:'订单类型', width:100},
				        {field: 'code', title:'订单号', width:180},
				        {field: 'createTimeStr', title:'录单日期', width:180},
				        {field: 'userName', title:'录单人', width:100},
				        {field: 'customerName', title:'客户', width:100},
				        {field: 'customerMobilePhone', title:'手机号码', width:100},
				        {field: 'account', title:'账户', width:100},
				        {field: 'productName', title:'产品', width:100},
				        {field: 'customerPay', title:'客户应付金额', width:140},
				        {field: 'costPrice', title:'成本价', width:140},
				        {field: 'grossProfit', title:'RMB毛利', width:100},
				        {field: 'status', title:'付款状态', width:180}
				       // {field: 'operate', title:'操作', width:100, formatter: this.operate}
				    ]],
				    rowStyler : function(index, row) {
				    	if (row.status == '已完结' || row.status == '客户已退款') {
				    		return "color:red";
				    	}
				    }
				});
				$('#order_table').datagrid('getPager').pagination({  
	                beforePageText: '第',//页数文本框前显示的汉字   
	                afterPageText: '页    共 {pages} 页',  
	                displayMsg: '共 {total} 条记录',
	                onSelectPage: function(page, rows) {
	                	$('#order_table').datagrid('gotoPage', page);
	                }
	            });
							
			},
			
			operate : function(value, row, index) {
				var auth = Order.Modules.Index.Auth;
				
				var html = '<div>';
				//if (auth.isPermission(1)) {
					html += '<a title="删除" class="icon-cancel" style="text-decoration:none; cursor:pointer;" onclick="Order.remove(' + row.id + ',\'' + row.name + '\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				//if (auth.isPermission(1)) {
					html += '<a title="修改" class="icon-edit" style="text-decoration:none; cursor:pointer;" onclick="Order.edit(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
					
				html += '</div>';
				return html;
			},
			
			search : function() {
				$("#order_table").datagrid('clearSelections').datagrid('clearChecked');
				$('#order_table').datagrid('reload');
			},
			
			add : function() {
				/*var auth = Order.Modules.Index.Auth;
				if (!auth.isPermission(6)) {
					$.messager.alert('您没有添加的权限');
					return;
				}
				$.messager.alert('您有添加的权限');*/
				var dialog = $('<div>');
				$('#order_edit').empty();
				$('#order_edit').append(dialog);
				$(dialog).dialog({
					title : '新增订单',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/order_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#order_edit_form').form('submit', {
							    url : '/order/save',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		Order.search();
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
				$('#order_edit').empty();
				$('#order_edit').append(dialog);
				$(dialog).dialog({
					title : '新增订单',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/order_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#order_edit_form').form('submit', {
							    url : '/order/update',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		Order.search();
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
						$('#order_edit_form').form('load', '/order/detail?id=' + id);
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
					msg : '确定删除订单(' + name + ')？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/order/delete',
								data : {
									ids : [id]
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		Order.search();
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
				var rows = $('#order_table').datagrid('getChecked');
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
					msg : '确定删除多个订单？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/order/delete',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		Order.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}
				});
			},
			
			invalid : function() {
				var rows = $('#order_table').datagrid('getChecked');
				if (!rows || rows.length == 0) {
					$.messager.alert('提示', '请选择要作废的订单');
					return;
				};
				
				var ids = [];
				for (var i = 0; i < rows.length; ++i) {
					ids.push(rows[i].id);
				}
				
				$.messager.confirm({
					title : '警告',
					msg : '确定作废多个订单？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/order/invalid',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		Order.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								}
							});
						}
					}
				});
			},
			
			confirm : function(status) {
				var rows = $('#order_table').datagrid('getChecked');
				if (!rows || rows.length == 0) {
					$.messager.alert('提示', '请选择要操作的订单');
					return;
				};
				
				var type = (status == '已付款' || status == '已完结') ? '广告订单' : '退款订单';
				var orderStatus = '';
				if (status == '已付款') {
					orderStatus = '未付款';
				} else if (status == '已完结') {
					orderStatus = '已付款';
				} else if (status == '代理商已退款') {
					orderStatus = '未付款';
				} else if (status == '客户已退款') {
					orderStatus = '代理商已退款';
				}
				
				var ids = [];
				for (var i = 0; i < rows.length; ++i) {
					if (rows[i].type != type || rows[i].status != orderStatus) {
						$.messager.alert('提示', '请选择' + orderStatus + '的' + type + '进行操作');
						return ;
					}
					ids.push(rows[i].id);
				}
				
				$.messager.confirm({
					title : '警告',
					msg : '确定修改订单？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/order/updateStatus',
								data : {
									ids : ids,
									status : status
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		Order.search();
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
	Order.init();
});

