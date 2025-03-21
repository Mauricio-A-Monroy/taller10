# MicroStream - Servicios de Posts Estilo Twitter

## Introducción

MicroStream es una aplicación que permite a los usuarios crear posts de 140 caracteres e ir registrándolos en streams (hilos) únicos, similar a Twitter. El sistema implementa una arquitectura de microservicios desplegada en AWS Lambda, con autenticación JWT y una base de datos MongoDB para el almacenamiento persistente.

Este proyecto fue desarrollado como parte del taller de Arquitecturas Empresariales, donde el objetivo principal fue crear una aplicación que demuestre el uso de microservicios, autenticación segura y despliegue en la nube.

## Arquitectura del Sistema

### Vista General

La aplicación está diseñada como un sistema de microservicios con las siguientes características:

- **Backend**: Desarrollado con Spring Boot y desplegado en AWS Lambda
- **Autenticación**: Implementación de JWT (JSON Web Tokens) para seguridad
- **Base de Datos**: MongoDB Atlas para almacenamiento persistente
- **Frontend**: Aplicación JavaScript desplegada en Amazon S3
- **API Gateway**: Para gestionar el enrutamiento de las solicitudes a los microservicios

### Diagrama de Arquitectura

```
┌───────────────┐       ┌───────────────┐
│               │       │               │
│  Cliente Web  │───────│  API Gateway  │
│   (S3 Bucket) │       │               │
│               │       └───────┬───────┘
└───────────────┘               │
                                ▼
         ┌─────────────────────┬─────────────────────┐
         │                     │                     │
┌────────▼─────────┐  ┌────────▼─────────┐  ┌────────▼─────────┐
│                  │  │                  │  │                  │
│ Microservicio    │  │ Microservicio    │  │ Microservicio    │
│ de Autenticación │  │ de Streams       │  │ de Posts         │
│ (AWS Lambda)     │  │ (AWS Lambda)     │  │ (AWS Lambda)     │
│                  │  │                  │  │                  │
└────────┬─────────┘  └────────┬─────────┘  └────────┬─────────┘
         │                     │                     │
         └─────────────────────┼─────────────────────┘
                               │
                               ▼
                     ┌─────────────────┐
                     │                 │
                     │  MongoDB Atlas  │
                     │                 │
                     └─────────────────┘
```

## Componentes del Sistema

### Capa de Controladores

Los controladores manejan las solicitudes HTTP y sirven como punto de entrada para la API:

1. **AuthController**: Maneja el registro e inicio de sesión de usuarios generando tokens JWT.
2. **UserController**: Gestiona las operaciones CRUD para usuarios.
3. **StreamController**: Administra la creación, consulta y eliminación de streams.
4. **PostController**: Controla la creación y consulta de posts dentro de los streams.

Cada controlador implementa una API RESTful con endpoints específicos que utilizan los métodos HTTP adecuados (GET, POST, PUT, DELETE).

### Capa de Servicios

Los servicios contienen la lógica de negocio de la aplicación:

1. **UserService**: Gestiona la creación, actualización y eliminación de usuarios.
2. **StreamService**: Maneja las operaciones relacionadas con los streams, incluyendo su creación y consulta.
3. **PostService**: Administra los posts dentro de los streams.
4. **CustomUserDetailsService**: Implementa la interfaz UserDetailsService de Spring Security para la autenticación.

### Capa de Repositorios

Los repositorios proporcionan acceso a la base de datos:

1. **UserRepository**: Accede a la colección de usuarios en MongoDB.
2. **StreamRepository**: Gestiona las operaciones de base de datos para streams.
3. **PostRepository**: Maneja las operaciones de base de datos para posts.

### Modelos de Datos

La aplicación gestiona tres entidades principales:

1. **User**: Representa a un usuario del sistema con atributos como nombre, apellido, email y contraseña.
2. **Stream**: Representa un hilo de posts creado por un usuario.
3. **Post**: Representa un mensaje de hasta 140 caracteres asociado a un stream.

### Configuración de Seguridad

El sistema implementa seguridad mediante JWT (JSON Web Tokens):

