package com.hackerda.platform.task;

import com.hackerda.platform.domain.time.SchoolTime;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.service.EmptyRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author Syaeldon
 */
public class EmptyRoomTask {
    @Resource(name = "emptyRoomService")
    private EmptyRoomService emptyRoomService;
    @Autowired
    private SchoolTimeManager schoolTimeManager;

    /**
     * 自动爬取三天内的所有空教室数据作为热点数据
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    void autoGetEmptyRoom() {

        SchoolTime schoolTime = schoolTimeManager.getCurrentSchoolTime();
        int day = schoolTime.getDay();
        for (int i = 0; i < 3; i++) {
            emptyRoomService.getFullEmptyRoomReply(String.valueOf(schoolTime.getSchoolWeek()), "01", day + i, 0);
            emptyRoomService.getFullEmptyRoomReply(String.valueOf(schoolTime.getSchoolWeek()), "02", day + i, 0);
            emptyRoomService.getFullEmptyRoomReply(String.valueOf(schoolTime.getSchoolWeek()), "03", day + i, 0);
        }
    }
}
