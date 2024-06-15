package com.arkflame.staffmodex.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.Infraction;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.player.StaffPlayer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {
    private HikariDataSource dataSource = null;
    private boolean isInitializedSuccessfully = false;

    public DatabaseManager(boolean enabled, String url, String username, String password) {
        if (!enabled || url == null || username == null || password == null) {
            StaffModeX.getInstance().getLogger().info("No database information provided. Using local configuration.");
            return;
        } else {
            StaffModeX.getInstance().getLogger().info("Using external database for warnings and reports.");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try {
            this.dataSource = new HikariDataSource(config);
            // Attempt to get a connection to check if the database is accessible
            Connection conn = dataSource.getConnection();
            createTables(conn); // Generate tables if they don't exist
            conn.close(); // Close the connection immediately after verifying it works
            isInitializedSuccessfully = true;
        } catch (Exception e) {
            StaffModeX.getInstance().getLogger().info("Failed to initialize database connection: " + e.getMessage());
            isInitializedSuccessfully = false;
            this.dataSource = null; // Ensure dataSource is null to avoid any further usage attempts
        }
    }

    private void createTables(Connection conn) throws SQLException {
        // Create the 'infractions' table if it doesn't exist
        String createInfractionsTableQuery = "CREATE TABLE IF NOT EXISTS staffmodex_infractions (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "accused_uuid VARCHAR(36) NOT NULL," +
                "type VARCHAR(10) NOT NULL," +
                "timestamp VARCHAR(20) NOT NULL," +
                "reporter_uuid VARCHAR(36) NOT NULL," +
                "reporter_name VARCHAR(16) NOT NULL," +
                "reason VARCHAR(255) NOT NULL)";

        // Create the 'ips' table if it doesn't exist
        String createIpsQuery = "CREATE TABLE IF NOT EXISTS staffmodex_ips (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "ip VARCHAR(45))";

        try (PreparedStatement stmt = conn.prepareStatement(createInfractionsTableQuery)) {
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement(createIpsQuery)) {
            stmt.executeUpdate();
        }
    }

    public boolean isInitializedSuccessfully() {
        return isInitializedSuccessfully && dataSource != null;
    }

    public void saveInfraction(Infraction infraction) {
        String query = "INSERT INTO staffmodex_infractions (accused_uuid, type, timestamp, reporter_uuid, reporter_name, reason, id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            // Debug: Check if the database connection is successful

            stmt.setString(1, infraction.getAccusedUUID().toString());
            stmt.setString(2, infraction.getType().toString());
            stmt.setString(3, infraction.getTimestamp());
            stmt.setString(4, infraction.getReporterUUID().toString());
            stmt.setString(5, infraction.getReporterName());
            stmt.setString(6, infraction.getReason());
            stmt.setString(7, infraction.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            close();
            // Debug: Print the SQL exception
            e.printStackTrace();
        }
    }

    public void loadInfractions(StaffPlayer staffPlayer) {
        String query = "SELECT id, type, timestamp, reporter_uuid, reporter_name, reason FROM staffmodex_infractions WHERE accused_uuid = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, staffPlayer.getUUID().toString());
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        String typeStr = resultSet.getString("type");
                        String timestampStr = resultSet.getString("timestamp");
                        String reason = resultSet.getString("reason");

                        InfractionType type = InfractionType.valueOf(typeStr);
                        UUID reporterUUID = UUID.fromString(resultSet.getString("reporter_uuid"));
                        String reporterName = resultSet.getString("reporter_name");
                        UUID id = UUID.fromString(resultSet.getString("id"));

                        switch (type) {
                            case REPORT: {
                                staffPlayer.getReports().addInfraction(new Infraction(id, timestampStr, reporterName,
                                        reason, staffPlayer.getUUID(), reporterUUID, type));
                                break;
                            }
                            case WARNING: {
                                staffPlayer.getWarnings().addInfraction(new Infraction(id, timestampStr, reporterName,
                                        reason, staffPlayer.getUUID(), reporterUUID, type));
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        StaffModeX.getInstance().getLogger().severe("Error while loading infraction from config");
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            close();
            // Debug: Print the SQL exception
            e.printStackTrace();
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    public void saveIP(UUID uuid, String ip) {
        String query = "INSERT INTO staffmodex_ips (id, ip) VALUES (?, ?) ON DUPLICATE KEY UPDATE ip = VALUES(ip)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            // Debug: Check if the database connection is successful

            stmt.setString(1, uuid.toString());
            stmt.setString(2, ip);
            stmt.executeUpdate();
        } catch (SQLException e) {
            close();
            // Debug: Print the SQL exception
            e.printStackTrace();
        }
    }

    public void loadIP(StaffPlayer staffPlayer) {
        String query = "SELECT id, ip FROM staffmodex_ips WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, staffPlayer.getUUID().toString());
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        String ip = resultSet.getString("ip");
                        staffPlayer.setIP(ip);
                    } catch (Exception ex) {
                        StaffModeX.getInstance().getLogger().severe("Error while loading infraction from config");
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            close();
            // Debug: Print the SQL exception
            e.printStackTrace();
        }
    }
}
