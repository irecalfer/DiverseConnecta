<?php
include 'conexion.php';

if(isset($_GET['id_alumno'])) {
    // Escapamos el valor del parámetro para evitar inyección SQL
    $id_alumno = $conexion->real_escape_string($_GET['id_alumno']);

    $consulta = "SELECT P.* FROM pae P WHERE P.fk_id_alumnos='$id_alumno'";

    $resultado = $conexion->query($consulta);

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }

    $return_arr['pae'] = array();
    while ($fila = $resultado->fetch_array()) {
        $return_arr['pae'][] = array(
            'id_pae' => $fila['id_pae'],
            'curso_emision_inicio' => $fila['curso_emision_inicio'],
            'curso_emision_final' => $fila['curso_emision_final'],
            'alergias' => $fila['alergias'],
            'diagnostico_clinico' => $fila['diagnostico_clinico'],
            'fiebre' => $fila['fiebre'],
            'dieta_alimentacion' => $fila['dieta_alimentacion'],
            'protesis' => $fila['protesis'],
            'ortesis' => $fila['ortesis'],
            'gafas' => $fila['gafas'],
'audifonos' => $fila['audifonos'],
'otros' => $fila['otros'],
'medicacion' => $fila['medicacion'],
'fk_id_enfermero' => $fila['fk_id_enfermero'],
'fk_id_profesor' => $fila['fk_id_profesor'],
'fk_id_alumnos' => $fila['fk_id_alumnos'],
'datos_importantes' => $fila['datos_importantes'],
'ultima_modificacion_personal' => $fila['ultima_modificacion_personal'],
'ultima_modificacion_tiempo' => $fila['ultima_modificacion_tiempo'],
'fk_id_aula' => $fila['fk_id_aula']
        );
    }

    echo json_encode($return_arr);

}
$resultado->close();
?>
