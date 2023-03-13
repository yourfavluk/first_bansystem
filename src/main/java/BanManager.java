import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BanManager {

    private List<Ban> bans;
    private File bansFile;
    private FileConfiguration bansConfig;

    public BanManager(File bansFile) {
        this.bans = new ArrayList<>();
        this.bansFile = bansFile;
        this.bansConfig = YamlConfiguration.loadConfiguration(bansFile);

        // Load existing bans from the bans file
        if (bansConfig.contains("bans")) {
            for (String key : bansConfig.getConfigurationSection("bans").getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                long expiry = bansConfig.getLong("bans." + key + ".expiry");
                String reason = bansConfig.getString("bans." + key + ".reason");
                bans.add(new Ban(uuid, expiry, reason));
            }
        }
    }

    public void saveBans() {
        // Save bans to the bans file
        for (Ban ban : bans) {
            bansConfig.set("bans." + ban.getUuid().toString() + ".expiry", ban.getExpiry());
            bansConfig.set("bans." + ban.getUuid().toString() + ".reason", ban.getReason());
        }

        try {
            bansConfig.save(bansFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBanned(UUID uuid) {
        for (Ban ban : bans) {
            if (ban.getUuid().equals(uuid)) {
                if (ban.isPermanent()) {
                    return true;
                } else {
                    if (System.currentTimeMillis() < ban.getExpiry()) {
                        return true;
                    } else {
                        bans.remove(ban);
                        saveBans();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public void ban(UUID uuid, String reason) {
        bans.add(new Ban(uuid, -1, reason)); // Permanent ban
        saveBans();
    }

    public void ban(UUID uuid, long duration, String reason) {
        bans.add(new Ban(uuid, System.currentTimeMillis() + duration, reason));
        saveBans();
    }

    public void unban(UUID uuid) {
        bans.removeIf(ban -> ban.getUuid().equals(uuid));
        saveBans();
    }

    public List<Ban> getBans() {
        return bans;
    }

    public class Ban {
        private UUID uuid;
        private long expiry;
        private String reason;

        public Ban(UUID uuid, long expiry, String reason) {
            this.uuid = uuid;
            this.expiry = expiry;
            this.reason = reason;
        }

        public UUID getUuid() {
            return uuid;
        }

        public long getExpiry() {
            return expiry;
        }

        public String getReason() {
            return reason;
        }

        public boolean isPermanent() {
            return expiry == -1;
        }
    }
}