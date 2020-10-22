use LS_Database

create table users(
	email varchar(50) primary key,
	username varchar(50) not null
);

create table programme(
	acr varchar(6) primary key,
	p_name varchar(50) not null,
	numSem int not null
);

create table student(
	num int primary key,
	email varchar(50) unique not null references users(email),
	p_acr varchar(6) references programme(acr)
);

create table teacher(
	num int primary key,
	email varchar(50) unique not null references users(email) 
);


create table course(
	acr varchar(3) primary key,
	c_name varchar(50) not null,
	numCoord int unique not null references teacher(num)
);

create table semester(
	yearSem int,
	season char,
	primary key (yearSem, season) 
);

create table mandatory_course(
	acr varchar(3) references course(acr),
	yearSem int,
	season char,
	acrProgramme varchar(6) references programme(acr),
	foreign key (yearSem, season) references semester(yearSem, season),
	primary key(acr,yearSem,season,acrProgramme)
);

create table optional_course(
	acr varchar(3) references course(acr),
	acrProgramme varchar(6) references programme(acr),
	primary key(acr,acrProgramme)
);

create table sem_optional_course(
	acr varchar(3),
	yearSem int,
	season char,
	acrProgramme varchar(6),
	foreign key (acr,acrProgramme) references optional_course(acr,acrProgramme),
	foreign key (yearSem, season) references semester(yearSem, season),
	primary key (acr,yearSem,season,acrProgramme)
);

create table class(
	id varchar(3),
	yearSem int,
	season char,
	acrCourse varchar(3) references course(acr),
	foreign key (yearSem, season) references semester(yearSem, season),
	primary key (id,yearSem,season,acrCourse)
);

create table teacher_class(
	id varchar(3),
	yearSem int,
	season char,
	numTeacher int references teacher(num),
	acrCourse varchar(3),
	foreign key (id, yearSem, season, acrCourse) references class(id,yearSem, season, acrCourse),
	primary key (id, yearSem, season, numTeacher, acrCourse)
);

create table groups(
	number int primary key
);

create table student_class(
	numStudent int unique references student(num),
	groupNum int references groups(number),
	idClass varchar(3),
	yearSem int,
	season char,
	acrCourse varchar(3),
	foreign key (idClass,yearSem,season,acrCourse) references class(id,yearSem,season,acrCourse),
	primary key (numStudent,idClass,yearSem,season,acrCourse)
);
