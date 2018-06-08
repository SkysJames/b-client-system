$(function() {
	//注册所有模块
	window.top.Modules = {}

	window.top.Modules.Index = Index = {
			
			init : function() {
				Index.loadMenu();
				Index.tabMenu();
				Index.Auth.init();
				Index.onTabSelect();
			},
			
			tabMenu : function() {
				$('#tabs').bind('contextmenu',function(e){
					e.preventDefault();
					$('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				});
			},

			loadMenu : function() {
				var menuTree = $('#menuTree');
				$.ajax({
					url : './datas/menu.json',
					type : 'get',
					dataType : 'json',
					success : function(data) {
						$(menuTree).tree({
							data : data,
							//lines : true,
							onLoadSuccess : function() {
								$(menuTree).tree('collapseAll');
							},
							onClick : function(node) {
								if (node.state == 'closed' && (!$(menuTree).tree('isLeaf', node.targer))) {
									$(menuTree).tree('expand', node.target);
									if (node.attributes && node.attributes.url) {
										Index.openTab(node);
									}
								} else {
									if ($(menuTree).tree('isLeaf', node.targer)) {
										if (node.attributes && node.attributes.url) {
											Index.openTab(node);
										}
									} else {
										$(menuTree).tree('expand', node.target);
									}
								}
							}
						});
						$(".tree-icon,.tree-file").removeClass("tree-icon tree-file");
						$(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed");
					}
				});
			},


		    openTab : function(node){
			    if ($("#tabs").tabs("exists",node.text)) {
			        $("#tabs").tabs("select", node.text);
			    }else{
			        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + node.attributes.url + "'></iframe>"
			        $("#tabs").tabs("add",{
			            title : node.text,
			            iconCls : node.iconCls,
			            closable : true,
			            content : content
			        });
			    }
			},

			closeTab : function() {
				var tab = $('#tabs').tabs('getSelected');//获取当前选中tabs  
				var index = $('#tabs').tabs('getTabIndex', tab);//获取当前选中tabs的index  
				$('#tabs').tabs('close', index);//关闭对应index的tabs  
			},

			flushTab : function() {
				var tab = $('#tabs').tabs('getSelected');//获取当前选中tabs  
				var url = $(tab.panel('options')).attr('href');
				$('#tabs').tabs('update', {
					tab: tab,
					options: {
				        href: url
					}
				});
				tab.panel('refresh');
			},
			
			onTabSelect : function() {
				$('#tabs').tabs({
					onSelect : function(d) {
						$.ajax({
							url : './user/getCurrentUser',
							dataType : 'json',
							type : 'get',
							async : false,
							success : function(user) {
								$('#_username').html(user.name);
							}
						});
					}
				});
			},
			
			//权限模块
			Auth : {
				permissions : [],//所有权限的集合
				
				//初始化权限
				init : function() {
					$.ajax({
						url : './user/getCurrentUser',
						dataType : 'json',
						type : 'get',
						success : function(user) {
							$('#_username').html(user.name);
							Index.Auth.permissions = user.permissions;
							console.log(Index.Auth.permissions);
						}
					});
					this.permissions.push(1);
				},
				
				//判断是否拥有权限
				isPermission : function(code) {
					for (var i = 0; i < this.permissions.length; ++i) {
						if (this.permissions[i] == code) {
							return true;
						}
					}
					return false;
				}
			}
			
	};
	Index.init();
});
