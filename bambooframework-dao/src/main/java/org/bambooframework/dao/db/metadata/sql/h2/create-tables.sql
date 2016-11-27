/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/6/12 12:40:46                           */
/*==============================================================*/


drop table if exists DB_COLUMN;

drop table if exists DB_DATABASE;

drop table if exists DB_TABLE;

drop table if exists T_CODE_DEFINE;

drop table if exists T_CODE_LIST;

drop table if exists T_FUNC;

drop table if exists T_ORG;

drop table if exists T_POSITION;

drop table if exists T_PROPERTY;

drop table if exists T_ROLE;

drop table if exists T_ROLE_FUNC;

drop table if exists T_USER;

drop table if exists T_USER_POS;

drop table if exists T_USER_ROLE;

/*==============================================================*/
/* Table: DB_COLUMN                                             */
/*==============================================================*/
create table DB_COLUMN
(
   ID_                  int not null comment 'ID',
   FOREIGN_KEY_         int comment '���',
   TABLE_ID_            int comment '��_ID',
   NAME_                varchar(255) not null comment '�ֶ���',
   PRIMARY_KEY_         varchar(32) comment '�Ƿ�������',
   TYPE_                int comment '����',
   LENGTH_              int comment '����',
   PRECISION_           int comment '����',
   DEFAULT_             varchar(255) comment 'Ĭ��ֵ',
   COMMENT_             varchar(255) comment '��ע',
   primary key (ID_)
);

/*==============================================================*/
/* Table: DB_DATABASE                                           */
/*==============================================================*/
create table DB_DATABASE
(
   ID_                  int not null comment 'ID',
   NAME_                varchar(255) not null comment '����',
   TYPE_                varchar(32) comment '����',
   CONNECTION_URL_      varchar(255) comment '��������',
   DRIVER_CLASS_        varchar(255) comment '������',
   USER_NAME_           varchar(255) comment '�û���',
   PASSWORD_            varchar(64) comment '����',
   primary key (ID_)
);

