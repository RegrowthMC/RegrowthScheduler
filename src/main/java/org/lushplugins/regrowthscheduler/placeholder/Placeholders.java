package org.lushplugins.regrowthscheduler.placeholder;

import org.lushplugins.lushlib.utils.TimeFormatter;
import org.lushplugins.placeholderhandler.annotation.Placeholder;
import org.lushplugins.placeholderhandler.annotation.SubPlaceholder;
import org.lushplugins.regrowthscheduler.RegrowthScheduler;
import org.lushplugins.regrowthscheduler.schedule.ScheduledCommands;

import java.time.Duration;
import java.time.Instant;

@SuppressWarnings("unused")
@Placeholder("scheduler")
public class Placeholders {

    @SubPlaceholder("countdown_<id>")
    public String countdown(String id) {
        ScheduledCommands action = RegrowthScheduler.getInstance().getScheduleManager().findNextActionWithId(id);
        if (action == null) {
            return "Not scheduled";
        }

        long duration = Math.max(0, action.epoch() - Instant.now().getEpochSecond());
        return TimeFormatter.formatDuration(Duration.ofSeconds(duration), TimeFormatter.FormatType.SHORT_FORM);
    }
}
