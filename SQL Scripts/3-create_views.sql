--DROP VIEW vw_paciente,vw_func;
--DROP EXTENSION pgcrypto;
CREATE EXTENSION pgcrypto;

--Listagem de pacientes
CREATE OR REPLACE VIEW vw_paciente AS
	SELECT p.id_paciente,p.em_tratamento,p.em_treino,u.id_utilizador,u.password,u.nome,u.morada,u.cod_postal,u.localidade,u.nacionalidade,u.nif,u.cc,u.sexo,u.data_nascimento,u.contacto,u.mail,u.funcao
	FROM paciente p LEFT JOIN utilizador u ON p.id_utilizador=u.id_utilizador
	ORDER BY p.id_paciente
;

--Listagem de funcionarios
CREATE OR REPLACE VIEW vw_func AS
	SELECT f.id_funcionario,f.data_admissao,f.activo,u.id_utilizador,u.password,u.nome,u.morada,u.cod_postal,u.localidade,u.nacionalidade,u.nif,u.cc,u.sexo,u.data_nascimento,u.contacto,u.mail,u.funcao	
	FROM funcionario f LEFT JOIN utilizador u ON f.id_utilizador=u.id_utilizador
	ORDER BY f.id_funcionario
;


--Rule para vista actualizável vw_paciente
CREATE OR REPLACE RULE rl_vw_paciente_INSERT AS ON INSERT TO vw_paciente DO INSTEAD (
	INSERT INTO utilizador VALUES(DEFAULT,NEW.password,NEW.nome,NEW.morada,NEW.cod_postal,NEW.localidade,NEW.nacionalidade,NEW.nif,NEW.cc,NEW.sexo,NEW.data_nascimento,NEW.contacto,NEW.mail,NEW.funcao);
	INSERT INTO paciente VALUES(DEFAULT,currval('utilizador_id_utilizador_seq'),NEW.em_tratamento,NEW.em_treino);
    );
	  
CREATE OR REPLACE RULE rl_vw_paciente_UPDATE AS ON UPDATE TO vw_paciente DO INSTEAD (
	UPDATE utilizador SET password=NEW.password,nome=NEW.nome,morada=NEW.morada,cod_postal=NEW.cod_postal,localidade=NEW.localidade,nacionalidade=NEW.nacionalidade,nif=NEW.nif,cc=NEW.cc,sexo=NEW.sexo,data_nascimento=NEW.data_nascimento,contacto=NEW.contacto,mail=NEW.mail,funcao=NEW.funcao WHERE id_utilizador=OLD.id_utilizador;
	UPDATE paciente SET em_tratamento=NEW.em_tratamento, em_treino=NEW.em_treino WHERE id_paciente=OLD.id_paciente;
    );
      
CREATE OR REPLACE RULE rl_vw_paciente_DELETE AS ON DELETE TO vw_paciente DO INSTEAD (
	DELETE FROM paciente WHERE id_paciente=OLD.id_paciente;
	DELETE FROM utilizador WHERE id_utilizador=OLD.id_utilizador;
    );


--Rule para vista actualizável  vw_func
CREATE OR REPLACE RULE rl_vw_func_INSERT AS ON INSERT TO vw_func DO INSTEAD (
	INSERT INTO utilizador VALUES(DEFAULT,NEW.password,NEW.nome,NEW.morada,NEW.cod_postal,NEW.localidade,NEW.nacionalidade,NEW.nif,NEW.cc,NEW.sexo,NEW.data_nascimento,NEW.contacto,NEW.mail,NEW.funcao);
	INSERT INTO funcionario VALUES(DEFAULT,currval('utilizador_id_utilizador_seq'),NEW.data_admissao,NEW.activo)
	);
	  
CREATE OR REPLACE RULE rl_vw_func_UPDATE AS ON UPDATE TO vw_func DO INSTEAD (
	UPDATE utilizador SET password=NEW.password,nome=NEW.nome, morada=NEW.morada,cod_postal=NEW.cod_postal,localidade=NEW.localidade,nacionalidade=NEW.nacionalidade,nif=NEW.nif,cc=NEW.cc,sexo=NEW.sexo,data_nascimento=NEW.data_nascimento,contacto=NEW.contacto,mail=NEW.mail,funcao=NEW.funcao WHERE id_utilizador=OLD.id_utilizador;
	UPDATE funcionario SET data_admissao=NEW.data_admissao,activo=NEW.activo WHERE id_funcionario=OLD.id_funcionario
    );
	  
CREATE OR REPLACE RULE rl_vw_func_DELETE AS ON DELETE TO vw_func DO INSTEAD (
	DELETE FROM funcionario WHERE id_funcionario=OLD.id_funcionario;
	DELETE FROM utilizador WHERE id_utilizador=OLD.id_utilizador;
    );
