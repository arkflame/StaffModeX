package com.arkflame.staffmodex.managers;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.player.StaffPlayer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {

    private JavaPlugin plugin;
    private JedisPool jedisPool;

    private boolean closed = true;

    public RedisManager(JavaPlugin plugin) {
        this.plugin = plugin;
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

        if (!redisEnabled || scheme == null || username == null || password == null || ip == null || port == 0) {
            StaffModeX.getInstance().getLogger().info("No redis information provided. Using local configuration.");
            return;
        } else {
            StaffModeX.getInstance().getLogger().info("Using redis for staff chat and staff lists.");
        }

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
        closed = false;

        listenForStaffMessages();
    }

    public boolean isClosed() {
        return closed;
    }

    private boolean isJedisValid(Jedis jedis) {
        return jedis != null && jedis.isConnected();
    }

    public void incrementOnlineStatus(String playerName, String serverName) {
        if (closed) return;
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
        if (closed) return;
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
        if (closed) return null;
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                return jedis.get("CONNECTED_SERVER_" + playerName);
            }
        }

        return null;
    }

    public int getOnlineStatus(String playerName) {
        if (closed) return 0;
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
            closed = true;
        }
    }

    public boolean isOnline(String playerName) {
        if (closed) return false;
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                Boolean isMember = jedis.sismember("ONLINE_STAFF", playerName);
                return isMember != null && isMember;
            }
        }
        return false;
    }

    public Collection<String> getOnline() {
        if (closed) return null;
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                return jedis.smembers("ONLINE_STAFF");
            }
        }
        return Collections.EMPTY_SET;
    }

    public void addPlayerToStaffMode(String playerName) {
        if (closed) return;
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                jedis.sadd("STAFFMODE", playerName);
            }
        }
    }

    public void removePlayerFromStaffMode(String playerName) {
        if (closed) return;
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                jedis.srem("STAFFMODE", playerName);
            }
        }
    }

    public boolean isStaffMode(String playerName) {
        if (closed) return false;
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                Boolean isMember = jedis.sismember("STAFFMODE", playerName);
                return isMember != null && isMember;
            }
        }
        return false;
    }

    public void sendStaffChatMessage(String sender, String message) {
        if (closed) return;
        try (Jedis jedis = jedisPool.getResource()) {
            if (isJedisValid(jedis)) {
                jedis.publish("staff_chat", sender + "\n" + message);
            }
        }
    }

    public void listenForStaffMessages() {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                if (isJedisValid(jedis)) {
                    jedis.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            if (closed) {
                                unsubscribe();
                                return;
                            }
                            String[] parts = message.split("\n");
                            if (parts.length == 2 && channel.equals("staff_chat")) {
                                String sender = parts[0];
                                String chatMessage = parts[1];
                                handleStaffChat(sender, chatMessage);
                            }
                        }
                    }, "staff_chat");
                }
            }
        }).start();
    }

    private void handleStaffChat(String sender, String message) {
        if (closed) return;
        Player player = Bukkit.getPlayer(sender);

        if (player == null) {
            for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers()
                    .values()) {
                if (staffPlayer.isStaffChatReceiver()) {
                    staffPlayer.sendMessage(message);
                }
            }
        }
    }
}
