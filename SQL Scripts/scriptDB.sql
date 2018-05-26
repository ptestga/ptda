--------------------------------------------------------------------------------
-- projeto tematico em desenvolvimento de aplicacoes
-- grupo 4
--------------------------------------------------------------------------------


--------------------------------------------------------------------------------
--Criacao das tabelas principais
--------------------------------------------------------------------------------
CREATE DOMAIN SEXO AS CHAR(1) NOT NULL CHECK (VALUE IN('m','f'));
CREATE DOMAIN FUNCAO AS VARCHAR(20) NOT NULL CHECK (VALUE IN('paciente','medico','fisioterapeuta','administrador'));
CREATE DOMAIN SN AS CHAR (1) NOT NULL CHECK (VALUE IN('s','n'));

CREATE TABLE utilizador (
	id_utilizador SERIAL PRIMARY KEY,
	password TEXT NOT NULL,
	nome VARCHAR(100) NOT NULL,
	morada VARCHAR(200) NOT NULL,
	cod_postal char(8) NOT NULL,
	localidade VARCHAR(100) NOT NULL,
	nacionalidade VARCHAR(50) NOT NULL,
	nif char(9) NOT NULL UNIQUE,
	cc VARCHAR(12) NOT NULL UNIQUE,
	sexo SEXO,
    data_nascimento DATE NOT NULL,
    contacto int NOT NULL,
	mail VARCHAR(50),
	funcao FUNCAO
);

CREATE TABLE paciente (
	id_paciente SERIAL PRIMARY KEY,
	id_utilizador int REFERENCES utilizador (id_utilizador) ON UPDATE CASCADE ON DELETE RESTRICT,
	em_tratamento SN,
    em_treino SN
);

CREATE TABLE funcionario (
	id_funcionario SERIAL PRIMARY KEY,
	id_utilizador int REFERENCES utilizador (id_utilizador) ON UPDATE CASCADE ON DELETE RESTRICT,
	data_admissao DATE NOT NULL,
	activo SN
);

