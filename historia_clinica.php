<?php

    include 'conexion.php';

if(isset($_GET["cip_sns"])){

$cip_sns =$_GET['cip_sns'];


$consulta = "SELECT HC.*
             FROM HISTORIA_CLINICA HC
            LEFT JOIN PACIENTES P ON P.fk_num_historia_clinica = HC.num_historia_clinica
            WHERE P.cip_sns= ?";


        // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("s", $cip_sns);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();
    
if($resultado -> num_rows > 0){
    // Obtén la primera fila, ya que se espera una sola historia clínica
        $fila = $resultado->fetch_array();

        $return_arr['historia_clinica'] = array(
            'datos_salud' => $fila['datos_salud'],
            'tratamiento' => $fila['tratamiento']
        );
echo json_encode($return_arr);
}


    $resultado -> close();
}

?>
