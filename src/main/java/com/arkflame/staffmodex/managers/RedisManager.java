package com.arkflame.staffmodex.managers;

import java.util.Collection;
import java.util.Collections;
import org.bukkit.plugin.java.JavaPlugin;
import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

    private final JavaPlugin plugin;
    private JedisPool jedisPool;

    public RedisManager(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeJedisPool();
    }

    private void initializeJedisPool() {
        ConfigWrapper config = StaffModeX.getInstance().getCfg();
        String scheme = config.getString("redis.scheme");
        String username = config.getString("redis.username");
        String password = config.getString("redis.password");
        String ip = config.getString("redis.ip");
        int port = config.getInt("redis.port");

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Example of setting pool config options
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        String url = scheme + "://" + (username.isEmpty() ? "" : (username + ":" + password + "@")) + ip + ":" + port;
        this.jedisPool = new JedisPool(poolConfig, url);
    }

    public boolean connectToRedis() {
        try (Jedis jedis = jedisPool.getResource()) {
            // Test connection
            jedis.ping();
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to connect to Redis: " + e.getMessage());
            return false;
        }
    }

    private boolean isJedisValid(Jedis jedis) {
        return jedis != null && jedis.isConnected();
    }

    public void incrementOnlineStatus(String playerName, String serverName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                long count = jedis.incr("ONLINE_STATUS_" + playerName);
                if (count >= 1) {
                    jedis.sadd("ONLINE_STAFF", playerName);
                    if (count > 1) {
                        jedis.set("ONLINE_STATUS_" + playerName, "1");
                    }
                    if (serverName != null) {
                        jedis.set("CONNECTED_SERVER_" + playerName, serverName);
                    }
                }
            }
        }
    }

    public void decrementOnlineStatus(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                long count = jedis.decr("ONLINE_STATUS_" + playerName);
                if (count <= 0) {
                    jedis.srem("ONLINE_STAFF", playerName);
                    
                    if (count <= 0) {
                        jedis.del("ONLINE_STATUS_" + playerName);
                    }
                    jedis.del("CONNECTED_SERVER_" + playerName);
                }
            }
        }
    }

    public String getConnectedServer(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                return jedis.get("CONNECTED_SERVER_" + playerName);
            }
        }

        return null;
    }

    public int getOnlineStatus(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                String countStr = jedis.get("ONLINE_STATUS_" + playerName);
                try {
                    return Integer.parseInt(countStr);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public void closeConnection() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public boolean isOnline(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                Boolean isMember = jedis.sismember("ONLINE_STAFF", playerName);
                return isMember != null && isMember;
            }
        }
        return false;
    }

    public Collection<String> getOnline() {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                return jedis.smembers("ONLINE_STAFF");
            }
        }
        return Collections.EMPTY_SET;
    }

    public void addPlayerToStaffMode(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                jedis.sadd("STAFFMODE", playerName);
            }
        }
    }

    public void removePlayerFromStaffMode(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                jedis.srem("STAFFMODE", playerName);
            }
        }
    }

    public boolean isStaffMode(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                Boolean isMember = jedis.sismember("STAFFMODE", playerName);
                return isMember != null && isMember;
            }
        }
        return false;
    }
}
