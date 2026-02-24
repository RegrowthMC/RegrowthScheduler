package org.lushplugins.regrowthscheduler.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.lushplugins.regrowthscheduler.util.TimeUtils;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
@Command("withdelayedtime")
public class WithDelayedTimeCommand {

    @Command("schedule")
    @CommandPermission("scheduler.withdelayedtime")
    public void schedule(CommandSender sender, String delayString, String commands) {
        long delay;
        try {
            delay = TimeUtils.getDelayInSeconds(delayString);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text()
                .content("Invalid time format")
                .color(TextColor.fromHexString("#ff6969"))
                .build());
            return;
        }

        Instant delayedInstant = Instant.now().plusSeconds(delay);

        String[] parsedCommands = String.join(" ", commands)
            .replace("%time%", delayedInstant.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm")))
            .split("&& ");

        for (String command : parsedCommands) {
            Bukkit.dispatchCommand(sender, command);
        }

        sender.sendMessage(Component.text()
            .content("Ran commands with delayed time!")
            .color(TextColor.fromHexString("#b7faa2"))
            .build());
    }
}
