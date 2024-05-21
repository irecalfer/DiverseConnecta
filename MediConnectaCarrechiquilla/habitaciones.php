<?php

include 'conexion.php';

// Establece la codificación de caracteres a UTF-8
header('Content-Type: application/json; charset=utf-8');

$resultados = array(); // Array para almacenar los resultados

if(isset($_GET['nombre_unidad'])){
    // Escapamos el valor del parámetro para evitar inyección SQL
    $nombre_unidad = $conexion->real_escape_string($_GET['nombre_unidad']);
    
    // Consulta SQL para obtener el ID de la unidad a partir del nombre
    $consulta_id_unidad = "SELECT id_unidad FROM unidades WHERE nombre = '$nombre_unidad'";
    
    // Ejecutar la consulta para obtener el ID de la unidad
    $resultado_id_unidad = mysqli_query($conexion, $consulta_id_unidad);

    if ($resultado_id_unidad && mysqli_num_rows($resultado_id_unidad) > 0) {
        $fila_id_unidad = mysqli_fetch_assoc($resultado_id_unidad);
        $id_unidad = $fila_id_unidad['id_unidad'];
        
        // Consulta SQL para obtener los números de habitación pertenecientes a la unidad
        $consulta_habitaciones = "SELECT num_habitacion FROM habitaciones WHERE fk_id_unidad = '$id_unidad'";
        
        // Ejecutar la consulta para obtener las habitaciones
        $resultado_habitaciones = mysqli_query($conexion, $consulta_habitaciones);

        if ($resultado_habitaciones) {
            $habitaciones = array();
            while($fila_habitacion = mysqli_fetch_assoc($resultado_habitaciones)) {
                $habitaciones[] = $fila_habitacion['num_habitacion'];
            }
            $resultados = $habitaciones;
            mysqli_free_result($resultado_habitaciones);
        } else {
            $resultados['error'] = "Error en la consulta de habitaciones: " . mysqli_error($conexion);
        }
        mysqli_free_result($resultado_id_unidad);
    } else {
        $resultados['error'] = "No se encontró la unidad con el nombre proporcionado.";
    }
} else {
    $resultados['error'] = "El parámetro nombre_unidad no está definido.";
}

/* cerrar conexión */
mysqli_close($conexion);

// Codificar el array de resultados a JSON con codificación UTF-8
echo json_encode($resultados, JSON_UNESCAPED_UNICODE);
?>

