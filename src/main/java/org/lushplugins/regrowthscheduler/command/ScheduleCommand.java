package org.lushplugins.regrowthscheduler.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.lushplugins.regrowthscheduler.RegrowthScheduler;
import org.lushplugins.regrowthscheduler.schedule.ScheduledCommands;
import org.lushplugins.regrowthscheduler.util.TimeUtils;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.time.Instant;
import java.util.List;

@SuppressWarnings("unused")
@Command("schedule")
public class ScheduleCommand {

    @Command("schedule")
    @CommandPermission("scheduler.schedule")
    public void schedule(CommandSender sender, String delayString, String commands, @Optional @Flag String id, @Optional @Flag Boolean persist) {
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

        long epoch = Instant.now().plusSeconds(delay).getEpochSecond();
        ScheduledCommands action = new ScheduledCommands(
            id != null ? id : String.valueOf(epoch),
            epoch,
            List.of(commands.split("&& ")),
            persist != null ? persist : true
        );
        RegrowthScheduler.getInstance().getScheduleManager().scheduleAction(action);

        sender.sendMessage(Component.text()
            .content("Successfully scheduled action!")
            .color(TextColor.fromHexString("#b7faa2"))
            .build());
    }
}
