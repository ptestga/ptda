--/*
DROP TRIGGER tg_def_alertas ON paciente;
DROP TRIGGER tg_alertas ON dados;
DROP TRIGGER tg_emtratamento ON tratamento;
DROP TRIGGER tg_emtreino ON treino;
DROP TRIGGER tg_password ON utilizador;
DROP TRIGGER tg_logs_utilizador ON utilizador;
DROP TRIGGER tg_logs_paciente ON paciente;
DROP TRIGGER tg_logs_funcionario ON funcionario;
DROP TRIGGER tg_logs_tratamento ON tratamento;
DROP TRIGGER tg_logs_treino ON treino;
DROP TRIGGER tg_logs_paciente_alertas ON paciente_alertas;
--*/

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
