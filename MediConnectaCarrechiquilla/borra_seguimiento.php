<?php

    include 'conexion.php';
    
    //Recibe los parámetro de la app
    $id_seguimiento=$_POST["id_seguimiento"];

    $consulta = "DELETE FROM seguimiento WHERE id_seguimiento =?";

    // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("s", $id_seguimiento);

    // Ejecuta la consulta
if ($stmt->execute()) {
    // Éxito, se registró el parte
    $response = array('message' => 'Borrado exitoso');
    http_response_code(200); // Éxito
} else {
    // Error al registrar el parte
    $response = array('message' => 'Error al borrar el seguimiento');
    http_response_code(500); // Error
}

// Devuelve la respuesta en formato JSON
echo json_encode($response);
?>