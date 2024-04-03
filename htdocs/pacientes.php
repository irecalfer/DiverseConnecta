<?php

    include 'conexion.php';

if(isset($_GET["nombre"])){
    $consulta = "SELECT P.*
                 FROM PACIENTES P
                 INNER JOIN UNIDADES U ON P.fk_id_unidad = U.id_unidad
                 WHERE U.nombre='$_GET[nombre]'";


    $resultado = $conexion ->query($consulta);
    
if($resultado -> num_rows > 0){
    $return_arr['alumnos'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['alumnos'], array(
        'cip_sns'=> $fila['cip_sns'],
'nombre'=> $fila['nombre'],
'apellidos'=> $fila['apellidos'],
'foto'=> $fila['foto'],
'num_seguridad_social'=> $fila['num_seguridad_social'],
'fecha_nacimiento'=> $fila['fecha_nacimiento'],
'dni'=> $fila['dni'],
'estado_civil'=> $fila['estado_civil'],
'lugar_nacimiento'=> $fila['lugar_nacimiento'],
'sexo'=> $fila['sexo'],
'fk_id_unidad'=> $fila['fk_id_unidad'],
'fk_id_seguro'=> $fila['fk_id_seguro'],
'fk_num_habitacion'=> $fila['fk_num_habitacion'],
'fk_num_historia_clinica'=> $fila['fk_num_historia_clinica'],
'fecha_ingreso'=> $fila['fecha_ingreso'],
'estado_civil'=> $fila['estado_civil']
        
        
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
}
?>