1. **JwtUtil**: Proporciona métodos para generar, validar y extraer información de tokens JWT.
2. **AuthTokenFilter**: Intercepta las solicitudes HTTP para validar el token JWT.
3. **SecurityConfig**: Configura Spring Security, definiendo reglas de acceso y filtros.
4. **AuthEntryPointJwt**: Maneja las excepciones de autorización.

## Implementación en AWS

### AWS Lambda

La aplicación utiliza la clase `StreamLambdaHandler` para adaptar la aplicación Spring Boot a AWS Lambda:

```java
public class StreamLambdaHandler implements RequestStreamHandler {
   private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
   
   static {
       try {
           handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MicroserviceApplication.class);
       } catch (ContainerInitializationException e) {
           e.printStackTrace();
           throw new RuntimeException("Could not initialize Spring Boot application", e);
       }
   }
   
   @Override
   public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
           throws IOException {
       handler.proxyStream(inputStream, outputStream, context);
   }
}
```

Esta clase permite que el framework Spring Boot se ejecute dentro del entorno AWS Lambda, adaptando el ciclo de vida y el manejo de solicitudes.

### Amazon S3

El frontend de la aplicación se despliega en un bucket de Amazon S3 configurado para alojar un sitio web estático. Esto proporciona:

- Alta disponibilidad
- Escalabilidad automática
- Baja latencia

### Conexión a MongoDB Atlas

La aplicación se conecta a MongoDB Atlas utilizando la siguiente configuración:

```properties
spring.data.mongodb.uri=mongodb+srv://[username]:[password]@bdservicex.qslq9.mongodb.net/?retryWrites=true&w=majority&appName=bdServicex
spring.data.mongodb.database=bdServicex
```

## Flujo de Autenticación con JWT

1. El usuario se registra o inicia sesión mediante el endpoint `/api/auth/signup` o `/api/auth/signin`.
2. El sistema valida las credenciales y genera un token JWT firmado con una clave secreta.
3. El cliente almacena el token y lo incluye en el encabezado `Authorization` de las solicitudes posteriores.
4. El filtro `AuthTokenFilter` intercepta cada solicitud, valida el token y establece la autenticación en el contexto de seguridad.
5. Los controladores protegidos verifican la autenticación antes de procesar las solicitudes.

## Instalación y Ejecución

### Requisitos Previos

- Java 11 o superior
- Maven
- Cuenta en AWS
- Cuenta en MongoDB Atlas

### Pasos para la Ejecución

1. **Clonar el repositorio**
```bash
git clone https://github.com/Samuelfdm/MicroserviceX.git
cd MicroserviceX
mvn clean package
```
2. Configurar las variables de entorno o el archivo `application.properties`
3. Ejecutar `mvn clean install`
4. Iniciar la aplicación con `mvn spring-boot:run`

### Repositorio del frontend
```bash
git clone https://github.com/Samuelfdm/FrontMicroserviceX.git
cd FrontMicroserviceX
```

### Despliegue en AWS Lambda

1. Empaquetar la aplicación:
   ```
   mvn clean package
   ```

2. Crear una función Lambda en AWS y cargar el archivo ZIP/JAR generado.

3. Configurar los permisos y variables de entorno necesarios.

4. Configurar API Gateway para exponer los endpoints.

---
## Videos del funcionamiento

https://youtu.be/ioTk5dV7DrE

---

## Pruebas

Para garantizar la calidad y correcto funcionamiento del sistema, se realizaron las siguientes pruebas:

![img.png](src/main/resources/img/img.png)

---

### Explicación de las Pruebas

* testGetUserById_Success: Verifica que el método getUserById retorne un usuario existente cuando se proporciona un ID válido.

* testGetUserById_NotFound: Verifica que el método getUserById lance una excepción UserException cuando el usuario no existe.

* testSaveUser_Success: Verifica que el método saveUser guarde un nuevo usuario correctamente.

* testSaveUser_UsernameAlreadyExists: Verifica que el método saveUser lance una excepción UserException cuando el nombre de usuario ya existe.

* testAuth_Success: Verifica que el método auth retorne true cuando las credenciales son correctas.

* testAuth_Failure: Verifica que el método auth retorne false cuando las credenciales son incorrectas.

