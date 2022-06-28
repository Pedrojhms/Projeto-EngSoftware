DROP DATABASE IF EXISTS cadastro;

CREATE DATABASE cadastro;

\c cadastro

CREATE TABLE usuario
(
    id bigserial NOT NULL PRIMARY KEY,
    nome character varying(150),
    email character varying(100),
    senha character varying(255),
    cep character varying(8),
    logradouro character varying(100),
    bairro character varying(50),
    cidade character varying(30),
    estado character varying(20),
    numero smallint,
    complemento character varying(50),
    data_cadastro date default now()
);
