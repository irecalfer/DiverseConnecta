<?php

    //llamamos a la conexion
    include_once "conexion.php";
    

    //Hacemos la consulta a la tabla, donde pediremos el nombre de la unidad y el área al que pertenece
    $consulta = "SELECT * FROM cargo";


    //Ejecutamos la consulta
    $resultado = $conexion ->query($consulta);


    //Ejecutamos un bucle
if($resultado -> num_rows > 0){
    $return_arr['cargos'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['cargos'], array(
            'id_cargo'=>$fila['id_cargo'], 
            'nombre'=> $fila['nombre']
        ));
    }
echo json_encode($return_arr);
}


?>
