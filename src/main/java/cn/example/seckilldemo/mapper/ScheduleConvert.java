package cn.example.seckilldemo.mapper;

import cn.example.seckilldemo.quartz.Schedule;
import cn.example.seckilldemo.quartz.ScheduleAddBo;
import cn.example.seckilldemo.quartz.ScheduleEditBo;
import cn.example.seckilldemo.quartz.ScheduleVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 功能描述: entity 转 vo
 *
 * @Date: 2023/6/24
 * @param null:
 * @return null
 **/
@Mapper
public interface ScheduleConvert {

    ScheduleConvert INSTANCE = Mappers.getMapper(ScheduleConvert.class);

    ScheduleVo scheduleToScheduleVo(Schedule schedule);

    List<ScheduleVo> scheduleListToScheduleVoList(List<Schedule> records);

    Schedule scheduleAddBoToSchedule(ScheduleAddBo scheduleAddBo);

    Schedule scheduleEditBoToSchedule(ScheduleEditBo scheduleeditBo);

}

