<?php 
include "connection.php";
?>
<html>
    <head>
        <title>ChristianCNU.com!</title>
		<link rel="stylesheet" type="text/css" href="tableformat.css">
    </head>
	<?php include "nav.php";?>
    <body>
        <h1>Overall Kills Highscore</h1>
		<?php 
        $mob = isset($_GET['mob']) ? $_GET['mob'] : 'totalmobs';
        if (isset($_GET['submit'])) {
            $mob = $_GET['mob'];
        }
        ?>
        <form method="GET" action="">
            <label for="mob-select">Select Mob:</label>
            <select name="mob" id="mob-select">
                <option value="zombies" <?php if($mob == 'zombies') echo 'selected'; ?>>Zombies</option>
                <option value="skeletons" <?php if($mob == 'skeletons') echo 'selected'; ?>>Skeletons</option>
                <option value="spiders" <?php if($mob == 'spiders') echo 'selected'; ?>>Spiders</option>
                <option value="cave_spiders" <?php if($mob == 'cave_spiders') echo 'selected'; ?>>Cave Spiders</option>
                <option value="creepers" <?php if($mob == 'creepers') echo 'selected'; ?>>Creepers</option>
                <option value="totalmobs" <?php if($mob == 'totalmobs') echo 'selected'; ?>>Total Mobs</option>
            </select>
            <button type="submit" name="submit">Sort</button>
        </form>
    </body>
<table>
		<thead>
			<tr>
				<th>Username<img src="images/mobskilled/steve.jpg" width="96" height="96" alt="Steve JPG"></th>
				<th>Zombie<img src="images/mobskilled/zombie.gif" width="96" height="96" alt="Zombie GIF"></th>
				<th>Skeletons<img src="images/mobskilled/fast-walker-crack-walk.gif" width="96" height="96" alt="Skeleton GIF"></th>
				<th>Spider<img src="images/mobskilled/spider.gif" width="96" height="96" alt="Spider GIF"></th>
				<th>Cave Spiders<img src="images/mobskilled/cavespider.gif" width="96" height="96" alt="Cave Spider GIF"></th>
				<th>Creeper<img src="images/mobskilled/creeper.gif" width="96" height="96" alt="Creeper GIF"></th>
				<th>Total Mobs</th>
			</tr>
		</thead>
		<tbody>
<?php 

$users = array();

	$sql = "SELECT uniqueId, username, mobskilled FROM users";
	$result = $conn->query($sql);
	
	if ($result->num_rows > 0) {
  // output data of each row
		while($row = $result->fetch_assoc()) {
			$mobsKilledSplit = explode(",", $row["mobskilled"]);
			$zombies = explode(":", $mobsKilledSplit[0])[1];
			$skeletons = explode(":", $mobsKilledSplit[1])[1];
			$spiders = explode(":", $mobsKilledSplit[2])[1];
			$cave_spiders = explode(":", $mobsKilledSplit[3])[1];
			$creepers = explode(":", $mobsKilledSplit[4])[1];
			$totalmobs = explode(":", $mobsKilledSplit[5])[1];
			
			$users[] = array(
						"uniqueId" => $row["uniqueId"],
						"username" => $row["username"],
						"mobskilled" => $row["mobskilled"],
						"totalmobs" => $totalmobs,
						"zombies" => $zombies,
						"skeletons" => $skeletons,
						"spiders" => $spiders,
						"cave_spiders" => $cave_spiders,
						"creepers" => $creepers
						);
					}
				}

// sort users based on selected mob
$key_values = array_column($users, $mob); 
array_multisort($key_values, SORT_DESC, $users);

// display users in table
foreach($users as $user){
    echo "<tr>";
    echo "<td>" . $user["username"] . "</td>";
    echo "<td>" . $user["zombies"] . "</td>";
    echo "<td>" . $user["skeletons"] . "</td>";
    echo "<td>" . $user["spiders"] . "</td>";
    echo "<td>" . $user["cave_spiders"] . "</td>";
    echo "<td>" . $user["creepers"] . "</td>";
    echo "<td>" . $user["totalmobs"] . "</td>";
    echo "</tr>";
	}


?>
</tbody>
</table>
</body>
</html>