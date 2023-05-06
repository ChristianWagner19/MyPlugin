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
	<?php 
        $skill = isset($_GET['skill']) ? $_GET['skill'] : 'overall';
        if (isset($_GET['submit'])) {
            $skill = $_GET['skill'];
        }
    ?>
	<h1><?php echo ucfirst($skill); ?> Highscore</h1>
	<form method="GET" action="">
		<label for="skill-select">Select Skill:</label>
		<select name="skill" id="skill-select">
			<option value="overall" <?php if($skill == 'overall') echo 'selected'; ?>>Overall Score</option>
			<option value="mining" <?php if($skill == 'mining') echo 'selected'; ?>>Mining</option>
			<option value="vitality" <?php if($skill == 'vitality') echo 'selected'; ?>>Vitality</option>
			<option value="woodworking" <?php if($skill == 'woodworking') echo 'selected'; ?>>Woodworking</option>
		</select>
		<button type="submit" name="submit">Sort</button>
	</form>
	<table>
		<thead>
			<tr>
				<th>Username<img src="images/playerstats/steve.jpg" width="96" height="96" alt="Steve JPG"></th>
				<th>Mining<img src="images/playerstats/mining.gif" width="128" height="96" alt="Mining GIF"></th>
				<th>Vitality<img src="images/playerstats/vitality.gif" width="128" height="96" alt="Vitality GIF"></th>
				<th>Woodworking<img src="images/playerstats/woodworking.gif" width="128" height="96" alt="Woodworking GIF"></th>
				<th>Overall Score</th>
			</tr>
		</thead>
		<tbody>
			<?php 

			$users = array();
			$sql = "SELECT uniqueId, username, mining, vitality, woodworking, (mining + vitality + woodworking) AS overall FROM users ORDER BY $skill DESC";
			$result = $conn->query($sql);

			if ($result->num_rows > 0) {
				while($row = $result->fetch_assoc()) {
					$users[] = array(
						"uniqueId" => $row["uniqueId"],
						"username" => $row["username"],
						"mining" => $row["mining"],
						"vitality" => $row["vitality"],
						"woodworking" => $row["woodworking"],
						"overall" => $row["overall"]
					);	
				}
			}

			foreach($users as $row => $value){
				echo "<tr>";
				echo "<td>" . $value["username"] . "</td>";
				echo "<td>" . $value["mining"] . "</td>";
				echo "<td>" . $value["vitality"] . "</td>";
				echo "<td>" . $value["woodworking"] . "</td>";
				echo "<td>" . $value["overall"] . "</td>";
				echo "</tr>";
			}
			?>
		</tbody>
	</table>
</body>
</html>
