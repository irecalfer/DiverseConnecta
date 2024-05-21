<?php
include 'conexion.php';


    $consulta = "SELECT A.* FROM alumnos A";

    $resultado = $conexion->query($consulta);

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }

    $return_arr['alumnos'] = array();
    while ($fila = $resultado->fetch_array()) {
        $return_arr['alumnos'][] = array(
            'id_alumno' => $fila['id_alumno'],
            'nombre' => $fila['nombre'],
            'apellidos' => $fila['apellidos'],
            'foto' => $fila['foto'],
            'dni' => $fila['dni'],
            'sexo' => $fila['sexo'],
            'fecha_nacimiento' => $fila['fecha_nacimiento'],
            'fk_aula' => $fila['fk_aula'],
            'grado_discapacidad' => $fila['grado_discapacidad']
        );
    }

    echo json_encode($return_arr);


$resultado->close();
?>
