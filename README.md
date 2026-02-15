# 🚀 Engine Data Processing Application

Una aplicación **Spring Boot** para procesamiento masivo y eficiente de datos de transacciones bancarias usando **Spring Batch**, con capacidad de leer archivos CSV y almacenarlos en **PostgreSQL**.

---

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Características](#características)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Herramientas y Tecnologías](#herramientas-y-tecnologías)
- [Arquitectura](#arquitectura)
- [Performance](#performance)
- [Logs y Monitoreo](#logs-y-monitoreo)
- [Solución de Problemas](#solución-de-problemas)
- [Contribución](#contribución)

---

## 📖 Descripción General

**Engine Data Processing Application** es una solución de procesamiento batch diseñada para:

- ✅ Leer masivamente datos de transacciones desde archivos CSV
- ✅ Procesar y validar cada transacción en tiempo real
- ✅ Almacenar los datos en una base de datos PostgreSQL
- ✅ Medir performance y throughput de manera automática
- ✅ Manejar errores y transacciones de forma segura
- ✅ Proporcionar estadísticas detalladas del procesamiento

**Ideal para**: Importación de datos financieros, procesamiento de grandes volúmenes de transacciones, ETL (Extract, Transform, Load) de datos bancarios.

---

## ⭐ Características

### 🔧 Procesamiento Batch
- **Spring Batch** para procesamiento eficiente de grandes volúmenes
- **Chunk processing** configurable (50,000 registros por defecto)
- **Transacciones ACID** con commits automáticos
- **Manejo de errores** y registros filtrados

### 📊 Monitoreo y Reporting
- **Estadísticas en tiempo real** de procesamiento
- **Throughput** (registros/segundo)
- **Tiempo promedio por registro**
- **Desglose de errores** y registros filtrados
- **Timestamps** con precisión de milisegundos

### 💾 Persistencia
- **PostgreSQL** como base de datos principal
- **Hibernate/JPA** para mapeo de entidades
- **Migraciones automáticas** con DDL auto

### 🔐 Seguridad y Confiabilidad
- **Validación de datos** antes de insertar
- **Rechazo automático** de montos inválidos
- **Transacciones atómicas** en chunks
- **Logging detallado** de todas las operaciones

---

## 📁 Estructura del Proyecto

```
EngineDataProcessingApplication/
│
├── 📄 README.md                          # Este archivo
├── 📄 pom.xml                           # Configuración Maven
├── 📄 mvnw                              # Maven Wrapper (Linux/Mac)
├── 📄 mvnw.cmd                          # Maven Wrapper (Windows)
│
├── 📁 src/
│   ├── main/
│   │   ├── 📁 java/com/andres_cmk/EngineDataProcessingApplication/
│   │   │   ├── 📄 EngineDataProcessingApplication.java       # Main - Punto de entrada
│   │   │   │
│   │   │   ├── 📁 Config/
│   │   │   │   └── 📄 BatchConfig.java                       # Configuración de Spring Batch
│   │   │   │       ├── FlatFileItemReader (CSV)
│   │   │   │       ├── TransactionProcessor
│   │   │   │       ├── RepositoryItemWriter (BD)
│   │   │   │       ├── Step1 (chunk processing)
│   │   │   │       └── Job (orquestación)
│   │   │   │
│   │   │   ├── 📁 domain/
│   │   │   │   ├── 📁 entity/
│   │   │   │   │   └── 📄 Transaction.java                   # Entidad JPA para BD
│   │   │   │   │       └── Mapeo de tabla "transacciones"
│   │   │   │   │
│   │   │   │   └── 📁 dto/
│   │   │   │       └── 📄 TransactionDTO.java                # Record para lectura CSV
│   │   │   │           └── Mapeo de campos del CSV
│   │   │   │
│   │   │   ├── 📁 listener/
│   │   │   │   ├── 📄 JobCompletionNotificationListener.java # Listener del Job
│   │   │   │   │   ├── beforeJob() - Inicio del procesamiento
│   │   │   │   │   └── afterJob() - Estadísticas finales
│   │   │   │
│   │   │   ├── 📁 processor/
│   │   │   │   └── 📄 TransactionProcessor.java              # Lógica de procesamiento
│   │   │   │       ├── Validación de montos
│   │   │   │       ├── Conversión de tipos
│   │   │   │       └── Filtrado de registros inválidos
│   │   │   │
│   │   │   ├── 📁 repository/
│   │   │   │   └── 📄 TransactionRepository.java             # Acceso a datos
│   │   │   │       └── Extends JpaRepository<Transaction,Long>
│   │   │   │
│   │   │   └── 📁 dataGenerator/
│   │   │       └── 📄 DataGenerator.java                     # Generador de datos (opcional)
│   │   │
│   │   └── 📁 resources/
│   │       ├── 📄 application.properties                      # Configuración de la app
│   │       │   ├── spring.datasource.*
│   │       │   ├── spring.jpa.*
│   │       │   ├── spring.batch.*
│   │       │   └── logging.level.*
│   │       │
│   │       ├── 📄 datos_bancarios.csv                        # Archivo de entrada
│   │       │   └── Formato: id,fecha,monto,moneda,...
│   │       │
│   │       ├── 📁 static/                                    # Recursos estáticos
│   │       └── 📁 templates/                                 # Plantillas (vacío)
│   │
│   └── test/
│       └── 📁 java/com/andres_cmk/EngineDataProcessingApplication/
│           └── 📄 EngineDataProcessingApplicationTests.java  # Tests unitarios
```

---

## 🔍 Descripción de Componentes Principales

### 1. **EngineDataProcessingApplication.java**
**Propósito**: Punto de entrada de la aplicación
```java
@SpringBootApplication
public class EngineDataProcessingApplication {
    public static void main(String[] args) {
        SpringApplication.run(EngineDataProcessingApplication.class, args);
    }
}
```

### 2. **BatchConfig.java**
**Propósito**: Configuración central de Spring Batch

#### Componentes:
- **FlatFileItemReader**: Lee el archivo CSV línea por línea
- **TransactionProcessor**: Valida y transforma cada registro
- **RepositoryItemWriter**: Persiste en PostgreSQL
- **Step1**: Configura el procesamiento en chunks
- **importTransaccionJob**: Orquesta el flujo completo

```
CSV → Reader → Processor → Writer → PostgreSQL
                ↓
            Validación
            Transformación
            Filtrado
```

### 3. **TransactionProcessor.java**
**Propósito**: Lógica de negocio del procesamiento

**Operaciones**:
```
├─ Convierte STRING → LocalDateTime (fecha)
├─ Convierte STRING → BigDecimal (monto)
├─ Valida que el monto sea > 0
├─ Mapea DTO → Entity
└─ Retorna null si es inválido (filtrado automático)
```

### 4. **JobCompletionNotificationListener.java**
**Propósito**: Reporte de estadísticas y performance

**Muestra**:
- Tiempo total de procesamiento
- Registros leídos/escritos/filtrados
- Throughput (registros/segundo)
- Tiempo promedio por registro
- Errores (si los hay)

### 5. **Transaction.java** (Entity)
**Propósito**: Mapeo JPA a tabla PostgreSQL

```sql
CREATE TABLE transacciones (
    id_transaccion VARCHAR(255) PRIMARY KEY,
    fecha_transaccion TIMESTAMP,
    monto NUMERIC(10,2),
    moneda VARCHAR(3),
    tipo_transferencia VARCHAR(50),
    cuenta_origen VARCHAR(20),
    banco_destino VARCHAR(50),
    created_at TIMESTAMP,
    responsable VARCHAR(100)
);
```

### 6. **TransactionDTO.java** (Record)
**Propósito**: Mapeo CSV → Objeto

```
CSV Header: idTransaccion, fecha, monto, moneda, comercio, tipo, cuentaOrigen, bancoDestino, nameResponsable
                            ↓
                        TransactionDTO
```

### 7. **TransactionRepository.java**
**Propósito**: Acceso a datos con JPA

```java
public interface TransactionRepository extends JpaRepository<Transaction, Long>
```

---

## 🛠️ Requisitos Previos

### Software
- **Java 21+** (Project Loom - Virtual Threads)
- **Maven 3.8+** (incluido mvnw)
- **PostgreSQL 12+**
- **Git** (opcional)

### Base de Datos
```sql
CREATE DATABASE transacciones;
```

### Archivo de Entrada
- Formato: **CSV** con headers
- Separador: **Comas (,)**
- Ubicación: `src/main/resources/datos_bancarios.csv`
- Mínimo: 2 líneas (header + 1 registro)

---

## 📦 Instalación

### 1. Clonar el Repositorio
```bash
git clone <url-repositorio>
cd EngineDataProcessingApplication
```

### 2. Configurar PostgreSQL
```bash
# Conectar a PostgreSQL
psql -U postgres

# Crear base de datos
CREATE DATABASE transacciones;

# Verificar
\l
```

### 3. Configurar application.properties
```properties
# src/main/resources/application.properties

# Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5432/transacciones
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD

# Hibernate DDL
spring.jpa.hibernate.ddl-auto=create-drop

# Spring Batch
spring.batch.job.enabled=true
spring.batch.jdbc.initialize-schema=always
```

### 4. Compilar el Proyecto
```bash
# Con Maven Wrapper (Windows)
mvnw clean compile

# Con Maven Wrapper (Linux/Mac)
./mvnw clean compile

# Con Maven instalado
mvn clean compile
```

### 5. Ejecutar la Aplicación
```bash
# Con Maven Wrapper (Windows)
mvnw spring-boot:run

# Con Maven Wrapper (Linux/Mac)
./mvnw spring-boot:run

# Con Maven instalado
mvn spring-boot:run
```

---

## ⚙️ Configuración

### application.properties

```properties
# ═══════════════════════════════════════════════════════════
# 🔌 CONFIGURACIÓN DE BASE DE DATOS
# ═══════════════════════════════════════════════════════════

spring.application.name=EngineDataProcessingApplication

# Conexión PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/transacciones
spring.datasource.username=postgres
spring.datasource.password=2805
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate/JPA
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create-drop    # create-drop | validate | update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# ═══════════════════════════════════════════════════════════
# ⚙️ CONFIGURACIÓN DE SPRING BATCH
# ═══════════════════════════════════════════════════════════

spring.batch.jdbc.initialize-schema=always   # always | embedded | never
spring.batch.job.enabled=true               # Auto-ejecutar jobs al iniciar

# ═══════════════════════════════════════════════════════════
# 📊 CONFIGURACIÓN DE LOGGING
# ═══════════════════════════════════════════════════════════

logging.level.org.springframework.batch=DEBUG
logging.level.com.andres_cmk.EngineDataProcessingApplication=DEBUG

# ═══════════════════════════════════════════════════════════
# 🚀 CONFIGURACIÓN DE VIRTUAL THREADS
# ═══════════════════════════════════════════════════════════

spring.threads.virtual.enabled=true
```

### BatchConfig.java - Configuración de Parámetros

```java
// Tamaño del chunk (cantidad de registros por transacción)
.chunk(50_000, transactionManager)
// Cambiar a un valor menor para más commits (más overhead)
// Cambiar a un valor mayor para menos commits (más memoria)

// Nombres de columnas del CSV
.names("idTransaccion", "fecha", "monto", "moneda", "comercio", 
       "tipo", "cuentaOrigen", "bancoDestino", "nameResponsable")
// Deben coincidir exactamente con el header del CSV
```

---

## 🚀 Uso

### Flujo Básico de Ejecución

```
1. Inicia la aplicación (mvnw spring-boot:run)
           ↓
2. Spring Boot carga la configuración
           ↓
3. Spring Batch detecta el Job (spring.batch.job.enabled=true)
           ↓
4. beforeJob() - Listener: Muestra inicio
           ↓
5. Reader lee el CSV línea por línea
           ↓
6. Processor valida y transforma cada registro
           ↓
7. Writer persiste en chunks (50,000 registros)
           ↓
8. Commit de transacción cada chunk
           ↓
9. afterJob() - Listener: Muestra estadísticas
           ↓
10. Aplicación termina (o continúa esperando si hay controladores REST)
```

### Ejecución Manual (Debugging)

Si necesitas ejecutar manualmente el Job para debug:

1. Descomenta `@Component` en `ManualBatchRunner.java`
2. Ejecuta la aplicación
3. El Job se ejecutará con información de diagnóstico detallada

```java
@Component  // ← Descomenta esta línea
public class ManualBatchRunner implements CommandLineRunner {
    // ... diagnosticar problemas ...
}
```

### Generar Datos de Prueba

Si no tienes datos, puedes generar un CSV de ejemplo:

```java
// En DataGenerator.java (si lo tienes)
@Component
public class DataGenerator {
    // Implementar generación de datos
}
```

### Script de Diagnóstico

Ejecutar script para diagnosticar problemas:

```bash
# Windows PowerShell
.\diagnostico.ps1

# Verifica:
# ✓ Archivo CSV existe y tiene datos
# ✓ Carpeta compilada tiene el CSV
# ✓ Conexión a PostgreSQL
# ✓ Jobs registrados en BD
```

---

## 🛠️ Herramientas y Tecnologías

### Framework Principal
| Herramienta | Versión | Propósito |
|-------------|---------|----------|
| **Spring Boot** | 3.5.10 | Framework principal |
| **Spring Batch** | 5.x | Procesamiento batch |
| **Spring Data JPA** | 3.x | Acceso a datos |

### Base de Datos
| Herramienta | Versión | Propósito |
|-------------|---------|----------|
| **PostgreSQL** | 12+ | Base de datos principal |
| **Hibernate** | 6.x | ORM (Object-Relational Mapping) |
| **Liquibase** | N/A | Migraciones (No usado, usa DDL auto) |

### Lenguaje y Compilación
| Herramienta | Versión | Propósito |
|-------------|---------|----------|
| **Java** | 21+ | Lenguaje de programación |
| **Maven** | 3.8+ | Gestor de dependencias y build |
| **Maven Wrapper** | Latest | Maven sin instalación previa |

### Librerías Adicionales
| Librería | Versión | Propósito |
|----------|---------|----------|
| **Lombok** | 1.18.x | Reduce boilerplate (getters, setters, logs) |
| **SLF4J** | Latest | Logging |
| **DataFaker** | 2.5.3 | Generación de datos fake (opcional) |
| **PostgreSQL Driver** | Latest | Driver JDBC para PostgreSQL |

### Dependencias Completas (pom.xml)

```xml
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Base de Datos -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Herramientas de Desarrollo -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<dependency>
    <groupId>net.datafaker</groupId>
    <artifactId>datafaker</artifactId>
    <version>2.5.3</version>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.springframework.batch</groupId>
    <artifactId>spring-batch-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 🏗️ Arquitectura

### Diagrama de Flujo General

```
┌─────────────────────────────────────────────────────────────┐
│                    SPRING BOOT APPLICATION                  │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │            SPRING BATCH - JOB EXECUTION              │  │
│  │                                                      │  │
│  │  ┌────────────────────────────────────────────────┐ │  │
│  │  │  beforeJob() - JobCompletionNotificationListener│ │  │
│  │  │  🚀 INICIANDO PROCESAMIENTO MASIVO             │ │  │
│  │  └────────────────────────────────────────────────┘ │  │
│  │                        ↓                             │  │
│  │  ┌────────────────────────────────────────────────┐ │  │
│  │  │            STEP 1 - CHUNK PROCESSING           │ │  │
│  │  │                                                │ │  │
│  │  │  ┌──────────────┐  ┌──────────────┐           │ │  │
│  │  │  │   READER     │→ │  PROCESSOR   │           │ │  │
│  │  │  │  CSV File    │  │  Validación  │           │ │  │
│  │  │  └──────────────┘  └──────────────┘           │ │  │
│  │  │         ↓                ↓                     │ │  │
│  │  │   50,000 regs      Transformación              │ │  │
│  │  │    por chunk           Filtrado                │ │  │
│  │  │                                                │ │  │
│  │  │              ↓                                 │ │  │
│  │  │  ┌──────────────────────────┐                 │ │  │
│  │  │  │   WRITER - INSERT BATCH   │                 │ │  │
│  │  │  │  RepositoryItemWriter     │                 │ │  │
│  │  │  │  → PostgreSQL             │                 │ │  │
│  │  │  └──────────────────────────┘                 │ │  │
│  │  │              ↓                                 │ │  │
│  │  │  ┌──────────────────────────┐                 │ │  │
│  │  │  │   TRANSACCIÓN COMMIT      │                 │ │  │
│  │  │  │   Cada 50,000 registros   │                 │ │  │
│  │  │  └──────────────────────────┘                 │ │  │
│  │  │                                                │ │  │
│  │  │  [REPETIR HASTA FIN DE ARCHIVO]                │ │  │
│  │  └────────────────────────────────────────────────┘ │  │
│  │                        ↓                             │  │
│  │  ┌────────────────────────────────────────────────┐ │  │
│  │  │  afterJob() - JobCompletionNotificationListener │ │  │
│  │  │  ✅ ESTADÍSTICAS Y PERFORMANCE                  │ │  │
│  │  └────────────────────────────────────────────────┘ │  │
│  └──────────────────────────────────────────────────────┘ │
│                                                             │
│  LISTENERS:                                                 │
│  • JobCompletionNotificationListener - Inicio/Fin del Job  │
│  • StepListener - Estadísticas del Step                    │
│                                                             │
│  REPOSITORIES:                                              │
│  • TransactionRepository - Acceso a datos JPA              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
        ↓
    ┌─────────────┐
    │ PostgreSQL  │
    │  TABLE:     │
    │ transacciones
    └─────────────┘
```

### Mapeo de Datos

```
datos_bancarios.csv
    │
    ├─ idTransaccion → TransactionDTO.idTransaccion
    ├─ fecha → TransactionDTO.fecha (String)
    ├─ monto → TransactionDTO.monto (String)
    ├─ moneda → TransactionDTO.moneda
    ├─ comercio → TransactionDTO.comercio
    ├─ tipo → TransactionDTO.tipo
    ├─ cuentaOrigen → TransactionDTO.cuentaOrigen
    ├─ bancoDestino → TransactionDTO.bancoDestino
    └─ nameResponsable → TransactionDTO.nameResponsable
            ↓
        PROCESSOR
            ├─ Convierte fecha: String → LocalDateTime
            ├─ Convierte monto: String → BigDecimal
            ├─ Valida: monto > 0
            └─ Mapea: DTO → Entity
            ↓
        Transaction Entity
            ├─ id_transaccion (PK)
            ├─ fecha_transaccion (TIMESTAMP)
            ├─ monto (NUMERIC)
            ├─ moneda (VARCHAR)
            ├─ tipo_transferencia (VARCHAR)
            ├─ cuenta_origen (VARCHAR)
            ├─ banco_destino (VARCHAR)
            ├─ responsable (VARCHAR)
            └─ created_at (TIMESTAMP - AUTO)
            ↓
        PostgreSQL Database
```

---

## 📊 Performance

### Benchmark Típico

Para **1 millón de registros** en una máquina estándar:

| Métrica | Valor |
|---------|-------|
| **Tiempo Total** | ~60-90 segundos |
| **Throughput** | ~11,000 - 16,000 registros/segundo |
| **Tiempo por Registro** | ~0.06 - 0.09 ms |
| **Chunk Size Óptimo** | 50,000 registros |
| **Memoria Usada** | ~500 MB - 1 GB |

### Factores que Afectan Performance

```
┌─ LECTURA DEL CSV
│  └─ Tamaño del archivo
│  └─ Velocidad del disco
│
├─ PROCESAMIENTO
│  └─ Complejidad de validaciones
│  └─ Conversiones de tipos
│  └─ CPU disponible
│
├─ ESCRITURA EN BD
│  └─ Velocidad de conexión a PostgreSQL
│  └─ Índices de la tabla
│  └─ Configuración del servidor
│
└─ TRANSACCIONES
   └─ Tamaño del chunk (más grande = menos commits)
   └─ Aislamiento de transacciones
```

### Optimizaciones Aplicadas

✅ **Virtual Threads** (Java 21+)
```properties
spring.threads.virtual.enabled=true
```

✅ **Chunk Processing**
```java
.chunk(50_000, transactionManager)  // Balancea memoria vs commits
```

✅ **Batch Inserts**
```java
.methodName("save")  // Usa saveAll() internamente
```

✅ **Logging Seleccionado**
```properties
logging.level.org.springframework.batch=DEBUG  // Solo info crítica
```

---

## 📋 Logs y Monitoreo

### Output Esperado

```
══════════════════════════════════════════════════════════════
🚀 INICIANDO PROCESAMIENTO MASIVO DE TRANSACCIONES
══════════════════════════════════════════════════════════════
📋 Job Name: importTransactionJob
🆔 Job Instance ID: 1
🆔 Job Execution ID: 1
🕒 Hora de inicio: 2026-02-14 10:30:45.123
══════════════════════════════════════════════════════════════

2026-02-14 10:30:46.100 INFO  🔹 INICIANDO STEP: step1
2026-02-14 10:30:46.200 INFO  🔄 Procesando transacción ID: TX001 | Monto: 1000.00 | Fecha: 2024-01-01 10:00:00
2026-02-14 10:30:46.201 INFO  ✅ Transacción procesada correctamente: TX001
...
2026-02-14 10:32:18.400 INFO  🔹 FINALIZANDO STEP: step1
2026-02-14 10:32:18.401 INFO  📊 Registros leídos: 1000000
2026-02-14 10:32:18.402 INFO  📝 Registros escritos: 1000000
2026-02-14 10:32:18.403 INFO  ⚠️  Registros con error: 0

══════════════════════════════════════════════════════════════
✅ ¡TRABAJO COMPLETADO EXITOSAMENTE!
══════════════════════════════════════════════════════════════
📊 ESTADÍSTICAS DEL JOB:
   ├─ Status: COMPLETED
   ├─ Exit Status: COMPLETED
   ├─ Inicio: 2026-02-14 10:30:45.123
   └─ Fin: 2026-02-14 10:32:18.456
──────────────────────────────────────────────────────────────
⏱️  TIEMPO TOTAL: 1 min 33 s 333 ms
⏱️  TIEMPO TOTAL (ms): 93333 ms
──────────────────────────────────────────────────────────────
📈 ESTADÍSTICAS POR STEP:
   🔹 Step: step1
      ├─ Registros leídos: 1000000
      ├─ Registros escritos: 1000000
      ├─ Registros filtrados: 0
      ├─ Registros con error: 0
      ├─ Tiempo del step: 93250 ms
      ├─ Tiempo promedio por registro: 0.093 ms
      └─ Throughput: 10723.86 registros/segundo
══════════════════════════════════════════════════════════════
🎉 Procesamiento completado con éxito!
══════════════════════════════════════════════════════════════
```

### Niveles de Logging

```properties
# DEBUG - Información detallada (desarrollo)
logging.level.org.springframework.batch=DEBUG

# INFO - Información general (producción)
logging.level.org.springframework.batch=INFO

# WARN - Solo advertencias
logging.level.org.springframework.batch=WARN

# ERROR - Solo errores
logging.level.org.springframework.batch=ERROR
```

---

## 🆘 Solución de Problemas

### ❌ El Batch no se ejecuta

**Síntoma**: La aplicación inicia pero el Job no se ejecuta.

**Soluciones**:
1. Verificar `spring.batch.job.enabled=true` en `application.properties`
2. Limpiar tablas de Spring Batch:
   ```bash
   sqlplus -U postgres -d transacciones < clean_batch_tables.sql
   ```
3. Ejecutar script de diagnóstico:
   ```bash
   .\diagnostico.ps1
   ```

### ❌ No se leen registros del CSV

**Síntoma**: El Job dice "Registros leídos: 0"

**Soluciones**:
1. Verificar que el archivo existe en `src/main/resources/datos_bancarios.csv`
2. Recompilar: `mvnw clean compile`
3. Verificar que el CSV tiene al menos 2 líneas (header + datos)
4. Verificar formato del CSV (separador por comas)
5. Ver los primeros 3 líneas del CSV:
   ```bash
   Get-Content "src\main\resources\datos_bancarios.csv" -Head 3
   ```

### ❌ Error de conexión a PostgreSQL

**Síntoma**: `Connection refused` o `user does not exist`

**Soluciones**:
1. Verificar que PostgreSQL está corriendo
2. Verificar credenciales en `application.properties`
3. Verificar que la base de datos `transacciones` existe:
   ```bash
   psql -U postgres -c "\l"
   ```

### ❌ Error: "violates unique constraint"

**Síntoma**: Las transacciones tienen duplicados

**Soluciones**:
1. `id_transaccion` debe ser único en el CSV
2. Verificar que no hay IDs repetidos:
   ```bash
   Get-Content "datos_bancarios.csv" | Select-Object -Skip 1 | ForEach-Object { $_.Split(',')[0] } | Group-Object | Where-Object { $_.Count -gt 1 }
   ```

### ❌ Bajo rendimiento

**Síntoma**: Throughput menor a 1,000 registros/segundo

**Optimizaciones**:
1. Aumentar `chunk size`: `chunk(100_000, transactionManager)`
2. Habilitar Virtual Threads: `spring.threads.virtual.enabled=true`
3. Reducir logging: `logging.level=WARN`
4. Optimizar índices en PostgreSQL
5. Verificar CPU/RAM disponible

---

## 🚀 Próximas Mejoras

### Planeadas
- [ ] Agregar REST API para consultar transacciones procesadas
- [ ] Implementar particionamiento de datos
- [ ] Añadir métricas con Micrometer y Prometheus
- [ ] Crear dashboard con Grafana
- [ ] Soportar múltiples formatos (JSON, XML, Parquet)
- [ ] Implementar reintentos automáticos de registros fallidos
- [ ] Agregar notificaciones (email, Slack) al finalizar

### Consideraciones
- Escalabilidad horizontal con múltiples instancias
- Persistencia de estado del batch
- Recuperación de fallos (fault recovery)
- Auditoría detallada de cambios

---

## 👥 Autor

**Andrés CMK**

---

## 📚 Documentación Adicional

- [Spring Batch Documentation](https://spring.io/projects/spring-batch)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Ejemplo de Parallel Processing con Spring Batch](https://docs.spring.io/spring-batch/reference/scalability.html)

---

## 📞 Soporte

Para reportar problemas o sugerencias:
- Crear un Issue en el repositorio
- Contactar al desarrollador
- Revisar la carpeta de documentación

---

**¡Gracias por usar Engine Data Processing Application!** 🎉

**Última actualización**: Febrero 2026

```
╔══════════════════════════════════════════════════════════════╗
║  Engine Data Processing Application - v1.0.0                ║
║  Procesamiento Batch eficiente de Transacciones Bancarias    ║
║  Powered by Spring Boot & Spring Batch                       ║
╚══════════════════════════════════════════════════════════════╝
```

