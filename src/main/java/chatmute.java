mport net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.UUID;

public class Chatmute extends Command {

    private HashMap<UUID, Boolean> mutedPlayers = new HashMap<>();

    public Chatmute() {
        super("chatmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /chatmute <player>");
            return;
        }

        String playerName = args[0];
        ProxiedPlayer player = getProxy().getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        UUID playerUUID = player.getUniqueId();

        if (mutedPlayers.containsKey(playerUUID) && mutedPlayers.get(playerUUID)) {
            mutedPlayers.put(playerUUID, false);
            sender.sendMessage(ChatColor.YELLOW + "Unmuted " + playerName);
            return;
        }

        mutedPlayers.put(playerUUID, true);
        sender.sendMessage(ChatColor.YELLOW + "Muted " + playerName);
    }

    public boolean isMuted(UUID playerUUID) {
        return mutedPlayers.getOrDefault(playerUUID, false);
    }
}