# Integrar el contenido en Prism

Los diez archivos de `content/secciones` tienen los mismos nombres que los capítulos observados en el proyecto compartido del equipo. Son una propuesta corregida lista para reemplazar esos capítulos después de conservar una copia del informe actual.

1. Subir las imágenes de `images` al proyecto Prism, conservando nombres.
2. Copiar los capítulos corregidos en sus archivos correspondientes. No hace falta reemplazar la portada institucional ni `inf-pucv.cls`.
3. Comprobar que el preámbulo cargue `graphicx`, `longtable`, `array`, `booktabs` e `hyperref`. No duplicar paquetes que ya cargue la plantilla.
4. Recompilar y revisar tabla por tabla, diagramas, referencias, autores y numeración.
5. Actualizar GM-10 con el enlace al historial final una vez publicada la rama y añadir la evidencia de Eclipse que obtenga el equipo.
6. Exportar el PDF final desde Prism.

`main.tex` es un documento independiente para comprobar estas secciones fuera de la plantilla institucional. No reemplaza automáticamente el proyecto remoto. Los diagramas editables se encuentran en `GameLluvia/docs/uml`.

Los enlaces de código apuntan a `codex/entrega-final`; funcionarán en GitHub cuando se publique esa rama. Las líneas fueron comprobadas sobre la copia local revisada.
