package cn.example.seckilldemo.mapper;

import cn.example.seckilldemo.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author LiChao
 * @since 2022-03-02
 */
@Mapper
public interface TUserMapper extends BaseMapper<TUser> {

}
