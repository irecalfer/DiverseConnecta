<?php

    include 'conexion.php';

if(isset($_GET["fechaInicio"]) && isset($_GET["fechaFin"])){

$fechaInicio = $_GET['fechaInicio'];
$fechaFin = $_GET['fechaFin'];

    $consulta = "SELECT P.fecha_y_hora, CONCAT(PC.nombre,' ', PC.apellidos) AS nombrePaciente,P.lugar_caida, P.factores_de_riesgo, P.causas, P.circunstancias, P.consecuencias, P.unidad, P.caida_presenciada, P.avisado_a_familiares, P.observaciones, CONCAT(E.nombre,' ', E.apellidos) AS empleado
             FROM PARTE_DE_CAIDAS P
            LEFT JOIN PACIENTES PC ON PC.cip_sns = P.fk_cip_sns_paciente
            LEFT JOIN EMPLEADOS E ON E.cod_empleado = P.fk_cod_empleado
            WHERE DATE(P.fecha_y_hora) >= ? AND DATE(P.fecha_y_hora) <= ?";

       // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("ss", $fechaInicio, $fechaFin);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();

    
if($resultado -> num_rows > 0){
    $return_arr['parte_caidas'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['parte_caidas'], array(
        'fecha_y_hora'=> $fila['fecha_y_hora'],
'nombrePaciente'=> $fila['nombrePaciente'],
'lugar_caida'=> $fila['lugar_caida'],
'factores_de_riesgo'=> $fila['factores_de_riesgo'],
'causas'=> $fila['causas'],
'circunstancias'=> $fila['circunstancias'],
'consecuencias'=> $fila['consecuencias'],
'unidad'=> $fila['unidad'],
'caida_presenciada'=> $fila['caida_presenciada'],
'avisado_a_familiares'=> $fila['avisado_a_familiares'],
'observaciones'=> $fila['observaciones'],
'empleado'=> $fila['empleado']
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
}
?>
