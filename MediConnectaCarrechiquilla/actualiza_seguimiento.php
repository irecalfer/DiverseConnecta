<?php

    include 'conexion.php';
    
    //Recibe los parámetro de la app
    $id_seguimiento=$_POST["id_seguimiento"];
    $id_alumno=$_POST["id_alumno"];
    $descripcion=$_POST["descripcion"];
    $fecha=$_POST["fecha"];

    $consulta = "UPDATE seguimiento SET fecha_y_hora=?, descripcion=? WHERE id_seguimiento =?";

    // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("sss", $fecha, $descripcion, $id_seguimiento);

    // Ejecuta la consulta
if ($stmt->execute()) {
    // Éxito, se registró el parte
    $response = array('message' => 'Actualización exitosa');
    http_response_code(200); // Éxito
} else {
    // Error al registrar el parte
    $response = array('message' => 'Error al registrar el parte');
    http_response_code(500); // Error
}

// Devuelve la respuesta en formato JSON
echo json_encode($response);
?>