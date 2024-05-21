<?php

    include 'conexion.php';
    
    //Recibe los parámetro de la app
    $peso=$_POST["peso"];
    $talla=$_POST["talla"];
    $imc=$_POST["imc"];
    $percentil=$_POST["percentil"];
    $temperatura=$_POST["temperatura"];
    $tension_arterial=$_POST["tension_arterial"];
    $saturacion_o2=$_POST["saturacion_o2"];
    $fk_trimestre=$_POST["fk_trimestre"];
    $fk_id_pae=$_POST["fk_id_pae"];
$frecuencia_cardiaca=$_POST["frecuencia_cardiaca"];

    $consulta = "INSERT INTO control_somatometrico(peso,talla,imc,percentil,temperatura,tension_arterial,frecuencia_cardiaca,saturacion_o2,fk_trimestre, fk_id_pae)
                VALUES(?,?,?,?,?,?,?,?,?,?)";

    // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("dssddiiiii", $peso, $talla, $imc, $percentil, $temperatura, $tension_arterial, $frecuencia_cardiaca, $saturacion_o2, $fk_trimestre, $fk_id_pae);

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
