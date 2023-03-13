import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.ProxyServer;

public class ban_command extends Command {
    public ban_command() {
        super("ban", "myplugin.ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /ban <player> <reason>");
            return;
        }

        String playerName = args[0];
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        // Check if the player is online
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        // Ban the player
        BanEntry ban = new BanEntry(playerName, reason, sender.getName(), System.currentTimeMillis() / 1000L, -1);
        BanManager.getInstance().addBan(ban);

        // Kick the player
        player.disconnect(ChatColor.RED + "You have been banned from the server.\nReason: " + reason);

        // Broadcast the ban message
        ProxyServer.getInstance().broadcast(ChatColor.RED + playerName + " has been banned from the server.\nReason: " + reason);
    }
}