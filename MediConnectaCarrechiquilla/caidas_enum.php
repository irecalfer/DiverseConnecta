<?php

    include 'conexion.php';

// Establece la codificación de caracteres a UTF-8
header('Content-Type: application/json; charset=utf-8');

    $consulta = "SELECT COLUMN_TYPE
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = 'mediconnecta'
                    AND TABLE_NAME = 'parte_de_caidas' 
                    AND COLUMN_NAME = 'lugar_caida'";

$resultados = array(); // array para almacenar los resultados

    // Ejecutar la consulta
    $resultado = mysqli_query($conexion, $consulta);

 if ($resultado) {
        $fila = mysqli_fetch_assoc($resultado);
        // La columna COLUMN_TYPE contiene información sobre los valores ENUM
        $columnType = $fila['COLUMN_TYPE'];
        
        // Extraer los valores ENUM utilizando expresiones regulares
        preg_match_all("/'([^']+)'/", $columnType, $matches);
        $enumValues = $matches[1];

        // Almacenar los valores ENUM en el array de resultados
        $resultados['lugar_caida'] = $enumValues;

        mysqli_free_result($resultado);
    }

/* cerrar conexión */
mysqli_close($conexion);


// Codificar el array de resultados a JSON con codificación UTF-8
echo json_encode($resultados, JSON_UNESCAPED_UNICODE);
?>
