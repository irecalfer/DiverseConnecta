<?php

    include 'conexion.php';


$consulta = "SELECT * FROM NORMAS_EMPRESA";


        $resultado = $conexion ->query($consulta);
    
if($resultado -> num_rows > 0){
    $return_arr['normas'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['normas'], array(
        'nombre_norma'=> $fila['nombre_norma'],
'contenido'=> $fila['contenido']
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();


?>
