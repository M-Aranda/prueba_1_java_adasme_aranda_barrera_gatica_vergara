USE cafesoft;

DELIMITER $$
CREATE FUNCTION is_usado (var BOOLEAN) RETURNS VARCHAR(50)
BEGIN
	
    IF var THEN
         RETURN 'Usada';    
    END IF;    
    RETURN 'Nueva';
END $$
DELIMITER ;
-- DROP FUNCTION crear_vendedor

CREATE VIEW vista_viviendas_disponibles AS -- DROP view vista_viviendas_disponibles
	SELECT 
    v.n_rol,
    t.nombre AS 'tipo',
    d.nombre AS 'es_disponible',
    v.precio_arriendo,
    v.precio_venta,
    v.cant_banios,
    v.cant_piezas,
    v.direccion,
    (SELECT is_usado(v.condicion)) AS 'condicion'
    FROM
    vivienda v
    INNER JOIN tipo_vivienda t ON t.id = v.tipo_fk
    INNER JOIN disponibilidad_vivienda d ON d.id = v.dis_vivienda
    WHERE
    d.nombre = 'Disponible';    
    
    
 --   SELECT * FROM vista_viviendas_disponibles
 -- SELECT * FROM vista_viviendas_disponibles  WHERE (tipo='Casa' OR tipo='Departamento') AND (condicion='Nueva' OR condicion='Usada') ORDER BY precio_venta DESC;

CREATE VIEW vista_estadisticas_viviendas AS -- DROP view vista_estadisticas_viviendas
	SELECT 
    v.n_rol,
    t.nombre AS 'tipo',
    d.nombre AS 'es_disponible',
    v.precio_arriendo,
    v.precio_venta,
    v.cant_banios,
    v.cant_piezas,
    v.direccion,
    c.cliente_fk,
    c.usuario_fk,
    c.fecha
    FROM
    vivienda v
    INNER JOIN tipo_vivienda t ON t.id = v.tipo_fk
    INNER JOIN disponibilidad_vivienda d ON d.id = v.dis_vivienda
    INNER JOIN contrato c ON c.vivienda_fk = v.n_rol
    WHERE
    d.nombre = 'Arrendada' OR d.nombre ='Vendida';

-- SELECT * FROM vista_estadisticas_viviendas;

CREATE VIEW vista_logs AS
    SELECT
        l.fecha,
        l.descripcion,
        l.usuario_fk,
        u.nombre
    FROM
        log l
        INNER JOIN usuario u ON u.run = l.usuario_fk;

-- SELECT * FROM vista_logs

DELIMITER $$
CREATE FUNCTION crear_vendedor (nuevo_run VARCHAR(15),nuevo_nombre VARCHAR(50), run_user VARCHAR (50)) RETURNS INT
BEGIN
	DECLARE existe_run INT;
    
    SET existe_run = (SELECT COUNT(*) FROM usuario WHERE nuevo_run = run);    
    
    IF existe_run = 0 THEN
    
    INSERT INTO usuario VALUES (nuevo_run,nuevo_nombre,2);
    INSERT INTO log VALUES (NULL,CONCAT('Se registra al vendedor de run ',nuevo_run),now(),run_user);
        
    RETURN 1;
    
    END IF;    
    RETURN 0;
END $$
DELIMITER ;

-- SELECT crear_vendedor ('22-2','Alexis Zuniga','11-1');

-- DROP FUNCTION crear_cliente

DELIMITER $$
CREATE FUNCTION crear_cliente (nuevo_run VARCHAR(15),nuevo_nombre VARCHAR(50),nuevo_sueldo INT, run_user VARCHAR (50)) RETURNS INT
BEGIN
	DECLARE existe_run INT;
    
    SET existe_run = (SELECT COUNT(*) FROM cliente WHERE nuevo_run = run);    
    IF existe_run = 0 THEN
    
    INSERT INTO cliente VALUES (nuevo_run,nuevo_nombre,nuevo_sueldo);
    INSERT INTO log VALUES (NULL,CONCAT('Se registra al cliente de run ',nuevo_run),now(),run_user);
        
    RETURN existe_run;
    
    END IF;    
    RETURN existe_run;
END $$
DELIMITER ;


