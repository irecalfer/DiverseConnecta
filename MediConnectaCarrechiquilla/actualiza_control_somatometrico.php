<?php

    include 'conexion.php';
    
    //Recibe los parámetro de la app
    $peso=$_POST["peso"];
    $talla=$_POST["talla"];
    $imc=$_POST["imc"];
    $percentil=$_POST["percentil"];
    $temperatura=$_POST["temperatura"];
    $tension_arterial=$_POST["tension_arterial"];
    $frecuencia_cardiaca=$_POST["frecuencia_cardiaca"];
    $saturacion_o2=$_POST["saturacion_o2"];
    $fk_trimestre=$_POST["fk_trimestre"];
    $fk_id_pae=$_POST["fk_id_pae"];


// Consulta SQL para actualizar el control somatométrico
$consulta = "UPDATE control_somatometrico 
             SET peso=?, talla=?, imc=?, percentil=?, temperatura=?, tension_arterial=?, frecuencia_cardiaca=?, saturacion_o2=? WHERE fk_id_pae=? AND fk_trimestre=?";

// Prepara la consulta
$stmt = $conexion->prepare($consulta);

// Vincula los parámetros
$stmt->bind_param("dsdddsiiii", $peso, $talla, $imc, $percentil, $temperatura, $tension_arterial, $frecuencia_cardiaca, $saturacion_o2, $fk_id_pae,$fk_trimestre);

// Registro de depuración para verificar el valor del trimestre recibido
error_log("Trimestre recibido: " . $fk_trimestre);

// Ejecuta la consulta
if ($stmt->execute()) {
    // Éxito, se actualizó el control somatométrico
    $response = array('message' => 'Actualización exitosa del control somatométrico');
    http_response_code(200); // Éxito
} else {
    // Error al actualizar el control somatométrico
    $response = array('message' => 'Error al actualizar el control somatométrico');
    http_response_code(500); // Error
}

// Devuelve la respuesta en formato JSON
echo json_encode($response);
?>
