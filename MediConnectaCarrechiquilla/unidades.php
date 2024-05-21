<?php

// Llamamos a la conexión
include_once "conexion.php";

$return_arr = array(); // Inicializamos el array de retorno

if(isset($_GET['nombre_area'])) {
    // Escapamos el valor del parámetro para evitar inyección SQL
    $nombre_area = $conexion->real_escape_string($_GET['nombre_area']);

    // Hacemos la consulta a la tabla, donde pediremos el nombre de la unidad y el área al que pertenece
    $consulta = "SELECT U.* FROM unidades U INNER JOIN area A ON U.fk_id_area = A.id_area WHERE A.nombre_area='$nombre_area'";

    // Ejecutamos la consulta
    $resultado = $conexion->query($consulta);

    // Verificamos si la consulta fue exitosa
    if($resultado) {
        // Ejecutamos un bucle
        if($resultado->num_rows > 0) {
            $return_arr['datos_unidades'] = array();
            while($fila = $resultado->fetch_array()) {
                array_push($return_arr['datos_unidades'], array(
                    'id_unidad' => $fila['id_unidad'], 
                    'nombre' => $fila['nombre'],
                    'fk_id_area' => $fila['fk_id_area']
                ));
            }
            echo json_encode($return_arr);
        } else {
            echo "No se encontraron unidades para el área especificada.";
        }
    } else {
        echo "Error en la consulta: " . $conexion->error;
    }
} else {
    $consulta = "SELECT * FROM unidades";
    $resultado = $conexion->query($consulta);

    // Verificamos si la consulta fue exitosa
    if($resultado) {
        // Ejecutamos un bucle
        if($resultado->num_rows > 0) {
            $return_arr['datos_unidades'] = array();
            while($fila = $resultado->fetch_array()) {
                array_push($return_arr['datos_unidades'], array(
                    'id_unidad' => $fila['id_unidad'], 
                    'nombre' => $fila['nombre'],
                    'fk_id_area' => $fila['fk_id_area']
                ));
            }
            echo json_encode($return_arr);
        } else {
            echo "No se encontraron unidades.";
        }
    } else {
        echo "Error en la consulta: " . $conexion->error;
    }
}

?>

