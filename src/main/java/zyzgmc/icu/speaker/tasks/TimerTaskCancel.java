package zyzgmc.icu.speaker.tasks;

import static zyzgmc.icu.speaker.tasks.InitialTimer.fixTimerMap;
import static zyzgmc.icu.speaker.tasks.InitialTimer.timerMap;

public class TimerTaskCancel {
    public static void cancelTask(String name){
        timerMap.get(name).cancel();
        timerMap.get(name).purge();
        timerMap.remove(name);
    }
}
