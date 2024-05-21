<?php

    include 'conexion.php';
    
    $exito = true; // Variable de bandera para rastrear el éxito de las operaciones

    //Recibe los parámetro de la app
    $cursoEmisionInicio=$_POST["cursoEmisionInicio"];
    $cursoEmisionFin=$_POST["cursoEmisionFin"];
    $nombreAulaNueva=$_POST["nombreAulaNueva"];
    $nombreTutor = $_POST["nombreTutor"];
    $nombreEnfermera = $_POST["nombreEnfermera"];


// Verificar y insertar curso_emision_inicio y curso_emision_final si no existen
$stmt_verificar_curso= $conexion->prepare("SELECT COUNT(*) AS total FROM curso WHERE añoInicio = ? AND añoFin= ?");
$stmt_verificar_curso->bind_param("ss", $cursoEmisionInicio, $cursoEmisionFin);
$stmt_verificar_curso->execute();
$resultado_verificar_curso = $stmt_verificar_curso->get_result();
$fila_verificar_curso = $resultado_verificar_curso->fetch_assoc();
$total_curso = $fila_verificar_curso['total'];

if($total_curso == 0){
    $stmt_cursos = $conexion->prepare("INSERT INTO curso (añoInicio, añoFin) VALUES (?, ?) ON DUPLICATE KEY UPDATE añoInicio=VALUES(añoInicio), añoFin=VALUES(añoFin)");
    $stmt_cursos->bind_param("ss", $cursoEmisionInicio, $cursoEmisionFin);
    if (!$stmt_cursos->execute()) {
        $exito = false; // Establecer la variable de bandera en false
    }
}
  



// Verificar y insertar tutor si no existe
// Obtener el ID del cargo "profesor"
    $stmt_cargo = $conexion->prepare("SELECT id_cargo FROM cargo WHERE nombre = 'Profesor'");
    $stmt_cargo->execute();
    $resultado_cargo = $stmt_cargo->get_result();
    $fila_cargo = $resultado_cargo->fetch_assoc();
    $id_cargo_profesor = $fila_cargo['id_cargo'];

    // Verificar si el tutor ya existe en la tabla empleados
    $stmt_verificar_tutor = $conexion->prepare("SELECT COUNT(*) AS total FROM empleados WHERE nombre = ? AND fk_cargo = ?");
    $stmt_verificar_tutor->bind_param("si", $nombreTutor, $id_cargo_profesor);
    $stmt_verificar_tutor->execute();
    $resultado_verificar_tutor = $stmt_verificar_tutor->get_result();
    $fila_verificar_tutor = $resultado_verificar_tutor->fetch_assoc();
    $total_tutor = $fila_verificar_tutor['total'];

if ($total_tutor == 0) {
    // Insertar el tutor en la tabla empleados
    $stmt_tutor = $conexion->prepare("INSERT INTO empleados (nombre, fk_cargo) VALUES (?, ?)");
    $stmt_tutor->bind_param("si", $nombreTutor, $id_cargo_profesor);
    if (!$stmt_tutor->execute()) {
        $exito = false; // Establecer la variable de bandera en false
    }
} 


// Verificar y insertar enfermera si no existe
// Obtener el ID del cargo "Enfermera"
    $stmt_cargo = $conexion->prepare("SELECT id_cargo FROM cargo WHERE nombre = 'Enfermera'");
    $stmt_cargo->execute();
    $resultado_cargo = $stmt_cargo->get_result();
    $fila_cargo = $resultado_cargo->fetch_assoc();
    $id_cargo_enfermera = $fila_cargo['id_cargo'];

// Verificar si el enfermero ya existe en la tabla empleados
$stmt_verificar_enfermero = $conexion->prepare("SELECT COUNT(*) AS total FROM empleados WHERE nombre = ? AND fk_cargo = ?");
$stmt_verificar_enfermero->bind_param("si", $nombreEnfermera, $id_cargo_enfermera);
$stmt_verificar_enfermero->execute();
$resultado_verificar_enfermero = $stmt_verificar_enfermero->get_result();
$fila_verificar_enfermero = $resultado_verificar_enfermero->fetch_assoc();
$total_enfermero = $fila_verificar_enfermero['total'];

