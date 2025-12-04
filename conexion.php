<?php
$servidor = "localhost";
$usuario_db = "root";     
$password_db = "";       
$nombre_db = "mejoratuhp_db";

$conexion = new mysqli($servidor, $usuario_db, $password_db, $nombre_db);

if ($conexion->connect_error) {
    die(json_encode([
        "estado" => "error", 
        "mensaje" => "Fallo al conectar: " . $conexion->connect_error
    ]));
}


$conexion->set_charset("utf8");

?>