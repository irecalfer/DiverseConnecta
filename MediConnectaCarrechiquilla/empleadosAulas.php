<?php
include 'conexion.php';


    $consulta = "SELECT E.* FROM empleados_trabajan_aulas E";

    $resultado = $conexion->query($consulta);

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }

    $return_arr['empleadosAulas'] = array();
    while ($fila = $resultado->fetch_array()) {
        $return_arr['empleadosAulas'][] = array(
            'id_relacion_empleados_aulas' => $fila['id_relacion_empleados_aulas'],
            'cod_empleado' => $fila['cod_empleado'],
            'id_aula' => $fila['id_aula']
        );
    }

    echo json_encode($return_arr);


$resultado->close();
?>
