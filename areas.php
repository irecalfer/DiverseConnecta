<?php

    //llamamos a la conexion
    include_once "conexion.php";
    

    //Hacemos la consulta a la tabla, donde pediremos el nombre de la unidad y el área al que pertenece
    $consulta = "SELECT * FROM AREA";


    //Ejecutamos la consulta
    $resultado = $conexion ->query($consulta);


    //Ejecutamos un bucle
if($resultado -> num_rows > 0){
    $return_arr['datos_area'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['datos_area'], array(
            'id_area'=>$fila['id_area'], 
            'nombre_area'=> $fila['nombre_area']
        ));
    }
echo json_encode($return_arr);
}


?>
