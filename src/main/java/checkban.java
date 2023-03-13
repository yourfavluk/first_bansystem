import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CheckCommand extends Command {

    private final BanManager banManager;

    public CheckCommand(BanManager banManager) {
        super("checkban");
        this.banManager = banManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatColor.RED + "Dieser Command kann nur von Spielern ausgeführt werden.");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Verwendung: /checkban <Spielername>");
            return;
        }

        String playerName = args[0];
        Ban ban = banManager.getBan(playerName);

        if (ban == null) {
            sender.sendMessage(ChatColor.GREEN + "Der Spieler " + playerName + " ist nicht gebannt.");
        } else {
            sender.sendMessage(ChatColor.RED + "Der Spieler " + playerName + " ist gebannt.");
            sender.sendMessage(ChatColor.RED + "Grund: " + ban.getReason());
            sender.sendMessage(ChatColor.RED + "Dauer: " + ban.getDuration() + " Sekunden");
            sender.sendMessage(ChatColor.RED + "Gebannt von: " + ban.getBannedBy());
        }
    }
}