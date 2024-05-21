<?php
include 'conexion.php';

if(isset($_GET['id_pae'])) {
    // Escapamos el valor del parámetro para evitar inyección SQL
    $id_pae = $conexion->real_escape_string($_GET['id_pae']);

    $consulta = "SELECT C.* FROM control_somatometrico C WHERE C.fk_id_pae='$id_pae'";

    $resultado = $conexion->query($consulta);

    if (!$resultado) {
        die("Error en la consulta: " . $conexion->error);
    }

    $return_arr['control'] = array();
    while ($fila = $resultado->fetch_array()) {
        $return_arr['control'][] = array(
            'id_control' => $fila['id_control'],
            'peso' => $fila['peso'],
            'talla' => $fila['talla'],
            'imc' => $fila['imc'],
            'percentil' => $fila['percentil'],
            'temperatura' => $fila['temperatura'],
            'tension_arterial' => $fila['tension_arterial'],
            'frecuencia_cardiaca' => $fila['frecuencia_cardiaca'],
            'saturacion_o2' => $fila['saturacion_o2'],
            'fk_trimestre' => $fila['fk_trimestre'],
'fk_id_pae' => $fila['fk_id_pae']
        );
    }

    echo json_encode($return_arr);
}

$resultado->close();
?>
