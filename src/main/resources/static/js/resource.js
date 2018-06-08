$(function(){
	window.top.Modules.Resource = Resource = {
			init : function() {
				$('#res_table').propertygrid({
				    url: '/auth/listGrid',
				    showGroup: true,
				    groupField: 'typeName',
				    scrollbarSize: 0,
				    fit : true,
				    columns:[[
				        {field:'id', title:'ID', width:100, hidden : true},
				        {field: 'name', title:'名称', width:100},
				        {field: 'code', title:'编号', width:100}
				    ]]
				});
			}
	};
	Resource.init();
});