if ($total_enfermero == 0) {
    // Insertar el enfermero en la tabla empleados
    $stmt_enfermera = $conexion->prepare("INSERT INTO empleados (nombre, fk_cargo) VALUES (?, ?)");
    $stmt_enfermera->bind_param("si", $nombreEnfermera, $id_cargo_enfermera);
    if (!$stmt_enfermera->execute()) {
        $exito = false; // Establecer la variable de bandera en false
    }
} 


// Verificar y insertar aula si no existe
// Verificar si el aula ya existe en la tabla aulas
$stmt_verificar_aulas = $conexion->prepare("SELECT COUNT(*) AS total FROM aulas WHERE nombre = ?");
$stmt_verificar_aulas->bind_param("s", $nombreAulaNueva);
$stmt_verificar_aulas->execute();
$resultado_verificar_aulas = $stmt_verificar_aulas->get_result();
$fila_verificar_aulas = $resultado_verificar_aulas->fetch_assoc();
$total_aulas = $fila_verificar_aulas['total'];

if($total_aulas == 0){
    $stmt_aula = $conexion->prepare("INSERT INTO aulas(nombre) VALUES (?) ON DUPLICATE KEY UPDATE nombre=VALUES(nombre)");
    $stmt_aula->bind_param("s", $nombreAulaNueva);
    if (!$stmt_aula->execute()) {
        $exito = false; // Establecer la variable de bandera en false
    }
} 

//Obtenemos id de profesor y del aula
//Obtenemos id del profesor
$stmt_cod_empleado_tutor = $conexion->prepare("SELECT cod_empleado FROM empleados WHERE nombre = ?");
$stmt_cod_empleado_tutor->bind_param("s", $nombreTutor);
$stmt_cod_empleado_tutor->execute();
$resultado_cod_empleado_tutor = $stmt_cod_empleado_tutor->get_result();
$fila_cod_empleado_tutor = $resultado_cod_empleado_tutor->fetch_assoc();
$cod_empleado_tutor = $fila_cod_empleado_tutor['cod_empleado'];
//Obtenemos id del aula
$stmt_id_aula = $conexion->prepare("SELECT id_aula FROM aulas WHERE nombre = ?");
$stmt_id_aula->bind_param("s", $nombreAulaNueva);
$stmt_id_aula->execute();
$resultado_id_aula = $stmt_id_aula->get_result();
$fila_id_aula = $resultado_id_aula->fetch_assoc();
$id_aula = $fila_id_aula['id_aula'];
//Verificar e insertar la relación profesores aulas si no existe
$stmt_verificar_relacion_empleados_aulas = $conexion->prepare("SELECT COUNT(*) AS total FROM empleados_trabajan_aulas WHERE cod_empleado= ? AND id_aula = ?");
$stmt_verificar_relacion_empleados_aulas->bind_param("ii",$cod_empleado_tutor,$id_aula);
$stmt_verificar_relacion_empleados_aulas->execute();
$resultado_verificar_tutor_aula = $stmt_verificar_relacion_empleados_aulas->get_result();
$fila_verificar_tutor_aula = $resultado_verificar_tutor_aula->fetch_assoc();
$total_tutor_aula = $fila_verificar_tutor_aula['total'];

if($total_tutor_aula == 0){
    $stmt_tutor_aula = $conexion->prepare("INSERT INTO empleados_trabajan_aulas(cod_empleado,id_aula) VALUES (?,?) ON DUPLICATE KEY UPDATE cod_empleado=VALUES(cod_empleado), id_aula=VALUES(id_aula)");
    $stmt_tutor_aula->bind_param("ii", $cod_empleado_tutor, $id_aula);
    if(!$stmt_tutor_aula->execute()){
        $exito = false;
    }
}


if ($exito) {
    $response = array('message' => 'Actualización exitosa');
    http_response_code(200); // Éxito
} else {
    $response = array('message' => 'Error al insertar nuevos registros');
    http_response_code(500); // Error
}


// Devuelve la respuesta en formato JSON
echo json_encode($response);
?>
