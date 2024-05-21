<?php

    //llamamos a la conexion
    include_once "conexion.php";
    

    //Hacemos la consulta a la tabla, donde pediremos el nombre de la unidad y el área al que pertenece
    $consulta = "SELECT * FROM curso";


    //Ejecutamos la consulta
    $resultado = $conexion ->query($consulta);


    //Ejecutamos un bucle
if($resultado -> num_rows > 0){
    $return_arr['cursos'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['cursos'], array(
            'añoInicio'=>$fila['añoInicio'], 
            'añoFin'=> $fila['añoFin']
        ));
    }
echo json_encode($return_arr);
}


?>