![img2.png](src%2Fmain%2Fresources%2Fimg%2Fimg2.png)

---

### Explicación de las Pruebas

* Inicialización de Mocks:
* MockitoAnnotations.openMocks(this) se usa en el método setUp para inicializar los mocks y inyectarlos en el controlador (authController).
* Simulación de AuthenticationManager:
* En testSignIn_Success, se simula el comportamiento de authenticationManager.authenticate() para devolver un objeto Authentication simulado.
* Luego, se simula el método getPrincipal() para devolver un UserDetails simulado.
* Simulación de UserRepository:
* En testSignUp_Success, se simula el comportamiento de userRepository.findByName() para devolver null (indicando que el usuario no existe).
* También se simula passwordEncoder.encode() para devolver una contraseña codificada.
* Verificación de Comportamiento:
* Se verifica que los métodos simulados se llamen el número correcto de veces usando verify.

---

![img_1.png](src%2Fmain%2Fresources%2Fimg%2Fimg_1.png)

### Explicación de las Pruebas

* testCreatePost_Success: Verifica que un post se crea correctamente cuando el usuario existe.
* testCreatePost_UserNotFound: Verifica que se lanza una excepción cuando el usuario no existe.
* testGetPostById_Success: Verifica que se obtiene un post por su ID correctamente.
* testGetPostsByStreamId_Success: Verifica que se obtienen los posts asociados a un stream.

---

![img_2.png](src%2Fmain%2Fresources%2Fimg%2Fimg_2.png)

### Explicación de las Pruebas

* testCreateStream_Success: Verifica que un stream se crea correctamente cuando el usuario existe.
* testCreateStream_UserNotFound: Verifica que se lanza una excepción cuando el usuario no existe.
* testGetStreamById_Success: Verifica que se obtiene un stream por su ID correctamente.
* testGetStreamById_NotFound: Verifica que se lanza una excepción cuando el stream no existe.
* testDeleteStream_Success: Verifica que un stream se elimina correctamente.
* testDeleteStream_NotFound: Verifica que se lanza una excepción cuando el stream no existe.

---

## Conclusiones

Este proyecto demuestra la implementación de una arquitectura de microservicios utilizando tecnologías modernas como Spring Boot, JWT y AWS Lambda. La separación en microservicios proporciona mayor flexibilidad, escalabilidad y facilidad de mantenimiento.

El uso de AWS Lambda elimina la necesidad de administrar servidores, permitiendo que el sistema escale automáticamente según la demanda. La autenticación JWT garantiza la seguridad de las comunicaciones entre el cliente y el servidor.

Contribuciones
--------------

Si deseas contribuir a este proyecto, por favor sigue los siguientes pasos:

1.  Haz un fork del repositorio.

2.  Crea una nueva rama (git checkout -b feature/nueva-funcionalidad).

3.  Realiza tus cambios y haz commit (git commit -am 'Añade nueva funcionalidad').

4.  Haz push a la rama (git push origin feature/nueva-funcionalidad).

5.  Abre un Pull Request.

---

## Construido con

- **Java**: El lenguaje de programación principal utilizado para implementar el servidor web.
- **Maven**: Herramienta de gestión y construcción de proyectos para manejar las dependencias y compilar el código.
- **Java Networking**: Librerías estándar de Java para manejar conexiones de red y protocolos HTTP.
- **Git**: Sistema de control de versiones para gestionar el código fuente.
- **HTML/CSS/JavaScript**: Tecnologías front-end utilizadas para crear la aplicación web de prueba.
- **JUnit**: Framework para realizar pruebas unitarias y asegurar la calidad del código.

---

## Autores

* **Johan Alejandro Estrada**
* **Mauricio Alejandro Monroy**
* **Samuel Felipe Díaz Mamanche**

Vea también la lista de [contribuidores](https://github.com/Samuelfdm/MicroserviceX/contributors) que participaron en este proyecto.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - vea el archivo [LICENSE.txt](LICENSE.txt) para más detalles

## Agradecimientos

* [Escuela Colombiana de Ingeniería: Julio Garavito](https://www.escuelaing.edu.co/es/)
