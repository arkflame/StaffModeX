package com.arkflame.staffmodex.managers;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

import redis.clients.jedis.*;

public class RedisManager implements AutoCloseable {

    private volatile JedisPool jedisPool;
    private volatile boolean closed;

    private JedisPubSub pubSub;
    private ExecutorService subscriberExecutor;

    public RedisManager() {
        initializeJedisPool();
    }

    private void initializeJedisPool() {
        ConfigWrapper config = StaffModeX.getInstance().getCfg();
        boolean redisEnabled = config.getConfig().getBoolean("redis.enabled");
        String scheme = config.getString("redis.scheme");
        String username = config.getString("redis.username");
        String password = config.getString("redis.password");
        String ip = config.getString("redis.ip");
        int port = config.getInt("redis.port");

        if (!redisEnabled || ip == null || port == 0) {
            StaffModeX.getInstance().getLogger().info("No Redis information provided. Using local configuration.");
            closed = true;
            return;
        }

        StaffModeX.getInstance().getLogger().info("Using Redis for staff chat and staff lists.");

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        // Initialize JedisPool with host, port, and optional password
        if (password != null && !password.isEmpty()) {
            jedisPool = new JedisPool(poolConfig, ip, port, 2000, password);
        } else {
            jedisPool = new JedisPool(poolConfig, ip, port);
        }

        closed = false;
        startSubscriber();
    }

    public boolean isClosed() {
        return closed;
    }

    private void startSubscriber() {
        pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if ("staff_chat".equals(channel)) {
                    String[] parts = message.split("\n", 2);
                    if (parts.length == 2) {
                        String sender = parts[0];
                        String chatMessage = parts[1];
                        handleStaffChat(sender, chatMessage);
                    }
                }
            }
        };

        subscriberExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "RedisSubscriberThread");
            t.setDaemon(true);
            return t;
        });

        subscriberExecutor.execute(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(pubSub, "staff_chat");
            } catch (Exception e) {
                if (!closed) {
                    StaffModeX.getInstance().getLogger().severe("Error in Redis subscriber: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void close() {
        closeConnection();
    }

    public void closeConnection() {
        closed = true;

        if (pubSub != null) {
            pubSub.unsubscribe();
            pubSub = null;
        }

        if (subscriberExecutor != null) {
            subscriberExecutor.shutdownNow();
            subscriberExecutor = null;
        }

        if (jedisPool != null) {
            jedisPool.close();
            jedisPool = null;
        }
    }

    private Jedis getJedisResource() {
        if (closed || jedisPool == null) {
            return null;
        }
        try {
            return jedisPool.getResource();
        } catch (Exception e) {
            StaffModeX.getInstance().getLogger().severe("Failed to get Jedis resource: " + e.getMessage());
            return null;
        }
    }

    public void incrementOnlineStatus(String playerName, String serverName) {
        if (closed) return;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return;
            long count = jedis.incr("ONLINE_STATUS_" + playerName);
            jedis.sadd("ONLINE_STAFF", playerName);
            if (count > 1) {
                jedis.set("ONLINE_STATUS_" + playerName, "1");
            }
            if (serverName != null) {
                jedis.set("CONNECTED_SERVER_" + playerName, serverName);
            }
        }
    }

    public void decrementOnlineStatus(String playerName) {
        if (closed) return;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return;
            long count = jedis.decr("ONLINE_STATUS_" + playerName);
            if (count <= 0) {
                jedis.srem("ONLINE_STAFF", playerName);
                jedis.del("ONLINE_STATUS_" + playerName);
                jedis.del("CONNECTED_SERVER_" + playerName);
            }
        }
    }

    public String getConnectedServer(String playerName) {
        if (closed) return null;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return null;
            return jedis.get("CONNECTED_SERVER_" + playerName);
        }
    }

    public int getOnlineStatus(String playerName) {
        if (closed) return 0;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return 0;
            String countStr = jedis.get("ONLINE_STATUS_" + playerName);
            return countStr != null ? Integer.parseInt(countStr) : 0;
        } catch (NumberFormatException e) {
            StaffModeX.getInstance().getLogger().severe("Invalid online status for player " + playerName);
            try (Jedis jedis = getJedisResource()) {
                if (jedis != null) {
                    jedis.del("ONLINE_STATUS_" + playerName);
                }
            }
            return 0;
        }
    }

    public boolean isOnline(String playerName) {
        if (closed) return false;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return false;
            return jedis.sismember("ONLINE_STAFF", playerName);
        }
    }

    public Collection<String> getOnline() {
        if (closed) return Collections.emptySet();
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return Collections.emptySet();
            Set<String> onlineStaff = jedis.smembers("ONLINE_STAFF");
            return onlineStaff != null ? onlineStaff : Collections.emptySet();
        }
    }

    public void addPlayerToStaffMode(String playerName) {
        if (closed) return;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return;
            jedis.sadd("STAFFMODE", playerName);
        }
    }

    public void removePlayerFromStaffMode(String playerName) {
        if (closed) return;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return;
            jedis.srem("STAFFMODE", playerName);
        }
    }

    public boolean isStaffMode(String playerName) {
        if (closed) return false;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return false;
            return jedis.sismember("STAFFMODE", playerName);
        }
    }

    public void sendStaffChatMessage(String sender, String message) {
        if (closed) return;
        try (Jedis jedis = getJedisResource()) {
            if (jedis == null) return;
            jedis.publish("staff_chat", sender + "\n" + message);
        }
    }

    private void handleStaffChat(String sender, String message) {
        if (closed) return;
        Player player = Bukkit.getPlayer(sender);
        if (player == null) {
            StaffModeX.getInstance().getStaffPlayerManager().sendStaffChat(message);
        }
    }
}
