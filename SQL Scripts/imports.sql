--Testes
--SELECT * FROM vw_func;
--SELECT * FROM vw_paciente;
--SELECT * FROM funcionario
--SELECT * FROM utilizador

--COPY utilizador FROM 'C:\insert_utilizador.txt' USING DELIMITERS ';';
--COPY paciente(nif,em_tratamento) FROM 'C:\insert_paciente.txt' USING DELIMITERS ';' WITH CSV HEADER;
--COPY vw_paciente FROM 'C:\insert_pacientes.txt' USING DELIMITERS ';' WITH CSV HEADER;



--Criacao de users
DROP USER "111111111";
DROP USER "222222222";

CREATE ROLE "111111111" ENCRYPTED PASSWORD '1' LOGIN INHERIT CREATEROLE;
GRANT Medico to "111111111";

CREATE ROLE "222222222" ENCRYPTED PASSWORD '1' LOGIN INHERIT CREATEROLE;
GRANT Administrador to "222222222";

--Criacao de paciente
INSERT INTO vw_paciente VALUES (DEFAULT,'s','s',DEFAULT,'pass','Jordan Ingram','Rua do camelo','3810-222','Aveiro','Português','868701391','123456789012','m','1980-01-01','961234567','yourmail@ua.pt','paciente');
--Criacao de funcionario
INSERT INTO vw_func VALUES (DEFAULT,'2017-01-01','s',DEFAULT,'pass','Jordan Ingram','Rua do camelo','3810-222','Aveiro','Português','868701392','123456789013','m','1980-01-01','961234567','yourmail@ua.pt','medico');

--Criacao de tratamento e teste de script
UPDATE paciente SET em_tratamento='n';
INSERT INTO tratamento VALUES (DEFAULT,'desc','notas','2018-01-01','2018-01-02',1,1);

--Criacao de treino
INSERT INTO treino VALUES (DEFAULT,'desc','notas','2018-01-01','2018-01-02',1,1);


--criacao de dados p teste de alertas
--INSERT INTO dados VALUES (1,current_date,current_time,min,max,freq
INSERT INTO dados VALUES (1,current_date,current_time,10,90,70);
