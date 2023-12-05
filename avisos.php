<?php

    include 'conexion.php';


if(isset($_GET["fecha_aviso"])){
    
    
    $fecha_aviso =$_GET['fecha_aviso'];
    $consulta = "SELECT *
                 FROM AVISOS
                 WHERE fecha_aviso = '{$fecha_aviso}'";
    

   
    $resultado = $conexion ->query($consulta);

   if($resultado -> num_rows > 0){
    $return_arr['avisos'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['avisos'], array(
        'num_aviso'=> $fila['num_aviso'],
        'fecha_aviso'=> $fila['fecha_aviso'],
        'contenido'=> $fila['contenido'],   
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
    
}else{
     $consulta = "SELECT *
                 FROM AVISOS";
    
   
    $resultado = $conexion ->query($consulta);

    if($resultado -> num_rows > 0){
    $return_arr['avisos'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['avisos'], array(
        'num_aviso'=> $fila['num_aviso'],
        'fecha_aviso'=> $fila['fecha_aviso'],
        'contenido'=> $fila['contenido'],   
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
}
?>
