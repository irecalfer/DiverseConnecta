<?php
include 'conexion.php';


    $consulta = "SELECT A.* FROM aulas A";

    $resultado = $conexion->query($consulta);

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }

    $return_arr['aulas'] = array();
    while ($fila = $resultado->fetch_array()) {
        $return_arr['aulas'][] = array(
            'id_aula' => $fila['id_aula'],
            'nombre' => $fila['nombre'],
            'fk_nivel_escolar' => $fila['fk_nivel_escolar']
        );
    }

    echo json_encode($return_arr);


$resultado->close();
?>
