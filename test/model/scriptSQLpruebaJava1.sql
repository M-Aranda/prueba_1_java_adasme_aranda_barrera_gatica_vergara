CREATE DATABASE cafesoft; -- DROP DATABASE cafesoft

USE cafesoft;


CREATE TABLE tipo_vivienda(
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE tipo_usuario(
    id INT AUTO_INCREMENT,
    nombre VARCHAR(20),
    PRIMARY KEY(id)
);

CREATE TABLE cliente(-- DROP TABLE cliente
    run VARCHAR(15) UNIQUE,
    nombre VARCHAR(50),
    sueldo INT,
    PRIMARY KEY(run)
);

CREATE TABLE disponibilidad_vivienda(
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE vivienda(
    n_rol INT,
    tipo_fk INT,
    dis_vivienda INT,
    precio_arriendo INT,
    precio_venta INT,
    cant_banios INT,
    cant_piezas INT,
    direccion VARCHAR(50),
    condicion BOOLEAN,
    PRIMARY KEY(n_rol),
    FOREIGN KEY(tipo_fk) REFERENCES tipo_vivienda(id),
    FOREIGN KEY(dis_vivienda) REFERENCES disponibilidad_vivienda(id)
);-- SELECT * FROM vivienda
/* ****************TEST INSERTS**********************

INSERT INTO vivienda VALUES (1111,1,1,280000,56000000,2,3,'Agustin Palominos #429',1);

   ****************TEST INSERTS**********************/

CREATE TABLE usuario(-- DROP TABLE usuario
    run VARCHAR(15),
    nombre VARCHAR(50),
    tipo_fk INT,
    PRIMARY KEY(run),
    FOREIGN KEY(tipo_fk) REFERENCES tipo_usuario(id)
);-- SELECT * FROM usuario

CREATE TABLE log( -- DROP TABLE log
    id INT AUTO_INCREMENT,
    descripcion VARCHAR(50),
    fecha DATETIME,
    usuario_fk VARCHAR(15),
    PRIMARY KEY (id),
    FOREIGN KEY(usuario_fk) REFERENCES usuario(run)
);-- SELECT * FROM log

CREATE TABLE contrato(
    id INT AUTO_INCREMENT,
    cliente_fk VARCHAR(15),
    usuario_fk VARCHAR(15),
    vivienda_fk INT,
    fecha DATE,
    PRIMARY KEY(id),
    FOREIGN KEY(cliente_fk) REFERENCES cliente(run),
    FOREIGN KEY(usuario_fk) REFERENCES usuario(run),
    FOREIGN KEY(vivienda_fk) REFERENCES vivienda(n_rol)
);



INSERT INTO tipo_usuario VALUES (NULL,'Admin');
INSERT INTO tipo_usuario VALUES (NULL,'Vendedor');

INSERT INTO tipo_vivienda VALUES (NULL,'Casa');
INSERT INTO tipo_vivienda VALUES (NULL,'Departamento');

INSERT INTO disponibilidad_vivienda VALUES (NULL, 'Arrendada');
INSERT INTO disponibilidad_vivienda VALUES (NULL, 'Vendida');
INSERT INTO disponibilidad_vivienda VALUES (NULL, 'Disponible');

INSERT INTO usuario VALUES ('11-1','admin',1);