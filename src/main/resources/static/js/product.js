$(function() {
	window.top.Modules.Product = Product = {
			
			Modules : window.top.Modules,
			
			//初始化
			init : function() {
				$('#product_table').datagrid({
				    url:'/product/search',
				    toolbar : '#product_table_tb',
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
				    	params['term'] = $('#product_table_term').val();
				    	return true;
				    },
				    onLoadSuccess : function(data) {return true},
				    columns:[[
				        {field:'ck',checkbox:true},
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'name', title:'产品名称', width:100},
				        {field: 'remark', title:'备注', width:100},
				        {field: 'operate', title:'操作', width:100, formatter: this.operate}
				    ]]
				});
				$('#product_table').datagrid('getPager').pagination({  
	                beforePageText: '第',//页数文本框前显示的汉字   
	                afterPageText: '页    共 {pages} 页',  
	                displayMsg: '共 {total} 条记录',
	                onSelectPage: function(page, rows) {
	                	$('#product_table').datagrid('gotoPage', page);
	                }
	            });
							
			},
			
			operate : function(value, row, index) {
				var auth = Product.Modules.Index.Auth;
				
				var html = '<div>';
				//if (auth.isPermission(1)) {
					html += '<a title="删除" class="icon-cancel" style="text-decoration:none; cursor:pointer;" onclick="Product.remove(' + row.id + ',\'' + row.name + '\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;';
				//}
				//if (auth.isPermission(1)) {
					html += '<a title="修改" class="icon-edit" style="text-decoration:none; cursor:pointer;" onclick="Product.edit(' + row.id + ')">&nbsp;&nbsp;&nbsp;&nbsp;</a>';
				//}
				html += '</div>';
				return html;
			},
			
			search : function() {
				$("#product_table").datagrid('clearSelections').datagrid('clearChecked');
				$('#product_table').datagrid('reload');
			},
			
			add : function() {
				/*var auth = Product.Modules.Index.Auth;
				if (!auth.isPermission(6)) {
					$.messager.alert('您没有添加的权限');
					return;
				}
				$.messager.alert('您有添加的权限');*/
				var dialog = $('<div>');
				$('#product_edit').empty();
				$('#product_edit').append(dialog);
				$(dialog).dialog({
					title : '新增产品',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 250,
				    collapsible : true,
				    modal : true,
					href: '../html/product_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#product_edit_form').form('submit', {
							    url:'/product/save',
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
							    		Product.search();
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
				$('#product_edit').empty();
				$('#product_edit').append(dialog);
				$(dialog).dialog({
					title : '新增产品',
					resizable : true,
					maximizable : true,
					width: 400,
				    height: 250,
				    collapsible : true,
				    modal : true,
					href: '../html/product_edit.html',
					buttons : [{
						text : '保存',
						handler : function() {
							$('#product_edit_form').form('submit', {
							    url:'/product/update',
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
							    		Product.search();
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
						$('#product_edit_form').form('load', '/product/detail?id=' + id);
					},
					onClose : function() {
						$(dialog).dialog('destroy');
					}
				});
			},
			
			remove : function(id, name) {
				$.messager.confirm({
					title : '警告',
					msg : '确定删除产品(' + name + ')？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							load('正在执行...');
							$.ajax({
								url : '/product/delete',
								data : {
									ids : [id]
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		Product.search();
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
				var rows = $('#product_table').datagrid('getChecked');
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
					msg : '确定删除多个产品？',
					ok : '确定',
					cancel : '取消',
					fn : function(r) {
						if (r) {
							load('正在执行...');
							$.ajax({
								url : '/product/delete',
								data : {
									ids : ids
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									disLoad();
									if (data.code == 0) {
							    		Product.search();
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
	Product.init();
});

