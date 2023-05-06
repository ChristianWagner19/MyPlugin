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
        <h1>Overall Ores Highscore</h1>
		<?php 
        $ore = isset($_GET['ore']) ? $_GET['ore'] : 'totalores';
        if (isset($_GET['submit'])) {
            $ore = $_GET['ore'];
        }
        ?>
        <form method="GET" action="">
    <label for="ore-select">Select Ore:</label>
    <select name="ore" id="ore-select">
        <option value="diamond" <?php if($ore == 'diamond') echo 'selected'; ?>>Diamond</option>
        <option value="gold" <?php if($ore == 'gold') echo 'selected'; ?>>Gold</option>
        <option value="iron" <?php if($ore == 'iron') echo 'selected'; ?>>Iron</option>
        <option value="coal" <?php if($ore == 'coal') echo 'selected'; ?>>Coal</option>
        <option value="copper" <?php if($ore == 'copper') echo 'selected'; ?>>Copper</option>
        <option value="totalores" <?php if($ore == 'totalores') echo 'selected'; ?>>Total Ores</option>
    </select>
    <button type="submit">Sort</button>
</form>

    </body>
<table>
    <thead>
        <tr>
            <th>Username<img src="images/oresmined/steve.jpg" width="96" height="96" alt="Steve JPG"></th>
            <th>Diamond<img src="images/oresmined/diamond.gif" width="96" height="96" alt="Diamond GIF"></th>
            <th>Gold<img src="images/oresmined/gold.gif" width="96" height="96" alt="Gold GIF"></th>
            <th>Iron<img src="images/oresmined/iron.gif" width="96" height="96" alt="Iron GIF"></th>
            <th>Coal<img src="images/oresmined/coal.gif" width="96" height="96" alt="Coal GIF"></th>
            <th>Copper<img src="images/oresmined/copper.gif" width="96" height="96" alt="Copper GIF"></th>
            <th>Total Ores</th>
        </tr>
    </thead>
    <tbody>
<?php 
$ore = isset($_GET['ore']) ? $_GET['ore'] : 'totalores';

$users = array();

$sql = "SELECT uniqueId, username, oresmined FROM users";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $oresMinedSplit = explode(",", $row["oresmined"]);
        $diamond = explode(":", $oresMinedSplit[0])[1];
        $gold = explode(":", $oresMinedSplit[1])[1];
        $iron = explode(":", $oresMinedSplit[2])[1];
        $coal = explode(":", $oresMinedSplit[3])[1];
        $copper = explode(":", $oresMinedSplit[4])[1];
        $totalores = explode(":", $oresMinedSplit[5])[1];
        
        $users[] = array(
            "uniqueId" => $row["uniqueId"],
            "username" => $row["username"],
            "diamond" => $diamond,
            "gold" => $gold,
            "iron" => $iron,
            "coal" => $coal,
            "copper" => $copper,
            "totalores" => $totalores
        );
    }
}

$key_values = array_column($users, $ore); 
array_multisort($key_values, SORT_DESC, $users);

foreach($users as $row => $value){
    echo "<tr>";
    echo "<td>" . $value["username"] . "</td>";
    echo "<td>" . $value["diamond"] . "</td>";
    echo "<td>" . $value["gold"] . "</td>";
    echo "<td>" . $value["iron"] . "</td>";
    echo "<td>" . $value["coal"] . "</td>";
    echo "<td>" . $value["copper"] . "</td>";
    echo "<td>" . $value["totalores"] . "</td>";
    echo "</tr>";
}

?>
</tbody>
</table>
</body>
</html>