package com.example.bansystem;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public class main extends Plugin {

    private BanDatabase database;

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "BanSystem Plugin enabled");
        database = new BanDatabase();
        getProxy().getPluginManager().registerCommand(this, new bancommand(database));
        getProxy().getPluginManager().registerCommand(this, new unbancommand(database));
        getProxy().getPluginManager().registerListener(this, new BanListener(database));
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "BanSystem Plugin disabled");
    }
}