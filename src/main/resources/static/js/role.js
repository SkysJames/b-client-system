$(function() {
	window.top.Modules.Role = Role = {
			
			Modules : window.top.Modules,
			
			//初始化
			init : function() {
				$('#role_table').datagrid({
				    url:'/role/search',
				    toolbar : '#role_table_tb',
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
				    	params['term'] = $('#role_table_term').val();
				    	return true;
				    },
				    onLoadSuccess : function(data) {return true},
				    columns:[[
				        {field:'ck',checkbox:true},
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'name', title:'角色名称', width:100},
				        {field: 'remark', title:'备注', width:100},
				        {field: 'operate', title:'操作', width:100, formatter: this.operate}
				    ]]
				});
				$('#role_table').datagrid('getPager').pagination({  
	                beforePageText: '第',//页数文本框前显示的汉字   
	                afterPageText: '页    共 {pages} 页',  
	                displayMsg: '共 {total} 条记录',
	                onSelectPage: function(page, rows) {
	                	$('#role_table').datagrid('gotoPage', page);
	                }
	            });
							
			},
			
			operate : function(value, row, index) {
				var auth = Role.Modules.Index.Auth;
				
				var html = '<div>';
				//if (auth.isPermission(1)) {
					html += '<a title="删除" class="icon-cancel" style="text-decoration:none; cursor:pointer;" onclick="Role.remove(' + row.id + ',\'' + row.name + '\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				//if (auth.isPermission(1)) {
					html += '<a title="修改" class="icon-edit" style="text-decoration:none; cursor:pointer;" onclick="Role.edit(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
					
					html += '<a title="授权" class="icon-tip" style="text-decoration:none; cursor:pointer;" onclick="Role.authorize(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>';
				html += '</div>';
				return html;
			},
			
			search : function() {
				$("#role_table").datagrid('clearSelections').datagrid('clearChecked');
				$('#role_table').datagrid('reload');
			},
			
			add : function() {
				/*var auth = Role.Modules.Index.Auth;
				if (!auth.isPermission(6)) {
					$.messager.alert('您没有添加的权限');
					return;
				}
				$.messager.alert('您有添加的权限');*/
				var dialog = $('<div>');
				$('#role_edit').empty();
				$('#role_edit').append(dialog);
				$(dialog).dialog({
					title : '新增角色',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 250,
				    collapsible : true,
				    modal : true,
					href: '../html/role_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#role_edit_form').form('submit', {
							    url:'/role/save',
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
							    		Role.search();
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
				$('#role_edit').empty();
				$('#role_edit').append(dialog);
				$(dialog).dialog({
					title : '新增角色',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 250,
				    collapsible : true,
				    modal : true,
					href: '../html/role_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#role_edit_form').form('submit', {
							    url:'/role/update',
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
							    		Role.search();
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
						$('#role_edit_form').form('load', '/role/detail?id=' + id);
					},
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			},
			
			remove : function(id, name) {
				$.messager.confirm({
					title : '警告',
					msg : '确定删除角色(' + name + ')？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							load('正在执行...');
							$.ajax({
								url : '/role/delete',
								data : {
									ids : [id]
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		Role.search();
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
				var rows = $('#role_table').datagrid('getChecked');
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
					msg : '确定删除多个角色？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							load('正在执行...');
							$.ajax({
								url : '/role/delete',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		Role.search();
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
				$('#role_edit').empty();
				$('#role_edit').append(dialog);
				$(dialog).dialog({
					title : '选择授权',
					resizable : true,
					maximizable : true,
					width: 700,
				    height: 400,
				    collapsible : true,
				    modal : true,
					href: '../html/resource_check.html',
					buttons : [{
						text : '保存',
						handler : function() {
							var rows = $('#res_table').datagrid('getChecked');
							var ids = [];
							for (var i = 0; i < rows.length; ++i) {
								ids.push(rows[i].id);
							}
							load('正在执行...');
							$.ajax({
								url : '/role/authorize',
								data : {
									resIds : ids,
									id : id
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
										$(dialog).dialog('destroy');
										$.messager.alert('提示', '授权成功');
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
						//$('#role_edit_form').form('load', '/role/detail?id=' + id);
						$('#res_table').propertygrid({
						    url: '/auth/listGrid',
						    showGroup: true,
						    groupField: 'typeName',
						    scrollbarSize: 0,
						    fit : true,
						    idField : 'id',
						    singleSelect : false,
						    selectOnCheck : true,
						    columns:[[
			                    {field:'ck',checkbox:true},
						        {field:'id', title:'ID', width:100, hidden : true},
						        {field: 'name', title:'名称', width:100},
						        {field: 'code', title:'编号', width:100}
						    ]],
						    onLoadSuccess : function(data) {
						    	$.ajax({
									url : '/role/listResIds',
									type : 'get',
									data : {
										id : id
									},
									dataType : 'json',
									success : function(data) {
										if (data && data.length) {
											for (var i = 0; i < data.length; ++i) {
												var rw = $('#res_table').datagrid('selectRecord', data[i]);
												var index = $('#res_table').datagrid('getRowIndex', rw);
												$('#res_table').datagrid('checkRow', index);
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
	Role.init();
});

