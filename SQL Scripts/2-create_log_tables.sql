--DROP TABLE logs_utilizador,logs_paciente,logs_funcionario,logs_tratamento,logs_treino,logs_paciente_alertas;
		
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

