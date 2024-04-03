<?php

include 'conexion.php';

if(isset($_GET["cip_sns"])){

$cip_sns =$_GET['cip_sns'];

// Utilizamos sentencias preparadas para evitar problemas de seguridad y errores de sintaxis.
    $consulta = "SELECT 
  P.pauta, 
    CASE
    WHEN P.observaciones != NULL THEN P.observaciones
    ELSE ''
    END AS observaciones,
  CASE 
    WHEN P.id_pauta = 4 THEN R.mañana
    ELSE ''
  END AS mañana,
  CASE 
    WHEN P.id_pauta = 4 THEN R.tarde
    ELSE ''
  END AS tarde,
  CASE 
    WHEN P.id_pauta = 4 THEN R.noche
    ELSE ''
  END AS noche
FROM PACIENTES_TIENEN_PAUTAS PP
LEFT JOIN PAUTAS P ON PP.fk_id_pauta = P.id_pauta
LEFT JOIN AREA A ON A.id_area = P.fk_id_area
LEFT JOIN RUTINA_ANATOMICOS R ON R.id_rutina_anatomicos = PP.fk_rutina_anatomicos
LEFT JOIN PACIENTES PC ON PC.cip_sns = PP.fk_cip_sns
WHERE PC.cip_sns = ?";


     // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("s", $cip_sns);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();


if($resultado -> num_rows > 0){
    $return_arr['pautas'] = array();
    while($fila = $resultado->fetch_array()){
        array_push($return_arr['pautas'], array(
    'pauta' => $fila['pauta'],
    'observaciones' => $fila['observaciones'],
    'mañana' => $fila['mañana'],
'tarde' => $fila['tarde'],
'noche' => $fila['noche']
       ));
    }
   // Genera el formato JSON una vez que se han agrupado todos los alumnos
     echo json_encode($return_arr);
}

    $resultado -> close();

}
?>
