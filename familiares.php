<?php

    include 'conexion.php';

if(isset($_GET["cip_sns"])){

$cip_sns =$_GET['cip_sns'];


$consulta = "SELECT F.*
             FROM FAMILIARES F
             LEFT JOIN PACIENTES_CONTACTO_FAMILIARES PC ON PC.dni_familiar = F.dni_familiar
            LEFT JOIN PACIENTES P ON P.cip_sns = PC.cip_sns
            WHERE P.cip_sns= ?";


        // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("s", $cip_sns);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();
    
if($resultado -> num_rows > 0){
    $return_arr['familiares_contacto'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['familiares_contacto'], array(
        'dni_familiar'=> $fila['dni_familiar'],
'nombre'=> $fila['nombre'],
'apellidos'=> $fila['apellidos'],
'telefono_contacto'=> $fila['telefono_contacto'],
'telefono_contacto_2'=> $fila['telefono_contacto_2']
       ));
    }
    echo json_encode($return_arr);
}


    $resultado -> close();
}

?>
