<?php

include 'conexion.php';

if(isset($_GET["fecha_rutina"]) && isset($_GET["nombre"]) && isset($_GET["diario"])){

$fecha_rutina =$_GET['fecha_rutina'];
$nombre =$_GET['nombre'];
$diario =$_GET['diario'];

// Utilizamos sentencias preparadas para evitar problemas de seguridad y errores de sintaxis.
    $consulta = "SELECT PR.fk_cip_sns, PR.fk_id_rutina, PR.hora_rutina
                 FROM programacion_rutinas_pacientes PR
                 LEFT JOIN pacientes P ON PR.fk_cip_sns = P.cip_sns 
                 LEFT JOIN unidades U ON P.fk_id_unidad = U.id_unidad
                 LEFT JOIN rutina R ON PR.fk_id_rutina = R.id_tipo_rutina
                 WHERE PR.fecha_rutina = ? AND U.nombre = ? AND R.diario = ?";


     // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("sss", $fecha_rutina, $nombre, $diario);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();


if($resultado -> num_rows > 0){
    $return_arr['programacion'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['programacion'], array(
    'fk_cip_sns' => $fila['fk_cip_sns'],
    'fk_id_rutina' => $fila['fk_id_rutina'],
    'hora_rutina' => $fila['hora_rutina']
       ));
    }
   // Genera el formato JSON una vez que se han agrupado todos los pacientes
     echo json_encode($return_arr);
}

    $resultado -> close();

}
?>
