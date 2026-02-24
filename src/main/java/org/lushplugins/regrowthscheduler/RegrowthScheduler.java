package org.lushplugins.regrowthscheduler;

import org.bukkit.plugin.java.JavaPlugin;

public final class RegrowthScheduler extends JavaPlugin {
    private static RegrowthScheduler plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Enable implementation
    }

    @Override
    public void onDisable() {
        // Disable implementation
    }

    public static RegrowthScheduler getInstance() {
        return plugin;
    }
}
