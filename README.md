# AMicroSpringBoot - Servidor Web con IoC y Reflexión en Java

## Descripción del Proyecto
Este proyecto consiste en la implementación de un servidor web minimalista inspirado en Apache, utilizando principios de Inversión de Control (IoC) y reflexión en Java. El servidor permite la construcción de aplicaciones web a partir de POJOs (Plain Old Java Objects), soportando mapeo de rutas HTTP mediante anotaciones personalizadas como @RequestMapping y @GetMapping. Además, permite la inyección de parámetros en métodos a través de la anotación @RequestParam.


El servidor es capaz de:

Entregar páginas HTML y archivos de imagen PNG.
Publicar servicios web desde POJOs anotados como @RestController.
Manejar solicitudes no concurrentes.
Explorar el classpath para cargar automáticamente las clases anotadas en la versión final.


## Arquitectura del Proyecto

1. Servidor Web
   El servidor web está construido en Java y utiliza sockets para escuchar peticiones HTTP en un puerto configurable. Se procesan solicitudes GET y las rutas son mapeadas a métodos en clases anotadas con @RestController.
   
2. Inversión de Control (IoC)
  El framework permite cargar dinámicamente componentes de la aplicación (POJOs) que se encuentran anotados con @RestController. La versión inicial carga los POJOs desde la línea de comandos, mientras que la versión final escanea el classpath para detectarlos automáticamente.
3. Reflexión en Java
El uso de reflexión permite que el servidor descubra y ejecute métodos anotados con @RequestMapping o @GetMapping, manejando rutas HTTP sin necesidad de configuración adicional. Los métodos pueden recibir parámetros a través de @RequestParam.

## Getting Started

  ### Requisitos

  * Java 17 o superior
  * Maven
  * Git
  * Un IDE que soporte proyectos Java (e.g., NetBeans, IntelliJ, Eclipse) o línea de comandos.

  ### Cómo Ejecutar el Proyecto

  1. Clonar el repositorio:
     ```
        git clone https://github.com/CesarPineda14/ARep_lab03V1.git

     ```
  2. Navegar a la carpeta del proyecto:
     ```
        cd ARep_lab03V1

     ```
   3. Construir el proyecto con Maven:
      ```
       mvn clean package

      ```
  4. Ejecutar la aplicacion desde comando:
      ```
    
       java -jar target/SimpleWebServer-1.0-SNAPSHOT-jar-with-dependencies.jar

      ```
5. Aceeder a paginas de prueba:
   * ```
    
      curl http://localhost:8080/App/greeting?name=John

      ```
   * ```
    
      curl http://localhost:8080/App/pi

      ```

    * ```
    
      curl http://localhost:8080/App/random

      ```


## Ejemplos de Código
  * Clase de Ejemplo: GreetingController
    ```
    
      @RestController
      public class GreetingController {
      private static final String template = "Hola, %s!";
    
      @GetMapping("/greeting")
      public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
          return String.format(template, name);
      }
      }

     ```

       Este controlador maneja solicitudes GET a la ruta /greeting y acepta un parámetro name, que por defecto es         "World" si no se proporciona.

* Clase Principal: SpringECI
      ```
  
       public static void registerController(Class<?> controllerClass) throws ClassNotFoundException {
              if (controllerClass.isAnnotationPresent(RestController.class)) {
                  Method[] methods = controllerClass.getDeclaredMethods();
                  for (Method method : methods) {
                      if (method.isAnnotationPresent(GetMapping.class)) {
                          String key = method.getAnnotation(GetMapping.class).value();
                          services.put(key, method);
                      }
                  }
              }
          }
  
       ```

  Esta clase carga dinámicamente el POJO pasado como argumento y utiliza reflexión para identificar métodos anotados.
  




## Pruebas Realizadas
 Las siguientes pruebas fueron realizadas para validar el correcto funcionamiento del servidor:
  
1. Prueba de Respuesta HTTP 200
   Se realizó una solicitud GET a la ruta /greeting?name=John y se verificó que el servidor devuelve el saludo esperado con el código de estado 200 (OK).
   ![image](https://github.com/user-attachments/assets/0a9f370a-452b-415e-84f9-b4f0a73843f2)

2. Prueba de Parámetro por Defecto
   Se hizo una solicitud GET a /greeting sin pasar el parámetro name, y se verificó que el servidor responde con "Hola, World!".
   ![image](https://github.com/user-attachments/assets/f34707fd-e012-4658-af87-2ab9db624f6a)

3. Prueba de Clase Anotada Dinámicamente
Se cargó un POJO con la anotación @RestController desde la línea de comandos y se comprobó que los métodos con @GetMapping eran detectados y mapeados correctamente.
  
