# POO-TP-Final


Benjamin Delasoie 61231
Lucas Agustin Ferreiro 61595
Melisa Limachi 59463

Funcionalidades agregadas:

-Se agregaron botones que permiten crear una elipse, un cuadrado  una linea.
---Para crear una elipse se debe hacer click, mantener presionado y arrastrar el mouse hacia abajo a la derecha , creando un rectangulo imaginario que cual contendra a la elipse.
---Para crear un cuadrado se debe hacer click, mantener presionado y arrastrar hacia abajo hacia a la derecha.
---Para crear una linea se hace click, se mantiene pulsado y se arrastra en cualquier direccion.

-Se agrego una seccion llamada "Borde", el cual consiste en una barra movible que permite elegir el grosor del borde de las figuras creadas; justo por debajo de esta misma se encuentra una casilla que permite elegir el color de dicho borde.
-Se agrego una seccion "Relleno" la cual posee una casilla que permite elegir el color del relleno de las figuras.

-Se modifico el comportamiento del boton "Seleccionar" para que ahora este permita crear un rectangulo imaginario, y todas las figuras que se encuentren dentro de el seran seleccionadas. Todas la figuras que estan seleccionadas cambiaran su color de borde a rojo y se les permitira moverse y cambiar los colores del relleno y el borden, ademas del grosor
-Tambien se agrego un nuevo boton "Borrar" que elimina todas las figuras que fueron seleccionadas.

-Se agregaron dos nuevos botones "Al Frente" y "Al Fondo" los cuales permiten traer al frente o mandar al fondo la/s figuras/s selecciondas.

-------------------------------------------------------------------

Modificaciones realizadas al proyecto original:

-Se modifico la clase CanvasState de forma tal que esta ahora extienda de LinkedList, con el proposito de ya poseer los metodos add y para obtener el iterador. La funcionalidad de la LinkedList es que permite recorrer la coleccion de figuras en ambos sentidos, lo cual fue una ventaja muy importante al momento de crear los botones "Al Frente" y "Al Fondo".

-Originalmente, la clase abstracta Figure se encontraba vacia. Dicha clase fue modificada pueda implementar las interfaces Movable, Drawable y Colorable, donde algunos de estos metodos se declaran default en Figure (con el proposito de que todas las figuras poseen dicho metodo, como por ejemplo "move" y los getters y setters de los edgeColor y fillColor). Ademas se agregaron metodos abstractos (tales como "pointBelong", "inEnclosedBy", "draw", entre otros), con el proposito de que todas las figuras esten obligadas a sobreescribir dichos metodos y tenerlos bien implementados.


-------------------------------------------------------------------

Problemas encontrados durante el desarrollo:


-------------------------------------------------------------------

Cambios hechos en la implementación provista por la Cátedra:
