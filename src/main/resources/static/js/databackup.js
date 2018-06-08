$(function() {
	window.top.Modules.DataBackup = DataBackup = {
			
			Modules : window.top.Modules,
			
			//初始化
			init : function() {
				$('#databackup_table').datagrid({
				    url:'/databackup/search',
				    toolbar : '#databackup_table_tb',
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
				    	params['term'] = $('#databackup_table_term').val();
				    	return true;
				    },
				    onLoadSuccess : function(data) {return true},
				    columns:[[
				        //{field:'ck',checkbox:true},
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'tableName', title:'表名', width:100},
				        {field: 'remark', title:'备注', width:100},
				        {field: 'backupPath', title:'路径', width:300},
				        {field: 'startTime', title:'开始时间', width:100},
				        {field: 'endTime', title:'结束时间', width:100}
				        //{field: 'operate', title:'操作', width:100, formatter: this.operate}
				    ]]
				});
				$('#databackup_table').datagrid('getPager').pagination({  
	                beforePageText: '第',//页数文本框前显示的汉字   
	                afterPageText: '页    共 {pages} 页',  
	                displayMsg: '共 {total} 条记录',
	                onSelectPage: function(page, rows) {
	                	$('#databackup_table').datagrid('gotoPage', page);
	                }
	            });
							
			},
			
			operate : function(value, row, index) {
				var auth = DataBackup.Modules.Index.Auth;
				
				var html = '<div>';
				//if (auth.isPermission(1)) {
					html += '<a title="删除" class="icon-cancel" style="text-decoration:none; cursor:pointer;" onclick="DataBackup.remove(' + row.id + ',\'' + row.name + '\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				//if (auth.isPermission(1)) {
					html += '<a title="修改" class="icon-edit" style="text-decoration:none; cursor:pointer;" onclick="DataBackup.edit(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>';
				//}
				html += '</div>';
				return html;
			},
			
			search : function() {
				$("#databackup_table").datagrid('clearSelections').datagrid('clearChecked');
				$('#databackup_table').datagrid('reload');
			},
			
			add : function() {
				/*var auth = DataBackup.Modules.Index.Auth;
				if (!auth.isPermission(6)) {
					$.messager.alert('您没有添加的权限');
					return;
				}
				$.messager.alert('您有添加的权限');*/
				var dialog = $('<div>');
				$('#databackup_edit').empty();
				$('#databackup_edit').append(dialog);
				$(dialog).dialog({
					title : '新增备份',
					resizable : true,
					maximizable : true,
					width: 500,
				    height: 500,
				    collapsible : true,
				    modal : true,
					href: '../html/databackup_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							/*$('#databackup_edit_form').form('submit', {
							    url:'/databackup/save',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		DataBackup.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
							    }
							});*/
							var row = $('#databackup_edit_table').datagrid('getChecked');
							if (!row || row.length == 0) {
								$.messager.alert('提示', '请选择要备份的表');
								return;
							}
							load(row.tableName + '备份执行中...');
							$.ajax({
								url : '/databackup/save',
								type : 'post',
								data : {
									tableName : row[0].tableName,
									remark : row[0].remark
								},
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		DataBackup.search();
							    	} else {
							    		$.messager.alert('提示', data.msg);
							    	}
								},
								error : function() {
									disLoad();
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
				$('#databackup_edit').empty();
				$('#databackup_edit').append(dialog);
				$(dialog).dialog({
					title : '新增备份',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 250,
				    collapsible : true,
				    modal : true,
					href: '../html/databackup_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#databackup_edit_form').form('submit', {
							    url:'/databackup/update',
							    onSubmit : function(){
							        return $(this).form('enableValidation').form('validate');
							    },
							    success : function(data) {
							    	var data = eval('(' + data + ')');
							    	if (data.code == 0) {
							    		$(dialog).dialog('destroy');
							    		DataBackup.search();
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
						$('#databackup_edit_form').form('load', '/databackup/detail?id=' + id);
					},
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			},
			
			remove : function(id, name) {
				$.messager.confirm({
					title : '警告',
					msg : '确定删除备份(' + name + ')？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/databackup/delete',
								data : {
									ids : [id]
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		DataBackup.search();
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
				var rows = $('#databackup_table').datagrid('getChecked');
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
					msg : '确定删除多个备份？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							$.ajax({
								url : '/databackup/delete',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.code == 0) {
							    		DataBackup.search();
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
	DataBackup.init();
});

