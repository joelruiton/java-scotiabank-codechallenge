# 🎓 Student Registry Service

Microservicio reactivo para gestión de alumnos.

**Stack:** Spring Boot 3.4.1 · WebFlux · R2DBC · H2 · Arquitectura Hexagonal · SOLID

---

## ✅ Requisitos previos

- **Java 17** instalado (verifica con: `java -version`)
- **IntelliJ IDEA** (Community o Ultimate)
- Conexión a internet (para que Gradle descargue dependencias la primera vez)

---

## 🚀 Abrir y levantar en IntelliJ IDEA

### Paso 1 — Abrir el proyecto
1. Abre IntelliJ IDEA
2. Click en **File → Open**
3. Selecciona la carpeta `student-registry-service` (donde está `build.gradle`)
4. Click **OK** → selecciona **Open as Project**

### Paso 2 — Importar Gradle
- IntelliJ detectará el `build.gradle` automáticamente
- Aparecerá una notificación en la esquina inferior derecha: haz click en **Load Gradle Project**
- Espera que descargue todas las dependencias (~1-2 minutos)

### Paso 3 — Configurar JDK 17
1. **File → Project Structure** (`Ctrl+Alt+Shift+S`)
2. En **SDK** selecciona Java 17
3. Si no lo tienes: **Add SDK → Download JDK → Version 17** (elige Eclipse Temurin o Amazon Corretto)
4. Click **Apply → OK**

### Paso 4 — Levantar la aplicación
**Opción A (recomendada):** Abre `StudentRegistryServiceApplication.java` y haz click en el ▶️ verde junto al método `main`

**Opción B:** Panel Gradle (derecha) → `Tasks → application → bootRun`

**Opción C:** Terminal integrada de IntelliJ:
```bash
./gradlew bootRun        # Linux/Mac
gradlew.bat bootRun      # Windows
```

### ✅ Verificar que levantó
Busca en la consola:
```
Started StudentRegistryServiceApplication in X seconds
Netty started on port 8080
```

---

## 📡 Endpoints y pruebas con Postman

### BASE URL: `http://localhost:8080`

---

### 1. Grabar un alumno
**`POST /api/v1/students`**

| Campo | Valor |
|-------|-------|
| Method | POST |
| URL | `http://localhost:8080/api/v1/students` |
| Headers | `Content-Type: application/json` |

**Body (alumno activo):**
```json
{
    "id": 1,
    "nombre": "Juan",
    "apellido": "Perez",
    "estado": "ACTIVO",
    "edad": 22
}
```
✅ Respuesta esperada: **204 No Content** (body vacío)

**Body (alumno inactivo):**
```json
{
    "id": 2,
    "nombre": "Maria",
    "apellido": "Lopez",
    "estado": "INACTIVO",
    "edad": 30
}
```
✅ Respuesta esperada: **204 No Content**

**Body (otro alumno activo):**
```json
{
    "id": 3,
    "nombre": "Carlos",
    "apellido": "Gutierrez",
    "estado": "ACTIVO",
    "edad": 25
}
```
✅ Respuesta esperada: **204 No Content**

---

### 2. Grabar con ID duplicado (error esperado)
Vuelve a enviar el mismo body con `id: 1`.

❌ Respuesta esperada: **409 Conflict**
```json
{
    "error": "No se pudo realizar la grabacion. Ya existe un alumno registrado con el id: 1"
}
```

---

### 3. Grabar con datos inválidos (error esperado)
**Body con estado incorrecto:**
```json
{
    "id": 10,
    "nombre": "X",
    "apellido": "Lopez",
    "estado": "SUSPENDIDO",
    "edad": 22
}
```
❌ Respuesta esperada: **400 Bad Request**
```json
{
    "errors": {
        "nombre": "El campo 'nombre' debe tener entre 2 y 100 caracteres",
        "estado": "El campo 'estado' solo acepta: ACTIVO o INACTIVO"
    }
}
```

---

### 4. Obtener alumnos activos
**`GET /api/v1/students/active`**

| Campo | Valor |
|-------|-------|
| Method | GET |
| URL | `http://localhost:8080/api/v1/students/active` |

✅ Respuesta esperada: **200 OK**
```json
[
    {
        "id": 1,
        "nombre": "Juan",
        "apellido": "Perez",
        "estado": "ACTIVO",
        "edad": 22
    },
    {
        "id": 3,
        "nombre": "Carlos",
        "apellido": "Gutierrez",
        "estado": "ACTIVO",
        "edad": 25
    }
]
```
> **Nota:** Solo aparecen los alumnos con estado `ACTIVO`. Maria Lopez (id=2) no aparece porque es `INACTIVO`.

---

## 🧪 Ejecutar tests

```bash
./gradlew test           # Linux/Mac
gradlew.bat test         # Windows
```

O en IntelliJ: clic derecho sobre `src/test` → **Run 'All Tests'**

Los reportes de cobertura se generan en: `build/reports/tests/test/index.html`

---

## 🗂️ Estructura del proyecto

```
src/main/java/com/academy/studentregistry/
├── StudentRegistryServiceApplication.java
├── domain/                          ← Núcleo del negocio (sin dependencias externas)
│   ├── model/Student.java
│   ├── exception/StudentAlreadyExistsException.java
│   └── port/
│       ├── in/StudentUseCase.java   ← Puerto de entrada
│       └── out/StudentRepositoryPort.java ← Puerto de salida
├── application/
│   └── service/StudentService.java  ← Orquesta el dominio
└── infrastructure/
    ├── adapter/
    │   ├── in/web/
    │   │   ├── StudentController.java
    │   │   └── dto/{StudentRequest, StudentResponse}.java
    │   └── out/persistence/
    │       ├── StudentEntity.java
    │       ├── StudentR2dbcRepository.java
    │       └── StudentRepositoryAdapter.java
    └── config/GlobalExceptionHandler.java
```

---

## 📋 Resumen de respuestas HTTP

| Endpoint | Caso | HTTP |
|----------|------|------|
| POST /api/v1/students | Grabación exitosa | 204 No Content |
| POST /api/v1/students | ID duplicado | 409 Conflict |
| POST /api/v1/students | Campos inválidos | 400 Bad Request |
| GET /api/v1/students/active | Éxito con datos | 200 OK |
| GET /api/v1/students/active | Sin alumnos activos | 200 OK (array vacío) |
