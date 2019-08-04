create database Vbes
go
use Vbes
go
create table Video
(
	id int primary key identity(1,1),
	name varchar(100) not null,
	url varchar(150) not null
)

create table Keys
(
    id int primary key identity(1,1),
	keys varchar(50) not null,
	maxc int not null,
	curr int not null,
	ver char(1) not null,
	cdate datetime not null,
	mark varchar(200)
)

create table Users
(
	id int primary key identity(1,1),
	name varchar(20) not null,
	psd varchar(100) not null,
	roles int not null,
	nickname varchar(20) not null,
	gender bit not null,
	email varchar(100) not null,
	valid bit not null,
	cdate datetime not null,
	mark varchar(200)
)

create table Quotations
(
	id int primary key identity(1,1),
	sentence varchar(200) not null,
	cdate datetime,
	tips bit not null
)

create table Knowledge
(
	id int primary key identity(1,1),
	title varchar(100) not null,
	artical text,
	cdate datetime,
	cuser varchar(50),
	edate datetime,
	euser varchar(50),
	comment datetime,
	cread int not null,
	valid bit not null,
)

create table Comment
(
	id int primary key identity(1,1),
	aid int not null,
	uid varchar(20) not null,
	star int not null, 
	comment varchar(200),
	cdate datetime not null,
	device varchar(50)
)

create table Feedback
(
	id int primary key identity(1,1),
	suggest varchar(500),
	device varchar(50),
	contact varchar(100),
	cdate datetime not null
)

insert into Video (name ,url)
values
('java_1','http://player.youku.com/player.php/sid/XNTk2NDkzNTI4/v.swf'),
('java_2','http://player.youku.com/player.php/sid/XNTk2NDk2OTg4/v.swf'),
('java_3','http://player.youku.com/player.php/sid/XNTk2NDk5ODE2/v.swf'),
('java_4','http://player.youku.com/player.php/sid/XNTk2NTAzMTU2/v.swf'),
('java_5','http://player.youku.com/player.php/sid/XNTk2NTA3ODcy/v.swf'),
('java_6','http://player.youku.com/player.php/sid/XNTk2NTIxNjEy/v.swf'),
('java_7','http://player.youku.com/player.php/sid/XNTk2NTQzMzYw/v.swf'),
('java_8','http://player.youku.com/player.php/sid/XNTk2OTE5OTg0/v.swf'),
('java_9','http://player.youku.com/player.php/sid/XNTk2OTIzNTA0/v.swf'),
('java_10','http://player.youku.com/player.php/sid/XNTk2OTc3ODA4/v.swf'),
('java_11','http://player.youku.com/player.php/sid/XNTk4NzIxMjEy/v.swf'),
('java_12','http://player.youku.com/player.php/sid/XNjAxMzM0OTQ0/v.swf'),
('java_13','http://player.youku.com/player.php/sid/XNjA3NjAwNTcy/v.swf'),
('java_14','http://player.youku.com/player.php/sid/XNjA5NDQ1OTUy/v.swf'),
('java_15','http://player.youku.com/player.php/sid/XNjEwNzQ4NjIw/v.swf'),
('java_16','http://player.youku.com/player.php/sid/XNjEwNzU1NzA4/v.swf'),
('java_17','http://player.youku.com/player.php/sid/XNjExNDQwOTMy/v.swf'),
('java_18','http://player.youku.com/player.php/sid/XNjEzMTk3ODYw/v.swf'),
('java_19','http://player.youku.com/player.php/sid/XNjE0MDcyNjM2/v.swf'),
('java_20','http://player.youku.com/player.php/sid/XNjIxNDg1MTky/v.swf'),
('java_21','http://player.youku.com/player.php/sid/XNjIxNDkxNDIw/v.swf'),
('java_22','http://player.youku.com/player.php/sid/XNjIxNTIxNjA0/v.swf'),
('java_23','http://player.youku.com/player.php/sid/XNjM0MDIzMjk2/v.swf'),
('java_24','http://player.youku.com/player.php/sid/XNjM4NjMxMjky/v.swf'),
('java_25','http://player.youku.com/player.php/sid/XNjM4NjMzNDI0/v.swf'),
('java_26','http://player.youku.com/player.php/sid/XNjM4NjQ4NTI0/v.swf'),
('java_27','http://player.youku.com/player.php/sid/XNjgyNTU5Njc2/v.swf'),
('java_28','http://player.youku.com/player.php/sid/XNjgyNTYyODQ4/v.swf'),
('java_29','http://player.youku.com/player.php/sid/XNjgyNTY0MDAw/v.swf'),
('java_30','http://player.youku.com/player.php/sid/XNjgzNjI4MzEy/v.swf'),
('java_31','http://player.youku.com/player.php/sid/XNjgzNjM4NDQw/v.swf'),
('java_32','http://player.youku.com/player.php/sid/XNjgzODQ5MDk2/v.swf'),
('java_33','http://player.youku.com/player.php/sid/XNjgzODUwMDAw/v.swf'),
('java_34','http://player.youku.com/player.php/sid/XNjgzODUwNzYw/v.swf'),
('java_35','http://player.youku.com/player.php/sid/XNjgzODUxNTI0/v.swf')
GO

--name	psd	roles	nickname	gender	email	valid	cdate	mark
--admin	3BE54FC5CCBBA30ACAEBF6F25C2CF5A1	1	Admin	0	vbea@foxmail.com	1	2016-01-07 12:00:00.000	管理员邠心工作室

