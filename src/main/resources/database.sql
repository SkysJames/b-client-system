drop table if exists sys_department;
create table sys_department (
    id int unsigned not null auto_increment,
	name varchar(32) not null,
	remark varchar(255) default null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	unique UK_NAME (name)
)engine=innodb charset=utf8 comment '部门表';

drop table if exists sys_role;
create table sys_role (
    id int unsigned not null auto_increment,
	name varchar(32) not null,
	remark varchar(255) default null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	unique UK_NAME (name)
)engine=innodb charset=utf8 comment '角色表';

drop table if exists sys_user;
create table sys_user (
    id int unsigned not null auto_increment,
	name varchar(32) not null,
	emp_no varchar(20) not null comment '工号',
	department_id int unsigned not null comment '部门编号',
	sex varchar(2) not null comment '男，女，未知',
	mobile_phone varchar(11) default null comment '手机号码',
	tele_phone varchar(20) default null comment '固话',
	email varchar(100) default null comment '邮箱',
	password varchar(32) not null,
	status varchar(10) not null comment '在职、锁定、离职',
	create_time datetime not null,
	create_user_id int unsigned not null,
	last_login_time datetime default null comment '最后登录时间',
	update_time datetime not null comment '最后更新时间',
	update_user_id int unsigned not null comment '最后更新的用户',
	primary key (id),
	unique UK_EMP_NO (emp_no)
)engine=innodb charset=utf8 comment '用户表';

drop table if exists cust_level;
create table cust_level (
    id int unsigned not null auto_increment,
	name varchar(32) not null,
	remark varchar(255) default null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	unique UK_NAME (name)
)engine=innodb charset=utf8 comment '客户级别';


drop table if exists cust_trade;
create table cust_trade (
    id int unsigned not null auto_increment,
	name varchar(32) not null,
	remark varchar(255) default null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	unique UK_NAME (name)
)engine=innodb charset=utf8 comment '客户行业';

drop table if exists product;
create table product (
    id int unsigned not null auto_increment,
	name varchar(32) not null,
	remark varchar(255) default null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	unique UK_NAME (name)
)engine=innodb charset=utf8 comment '产品信息';

drop table if exists sys_resource;
create table sys_resource (
  id int unsigned not null auto_increment,
	name varchar(32) not null,
	code varchar(32) not null,
	type_id int unsigned default null,
	primary key (id),
  unique UK_CODE (code)
)engine=innodb charset=utf8 comment '权限表';

drop table if exists sys_resource_type;
create table sys_resource_type (
  id int unsigned not null auto_increment,
	name varchar(32) not null,
	primary key (id),
  unique UK_CODE (name)
)engine=innodb charset=utf8 comment '权限类型';

drop table if exists sys_role_res;
create table sys_role_res (
  id int unsigned not null auto_increment,
  role_id int unsigned not null,
  resource_id int unsigned not null,
  PRIMARY KEY (id),
  UNIQUE INX_UK_ROLE_RES (role_id, resource_id)
)engine=innodb charset=utf8 comment '角色权限关联表';

drop table if exists sys_user_role;
create table sys_user_role (
  id int unsigned not null auto_increment,
  user_id int unsigned not null,
  role_id int unsigned not null,
  PRIMARY KEY (id),
  UNIQUE INX_UK_USER_ROLE (user_id, role_id)
)engine=innodb charset=utf8 comment '用户角色关联表';


drop table if exists customer;
create table customer (
    id int unsigned not null auto_increment,
	name varchar(32) not null,
	tele_phone varchar(20) default null,
	mobile_phone varchar(11) default null,
	qq varchar(20) default null,
	wechat varchar(100) default null,
	level_id int unsigned not null,
	trade_id int unsigned not null,
	company varchar(50) not null,
	is_share tinyint(1) not null default 0,
	follow_user_id int unsigned not null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	key INX_FW_USER_ID (follow_user_id),
	key INX_CREATE_TIME (create_time)
)engine=innodb charset=utf8 comment '客户表';

drop table if exists appointment;
create table appointment (
    id int unsigned not null auto_increment,
	customer_id int unsigned not null,
	product_id int unsigned not null,
	appointment_time varchar(20) not null,
	status varchar(10) not null,
	remark varchar(255) default null,
    create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	key INX_CREATE_TIME (create_time)
)engine=innodb charset=utf8 comment '预约表';


