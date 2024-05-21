<?php

    include 'conexion.php';
    
    //Recibe los parámetro de la app
    $fecha_y_hora=$_POST["fecha_y_hora"];
    $fk_cip_sns_paciente=$_POST["fk_cip_sns_paciente"];
    $lugar_caida=$_POST["lugar_caida"];
    $factores_de_riesgo=$_POST["factores_de_riesgo"];
    $causas=$_POST["causas"];
    $circunstancias=$_POST["circunstancias"];
    $consecuencias=$_POST["consecuencias"];
    $unidad=$_POST["unidad"];
    $caida_presenciada=$_POST["caida_presenciada"];
    $avisado_a_familiares=$_POST["avisado_a_familiares"];
    $observaciones=$_POST["observaciones"];
    $fk_cod_empleado=$_POST["fk_cod_empleado"];

    $consulta = "INSERT INTO parte_de_caidas(fecha_y_hora, fk_cip_sns_paciente, lugar_caida, factores_de_riesgo, causas, circunstancias, consecuencias, unidad, caida_presenciada, avisado_a_familiares, observaciones, fk_cod_empleado)
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

    // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("ssssssssssss", $fecha_y_hora, $fk_cip_sns_paciente, $lugar_caida, $factores_de_riesgo, $causas, $circunstancias, $consecuencias, $unidad, $caida_presenciada, $avisado_a_familiares, $observaciones, $fk_cod_empleado);

    // Ejecuta la consulta
if ($stmt->execute()) {
    // Éxito, se registró el parte
    $response = array('message' => 'Registro exitoso');
    http_response_code(200); // Éxito
} else {
    // Error al registrar el parte
    $response = array('message' => 'Error al registrar la caída');
    http_response_code(500); // Error
}

// Devuelve la respuesta en formato JSON
echo json_encode($response);
?>
