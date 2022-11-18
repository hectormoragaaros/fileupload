## Motivación

Este proyecto fue realizado como una prueba para subir y obtener un archivo mediante un servicio REST. El proyecto está dividido en 2 

## Objetivo

La idea del proyecto es subir un archivo a una carpeta particular del servidor. El archivo será guardado con un nombre de un hash creado a partir del nombre del archivo, el tipo MIME del archivo y el tamaño. En una base de datos se guardará el hash creado y los datos del archivo (nombre, extensión, tipo MIME, tamaño en bytes), lo que permitirá restablecer esos datos en el archivo cuando se ejecute el servicio REST para la obtención de dicho archivo. 

### Nota

## Tecnologías Usadas

- Eclipse IDE for Enterprise Java and Web Developers. Version: 2022-09 (4.25.0)
- Java 11
- SpringBoot 2.7.5
- Lombok 1.18.24

## Descripción del proyecto

Corresponde a la parte servidor REST del proyecto. Se exponen 2 endpoints, uno para subir un archivo y el otro para obtenerlo.
El endpoint "/upload" permite subir un archivo al servidor, donde quedará almacenado en la carpeta "/uploads" dentro del mismo proyecto. Quedará almacenado con un nombre generado por un hash. El hash, el nombre original, la extensión y su tamaño en bytes quedarán almacenados en una base de datos, la que viene incluída dentro del proyecto.
El endpoint "/dowload/{hash}" me permite recuperar el archivo original, con su nombre y extensión originales.

## Notas
   Por alguna razón que no he indagado, lombok no se instalaba al agregarlo como referencia mediante Maven. Tuve que bajar el archivo .jar y ejecutarlo para que quedara instalado dentro de Eclipse, como lo indica [\[5\]](#ref5).

## Referencias

- [1] <a id="ref1" href="https://www.baeldung.com/sprint-boot-multipart-requests">Multipart Request Handling in Spring</a>
- [2] <a id="ref2" href="https://www.bezkoder.com/spring-boot-file-upload/">Spring Boot File upload example with Multipart File</a>
- [3] <a id="ref3" href="https://spring.io/guides/gs/uploading-files/">Uploading Files</a>
- [4] <a id="ref4" href="https://javabydeveloper.com/lombok-spring-boot-example/">Lombok + Spring Boot Example</a>
- [5] <a id="ref5" href="https://www.baeldung.com/lombok-ide">Setting up Lombok with Eclipse and Intellij</a>
