<?php
include 'conexion.php';


    $consulta = "SELECT N.* FROM nivel_escolar N";

    $resultado = $conexion->query($consulta);

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }

    $return_arr['nivel_escolar'] = array();
    while ($fila = $resultado->fetch_array()) {
        $return_arr['nivel_escolar'][] = array(
            'id_nivel_escolar' => $fila['id_nivel_escolar'],
            'nombre_nivel' => $fila['nombre_nivel']
        );
    }

    echo json_encode($return_arr);


$resultado->close();
?>
