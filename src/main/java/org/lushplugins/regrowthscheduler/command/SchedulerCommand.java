package org.lushplugins.regrowthscheduler.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.lushplugins.regrowthscheduler.RegrowthScheduler;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@SuppressWarnings("unused")
@Command("scheduler")
public class SchedulerCommand {

    @Subcommand("reload")
    @CommandPermission("scheduler.reload")
    public void reload(CommandSender sender) {
        RegrowthScheduler.getInstance().getScheduleManager().reload();

        sender.sendMessage(Component.text()
            .content("RegrowthScheduler reloaded!")
            .color(TextColor.fromHexString("#b7faa2"))
            .build());
    }
}
