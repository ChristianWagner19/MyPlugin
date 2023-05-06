 <?php
$servername = "localhost";
$username = "scoreboard";
$password = "CapstoneScoreboard";
$db = "minecraft_myplugin";

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}
?> 