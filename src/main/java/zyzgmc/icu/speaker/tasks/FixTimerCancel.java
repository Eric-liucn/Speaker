package zyzgmc.icu.speaker.tasks;

import static zyzgmc.icu.speaker.tasks.InitialTimer.fixTimerMap;

public class FixTimerCancel {

    public static void cancelFixTimer(String name){
        if(!fixTimerMap.isEmpty()){
            for (String key : fixTimerMap.get(name).keySet()
            ) {
                fixTimerMap.get(name).get(key).cancel();
                fixTimerMap.get(name).get(key).purge();
                fixTimerMap.get(name).remove(key);
            }
            fixTimerMap.remove(name);
        }

    }
}
