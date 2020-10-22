use LS_Database


insert into users
	values	
		('joao@outlook.com','João'),
		('jose@hotmail.com', 'José'),
		('luis_war@live.com.pt','LuisGuerra'),
		('manuel@gmail.com','Manuel'),
		('maria@hotmail.com','Maria'),		
		('pedrotereso4@gmail.com', 'PedroTereso'),
		('wilson-ruben@hotmail.com', 'WilsonRCosta')

insert into programme
	values
		('LEIC', 'Engenharia Informática e de Computadores', 6),
		('LEIM', 'Engenharia Informática e Multimédia', 6),
		('LMATE', 'Matemática', 6)

insert into student
	values
		(43593,'wilson-ruben@hotmail.com', 'LEIC'),
		(43755,'luis_war@live.com.pt','LEIC'),
		(44015,'pedrotereso4@gmail.com', 'LEIC')

insert into teacher
	values
		(4,'joao@outlook.com'),
		(3,'jose@hotmail.com'),
		(1,'maria@hotmail.com'),
		(2,'manuel@gmail.com')

insert into course
	values 
		('LS', 'Laboratório de Software', 1),
		('AVE', 'Ambientes Virtuais de Execução', 2),
		('IA', 'Inteligência Artificial', 3)
		
insert into semester
	values 
		(1718, 'v'),
		(1819, 'i'),
		(1819, 'v')

insert into mandatory_course
	values	
		('LS',1718,'v','LEIC'),
		('LS',1819,'v','LEIC'),
		('LS',1819,'i','LEIC')

insert into optional_course
	values
		('IA','LEIC')

insert into sem_optional_course
	values
		('IA',1819,'v','LEIC'),
		('IA',1718,'v','LEIC')

insert into class
	values
		('42D', 1819, 'v','LS'),
		('42D', 1819, 'v','AVE'),
		('42D', 1819, 'v','IA'),
		('42D', 1819, 'i','LS'),
		('42D', 1819, 'i','AVE'),
		('41D', 1819, 'v','LS'),
		('41D', 1819, 'v','AVE'),
		('41D', 1819, 'v','IA'),
		('41D', 1819, 'i','LS'),
		('41D', 1819, 'i','AVE'),
		('41D', 1718, 'v','LS'),
		('41D', 1718, 'v','AVE'),
		('41D', 1718, 'v','IA'),
		('42D', 1718, 'v','LS'),
		('42D', 1718, 'v','AVE'),
		('42D', 1718, 'v','IA')

insert into teacher_class
	values
		('42D', 1819, 'v', 1,'LS'),
		('42D', 1819, 'v', 2,'AVE'),
		('42D', 1819, 'v', 3,'IA'),
		('42D', 1819, 'i', 1,'LS'),
		('42D', 1819, 'i', 2,'AVE'),
		('41D', 1819, 'v', 3,'LS'),
		('41D', 1819, 'v',1,'AVE'),
		('41D', 1819, 'v',2,'IA'),
		('41D', 1819, 'i',3,'LS'),
		('41D', 1819, 'i',1,'AVE'),
		('41D', 1718, 'v',2,'LS'),
		('41D', 1718, 'v',3,'AVE'),
		('41D', 1718, 'v',1,'IA'),
		('42D', 1718, 'v',2,'LS'),
		('42D', 1718, 'v',3,'AVE'),
		('42D', 1718, 'v',1,'IA')

insert into groups
	values (1),(2),(3),(4),(5);

insert into student_class
	values
		(43593,1,'41D',1819,'v','LS'),
		(43755,1,'41D',1819,'v','LS'),
		(44015,1,'41D',1819,'v','LS');