drop table if exists appointment_tip;
create table appointment_tip (
    id int unsigned not null auto_increment,
	appointment_id int unsigned not null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	tip varchar(500) not null,
	primary key (id),
	key FK_APPOINTMENT_ID (appointment_id)
)engine=innodb charset=utf8 comment '预约回访表';

drop table if exists sale_order;
create table sale_order (
    id int unsigned not null auto_increment,
	code varchar(32) not null comment '订单号',
	`type` varchar(32) not null,
	product_id int unsigned not null,
	customer_id int unsigned not null,
	account varchar(100) default null comment '账号',
	remark varchar(255) default null,
	status varchar(20) not null comment '付款状态',
	advertising_money decimal(20, 2) not null comment '广告币',
	customer_rebates decimal(20, 2) not null comment '客户返点',
	cost_rebates decimal(20, 2) not null comment '成本返点',
	customer_pay decimal(20, 2) not null comment '客户应付金额',
	cost_price decimal(20, 2) not null comment '成本价',
	gross_profit decimal(20, 2) not null comment '毛利',
	create_time datetime not null,
	create_user_id int unsigned not null,
	update_time datetime not null,
	update_user_id int unsigned not null,
	primary key (id),
	unique UK_CODE (code),
	key INX_CREATE_TIME (create_time)
)engine=innodb charset=utf8 comment '销售订单';

drop table if exists sale_order_log;
create table sale_order_log (
    id int unsigned not null auto_increment,
	code varchar(32) not null comment '订单号',
	old_status varchar(20) not null,
	new_status varchar(20) not null,
	create_time datetime not null,
	create_user_id int unsigned not null,
	primary key (id),
	key FK_CODE (code)
)engine=innodb charset=utf8 comment '销售订单操作日志';

drop table if exists sys_data_backup_log ;
create table sys_data_backup_log (
    id int unsigned not null auto_increment,
	table_name varchar(100) not null,
	remark varchar(255) default null,
	backup_path varchar(255) not null,
	start_time datetime not null,
	end_time datetime not null,
	create_user_id int unsigned not null,
	primary key (id)
)engine=innodb charset=utf8 comment '销售订单操作日志';





#初始化数据
insert into sys_resource_type(id, name) values(1, '客户管理模块');
insert into sys_resource_type(id, name) values(2, '产品管理模块');
insert into sys_resource_type(id, name) values(3, '销售单管理模块');
insert into sys_resource_type(id, name) values(4, '预约管理模块');
insert into sys_resource_type(id, name) values(5, '系统管理模块');

insert into sys_resource(code, name, type_id) values('CUSTOMER_MANAGER', '客户管理', 1);
insert into sys_resource(code, name, type_id) values('CUSTOMER_LEVEL', '客户级别管理', 1);
insert into sys_resource(code, name, type_id) values('CUSTOMER_TRADE', '客户行业管理', 1);
insert into sys_resource(code, name, type_id) values('CUSTOMER_EXPORT', '客户信息导出', 1);

insert into sys_resource(code, name, type_id) values('PRODUCT_MANAGER', '产品管理', 2);
insert into sys_resource(code, name, type_id) values('PRODUCT_EXPORT', '产品信息导出', 2);

#insert into sys_resource(code, name, type_id) values('SALES_INSERT', '销售单录入', 3);
insert into sys_resource(code, name, type_id) values('SALES_MANAGER', '销售单管理', 3);
insert into sys_resource(code, name, type_id) values('SALES_EXPORT', '销售单导出', 3);
insert into sys_resource(code, name, type_id) values('SALES_STASTIC', '销售单统计', 3);

insert into sys_resource(code, name, type_id) values('BOOK_MANAGER', '预约管理', 4);

insert into sys_resource(code, name, type_id) values('SYS_USER', '用户管理', 5);
insert into sys_resource(code, name, type_id) values('SYS_DEPARTMENT', '部门管理', 5);
insert into sys_resource(code, name, type_id) values('SYS_ROLE', '角色管理', 5);
insert into sys_resource(code, name, type_id) values('SYS_DATA', '数据维护', 5);


INSERT INTO sys_user(id, name, emp_no, department_id, sex, password, status, 
create_time, create_user_id, update_time,update_user_id)
VALUES(1, 'admin', 'admin', 1, '未知', '123456', '在职', CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1);

insert into sys_department(id, name, remark, create_time, create_user_id, update_time,update_user_id)
values(1, '总部', '总部', CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP, 1);