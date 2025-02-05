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
    private HikariDataSource dataSource;
    private boolean initializedSuccessfully;

    // SQL Queries
    private static final String CREATE_INFRACTIONS_TABLE = 
        "CREATE TABLE IF NOT EXISTS staffmodex_infractions (" +
        "id VARCHAR(36) PRIMARY KEY," +
        "accused_uuid VARCHAR(36) NOT NULL," +
        "type VARCHAR(10) NOT NULL," +
        "timestamp VARCHAR(20) NOT NULL," +
        "reporter_uuid VARCHAR(36) NOT NULL," +
        "reporter_name VARCHAR(16) NOT NULL," +
        "reason VARCHAR(255) NOT NULL)";

    private static final String CREATE_IPS_TABLE = 
        "CREATE TABLE IF NOT EXISTS staffmodex_ips (" +
        "id VARCHAR(36) PRIMARY KEY," +
        "ip VARCHAR(45) NOT NULL)";

    private static final String INSERT_INFRACTION = 
        "INSERT INTO staffmodex_infractions (id, accused_uuid, type, timestamp, reporter_uuid, reporter_name, reason) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_INFRACTIONS_BY_UUID = 
        "SELECT id, type, timestamp, reporter_uuid, reporter_name, reason " +
        "FROM staffmodex_infractions WHERE accused_uuid = ?";

    private static final String INSERT_IP = 
        "INSERT INTO staffmodex_ips (id, ip) VALUES (?, ?) " +
        "ON DUPLICATE KEY UPDATE ip = VALUES(ip)";

    private static final String SELECT_IP_BY_ID = 
        "SELECT ip FROM staffmodex_ips WHERE id = ?";

    public DatabaseManager(boolean enabled, String url, String username, String password) {
        if (!enabled || url == null || username == null || password == null) {
            StaffModeX.getInstance().getLogger().info("No database information provided. Using local configuration.");
            this.initializedSuccessfully = false;
            return;
        }

        StaffModeX.getInstance().getLogger().info("Using external database for warnings and reports.");
        initializeDataSource(url, username, password);
    }

    private void initializeDataSource(String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try {
            this.dataSource = new HikariDataSource(config);
            try (Connection conn = dataSource.getConnection()) {
                createTables(conn);
            }
            this.initializedSuccessfully = true;
        } catch (SQLException e) {
            StaffModeX.getInstance().getLogger().severe("Failed to initialize database connection: " + e.getMessage());
            StaffModeX.getInstance().getLogger().severe("Retrying after 5 seconds...");
            this.initializedSuccessfully = false;
            try {
                Thread.sleep(5000);
                initializeDataSource(url, username, password);
            } catch (InterruptedException e1) { /* Interrupted. Finish operation. */ }
        }
    }

    private void createTables(Connection conn) throws SQLException {
        try (PreparedStatement infractionsStmt = conn.prepareStatement(CREATE_INFRACTIONS_TABLE);
             PreparedStatement ipsStmt = conn.prepareStatement(CREATE_IPS_TABLE)) {

            infractionsStmt.executeUpdate();
            ipsStmt.executeUpdate();
        }
    }

    public boolean isInitializedSuccessfully() {
        return initializedSuccessfully && dataSource != null;
    }

    public void saveInfraction(Infraction infraction) {
        if (!isInitializedSuccessfully()) {
            return;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INFRACTION)) {

            stmt.setString(1, infraction.getId().toString());
            stmt.setString(2, infraction.getAccusedUUID().toString());
            stmt.setString(3, infraction.getType().toString());
            stmt.setString(4, infraction.getTimestamp());
            stmt.setString(5, infraction.getReporterUUID().toString());
            stmt.setString(6, infraction.getReporterName());
            stmt.setString(7, infraction.getReason());

            stmt.executeUpdate();
        } catch (SQLException e) {
            StaffModeX.getInstance().getLogger().severe("Error saving infraction: " + e.getMessage());
        }
    }

    public void loadInfractions(StaffPlayer staffPlayer) {
        if (!isInitializedSuccessfully()) {
            return;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_INFRACTIONS_BY_UUID)) {

            stmt.setString(1, staffPlayer.getUUID().toString());

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        UUID id = UUID.fromString(resultSet.getString("id"));
                        InfractionType type = InfractionType.valueOf(resultSet.getString("type"));
                        String timestamp = resultSet.getString("timestamp");
                        UUID reporterUUID = UUID.fromString(resultSet.getString("reporter_uuid"));
                        String reporterName = resultSet.getString("reporter_name");
                        String reason = resultSet.getString("reason");

                        Infraction infraction = new Infraction(id, timestamp, reporterName, reason, 
                                                               staffPlayer.getUUID(), reporterUUID, type);

                        if (type == InfractionType.REPORT) {
                            staffPlayer.getReports().addInfraction(infraction);
                        } else if (type == InfractionType.WARNING) {
                            staffPlayer.getWarnings().addInfraction(infraction);
                        }
                    } catch (IllegalArgumentException ex) {
                        StaffModeX.getInstance().getLogger().severe("Invalid infraction type in database: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            StaffModeX.getInstance().getLogger().severe("Error loading infractions: " + e.getMessage());
        }
    }

    public void saveIP(UUID uuid, String ip) {
        if (!isInitializedSuccessfully()) {
            return;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_IP)) {

            stmt.setString(1, uuid.toString());
            stmt.setString(2, ip);

            stmt.executeUpdate();
        } catch (SQLException e) {
            StaffModeX.getInstance().getLogger().severe("Error saving IP address: " + e.getMessage());
        }
    }

    public void loadIP(StaffPlayer staffPlayer) {
        if (!isInitializedSuccessfully()) {
            return;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_IP_BY_ID)) {

            stmt.setString(1, staffPlayer.getUUID().toString());

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String ip = resultSet.getString("ip");
                    staffPlayer.setIP(ip);
                }
            }
        } catch (SQLException e) {
            StaffModeX.getInstance().getLogger().severe("Error loading IP address: " + e.getMessage());
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
            initializedSuccessfully = false;
        }
    }
}
