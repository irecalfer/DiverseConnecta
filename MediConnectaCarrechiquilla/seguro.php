<?php

    include 'conexion.php';

$consulta = "SELECT * FROM seguro";


    $resultado = $conexion ->query($consulta);
    
if($resultado -> num_rows > 0){
    $return_arr['seguro'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['seguro'], array(
        'id_seguro'=> $fila['id_seguro'],
'nombre'=> $fila['nombre'],
'telefono'=> $fila['telefono']
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();

?>
