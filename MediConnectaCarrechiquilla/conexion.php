<?php

$hostname = "localhost"; // El servidor que utilizaremos, en este caso será el localhost
$username = "root"; // El usuario que acabamos de crear en la base de datos
$password = ""; // La contraseña del usuario que utilizaremos
$database = "mediconnectaCarrechiquilla"; // El nombre de la base de datos

/*
Aquí abrimos la conexión en el servidor. Normalmente se envian 3 parametros (los datos del servidor, usuario y contraseña) a la función mysql_connect, si no hay ningún error la conexión será un éxito El @ que se ponde delante de la funcion, es para que no muestre el error al momento de ejecutarse, ya crearemos un código para eso
*/

$conexion=mysqli_connect($hostname,$username,$password,$database);


?>