-- SELECT crear_cliente ('33-3','Marcelo Aranda',250000,'22-2');

-- DROP FUNCTION crear_vivienda
-- INSERT INTO vivienda VALUES (1111,1,1,280000,56000000,2,3,'Agustin Palominos #429',1);
-- INSERT INTO vivienda VALUES (1211,1,3,280000,56000000,2,3,'Agustin Palominos #429',1);
-- INSERT INTO vivienda VALUES (1311,1,2,280000,56000000,2,3,'Agustin Palominos #429',1);
-- INSERT INTO vivienda VALUES (3000,2,3,280000,56000000,2,3,'Agustin Palominos #429',1);

DELIMITER $$
CREATE FUNCTION crear_vivienda (nuevo_rol INT,set_tipo INT,disponibilidad INT,precio_a INT,precio_v INT,banios INT,piezas INT, new_direccion VARCHAR (50),new_condicion BOOLEAN, run_user VARCHAR (15)) RETURNS INT
BEGIN
	DECLARE existe_rol BOOLEAN;
    DECLARE rol VARCHAR(50);
    
    SET existe_rol = (SELECT COUNT(*) FROM vivienda WHERE nuevo_rol = n_rol);
    
    IF existe_rol = 0 THEN
    
    INSERT INTO vivienda VALUES (nuevo_rol,set_tipo,disponibilidad,precio_a,precio_v,banios,piezas,new_direccion,new_condicion);    
    INSERT INTO log VALUES (NULL,CONCAT('Se registra la vivienda de nº de rol ',nuevo_rol),now(),run_user);
        
    RETURN 1;
    
    END IF;    
    RETURN 0;
END $$
DELIMITER ;

-- SELECT crear_vivienda (333212,1,3,250000,98000000,3,12,'Aasdddddddddddasasrga #0149',1,'11-1');
-- SELECT crear_vivienda (123,1,3,250000,98000000,3,12,'Aasdddddddddddasasrga #0149',1,'11-1');
-- SELECT crear_vivienda (124,1,3,250344,98000000,3,12,'Aasdddddddddddasasrga #0149',1,'11-1');
-- SELECT crear_vivienda (4567,2,1,260002,98000000,3,12,'jashajdjadja',2,'22-2');


DELIMITER $$
CREATE PROCEDURE nuevo_log (detalle VARCHAR(200),run_user VARCHAR(200))
BEGIN   
    INSERT INTO log VALUES (NULL,detalle,now(),run_user);
END $$
DELIMITER ;

--CALL nuevo_log('admin ha restaurado desde backup 2142018 211548.sql','11-1');



DELIMITER $$
CREATE PROCEDURE crear_contrato (nRol INT, tipoContrato VARCHAR (50), runCliente VARCHAR(50), runVendedor VARCHAR (50))
BEGIN  
    DECLARE detalle VARCHAR(200);
    DECLARE nomCliente VARCHAR (200);
	DECLARE disVi INT;
	DECLARE nombCont VARCHAR (50);
    
    IF(tipoContrato='Vendida') THEN
    
    SET nombCont=('venta');
    
    END IF;
    
    IF(tipoContrato='Arrendada') THEN
    
    SET nombCont=('arriendo');
    
    END IF;
   
    SET nomCliente=(SELECT nombre FROM cliente WHERE run=runCliente);
    SET detalle=CONCAT('Se crea contrato de ', nombCont,' de la vivienda de numero de rol ',nRol,' para el/la cliente de run ',runCliente,' conocido/a como ',nomCliente);
	
    SET disVi=(SELECT id FROM disponibilidad_vivienda WHERE nombre=tipoContrato); 
   
   
   
	UPDATE vivienda SET dis_vivienda=disVi WHERE n_rol=nRol;
    INSERT INTO contrato VALUES(NULL, runCliente, runVendedor, nRol, NOW());
    INSERT INTO log VALUES (NULL,detalle,NOW(),runVendedor);    
   
END $$
DELIMITER ;
-- DROP PROCEDURE crear_contrato;

DELIMITER //
CREATE PROCEDURE borrarUsuario (runDelete VARCHAR(15))
BEGIN
	SET SQL_SAFE_UPDATES = 0;
	DELETE FROM log WHERE usuario_fk = runDelete;
    DELETE FROM contrato WHERE usuario_fk = runDelete;
    DELETE FROM usuario WHERE run = runDelete;
	SET SQL_SAFE_UPDATES = 1;
