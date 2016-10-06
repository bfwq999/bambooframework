insert into t_org(id_,code_,name_,sort_) values('0','000','根机构',1);
insert into t_org(id_,code_,name_,parent_id_,sort_) values('00001','001','机构1','0',1);
insert into t_org(id_,code_,name_,parent_id_,sort_) values('00002','002','机构2','0',2);

insert into t_position(id_,org_id_,name_,comment_)
values(1,'00001','测试岗位','测试用');

insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(1,'admin','测试1@test.com','00001','0','admin','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(2,'user','测试@test.com','00001','0','user','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(3,'测试3','测试@test.com','00001','0','user3','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(4,'测试4','测试@test.com','00001','0','user4','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(5,'测试5','测试@test.com','00001','0','user5','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(6,'测试6','测试@test.com','00001','0','user6','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(7,'测试7','测试@test.com','00001','0','user7','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(8,'测试8','测试@test.com','00001','0','user8','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(9,'测试9','测试@test.com','00001','0','user9','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(10,'测试10','测试@test.com','00001','0','user10','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(11,'测试11','测试@test.com','00001','0','user11','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(12,'测试12','测试@test.com','00002','0','user12','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(13,'测试13','测试@test.com','00002','0','user13','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(14,'测试14','测试@test.com','00002','0','user14','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(15,'测试15','测试@test.com','00002','0','user15','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(16,'测试16','测试@test.com','00001','0','user16','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(17,'测试17','测试@test.com','00001','0','user17','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(18,'测试18','测试@test.com','00001','0','user18','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(19,'测试19','测试@test.com','00001','0','user19','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(20,'测试20','测试@test.com','00001','0','user20','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(21,'测试21','测试@test.com','00001','0','user21','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(22,'测试22','测试@test.com','00001','0','user22','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(23,'测试23','测试@test.com','00001','0','user23','888888');
insert into t_user(id_,name_,email_,org_id_,STATUS_,LOGIN_NAME_,PASSWORD_)
values(24,'测试24','测试@test.com','00001','0','user24','888888');

insert into t_role(id_,code_,name_) values(1,'admin','管理员');
insert into t_role(id_,code_,name_) values(2,'user','普通员工');

insert into t_user_role(user_id_,role_id_) values(1,1);
insert into t_user_role(user_id_,role_id_) values(2,2);

insert into t_func(ID_,CODE_,NAME_,URL_,TYPE_,SORT_)
values(5,'org:query','机构管理','views/auth/org.html','M',1);

insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(12,'org:add',5,'添加机构',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(13,'org:edit',5,'修改机构',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(14,'org:delete',5,'删除机构',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(15,'position:add',5,'添加岗位',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(16,'position:edit',5,'修改岗位',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(17,'position:delete',5,'删除岗位',null,'A');

insert into t_func(ID_,CODE_,NAME_,URL_,TYPE_,SORT_)
values(1,'user:query','用户管理','views/auth/user.html','M',2);
insert into t_func(ID_,CODE_,NAME_,URL_,TYPE_,SORT_)
values(7,'role:query','角色管理','views/auth/role.html','M',3);
insert into t_func(ID_,CODE_,NAME_,URL_,TYPE_,SORT_)
values(8,'func:query','权限管理','views/auth/func.html','M',4);


insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(2,'user:edit','1','修改用户','','A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(3,'user:add','1','添加用户','','A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(4,'user:delete','1','删除用户',null,'A');

insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(9,'func:add','8','添加菜单',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(10,'func:edit','8','修改菜单',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(11,'func:delete','8','删除菜单',null,'A');

insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(18,'role:add','7','添加角色',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(19,'role:edit','7','修改角色',null,'A');
insert into t_func(ID_,CODE_,PARENT_ID_,NAME_,URL_,TYPE_)
values(20,'role:delete','7','删除角色',null,'A');

insert into t_role_func(role_id_,func_id_) values(2,1);
insert into t_role_func(role_id_,func_id_) values(2,5);

insert into t_code_define(name_,description_) values('USER_STATUS','状态');
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('USER_STATUS','0','在职',null,null,1);
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('USER_STATUS','1','离职',null,null,2);

insert into t_code_define(name_,description_) values('FUNC_STATUS','状态');
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('FUNC_STATUS','0','正常',null,null,1);
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('FUNC_STATUS','1','停用',null,null,2);

insert into t_code_define(name_,description_) values('YESNO','是否');
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('YESNO','0','否',null,null,1);
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('YESNO','1','是',null,null,2);

insert into t_code_define(name_,description_) values('SEX','性别');
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('SEX','male','男',null,null,1);
insert into t_code_list(name_,value_,text_,parent_,filter_,sort_)
values('SEX','female','女',null,null,2);
