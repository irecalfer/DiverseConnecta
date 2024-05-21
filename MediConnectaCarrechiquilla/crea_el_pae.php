<?php

    include 'conexion.php';
    
    //Recibe los parámetro de la app
    $cursoEmisionInicio=$_POST["cursoEmisionInicio"];
    $cursoEmisionFin=$_POST["cursoEmisionFin"];
    $tutor=$_POST["tutor"];
    $enfermera=$_POST["enfermera"];
    $protesisPae=$_POST["protesisPae"];
    $ortesisPae=$_POST["ortesisPae"];
    $gafasPae=$_POST["gafasPae"];
    $audifonosPae=$_POST["audifonosPae"];
    $otrosPae=$_POST["otrosPae"];
$fiebrePae=$_POST["fiebrePae"];
$alergiasPae=$_POST["alergiasPae"];
$diagnostico=$_POST["diagnostico"];
$dietasPae=$_POST["dietasPae"];
$medicacionPae=$_POST["medicacionPae"];
$datos=$_POST["datos"];
$id_alumno=$_POST["id_alumno"];
$id_enfermera_modifica=$_POST["id_enfermera_modifica"];
$tiempo_modificado = $_POST["tiempo_modificado"];
$fk_id_aula=$_POST["fk_id_aula"];

    $consulta = "INSERT INTO pae(curso_emision_inicio,curso_emision_final, alergias,diagnostico_clinico,fiebre,dieta_alimentacion,protesis,ortesis, gafas,audifonos,otros, medicacion,fk_id_enfermero,fk_id_profesor,fk_id_alumnos,datos_importantes,ultima_modificacion_personal, ultima_modificacion_tiempo,fk_id_aula)
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("sssssssssssssssssss", $cursoEmisionInicio, $cursoEmisionFin, $alergiasPae, $diagnostico, $fiebrePae, $dietasPae, $protesisPae, $ortesisPae, $gafasPae, $audifonosPae, $otrosPae, $medicacionPae, $enfermera, $tutor, $id_alumno, $datos, $id_enfermera_modifica, $tiempo_modificado, $fk_id_aula);

    // Ejecuta la consulta
if ($stmt->execute()) {
    // Éxito, se registró el parte
    $response = array('message' => 'Registro exitoso');
    http_response_code(200); // Éxito
} else {
    // Error al registrar el parte
    $response = array('message' => 'Error al registrar la caída');
    http_response_code(500); // Error
}

// Devuelve la respuesta en formato JSON
echo json_encode($response);
?>
