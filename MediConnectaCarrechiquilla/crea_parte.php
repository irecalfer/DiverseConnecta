<?php

    include 'conexion.php';
    
    //Recibe los parámetro de la app
    $fk_cip_sns=$_POST["fk_cip_sns"];
    $descripcion=$_POST["descripcion"];
    $fk_cod_empleado=$_POST["fk_cod_empleado"];
    $fecha=$_POST["fecha"];

    $consulta = "INSERT INTO parte(fk_cip_sns, descripcion, fk_cod_empleado, fecha)
                VALUES(?,?,?,?)";

    // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("ssss", $fk_cip_sns, $descripcion, $fk_cod_empleado, $fecha);

    // Ejecuta la consulta
if ($stmt->execute()) {
    // Éxito, se registró el parte
    $response = array('message' => 'Registro exitoso');
    http_response_code(200); // Éxito
} else {
    // Error al registrar el parte
    $response = array('message' => 'Error al registrar el parte');
    http_response_code(500); // Error
}

// Devuelve la respuesta en formato JSON
echo json_encode($response);
?>
