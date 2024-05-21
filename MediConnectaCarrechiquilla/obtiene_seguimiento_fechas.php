<?php
include 'conexion.php';


    // Escapamos el valor del parámetro para evitar inyección SQL
    $fecha_inicio = $_GET['fecha_inicio'];
    $fecha_fin = $_GET['fecha_fin'];
    $fk_id_alumno =$_GET['fk_id_alumno'];

    $consulta = "SELECT * FROM seguimiento WHERE fk_id_alumno = ? AND DATE(fecha_y_hora)>=? AND DATE(fecha_y_hora)<=? ORDER BY fecha_y_hora DESC";

    $stmt = $conexion->prepare($consulta);

     // Vincula los parámetros
     $stmt->bind_param("iss", $fk_id_alumno, $fecha_inicio, $fecha_fin);
 
     // Ejecuta la consulta
     $stmt->execute();
 
     // Obtiene el resultado
     $resultado = $stmt->get_result();

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }
    if($resultado -> num_rows > 0){
        $return_arr['seguimiento_fecha'] = array();
        while($fila = $resultado->fetch_array()){
            array_push($return_arr['seguimiento_fecha'], array(
            'id_seguimiento'=> $fila['id_seguimiento'],
    'fecha_y_hora'=> $fila['fecha_y_hora'],
    'descripcion'=> $fila['descripcion'],
    'fk_id_alumno'=> $fila['fk_id_alumno']
           ));
        }
    }
    echo json_encode($return_arr);


$resultado->close();
?>