/*==============================================================*/
/* Table: DB_TABLE                                              */
/*==============================================================*/
create table DB_TABLE
(
   ID_                  int not null comment 'ID',
   DB_ID_               int comment 'ID',
   NAME_                varchar(255) not null comment '����',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_CODE_DEFINE                                         */
/*==============================================================*/
create table T_CODE_DEFINE
(
   NAME_                varchar(255) not null,
   DESCRIPTION_         varchar(255),
   primary key (NAME_)
);

/*==============================================================*/
/* Table: T_CODE_LIST                                           */
/*==============================================================*/
create table T_CODE_LIST
(
   NAME_                varchar(255) not null,
   VALUE_               varchar(64) not null comment 'ֵ',
   TEXT_                varchar(255) not null comment '����',
   PARENT_              varchar(64) comment 'ֵ',
   FILTER_              varchar(64) comment '����',
   SORT_                int comment '����',
   T_C_NAME_            varchar(255),
   primary key (NAME_, VALUE_)
);

/*==============================================================*/
/* Table: T_FUNC                                                */
/*==============================================================*/
create table T_FUNC
(
   ID_                  varchar(64) not null,
   PARENT_ID_           varchar(64),
   CODE_                varchar(64) comment '����',
   NAME_                varchar(255) comment '����',
   URL_                 varchar(255) comment 'URL',
   TYPE_                varchar(32) comment '���ͣ�M���˵���A��Ȩ��',
   STATUS_              varchar(32) default '0' comment '״̬��0��������1��ͣ�ã�',
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
   STATUS_              varchar(32) default '0' comment '״̬��0��������1��ɾ��',
   TYPE_                varchar(32) default '1' comment '���ͣ�1��������2������',
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
   STATUS_              varchar(32) default '0' comment '״̬��0��������1��ͣ�ã�',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_PROPERTY                                            */
/*==============================================================*/
create table T_PROPERTY
(
   ID_                  varchar(64) not null,
   VALUE_               varchar(64),
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_ROLE                                                */
/*==============================================================*/
create table T_ROLE
(
   ID_                  int not null comment '��ɫID',
   CODE_                varchar(64) comment '��ɫ����',
   NAME_                varchar(255) comment '��ɫ����',
   COMMENT_             varchar(255) comment '����',
   STATUS_              varchar(32) default '0' comment '״̬��0��������1��ɾ��',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_ROLE_FUNC                                           */
/*==============================================================*/
create table T_ROLE_FUNC
(
   ROLE_ID_             int not null comment '��ɫID',
   FUNC_ID_             varchar(64) not null,
   primary key (ROLE_ID_, FUNC_ID_)
);

/*==============================================================*/
/* Table: T_USER                                                */
/*==============================================================*/
create table T_USER
(
   ID_                  int not null comment 'ID',
   ORG_ID_              varchar(64) comment '����ID',
   CODE_                varchar(64) comment 'Ա�����',
   NAME_                varchar(255) not null comment '����',
   SEX_                 varchar(32) comment '�Ա�',
   EMAIL_               varchar(64) comment '����',
   TELEPHONE_           varchar(20) comment '�绰',
   CELLPHONE_           varchar(20) comment '�ֻ�',
   STATUS_              varchar(32) default '0' comment '״̬',
   LOGIN_NAME_          varchar(255) comment '�˻���',
   PASSWORD_            varchar(64) comment '����',
   primary key (ID_)
);

/*==============================================================*/
/* Table: T_USER_POS                                            */
/*==============================================================*/
create table T_USER_POS
(
   POS_ID_              int not null comment '��λID',
   USER_ID_             int not null comment 'ID',
   primary key (POS_ID_, USER_ID_)
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

alter table DB_COLUMN add constraint FK_FOREIGNKEY foreign key (FOREIGN_KEY_)
      references DB_COLUMN (ID_) on delete restrict on update restrict;

alter table DB_COLUMN add constraint FK_TABLE_COLUMN foreign key (TABLE_ID_)
      references DB_TABLE (ID_) on delete restrict on update restrict;

alter table DB_TABLE add constraint FK_TABLE_DATABASE foreign key (DB_ID_)
      references DB_DATABASE (ID_) on delete restrict on update restrict;

alter table T_CODE_LIST add constraint FK_DEFINE_LIST foreign key (NAME_)
      references T_CODE_DEFINE (NAME_) on delete restrict on update restrict;

alter table T_CODE_LIST add constraint FK_PARENT_VALUE_ foreign key (T_C_NAME_, PARENT_)
      references T_CODE_LIST (NAME_, VALUE_) on delete restrict on update restrict;

alter table T_FUNC add constraint FK_FK_PARENT_FUNC_ID foreign key (PARENT_ID_)
      references T_FUNC (ID_) on delete restrict on update restrict;

alter table T_ORG add constraint FK_PK_PARENT_ORG_ID foreign key (PARENT_ID_)
      references T_ORG (ID_) on delete restrict on update restrict;

alter table T_POSITION add constraint FK_FK_POS_ORGID foreign key (ORG_ID_)
      references T_ORG (ID_) on delete restrict on update restrict;

alter table T_ROLE_FUNC add constraint FK_T_ROLE_FUNC foreign key (ROLE_ID_)
      references T_ROLE (ID_) on delete restrict on update restrict;

alter table T_ROLE_FUNC add constraint FK_T_ROLE_FUNC2 foreign key (FUNC_ID_)
      references T_FUNC (ID_) on delete restrict on update restrict;

alter table T_USER add constraint FK_FK_EMP_ORGID foreign key (ORG_ID_)
      references T_ORG (ID_) on delete restrict on update restrict;

alter table T_USER_POS add constraint FK_T_USER_POS foreign key (POS_ID_)
      references T_POSITION (ID_) on delete restrict on update restrict;

alter table T_USER_POS add constraint FK_T_USER_POS2 foreign key (USER_ID_)
      references T_USER (ID_) on delete restrict on update restrict;

alter table T_USER_ROLE add constraint FK_T_USER_ROLE foreign key (USER_ID_)
      references T_USER (ID_) on delete restrict on update restrict;

alter table T_USER_ROLE add constraint FK_T_USER_ROLE2 foreign key (ROLE_ID_)
      references T_ROLE (ID_) on delete restrict on update restrict;

