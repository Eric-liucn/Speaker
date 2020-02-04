package zyzgmc.icu.speaker.tasks;

import static zyzgmc.icu.speaker.tasks.InitialTimer.fixTimerMap;
import static zyzgmc.icu.speaker.tasks.InitialTimer.timerMap;

public class TimerTaskCancel {
    public static void cancelTask(String name){
        for (String key:fixTimerMap.get(name).keySet()
             ) {
            fixTimerMap.get(name).get(key).cancel();
            fixTimerMap.get(name).get(key).purge();
        }
        fixTimerMap.remove(name);
    }
}
