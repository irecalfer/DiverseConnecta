<?php

    include 'conexion.php';

    if(isset($_GET["fk_id_alumno"])){
        $fk_id_alumno =$_GET['fk_id_alumno'];

        $consulta = "SELECT * FROM seguimiento WHERE fk_id_alumno = ? ORDER BY fecha_y_hora DESC";


     // Prepara la consulta
     $stmt = $conexion->prepare($consulta);

     // Vincula los parámetros
     $stmt->bind_param("i", $fk_id_alumno);
 
     // Ejecuta la consulta
     $stmt->execute();
 
     // Obtiene el resultado
     $resultado = $stmt->get_result();
    
if($resultado -> num_rows > 0){
    $return_arr['seguimiento'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['seguimiento'], array(
        'id_seguimiento'=> $fila['id_seguimiento'],
'fecha_y_hora'=> $fila['fecha_y_hora'],
'descripcion'=> $fila['descripcion'],
'fk_id_alumno'=> $fila['fk_id_alumno']
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
    }else{
        $consulta = "SELECT * FROM seguimiento";


    $resultado = $conexion ->query($consulta);
    
if($resultado -> num_rows > 0){
    $return_arr['seguimiento'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['seguimiento'], array(
        'id_seguimiento'=> $fila['id_seguimiento'],
'fecha_y_hora'=> $fila['fecha_y_hora'],
'descripcion'=> $fila['descripcion'],
'fk_id_alumno'=> $fila['fk_id_alumno']
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
    }



?>
