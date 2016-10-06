/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/9/5 23:12:56                            */
/*==============================================================*/


drop table if exists T_EMPLOYEE;

drop table if exists T_EMP_POS;

drop table if exists T_FUNC;

drop table if exists T_ORG;

drop table if exists T_POSITION;

drop table if exists T_ROLE;

drop table if exists T_ROLE_FUNC;

drop table if exists T_USER;

drop table if exists T_USER_ROLE;

/*==============================================================*/
/* Table: T_EMPLOYEE                                            */
/*==============================================================*/
create table T_EMPLOYEE
(
   ID_                  int not null comment 'ID',
   ORG_ID_              varchar(64) comment '����ID',
   CODE_                varchar(64) comment 'Ա�����',
   NAME_                varchar(255) comment '����',
   SEX_                 varchar(2) comment '�Ա�',
   EMAIL_               varchar(64) comment '����',
   TELEPHONE_           varchar(20) comment '�绰',
   CELLPHONE_           varchar(20) comment '�ֻ�',
   STATUS_              varchar(2) comment '״̬',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_EMP_POS                                             */
/*==============================================================*/
create table T_EMP_POS
(
   POS_ID_              int not null comment '��λID',
   EMP_ID_              int not null comment 'ID',
   primary key (POS_ID_, EMP_ID_)
);

/*==============================================================*/
/* Table: T_FUNC                                                */
/*==============================================================*/
create table T_FUNC
(
   ID_                  int not null,
   PARENT_ID_           int,
   CODE_                varchar(64) comment '����',
   NAME_                varchar(255) comment '����',
   URL_                 varchar(255) comment 'URL',
   TYPE_                varchar(2) comment '���ͣ�M���˵���A��Ȩ��',
   STATUS_              varchar(2) default '0' comment '״̬��0��������1��ͣ�ã�',
   SORT_                int comment '����',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_ORG                                                 */
/*==============================================================*/
create table T_ORG
(
   ID_                  varchar(64) not null comment '����ID',
   PARENT_ID_           varchar(64) comment '����ID',
   NAME_                varchar(255) comment '��������',
   CODE_                varchar(64) not null comment '��������',
   SORT_                int comment '����',
   SHORT_NAME_          varchar(255) comment '�������',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_POSITION                                            */
/*==============================================================*/
create table T_POSITION
(
   ID_                  int not null comment '��λID',
   ORG_ID_              varchar(64) comment '����ID',
   NAME_                varchar(255) comment '��λ����',
   COMMENT_             varchar(255) comment '��λ����',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_ROLE                                                */
/*==============================================================*/
create table T_ROLE
(
   ID                   int not null comment '��ɫID',
   CODE_                varchar(64) comment '��ɫ����',
   NAME_                varchar(255) comment '��ɫ����',
   COMMENT_             varchar(255) comment '����',
   primary key (ID)
);

/*==============================================================*/
/* Table: T_ROLE_FUNC                                           */
/*==============================================================*/
create table T_ROLE_FUNC
(
   ROLE_ID_             int not null comment '��ɫID',
   FUNC_ID_             int not null,
   primary key (ROLE_ID_, FUNC_ID_)
);

/*==============================================================*/
/* Table: T_USER                                                */
/*==============================================================*/
create table T_USER
(
   ID_                  int not null comment 'ID',
   USER_NAME_           varchar(255) not null comment '�˻���',
   PASSWORD_            varchar(64) not null comment '����',
   STATUS_              varchar(2) comment '״̬��0-������1��ͣ�ã�',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_USER_ROLE                                           */
/*==============================================================*/
create table T_USER_ROLE
(
   USER_ID_             int not null comment 'ID',
   ROLE_ID_             int not null comment '��ɫID',
   primary key (USER_ID_, ROLE_ID_)
);

alter table T_EMPLOYEE add constraint FK_FK_EMP_ORGID foreign key (ORG_ID_)
      references T_ORG (ID_) on delete restrict on update restrict;

alter table T_EMP_POS add constraint FK_T_EMP_POS foreign key (POS_ID_)
      references T_POSITION (ID_) on delete restrict on update restrict;

alter table T_EMP_POS add constraint FK_T_EMP_POS2 foreign key (EMP_ID_)
      references T_EMPLOYEE (ID_) on delete restrict on update restrict;

alter table T_FUNC add constraint FK_FK_PARENT_FUNC_ID foreign key (PARENT_ID_)
      references T_FUNC (ID_) on delete restrict on update restrict;

alter table T_ORG add constraint FK_PK_PARENT_ORG_ID foreign key (PARENT_ID_)
      references T_ORG (ID_) on delete restrict on update restrict;

alter table T_POSITION add constraint FK_FK_POS_ORGID foreign key (ORG_ID_)
      references T_ORG (ID_) on delete restrict on update restrict;

alter table T_ROLE_FUNC add constraint FK_T_ROLE_FUNC foreign key (ROLE_ID_)
      references T_ROLE (ID) on delete restrict on update restrict;

alter table T_ROLE_FUNC add constraint FK_T_ROLE_FUNC2 foreign key (FUNC_ID_)
      references T_FUNC (ID_) on delete restrict on update restrict;

alter table T_USER add constraint FK_FK_USER_EMPID foreign key (ID_)
      references T_EMPLOYEE (ID_) on delete restrict on update restrict;

alter table T_USER_ROLE add constraint FK_T_USER_ROLE foreign key (USER_ID_)
      references T_USER (ID_) on delete restrict on update restrict;

alter table T_USER_ROLE add constraint FK_T_USER_ROLE2 foreign key (ROLE_ID_)
      references T_ROLE (ID) on delete restrict on update restrict;

