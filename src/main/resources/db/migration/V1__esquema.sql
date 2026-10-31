-- Esquema inicial. Rediseño del de la versión de escritorio (TFG):
--  * contraseñas con hash BCrypt y rol en vez de un booleano "administrador"
--  * el coche apunta al modelo (y el modelo a su marca): sin la marca duplicada en coche
--  * motor y cambio como valores cerrados (CHECK) en vez de tablas sueltas
--  * favoritos sin duplicados (PK compuesta) y una compra por coche (UNIQUE)
--  * FK con ON UPDATE/DELETE CASCADE donde tiene sentido
-- SQL compatible con MySQL/MariaDB y con H2 en modo MySQL (tests).

CREATE TABLE usuarios (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario   VARCHAR(50)  NOT NULL,
    contrasena_hash  VARCHAR(100) NOT NULL,
    nombre           VARCHAR(50)  NOT NULL,
    apellido1        VARCHAR(50)  NOT NULL,
    apellido2        VARCHAR(50),
    correo           VARCHAR(100) NOT NULL,
    rol              VARCHAR(20)  NOT NULL DEFAULT 'CLIENTE',
    imagen           VARCHAR(255),
    fecha_alta       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_usuarios_nombre_usuario UNIQUE (nombre_usuario),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo),
    CONSTRAINT ck_usuarios_rol CHECK (rol IN ('CLIENTE', 'ADMIN'))
);

CREATE TABLE marcas (
    nombre  VARCHAR(50) PRIMARY KEY,
    imagen  VARCHAR(255)
);

CREATE TABLE modelos (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(50)  NOT NULL,
    marca   VARCHAR(50)  NOT NULL,
    imagen  VARCHAR(255),
    CONSTRAINT uk_modelos_marca_nombre UNIQUE (marca, nombre),
    CONSTRAINT fk_modelos_marca FOREIGN KEY (marca) REFERENCES marcas (nombre)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE colores (
    id          VARCHAR(5)  PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL,
    metalizado  BOOLEAN     NOT NULL
);

CREATE TABLE coches (
    bastidor           VARCHAR(17)   PRIMARY KEY,
    matricula          VARCHAR(10)   NOT NULL,
    modelo_id          INT           NOT NULL,
    fecha_fabricacion  DATE          NOT NULL,
    motor              VARCHAR(20)   NOT NULL,
    color_id           VARCHAR(5)    NOT NULL,
    cilindrada         DECIMAL(3, 1) NOT NULL,
    potencia_cv        INT           NOT NULL,
    puertas            INT           NOT NULL,
    plazas             INT           NOT NULL,
    cambio             VARCHAR(20)   NOT NULL,
    precio             DECIMAL(10, 2) NOT NULL,
    estado             VARCHAR(20)   NOT NULL DEFAULT 'DISPONIBLE',
    creado_por         INT,
    fecha_alta         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_coches_matricula UNIQUE (matricula),
    CONSTRAINT fk_coches_modelo FOREIGN KEY (modelo_id) REFERENCES modelos (id),
    CONSTRAINT fk_coches_color FOREIGN KEY (color_id) REFERENCES colores (id),
    CONSTRAINT fk_coches_creado_por FOREIGN KEY (creado_por) REFERENCES usuarios (id) ON DELETE SET NULL,
    CONSTRAINT ck_coches_motor CHECK (motor IN ('GASOLINA', 'DIESEL', 'HIBRIDO', 'ELECTRICO')),
    CONSTRAINT ck_coches_cambio CHECK (cambio IN ('MANUAL', 'AUTOMATICO')),
    CONSTRAINT ck_coches_estado CHECK (estado IN ('DISPONIBLE', 'COMPRADO')),
    CONSTRAINT ck_coches_valores CHECK (precio >= 0 AND potencia_cv > 0 AND puertas > 0 AND plazas > 0)
);

CREATE INDEX idx_coches_estado ON coches (estado);

CREATE TABLE favoritos (
    usuario_id  INT         NOT NULL,
    bastidor    VARCHAR(17) NOT NULL,
    fecha       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, bastidor),
    CONSTRAINT fk_favoritos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT fk_favoritos_coche FOREIGN KEY (bastidor) REFERENCES coches (bastidor)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE compras (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    bastidor    VARCHAR(17)    NOT NULL,
    usuario_id  INT            NOT NULL,
    precio      DECIMAL(10, 2) NOT NULL,
    fecha       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_compras_bastidor UNIQUE (bastidor),
    CONSTRAINT fk_compras_coche FOREIGN KEY (bastidor) REFERENCES coches (bastidor) ON UPDATE CASCADE,
    CONSTRAINT fk_compras_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);
