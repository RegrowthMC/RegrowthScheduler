package org.lushplugins.regrowthscheduler.schedule;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.lushplugins.regrowthscheduler.RegrowthScheduler;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public record ScheduledCommands(String id, long epoch, List<String> commands, boolean persist) {

    public void run() {
        Bukkit.getScheduler().runTask(RegrowthScheduler.getInstance(), () -> {
            CommandSender console = Bukkit.getServer().getConsoleSender();
            for (String command : this.commands) {
                try {
                    Bukkit.dispatchCommand(console, command);
                } catch (CommandException e) {
                    RegrowthScheduler.getInstance().getLogger().log(Level.WARNING, "Error occurred when executing command: ", e);
                }
            }
        });
    }

    public Map<?, ?> asMap() {
        return Map.of(
            "id", this.id,
            "epoch", this.epoch,
            "commands", this.commands
        );
    }
}
