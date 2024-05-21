<?php

    include 'conexion.php';

if(isset($_GET["fechaInicio"]) && isset($_GET["fechaFin"])){

$fechaInicio = $_GET['fechaInicio'];
$fechaFin = $_GET['fechaFin'];

    $consulta = "SELECT CONCAT(PC.nombre,' ', PC.apellidos) AS nombrePaciente, P.descripcion, CONCAT(E.nombre,' ', E.apellidos) AS empleado, P.fecha
             FROM parte P
            LEFT JOIN pacientes PC ON PC.cip_sns = P.fk_cip_sns
            LEFT JOIN empleados E ON E.cod_empleado = P.fk_cod_empleado
            WHERE DATE(P.fecha) >= ? AND DATE(P.fecha) <= ?";

       // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("ss", $fechaInicio, $fechaFin);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();

    
if($resultado -> num_rows > 0){
    $return_arr['parte'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['parte'], array(
        'nombrePaciente'=> $fila['nombrePaciente'],
'descripcion'=> $fila['descripcion'],
'empleado'=> $fila['empleado'],
'fecha'=> $fila['fecha']
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
}
?>
