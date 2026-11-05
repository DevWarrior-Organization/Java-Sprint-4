-- ============================================
-- SCRIPT SQL - PLATAFORMA DE MOBILIDADE
-- Oracle Database (compativel com FIAP)
-- ============================================

-- Sequencia para Usuario
CREATE SEQUENCE seq_usuario START WITH 1 INCREMENT BY 1;

-- Sequencia para Viagem
CREATE SEQUENCE seq_viagem START WITH 1 INCREMENT BY 1;

-- Tabela de Usuarios
CREATE TABLE T_SP_USUARIO (
    id_usuario   NUMBER(10) PRIMARY KEY,
    nm_usuario   VARCHAR2(100) NOT NULL,
    nr_cpf       VARCHAR2(14) NOT NULL UNIQUE,
    qt_pontos    NUMBER(10,2) DEFAULT 0
);

-- Tabela de Viagens
CREATE TABLE T_SP_VIAGEM (
    id_viagem            NUMBER(10) PRIMARY KEY,
    id_usuario           NUMBER(10) NOT NULL,
    ds_tipo_transporte   VARCHAR2(20) NOT NULL,
    nr_distancia_km      NUMBER(10,2) NOT NULL,
    nr_pontos            NUMBER(10,2) NOT NULL,
    dt_viagem            DATE NOT NULL,
    CONSTRAINT fk_viagem_usuario FOREIGN KEY (id_usuario)
        REFERENCES T_SP_USUARIO(id_usuario)
        ON DELETE CASCADE
);

-- Indices para performance
CREATE INDEX idx_viagem_usuario ON T_SP_VIAGEM(id_usuario);
CREATE INDEX idx_viagem_data ON T_SP_VIAGEM(dt_viagem);
CREATE INDEX idx_usuario_cpf ON T_SP_USUARIO(nr_cpf);
