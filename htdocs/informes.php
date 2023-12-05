<?php

    include 'conexion.php';

if(isset($_GET["fk_num_historia_clinica"])){

$fk_num_historia_clinica =$_GET['fk_num_historia_clinica'];

// Consulta SQL para recuperar informes y archivos PDF basados en fk_num_historia_clinica
$consulta = "SELECT I.*, IFNULL(I.PDF, '') AS PDF
            FROM INFORMES I
            LEFT JOIN PACIENTES P ON P.fk_num_historia_clinica = I.fk_num_historia_clinica
            WHERE P.fk_num_historia_clinica= ?";


        // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("i", $fk_num_historia_clinica);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();
    
    
if($resultado -> num_rows > 0){
    $return_arr['informes'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['informes'], array(
        'num_informe'=> $fila['num_informe'],
'fk_num_historia_clinica'=> $fila['fk_num_historia_clinica'],
'tipo_informe'=> $fila['tipo_informe'],
'fecha'=> $fila['fecha'],
'centro'=> $fila['centro'],
'responsable'=> $fila['responsable'],
'servicio_unidad_dispositivo'=> $fila['servicio_unidad_dispositivo'],
'servicio_de_salud'=> $fila['servicio_de_salud'],
'PDF' => base64_encode($fila['PDF'])  // Codifica el PDF en base64
));
    }

    echo json_encode($return_arr);
}


    $resultado -> close();
}

?>
