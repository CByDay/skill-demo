package cn.example.seckilldemo.mapper;

import cn.example.seckilldemo.entity.TSeckillGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀商品表 Mapper 接口
 *
 * @author LiChao
 * @since 2022-03-03
 */
@Mapper
public interface TSeckillGoodsMapper extends BaseMapper<TSeckillGoods> {

}
