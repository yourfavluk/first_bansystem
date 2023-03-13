import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class BanDatabase {

    private Connection connection;

    public BanDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bansystem", "user", "password");
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS bans (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(36) NOT NULL, "
                            + "reason VARCHAR(255), admin VARCHAR(16) NOT NULL, time TIMESTAMP NOT NULL, "
                            + "duration INT NOT NULL, PRIMARY KEY (id))");
            statement.executeUpdate();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void banPlayer(UUID uuid, String reason, String admin, int duration) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO bans (uuid, reason, admin, time, duration) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, reason);
            statement.setString(3, admin);
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setInt(5, duration);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unbanPlayer(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM bans WHERE uuid=?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerBanned(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM bans WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            boolean isBanned = resultSet.next();
            resultSet.close();
            statement.close();
            return isBanned;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getBanReason(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM bans WHERE uuid=?");
            statement.setString(1, uuid.toString());