import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.CommandSender;

import java.net.InetSocketAddress;

public class IpBanCommand extends Command {
    private final BanManager banManager;

    public IpBanCommand(BanManager banManager) {
        super("ipban");
        this.banManager = banManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /ipban <player>");
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        InetSocketAddress address = target.getAddress();
        if (address == null) {
            sender.sendMessage(ChatColor.RED + "Could not get player's IP address.");
            return;
        }

        String ip = address.getHostString();
        if (banManager.isIpBanned(ip)) {
            sender.sendMessage(ChatColor.RED + "This IP address is already banned.");
            return;
        }

        banManager.banIp(ip);
        banManager.banPlayer(target.getUniqueId());

        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.hasPermission("ipban.notify")) {
                player.sendMessage(ChatColor.YELLOW + sender.getName() + " has IP-banned " + target.getName() +
                        " (IP: " + ip + ")");
            }
        }

        target.disconnect(ChatColor.RED + "You have been IP-banned from this server.");
    }
}