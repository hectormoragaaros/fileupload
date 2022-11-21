## Motivación

Este proyecto fue realizado como una prueba para subir y obtener un archivo mediante un servicio REST. El proyecto está dividido en 2 

## Objetivo

La idea del proyecto es subir un archivo a una carpeta particular del servidor. El archivo será guardado con un nombre creado a partir de un UUID aleatorio. En una base de datos se guardará el UUID y los datos del archivo (nombre, tipo MIME, tamaño en bytes), lo que permitirá restablecer esos datos en el archivo cuando se ejecute el servicio REST para la obtención de dicho archivo.

## Tecnologías Usadas

- Eclipse IDE for Enterprise Java and Web Developers. Version: 2022-09 (4.25.0)
- Java 11
- SpringBoot 2.7.5
- Lombok 1.18.24
- MariaDb 3.0.8

## Descripción del proyecto

Corresponde a la parte servidor REST del proyecto. Se exponen 2 endpoints, uno para subir un archivo y el otro para bajarlo.
El endpoint "/upload" permite subir un archivo al servidor, donde quedará almacenado en la carpeta "/uploads" dentro del mismo proyecto, con un nombre generado por su id. Este id corresponde a un tipo de datos UUID generado por la base de datos. Además del id, el nombre original, el tipo MIME y su tamaño en bytes quedarán almacenados en la base de datos, la que viene incluída dentro del proyecto.
El endpoint "/dowload/{UUID}" me permite recuperar el archivo original, con su nombre y extensión originales.

## Notas
   
<p>Por alguna razón que no he indagado, lombok no se instalaba al agregarlo como referencia mediante Maven. Tuve que bajar el archivo .jar y ejecutarlo para que quedara instalado dentro de Eclipse, como lo indica [\[1\]](#ref1). La utilidad de Lombok es que te evita la creación de código repetitivo, mediante la utilización de algunos **annotations**. Un ejemplo de ello se encuentra en [\[2\]](#ref2).</p>
<p>Dado que el UUID se utiliza como clave primaria dentro de la base de datos, se debe tener alguna estrategia de creación de ese UUID tanto dentro de Java como también dentro de la base de datos. Para la creación de un UUID único se usó **UUID.randomUUID()**, que de acuerdo a [\[3\]](#ref3) tiene una muy baja probabilidad de colisiones. La opción elegida se basó en [\[4\]](#ref4).</p>
<p>Respecto a la subida y bajada de archivos me basé en los proyectos [\[5\]](#ref5), [\[6\]](#ref6), [\[7\]](#ref7), [\[8\]](#ref8) y [\[9\]](#ref9). Como se almacenaba los datos del archivo, como nombre y tipo MIME, al recomponer el archivo con el endpoint /files/{uuid}, se debe indicar todos esos datos en la respuesta, de acuerdo a lo sugerido en [\[10\]](#ref10).</p>

## Referencias

- [1] <a id="ref1" href="https://www.baeldung.com/lombok-ide">Setting up Lombok with Eclipse and Intellij</a>
- [2] <a id="ref2" href="https://javabydeveloper.com/lombok-spring-boot-example/">Lombok + Spring Boot Example</a>
- [3] <a id="ref3" href="https://stackoverflow.com/questions/65674455/method-to-generating-random-uuid-using-java">method to generating random UUID using Java \[duplicate\]</a>
- [4] <a id="ref4" href="https://www.baeldung.com/java-uuid">Guide to UUID in Java</a>
- [5] <a id="ref5" href="https://spring.io/guides/gs/uploading-files/">Uploading Files</a>
- [6] <a id="ref6" href="https://www.codejava.net/frameworks/spring-boot/file-download-upload-rest-api-examples">Spring Boot File Download and Upload REST API Examples</a>
- [7] <a id="ref7" href="https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/">Spring Boot File Upload / Download Rest API Example</a>
- [8] <a id="ref8" href="https://www.bezkoder.com/spring-boot-file-upload/">Spring Boot File upload example with Multipart File</a>
- [9] <a id="ref9" href="https://www.bezkoder.com/spring-boot-upload-file-database/">Spring Boot Upload/Download File to/from Database example</a>
- [10] <a id="ref10" href="https://stackoverflow.com/questions/67756018/set-the-content-type-header-dynamically-in-spring-boot">Set the Content-Type header dynamically in Spring Boot</a>