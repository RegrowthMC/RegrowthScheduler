package org.lushplugins.regrowthscheduler.schedule;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.utils.YamlUtils;
import org.lushplugins.regrowthscheduler.RegrowthScheduler;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ScheduleManager {
    private static final File DATA_FILE = RegrowthScheduler.getInstance().getDataPath()
        .resolve("scheduled_actions.yml")
        .toFile();

    private ScheduledExecutorService scheduler;
    private PriorityQueue<ScheduledCommands> scheduledActions;

    public void reload() {
        if (this.scheduler != null) {
            this.scheduler.shutdown();
        }

        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduledActions = new PriorityQueue<>(Comparator.comparingLong(ScheduledCommands::epoch));
        if (!DATA_FILE.exists()) {
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(DATA_FILE);
        YamlUtils.getConfigurationSections(config, "scheduled-actions").forEach(section -> {
            ScheduledCommands action = new ScheduledCommands(
                section.getString("id"),
                section.getLong("epoch"),
                section.getStringList("commands"),
                true
            );

            this.scheduledActions.add(action);
        });

        scheduleNextAction();
    }

    private void scheduleNextAction() {
        ScheduledCommands action = this.scheduledActions.peek();
        if (action == null) {
            return;
        }

        long now = Instant.now().getEpochSecond();
        long delay = Math.max(0, action.epoch() - now);

        this.scheduler.schedule(() -> {
            this.scheduledActions.remove(action);
            action.run();

            scheduleNextAction();

            saveScheduledActions();
        }, delay, TimeUnit.SECONDS);
    }

    public void shutdown() {
        this.scheduler.shutdown();
    }

    public ScheduledCommands findNextActionWithId(String id) {
        return this.scheduledActions.stream()
            .filter(action -> action.id().equals(id))
            .findFirst()
            .orElse(null);
    }

    public void scheduleAction(@NotNull ScheduledCommands action) {
        this.scheduledActions.add(action);

        if (this.scheduledActions.peek() == action) {
            if (this.scheduledActions.size() > 1) {
                this.scheduler.shutdown();
                this.scheduler = Executors.newScheduledThreadPool(1);
            }

            scheduleNextAction();
        }

        if (action.persist()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(DATA_FILE);
            List<Map<?, ?>> entries = config.getMapList("scheduled-actions");
            entries.add(action.asMap());
            config.set("scheduled-actions", entries);

            try {
                config.save(DATA_FILE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveScheduledActions() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(DATA_FILE);
        List<Map<?, ?>> entries = this.scheduledActions.stream()
            .map(ScheduledCommands::asMap)
            .collect(Collectors.toUnmodifiableList());

        config.set("scheduled-actions", entries);

        try {
            config.save(DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