CREATE TABLE tratamento (
	id_tratamento SERIAL PRIMARY KEY,
	desc_tratamento VARCHAR NOT NULL,
	notas_tratamento VARCHAR,
	data_inicio DATE NOT NULL,
	data_fim DATE NOT NULL,
	paciente int NOT NULL,
	medico int NOT NULL,
	FOREIGN KEY(paciente) REFERENCES paciente(id_paciente) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(medico) REFERENCES funcionario(id_funcionario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE treino (
	id_treino SERIAL PRIMARY KEY,
	desc_treino VARCHAR NOT NULL,
	notas_treino VARCHAR,
	data_inicio DATE NOT NULL,
	data_fim DATE NOT NULL,
	fisioterapeuta int NOT NULL,
	tratamento int NOT NULL,
	FOREIGN KEY(fisioterapeuta) REFERENCES funcionario(id_funcionario) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(tratamento) REFERENCES tratamento(id_tratamento) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE dados (
	paciente int REFERENCES paciente(id_paciente) ON UPDATE CASCADE ON DELETE RESTRICT,
	data DATE NOT NULL,
	hora TIME NOT NULL,
	pressao_arterial_min int NOT NULL,
	pressao_arterial_max int NOT NULL,
	freq_cardiaca int NOT NULL,
	PRIMARY KEY (paciente,data,hora)
);

CREATE TABLE alertas (
	id SERIAL PRIMARY KEY,
	tipo VARCHAR(50) NOT NULL,
	paciente int NOT NULL,
	data DATE NOT NULL,
	hora TIME NOT NULL,
	FOREIGN KEY(paciente,data,hora) REFERENCES dados(paciente,data,hora) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE paciente_alertas (
	id_paciente SERIAL PRIMARY KEY REFERENCES paciente(id_paciente) ON UPDATE CASCADE ON DELETE RESTRICT,
	min_pressao_arterial_max int DEFAULT 80,
	max_pressao_arterial_max int DEFAULT 140,
	min_pressao_arterial_min int DEFAULT 40,
	max_pressao_arterial_min int DEFAULT 90,
	min_freq_cardiaca int DEFAULT 60,
	max_freq_cardiaca int DEFAULT 100
);

--------------------------------------------------------------------------------
-- Cricao de tabelas de logs
--------------------------------------------------------------------------------
CREATE TABLE logs_utilizador(
id SERIAL PRIMARY KEY,
tipo char(1),
datalog TIMESTAMP,
utilizador varchar,
id_utilizador int,
password TEXT,
nome VARCHAR(100),
morada VARCHAR(200),
cod_postal CHAR(8),
localidade VARCHAR(100),
nacionalidade VARCHAR(50),
nif char(9),
cc varchar(12),
sexo SEXO,
	data_nascimento DATE,
contacto int,
mail VARCHAR(50),
funcao FUNCAO
);

CREATE TABLE logs_paciente (
id SERIAL PRIMARY KEY,
tipo char(1),
data_log TIMESTAMP,
utilizador varchar,
id_paciente int,
id_utilizador int,
em_tratamento SN,
	em_treino SN
);

CREATE TABLE logs_funcionario (
id SERIAL PRIMARY KEY,
tipo char(1),
data_log TIMESTAMP,
utilizador varchar,
id_funcionario int,
id_utilizador int,
data_admissao DATE,
activo SN
);

CREATE TABLE logs_tratamento (
id SERIAL PRIMARY KEY,
tipo char(1),
data_log TIMESTAMP,
utilizador varchar,
id_tratamento int,
desc_tratamento VARCHAR,
notas_tratamento VARCHAR,
data_inicio DATE,
data_fim DATE,
paciente int,
medico int
);

CREATE TABLE logs_treino (
id SERIAL PRIMARY KEY,
tipo char(1),
data_log TIMESTAMP,
utilizador varchar,
id_treino int,
desc_treino VARCHAR,
notas_treino VARCHAR,
data_inicio DATE,
data_fim DATE,
fisioterapeuta int,
tratamento int
);

CREATE TABLE logs_paciente_alertas (
id SERIAL PRIMARY KEY,
tipo char(1),
data_log TIMESTAMP,
utilizador varchar,
id_paciente int,
min_pressao_arterial_min int,
max_pressao_arterial_min int,
min_pressao_arterial_max int,
max_pressao_arterial_max int,
min_freq_cardiaca int,
max_freq_cardiaca int
);


--------------------------------------------------------------------------------
-- cricao de views
--------------------------------------------------------------------------------
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


--------------------------------------------------------------------------------
-- cricao de triggers
--------------------------------------------------------------------------------
--Trigger para alterar o estado da coluna em_tratamento quando 1 tratamento é criado
CREATE OR REPLACE FUNCTION func_emtratamento() RETURNS trigger AS $emtratamento$
	BEGIN
		UPDATE paciente SET em_tratamento='s' WHERE id_paciente = NEW.paciente;
		RETURN NULL;
	END;
$emtratamento$ LANGUAGE plpgsql;

CREATE TRIGGER tg_emtratamento AFTER INSERT ON tratamento
FOR EACH ROW EXECUTE PROCEDURE func_emtratamento();


--Trigger para alterar o estado da coluna em_treino quando 1 treino é criado
CREATE OR REPLACE FUNCTION func_emtreino() RETURNS trigger AS $emtreino$
	BEGIN
		UPDATE paciente SET em_treino='s' WHERE id_paciente = (SELECT paciente FROM tratamento WHERE id_tratamento=NEW.tratamento);
		RETURN NULL;
	END;
$emtreino$ LANGUAGE plpgsql;

CREATE TRIGGER tg_emtreino AFTER INSERT ON treino
FOR EACH ROW EXECUTE PROCEDURE func_emtreino();


--Trigger para inserir valores default de alerta quando 1 paciente é criado
CREATE OR REPLACE FUNCTION func_def_alertas() RETURNS trigger AS $def_alertas$
	BEGIN
		INSERT INTO paciente_alertas VALUES (NEW.id_paciente);
		RETURN NULL;
	END
$def_alertas$ LANGUAGE plpgsql;

CREATE TRIGGER tg_def_alertas AFTER INSERT ON paciente
FOR EACH ROW EXECUTE PROCEDURE func_def_alertas();


--Autenticação - devolve true se password validacao
--SELECT password = crypt('entered password', password) FROM tabela;

--Trigger para encriptar a password
CREATE OR REPLACE FUNCTION func_password() RETURNS TRIGGER AS $utilizador$
    BEGIN
		NEW.password := crypt(NEW.password, gen_salt('md5'));
		RETURN NEW;
    END;
$utilizador$ LANGUAGE plpgsql;

CREATE TRIGGER tg_password
BEFORE INSERT OR UPDATE OF password ON utilizador
FOR EACH ROW EXECUTE PROCEDURE func_password();

-- Trigger para gerar os alertas
CREATE OR REPLACE FUNCTION func_alertas() RETURNS TRIGGER AS $alertas$

	DECLARE min_pressao_arterial_min int := (SELECT min_pressao_arterial_min FROM paciente_alertas WHERE id_paciente = NEW.paciente);
	DECLARE max_pressao_arterial_min int := (SELECT max_pressao_arterial_min FROM paciente_alertas WHERE id_paciente = NEW.paciente);
	DECLARE min_pressao_arterial_max int := (SELECT min_pressao_arterial_max FROM paciente_alertas WHERE id_paciente = NEW.paciente);
	DECLARE max_pressao_arterial_max int := (SELECT max_pressao_arterial_max FROM paciente_alertas WHERE id_paciente = NEW.paciente);
	DECLARE min_freq_cardiaca int := (SELECT min_freq_cardiaca FROM paciente_alertas WHERE id_paciente = NEW.paciente);
	DECLARE max_freq_cardiaca int := (SELECT max_freq_cardiaca FROM paciente_alertas WHERE id_paciente = NEW.paciente);
	BEGIN
		IF (NEW.pressao_arterial_min <= 0) OR (NEW.pressao_arterial_max <= 0) OR (NEW.freq_cardiaca <=0) THEN
			INSERT INTO alertas VALUES (DEFAULT,'ALERTA: Técnico',NEW.paciente,NEW.data,NEW.hora);
			RETURN NEW;
		ELSEIF (NEW.pressao_arterial_min >= min_pressao_arterial_min) OR (NEW.pressao_arterial_min <= max_pressao_arterial_min)
			OR (NEW.pressao_arterial_max >= min_pressao_arterial_max) OR (NEW.pressao_arterial_max <= max_pressao_arterial_max) THEN
			INSERT INTO alertas VALUES (DEFAULT,'ALERTA: Pressão Arterial',NEW.paciente,NEW.data,NEW.hora);
			RETURN NEW;
		ELSIF (NEW.freq_cardiaca <= min_freq_cardiaca) OR (NEW.freq_cardiaca >= max_freq_cardiaca) THEN
			INSERT INTO alertas VALUES (DEFAULT,'ALERTA: Frequência Cardíaca',NEW.paciente,NEW.data,NEW.hora);
			RETURN NEW;
		END IF;
		RETURN NULL;
    END;
$alertas$ LANGUAGE plpgsql;

CREATE TRIGGER tg_alertas
AFTER INSERT OR UPDATE OR DELETE ON dados
FOR EACH ROW EXECUTE PROCEDURE func_alertas();


--Authoring (Criacao de logs)
CREATE OR REPLACE FUNCTION func_logs_utilizador() RETURNS TRIGGER AS $logs_utilizador$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO logs_utilizador SELECT nextval('logs_utilizador_id_seq'), 'D', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO logs_utilizador SELECT nextval('logs_utilizador_id_seq'), 'U', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'INSERT') THEN
	    INSERT INTO logs_utilizador SELECT nextval('logs_utilizador_id_seq'), 'I', now(), user, NEW.*;
            RETURN NEW;
        END IF;
        RETURN NULL;
    END;
$logs_utilizador$ LANGUAGE plpgsql;

CREATE TRIGGER tg_logs_utilizador
AFTER INSERT OR UPDATE OR DELETE ON utilizador
FOR EACH ROW EXECUTE PROCEDURE func_logs_utilizador();


CREATE OR REPLACE FUNCTION func_logs_paciente() RETURNS TRIGGER AS $logs_paciente$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO logs_paciente SELECT nextval('logs_paciente_id_seq'), 'D', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO logs_paciente SELECT nextval('logs_paciente_id_seq'), 'U', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'INSERT') THEN
			INSERT INTO logs_paciente SELECT nextval('logs_paciente_id_seq'), 'I', now(), user, NEW.*;
            RETURN NEW;
        END IF;
        RETURN NULL;
    END;
$logs_paciente$ LANGUAGE plpgsql;

CREATE TRIGGER tg_logs_paciente
AFTER INSERT OR UPDATE OR DELETE ON paciente
    FOR EACH ROW EXECUTE PROCEDURE func_logs_paciente();


CREATE OR REPLACE FUNCTION func_logs_funcionario() RETURNS TRIGGER AS $logs_funcionario$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO logs_funcionario SELECT nextval('logs_funcionario_id_seq'), 'D', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO logs_funcionario SELECT nextval('logs_funcionario_id_seq'), 'U', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'INSERT') THEN
	    INSERT INTO logs_funcionario SELECT nextval('logs_funcionario_id_seq'), 'I', now(), user, NEW.*;
            RETURN NEW;
        END IF;
        RETURN NULL;
    END;
$logs_funcionario$ LANGUAGE plpgsql;

CREATE TRIGGER tg_logs_funcionario
AFTER INSERT OR UPDATE OR DELETE ON funcionario
    FOR EACH ROW EXECUTE PROCEDURE func_logs_funcionario();


CREATE OR REPLACE FUNCTION func_logs_tratamento() RETURNS TRIGGER AS $logs_tratamento$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO logs_tratamento SELECT nextval('logs_tratamento_id_seq'), 'D', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO logs_tratamento SELECT nextval('logs_tratamento_id_seq'), 'U', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'INSERT') THEN
			INSERT INTO logs_tratamento SELECT nextval('logs_tratamento_id_seq'), 'I', now(), user, NEW.*;
            RETURN NEW;
        END IF;
        RETURN NULL;
    END;
