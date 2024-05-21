<?php

    include 'conexion.php';

	if(isset($_GET["user"]) && isset($_GET["pwd"])){
		$user=$_GET['user'];
		$pwd=$_GET['pwd'];
        
       
		
		$consulta="SELECT E.*, C.nombre AS nombreCargo 
                   FROM empleados E 
                   INNER JOIN cargo C ON C.id_cargo = E.fk_cargo
                   WHERE E.user= ? AND E.pwd = ?";

		 // Prepara la consulta
    $stmt = $conexion->prepare($consulta);

    // Vincula los parámetros
    $stmt->bind_param("ss", $user, $pwd);

    // Ejecuta la consulta
    $stmt->execute();

    // Obtiene el resultado
    $resultado = $stmt->get_result();



		
			if($resultado -> num_rows > 0){
                $return_arr['empleados'] = array();
                while($fila = $resultado->fetch_array()){
                    array_push($return_arr['empleados'], array(
                    'foto'=> $fila['foto'],
                    'cod_empleado'=> $fila['cod_empleado'],
                    'user' => $fila['user'],
                    'pwd' => $fila['pwd'],
                    'nombre'=> $fila['nombre'],
                    'apellidos'=> $fila['apellidos'], 
                    'fk_cargo'=> $fila['fk_cargo'],
                    'nombreCargo'=> $fila['nombreCargo'],
        
                    ));
                }
            echo json_encode($return_arr);
            }


    $resultado -> close();
		}



?>
