-- =====================================================================
-- ProsperApp - Script de creacion de tablas (PostgreSQL)
-- =====================================================================
-- Orden de creacion respetando las dependencias de llaves foraneas.
-- =====================================================================

DROP TABLE IF EXISTS fragmento_codigo CASCADE;
DROP TABLE IF EXISTS nota_diseno CASCADE;
DROP TABLE IF EXISTS subtarea CASCADE;
DROP TABLE IF EXISTS decision_tecnica CASCADE;
DROP TABLE IF EXISTS descripcion_detallada CASCADE;
DROP TABLE IF EXISTS funcionalidad CASCADE;
DROP TABLE IF EXISTS seccion CASCADE;
DROP TABLE IF EXISTS proyecto_colaborador CASCADE;
DROP TABLE IF EXISTS proyecto CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- ---------------------------------------------------------------------
-- usuario
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id_usuario      SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    correo          VARCHAR(100) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    fecha_registro  DATE NOT NULL DEFAULT CURRENT_DATE
);

-- ---------------------------------------------------------------------
-- proyecto
-- ---------------------------------------------------------------------
CREATE TABLE proyecto (
    id_proyecto     SERIAL PRIMARY KEY,
    id_usuario      INTEGER NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(500),
    fecha_creacion  DATE NOT NULL DEFAULT CURRENT_DATE,
    estado          VARCHAR(20) NOT NULL DEFAULT 'activo'
                        CHECK (estado IN ('activo', 'pausado', 'finalizado', 'archivado'))
);

-- ---------------------------------------------------------------------
-- proyecto_colaborador (tabla intermedia N:M usuario <-> proyecto)
-- ---------------------------------------------------------------------
CREATE TABLE proyecto_colaborador (
    id_proyecto     INTEGER NOT NULL REFERENCES proyecto(id_proyecto) ON DELETE CASCADE,
    id_usuario      INTEGER NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    rol             VARCHAR(20) NOT NULL DEFAULT 'colaborador'
                        CHECK (rol IN ('propietario', 'colaborador', 'lector')),
    fecha_ingreso   DATE NOT NULL DEFAULT CURRENT_DATE,
    PRIMARY KEY (id_proyecto, id_usuario)
);

-- ---------------------------------------------------------------------
-- seccion  (min 1, max 6 por proyecto -> ver trigger al final)
-- ---------------------------------------------------------------------
CREATE TABLE seccion (
    id_seccion      SERIAL PRIMARY KEY,
    id_proyecto     INTEGER NOT NULL REFERENCES proyecto(id_proyecto) ON DELETE CASCADE,
    nombre          VARCHAR(50) NOT NULL,
    orden           INTEGER NOT NULL
);

-- ---------------------------------------------------------------------
-- funcionalidad
-- ---------------------------------------------------------------------
CREATE TABLE funcionalidad (
    id_funcionalidad  SERIAL PRIMARY KEY,
    id_seccion        INTEGER NOT NULL REFERENCES seccion(id_seccion) ON DELETE CASCADE,
    titulo            VARCHAR(100) NOT NULL,
    historia_usuario  VARCHAR(500),
    prioridad         INTEGER NOT NULL DEFAULT 3 CHECK (prioridad BETWEEN 1 AND 5),
    fecha_creacion    DATE NOT NULL DEFAULT CURRENT_DATE
);

-- ---------------------------------------------------------------------
-- descripcion_detallada
-- ---------------------------------------------------------------------
CREATE TABLE descripcion_detallada (
    id_descripcion    SERIAL PRIMARY KEY,
    id_funcionalidad  INTEGER NOT NULL REFERENCES funcionalidad(id_funcionalidad) ON DELETE CASCADE,
    contenido         VARCHAR(1000) NOT NULL,
    fecha_creacion    DATE NOT NULL DEFAULT CURRENT_DATE
);

-- ---------------------------------------------------------------------
-- decision_tecnica
-- ---------------------------------------------------------------------
CREATE TABLE decision_tecnica (
    id_decision       SERIAL PRIMARY KEY,
    id_funcionalidad  INTEGER NOT NULL REFERENCES funcionalidad(id_funcionalidad) ON DELETE CASCADE,
    titulo            VARCHAR(100) NOT NULL,
    justificacion     VARCHAR(1000),
    fecha_creacion    DATE NOT NULL DEFAULT CURRENT_DATE
);

-- ---------------------------------------------------------------------
-- subtarea
-- ---------------------------------------------------------------------
CREATE TABLE subtarea (
    id_subtarea       SERIAL PRIMARY KEY,
    id_funcionalidad  INTEGER NOT NULL REFERENCES funcionalidad(id_funcionalidad) ON DELETE CASCADE,
    descripcion       VARCHAR(255) NOT NULL,
    completada        BOOLEAN NOT NULL DEFAULT FALSE
);

-- ---------------------------------------------------------------------
-- nota_diseno
-- ---------------------------------------------------------------------
CREATE TABLE nota_diseno (
    id_nota           SERIAL PRIMARY KEY,
    id_funcionalidad  INTEGER NOT NULL REFERENCES funcionalidad(id_funcionalidad) ON DELETE CASCADE,
    contenido         VARCHAR(1000) NOT NULL,
    fecha_creacion    DATE NOT NULL DEFAULT CURRENT_DATE
);

-- ---------------------------------------------------------------------
-- fragmento_codigo
-- ---------------------------------------------------------------------
CREATE TABLE fragmento_codigo (
    id_fragmento      SERIAL PRIMARY KEY,
    id_funcionalidad  INTEGER NOT NULL REFERENCES funcionalidad(id_funcionalidad) ON DELETE CASCADE,
    lenguaje          VARCHAR(30) NOT NULL,
    codigo            VARCHAR(2000) NOT NULL
);

-- ---------------------------------------------------------------------
-- Indices sobre llaves foraneas (mejoran los JOIN)
-- ---------------------------------------------------------------------
CREATE INDEX idx_proyecto_usuario            ON proyecto(id_usuario);
CREATE INDEX idx_proyecto_colab_usuario       ON proyecto_colaborador(id_usuario);
CREATE INDEX idx_seccion_proyecto             ON seccion(id_proyecto);
CREATE INDEX idx_funcionalidad_seccion        ON funcionalidad(id_seccion);
CREATE INDEX idx_descripcion_funcionalidad    ON descripcion_detallada(id_funcionalidad);
CREATE INDEX idx_decision_funcionalidad       ON decision_tecnica(id_funcionalidad);
CREATE INDEX idx_subtarea_funcionalidad       ON subtarea(id_funcionalidad);
CREATE INDEX idx_nota_funcionalidad           ON nota_diseno(id_funcionalidad);
CREATE INDEX idx_fragmento_funcionalidad      ON fragmento_codigo(id_funcionalidad);

-- ---------------------------------------------------------------------
-- Regla de negocio: cada proyecto debe tener entre 1 y 6 secciones.
-- No se puede expresar con CHECK (depende de otras filas), se usa un
-- trigger que valida el limite maximo al insertar una nueva seccion.
-- El minimo de 1 se debe garantizar desde la aplicacion al crear el
-- proyecto (crear la primera seccion en la misma transaccion).
-- ---------------------------------------------------------------------
CREATE OR REPLACE FUNCTION validar_max_secciones()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM seccion WHERE id_proyecto = NEW.id_proyecto) >= 6 THEN
        RAISE EXCEPTION 'Un proyecto no puede tener mas de 6 secciones (id_proyecto=%)', NEW.id_proyecto;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_max_secciones
BEFORE INSERT ON seccion
FOR EACH ROW EXECUTE FUNCTION validar_max_secciones();
