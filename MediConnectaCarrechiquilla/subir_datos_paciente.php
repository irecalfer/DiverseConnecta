<?php
// Archivo de conexión a la base de datos
include 'conexion.php';

$response = array(); // Array para la respuesta JSON

// Obtener datos de la solicitud POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['foto'])) {
        $cipSns = $_POST["cip_sns"];
        $numHistoriaClinica = $_POST["num_historia_clinica"];

        // Insertar los demás datos en la base de datos
        $sql = "INSERT INTO historia_clinica (num_historia_clinica) VALUES ('$numHistoriaClinica')";
        $sql .= ";";
        $sql .= "INSERT INTO pacientes (fk_num_historia_clinica) VALUES ('$numHistoriaClinica') WHERE cip_sns = $cipSns";

        $resultadoQuery = $conexion->multi_query($sql);
        if ($resultadoQuery) {
            $response["status"] = "success";
            $response["message"] = "Número de historia clínica subida correctamente";
        } else {
            $response["status"] = "error";
            $response["message"] = "Error al subir el número de historia clínica: " . $conexion->error;
        }
    } else {
        $response["status"] = "error";
        $response["message"] = "No se recibió ninguna historia clínica";
    }
} else {
    $response["status"] = "error";
    $response["message"] = "Método de solicitud no permitido";
}

echo json_encode($response);
?>

