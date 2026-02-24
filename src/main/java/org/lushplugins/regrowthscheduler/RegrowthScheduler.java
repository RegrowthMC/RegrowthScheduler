package org.lushplugins.regrowthscheduler;

import org.lushplugins.lushlib.plugin.SpigotPlugin;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.regrowthscheduler.command.ScheduleCommand;
import org.lushplugins.regrowthscheduler.placeholder.Placeholders;
import org.lushplugins.regrowthscheduler.schedule.ScheduleManager;
import revxrsal.commands.bukkit.BukkitLamp;


public final class RegrowthScheduler extends SpigotPlugin {
    private static RegrowthScheduler plugin;

    private ScheduleManager scheduleManager;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.scheduleManager = new ScheduleManager();
        this.scheduleManager.reload();

        BukkitLamp.builder(this)
            .build()
            .register(new ScheduleCommand());

        PlaceholderHandler.builder(this)
            .build()
            .register(new Placeholders());
    }

    @Override
    public void onDisable() {
        if (this.scheduleManager != null) {
            this.scheduleManager.shutdown();
        }
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public static RegrowthScheduler getInstance() {
        return plugin;
    }
}
