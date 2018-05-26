--DROP ROLE dr,rui;
--DROP OWNED BY Administrador,Medico,Fisioterapeuta,Paciente;
--DROP ROLE Administrador,Medico,Fisioterapeuta,Paciente;
CREATE ROLE Tecnico ENCRYPTED PASSWORD 'ptda4' LOGIN SUPERUSER CREATEDB CREATEROLE; 
CREATE ROLE Administrador NOSUPERUSER NOINHERIT NOCREATEDB CREATEROLE;
CREATE ROLE Medico NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
CREATE ROLE Fisioterapeuta NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
CREATE ROLE Paciente NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;

--REVOKE ALL PRIVILEGES ON DATABASE ptda4 FROM Medico;



--Administrador
GRANT SELECT, INSERT, UPDATE ON TABLE utilizador,paciente,funcionario,dados,alertas,paciente_alertas,vw_paciente,vw_func TO Administrador;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE tratamento, treino TO Administrador;
GRANT SELECT, INSERT ON logs_utilizador,logs_paciente,logs_funcionario,logs_tratamento,logs_treino,logs_paciente_alertas TO Administrador;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO Administrador;

--Medico
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE tratamento TO Medico;
GRANT SELECT, INSERT, UPDATE ON TABLE paciente_alertas TO Medico;
GRANT SELECT ON TABLE treino,dados,alertas,vw_func,utilizador TO Medico;
GRANT INSERT ON TABLE logs_tratamento,logs_utilizador,logs_paciente,logs_paciente_alertas TO Medico;
GRANT SELECT, UPDATE ON TABLE paciente,vw_paciente TO Medico;

GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO Medico;

--Fisioterapeuta
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE treino TO Fisioterapeuta;
GRANT SELECT ON TABLE tratamento,dados,alertas,paciente_alertas,vw_paciente,vw_func,utilizador TO Fisioterapeuta;
GRANT INSERT ON TABLE logs_treino,logs_utilizador,logs_paciente TO Fisioterapeuta;
GRANT SELECT, UPDATE ON TABLE paciente,vw_paciente TO Fisioterapeuta;

GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO Fisioterapeuta;

--Paciente
GRANT SELECT ON TABLE tratamento,treino,paciente_alertas,vw_paciente TO Paciente;
GRANT INSERT ON TABLE dados,alertas TO Paciente;

GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO Paciente;

--Criacao do "master admin"
CREATE ROLE ptda4 CREATEROLE ENCRYPTED PASSWORD 'ptda4' LOGIN INHERIT;
GRANT Administrador to ptda4;

--CREATE ROLE pessoa CREATEROLE ENCRYPTED PASSWORD '1234' LOGIN INHERIT;
--GRANT Medico to pessoa;
