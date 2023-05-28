package cn.example.seckilldemo.service.impl;

import cn.example.seckilldemo.entity.TGoods;
import cn.example.seckilldemo.mapper.TGoodsMapper;
import cn.example.seckilldemo.service.ITGoodsService;
import cn.example.seckilldemo.vo.GoodsVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品表 服务实现类
 *
 * @author LiChao
 * @since 2022-03-03
 */
@Service
@Primary
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements ITGoodsService {

    @Autowired
    private TGoodsMapper tGoodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return tGoodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVobyGoodsId(Long goodsId) {
        return tGoodsMapper.findGoodsVobyGoodsId(goodsId);
    }
}