$logs_tratamento$ LANGUAGE plpgsql;

CREATE TRIGGER tg_logs_tratamento
AFTER INSERT OR UPDATE OR DELETE ON tratamento
    FOR EACH ROW EXECUTE PROCEDURE func_logs_tratamento();


CREATE OR REPLACE FUNCTION func_logs_treino() RETURNS TRIGGER AS $logs_treino$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO logs_treino SELECT nextval('logs_treino_id_seq'), 'D', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO logs_treino SELECT nextval('logs_treino_id_seq'), 'U', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'INSERT') THEN
			INSERT INTO logs_treino SELECT nextval('logs_treino_id_seq'), 'I', now(), user, NEW.*;
            RETURN NEW;
        END IF;
        RETURN NULL;
    END;
$logs_treino$ LANGUAGE plpgsql;

CREATE TRIGGER tg_logs_treino
AFTER INSERT OR UPDATE OR DELETE ON treino
    FOR EACH ROW EXECUTE PROCEDURE func_logs_treino();


CREATE OR REPLACE FUNCTION func_logs_paciente_alertas() RETURNS TRIGGER AS $logs_paciente_alertas$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO logs_paciente_alertas SELECT nextval('logs_paciente_alertas_id_seq'), 'D', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO logs_paciente_alertas SELECT nextval('logs_paciente_alertas_id_seq'), 'U', now(), user, OLD.*;
            RETURN OLD;
        ELSIF (TG_OP = 'INSERT') THEN
			INSERT INTO logs_paciente_alertas SELECT nextval('logs_paciente_alertas_id_seq'), 'I', now(), user, NEW.*;
            RETURN NEW;
        END IF;
        RETURN NULL;
    END;
$logs_paciente_alertas$ LANGUAGE plpgsql;

CREATE TRIGGER tg_logs_paciente_alertas
AFTER INSERT OR UPDATE OR DELETE ON paciente_alertas
    FOR EACH ROW EXECUTE PROCEDURE func_logs_paciente_alertas();


--------------------------------------------------------------------------------
--cricao de roles
--------------------------------------------------------------------------------
CREATE ROLE Tecnico ENCRYPTED PASSWORD 'ptda4' LOGIN SUPERUSER CREATEDB CREATEROLE;
CREATE ROLE Administrador NOSUPERUSER NOINHERIT NOCREATEDB CREATEROLE;
CREATE ROLE Medico NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
CREATE ROLE Fisioterapeuta NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
CREATE ROLE Paciente NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;

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
