CREATE TABLE IF NOT EXISTS students (
    id        BIGINT       PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    apellido  VARCHAR(100) NOT NULL,
    estado    VARCHAR(10)  NOT NULL,
    edad      INT          NOT NULL
);