END //
DELIMITER ; -- CALL borrarUsuario();

CALL nuevo_log('holaaaah','11-1');

CREATE VIEW estat_default AS -- DROP VIEW estat_default
SELECT
(SELECT COUNT(*) FROM vivienda WHERE dis_vivienda != '3' AND tipo_fk = '1'),
(SELECT COUNT(*) FROM vivienda WHERE dis_vivienda != '3' AND tipo_fk = '2'),
(SELECT COUNT(*) FROM vivienda WHERE dis_vivienda != '3');

SELECT COUNT(*) FROM contrato c INNER JOIN vivienda v ON v.n_rol = c.vivienda_fk WHERE v.dis_vivienda = '1' OR v.dis_vivienda = '2' AND c.fecha BETWEEN '2018-05-03' AND '2018-05-25';

DELIMITER //
CREATE PROCEDURE estat_por_fechas (fecha1 DATE,fecha2 DATE)-- DROP PROCEDURE estat_por_fechas
BEGIN
    SELECT
        (SELECT COUNT(*) FROM contrato c INNER JOIN vivienda v ON v.n_rol = c.vivienda_fk WHERE v.dis_vivienda != '3' AND v.tipo_fk = '1' AND c.fecha BETWEEN fecha1 AND fecha2),
        (SELECT COUNT(*) FROM contrato c INNER JOIN vivienda v ON v.n_rol = c.vivienda_fk WHERE v.dis_vivienda != '3' AND v.tipo_fk = '2' AND c.fecha BETWEEN fecha1 AND fecha2),
        (SELECT COUNT(*) FROM contrato c INNER JOIN vivienda v ON v.n_rol = c.vivienda_fk WHERE (v.dis_vivienda != '3' OR v.dis_vivienda = '2') AND c.fecha BETWEEN fecha1 AND fecha2);
END //
DELIMITER ; -- CALL estat_por_fechas();


    
DELIMITER // -- NO PREGUNTE... SOLO QUIERALO, HACE LO QUE DEBE XD (SON SOLO 3 IF ANIDADOS)
CREATE PROCEDURE vendedores_por_fechas (fecha1 DATE,fecha2 DATE,top INT,filtro INT)-- DROP PROCEDURE vendedores_por_fechas
BEGIN    
    IF(fecha1 = '1995-05-01') THEN
		IF(top = 0) THEN
			IF(filtro = 0) THEN
					SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%') AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY contratos DESC;
				ELSE
                SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%') AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY login DESC;
				END IF;
		ELSE
        IF(filtro = 0) THEN
					SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%') AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY login DESC LIMIT top;
				ELSE
                SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%') AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY contratos DESC LIMIT top;
				END IF;
				
        END IF;
    ELSE
		IF(top = 0) THEN
			IF(filtro = 0) THEN
					SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha BETWEEN fecha1 AND fecha2) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%' AND fecha BETWEEN fecha1 AND fecha2) AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY contratos DESC;
				ELSE
					SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha BETWEEN fecha1 AND fecha2) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%' AND fecha BETWEEN fecha1 AND fecha2) AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY login DESC;
                END IF;
		ELSE
			IF(filtro = 0) THEN
					SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha BETWEEN fecha1 AND fecha2) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%' AND fecha BETWEEN fecha1 AND fecha2) AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY login DESC LIMIT top;
				ELSE
					SELECT 
					u.run,
					u.nombre,
					(SELECT COUNT(*) FROM contrato  WHERE usuario_fk = u.run AND fecha BETWEEN fecha1 AND fecha2) AS 'contratos',
					(SELECT COUNT(*) FROM log WHERE usuario_fk = u.run AND descripcion LIKE '%Ha iniciado sesion%' AND fecha BETWEEN fecha1 AND fecha2) AS 'login'
					FROM
					usuario u WHERE u.tipo_fk = 2 ORDER BY contratos DESC LIMIT top;
                END IF;
        END IF;
	END IF;
END //
DELIMITER ;
 -- CALL vendedores_por_fechas('2018-05-01','2018-05-23','10','0');