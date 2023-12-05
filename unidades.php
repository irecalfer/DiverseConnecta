<?php

    //llamamos a la conexion
    include_once "conexion.php";
    


if(isset($_GET['nombre_area'])){


//Hacemos la consulta a la tabla, donde pediremos el nombre de la unidad y el área al que pertenece
    $consulta = "SELECT U.* FROM UNIDADES U INNER JOIN AREA A ON U.fk_id_area = A.id_area WHERE A.nombre_area='$_GET[nombre_area]'";


    //Ejecutamos la consulta
    $resultado = $conexion ->query($consulta);


    //Ejecutamos un bucle
if($resultado -> num_rows > 0){
    $return_arr['datos_unidades'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['datos_unidades'], array(
            'id_unidad'=>$fila['id_unidad'], 
            'nombre'=> $fila['nombre'],
            'fk_id_area'=>$fila['fk_id_area']

        ));
    }
echo json_encode($return_arr);
}
}

    //INNER JOIN AREA A ON U.fk_id_area = A.id_area WHERE A.nombre_area=Geriatría


?>
