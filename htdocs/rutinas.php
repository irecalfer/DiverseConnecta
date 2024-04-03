<?php

include 'conexion.php';


    $consulta = "SELECT * FROM RUTINA";


    $resultado = $conexion ->query($consulta);


if($resultado -> num_rows > 0){
    $return_arr['rutina'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['rutina'], array(
    'id_tipo_rutina' => $fila['id_tipo_rutina'],
    'diario' => $fila['diario']
       ));
    }
   // Genera el formato JSON una vez que se han agrupado todos los alumnos
     echo json_encode($return_arr);
}

    $resultado -> close();


?>
