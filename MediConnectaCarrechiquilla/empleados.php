<?php
include 'conexion.php';


    $consulta = "SELECT E.* FROM empleados E";

    $resultado = $conexion->query($consulta);

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }

    $return_arr['empleados'] = array();
    while ($fila = $resultado->fetch_array()) {
        $return_arr['empleados'][] = array(
            'cod_empleado' => $fila['cod_empleado'],
            'foto' => $fila['foto'],
            'user' => $fila['user'],
            'pwd' => $fila['pwd'],
            'nombre' => $fila['nombre'],
            'apellidos' => $fila['apellidos'],
            'fk_cargo' => $fila['fk_cargo']
        );
    }

    echo json_encode($return_arr);


$resultado->close();
?>
