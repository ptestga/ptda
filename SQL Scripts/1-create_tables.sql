--DROP VIEW vw_paciente,vw_funcionario;
--DROP TABLE paciente_alertas,dados, treino, tratamento;
--DROP TABLE funcionario,paciente,utilizador;
--DROP TABLE logs_utilizador,logs_paciente,logs_funcionario,logs_tratamento,logs_treino;
--DROP DOMAIN SN,FUNCAO,SEXO;

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
