package cn.example.seckilldemo.quartz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author GorryLee
*/
@Mapper
public interface IScheduleMapper extends BaseMapper<Schedule> {

    Schedule findOne(@Param("query") ScheduleQueryBo scheduleQuery);

    List<Schedule> findList(@Param("page") Page<Schedule> page, @Param("query") ScheduleQueryBo scheduleQuery);

    List<Schedule> findListAll(@Param("query") ScheduleQueryBo scheduleQuery);

}
