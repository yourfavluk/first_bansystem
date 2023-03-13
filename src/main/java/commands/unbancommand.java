import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class unban_command extends Command {
    public unban_command() {
        super("unban", "myplugin.ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /unban <player>");
            return;
        }

        String playerName = args[0];

        // Unban the player
        BanEntry ban = BanManager.getInstance().getBan(playerName);
        if (ban == null) {
            sender.sendMessage("Player is not banned.");
            return;
        }
        BanManager.getInstance().removeBan(ban);

        // Broadcast the unban message
        ProxyServer.getInstance().broadcast(ChatColor.GREEN + playerName + " has been unbanned from the server.");
    }
}