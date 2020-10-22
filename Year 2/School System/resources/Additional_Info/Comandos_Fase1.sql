use LS_Database

	/*get /courses/{acr}/classes/{sem}/{num}/teachers - shows all teachers for a class*/
	SELECT teacher.* FROM teacher_class
	INNER JOIN teacher
	ON teacher.num = teacher_class.numTeacher
	WHERE acrCourse = 'LS' AND yearSem = 1819 AND season = 'v' AND id = '41D'

	/*get /programmes/{pid} - shows the details of programme with pid acronym.*/
	SELECT * FROM programme
	WHERE ACR = 'LEIC'

	/*get /programmes/{pid}/courses - shows the course structure of programme pid.*/
	SELECT C.* FROM 
		(SELECT DISTINCT MC.acr FROM mandatory_course as MC
		WHERE MC.acrProgramme = 'LEIC'
		UNION 
		SELECT DISTINCT OC.acr FROM sem_optional_course as OC
		WHERE OC.acrProgramme = 'LEIC'
		) AS M_O
	INNER JOIN course as C
	ON C.acr = M_O.acr

	/*get /teachers/{num}/classes - shows all classes for the teacher with num number*/
	SELECT id,yearSem,season,acrCourse FROM teacher_class
	WHERE numTeacher = 2

	/*get /courses/{acr}/classes/{sem} - shows all classes of the acr course on the sem semester.*/
	SELECT * FROM class WHERE acrCourse = 'LS' and yearSem = 1819 AND season = 'v' 
	
	/*get /courses/{acr}/classes - shows all classes for a course.*/
	SELECT * FROM class WHERE acrCourse = 'LS'

	/*get /courses/{acr}/classes/{sem}/{num} - shows the classes of the acr course on the sem semester and with num number.*/
	SELECT * FROM class
	WHERE id = '41D' AND yearSem = 1819 AND season = 'v' AND acrCourse = 'LS'



	/*GET /courses/{acr}/classes/{sem}/{num}/students - shows all students of a class.*/
	SELECT * FROM student as ST
	INNER JOIN student_class as SC
	ON ST.num = SC.numStudent
	WHERE idClass='41D' AND season = 'v' AND yearSem=1819 AND acrCourse = 'LS'
	
	/*GET /courses/{acr}/classes/{sem}/{num}/groups - shows all groups of a class.*/
	SELECT DISTINCT groupNum FROM student_class
	WHERE idClass='41D' AND season = 'v' AND yearSem=1819 AND acrCourse = 'LS'

	/*GET /courses/{acr}/classes/{sem}/{num}/groups/{gno} - shows the details for the group with number gno*/
	SELECT * FROM student_class WHERE groupNum = 1
