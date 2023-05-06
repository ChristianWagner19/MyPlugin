<?php 
include "connection.php";
?>


<html>
<head>
	<title>ChristianCNU.com!</title>
	<link rel="stylesheet" type="text/css" href="tableformat.css">
</head>
<body>
<?php include "nav.php";?>
	<h1>Scores (by date joined)</h1>
	<table>
		<thead>
			<tr>
				<th>UUID<img src="images/UUID.gif" width="128" height="96" alt="UUID GIF"></th>
				<th>Username<img src="images/playerstats/steve.jpg" width="96" height="96" alt="Steve JPG"></th>
				<th>Mining<img src="images/playerstats/mining.gif" width="128" height="96" alt="Mining GIF"></th>
				<th>Vitality<img src="images/playerstats/vitality.gif" width="128" height="96" alt="Vitality GIF"></th>
				<th>Woodworking<img src="images/playerstats/woodworking.gif" width="128" height="96" alt="Woodworking GIF"></th>
			</tr>
		</thead>
		<tbody>
			<?php 

			$users = array();
			$sql = "SELECT uniqueId, username, woodworking, mining, vitality FROM users";
			$result = $conn->query($sql);

			if ($result->num_rows > 0) {
				while($row = $result->fetch_assoc()) {
					$users[] = array(
						$row["uniqueId"] => array(
						"username" => $row["username"],
						"woodworking" => $row["woodworking"],
						"mining" => $row["mining"],
						"vitality" => $row["vitality"]
						)
					);	
				}
			}

			foreach($users as $user){
				foreach($user as $uuid => $value){
					echo "<tr>";
					echo "<td>" . $uuid . "</td>";
					echo "<td>" . $value["username"] . "</td>";
					echo "<td>" . $value["woodworking"] . "</td>";
					echo "<td>" . $value["mining"] . "</td>";
					echo "<td>" . $value["vitality"] . "</td>";
					echo "</tr>";
				}
			}
			?>
		</tbody>
	</table>
</body>
</html>