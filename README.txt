==============================
        MEJORA TU HP
==============================
Versión: 1.0
Idioma: Español
Tecnologías: Java, MySQL, PHP


1. DESCRIPCIÓN
------------------------------------------------------------------------
"MejoraTuHP" es una aplicación de escritorio diseñada para transformar
la gestión de tareas diarias en una experiencia similar a la de un 
videojuego estilo RPG.

El objetivo principal es motivar al usuario a cumplir sus deberes diarios
otorgando recompensas inmediatas en forma de puntos de experiencia (XP)
y Monedas de Oro virtuales.

Características Principales:
- Sistema de misiones y tareas personalizables.
- Progresión RPG al poder subir de nivel ganando experiencia.
- Economía virtual al poder ganar oro completando tareas.
- Tienda donde se pueden comprar ítems para personalización del avatar. 
- Conectividad híbrida con lenguaje PHP para consultas y guardado de datos
en bases.


2. REQUISITOS
------------------------------------------------------------------------
- Sistema operativo: Windows 10/11, macOS o Linux.
- Java: Tener instalado el JRE o JDK (Versión 8 o superior).
- Base de datos: Servidor MySQL activo (Recomendado: XAMPP).
- Conexión: Puerto 3306 disponible para la base de datos.


3. GUÍA DE INSTALACIÓN
------------------------------------------------------------------------
> PREPARAR LA BASE DE DATOS
1. Asegúrate de tener XAMPP abierto y el servicio de MySQL iniciado.
2. Abre un gestor de base de datos como phpMyAdmin o MySQL Workbench.
3. Importa el archivo "database.sql" incluido en este proyecto.

> EJECUCIÓN
Si es Usuario:
1. Descomprime el archivo .zip descargado.
2. Mantén la carpeta "lib" junto al archivo "MejoraTuHP.jar".
3. Haz doble clic en "MejoraTuHP.jar".

Si es admin o desarrollador:
1. Abre NetBeans.
2. Ve a "Archivo" > "Abrir proyecto" y selecciona la carpeta de este proyecto.
3. Presiona "Run".


4. GUÍA DE USO
------------------------------------------------------------------------

[INICIO DE SESIÓN]
- Al abrir la app, verás la pantalla de "Login".
- Si es tu primera vez, haz clic en "Registrarse".
- Crea un usuario con tu nombre, correo y contraseña.

[PESTAÑA 1: MISIONES]
- Aquí gestionas tus tareas diarias.
- Botón "Crear Nueva" permite definir un título, tipo de misión y las recompensas
(XP y Oro) que obtendrás.
- Botón "Predefinidas" permite cargar misiones de ejemplo si no quieres escribir.
- Botón "Completar" se presiona tras seleccionar una misión para ganar tus premios.

[PESTAÑA 2: MI PERFIL]
- En el panel izquierdo se muestra tu avatar, nivel actual y barras de progreso.
- En el panel derecho "Inventario" se muestran los objetos que has comprado.
- El botón "Equipar" se puede presionar tras seleccionar un objeto del inventario, 
para hacerlo aparecer en tu avatar.

[PESTAÑA 3: TIENDA]
- Gasta tus monedas de oro aquí.
- Puedes comprar fondos, marcos, pociones y accesorios.
- Si no tienes suficiente oro, vuelve a las misiones y proponte a realizarlas.

========================================================================