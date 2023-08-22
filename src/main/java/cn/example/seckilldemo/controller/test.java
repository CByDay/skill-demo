//package cn.example.seckilldemo.controller;
//
//import cn.example.seckilldemo.entity.TGoods;
//import cn.example.seckilldemo.handlers.ReviseService;
//import cn.example.seckilldemo.utils.Result;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//@RestController
//public class test {
//
//    @Resource
//    private ReviseService reviseServie;
//
//    @PostMapping("/ac/review")
//    public ResponseEntity<String > rrss (@RequestBody TGoods tGoods){
//        Result result = reviseServie.rev(tGoods);
//        if(result.getStatus() == 200){
//            return ResponseEntity.ok(result.getMessage());
//        }else {
//            return ResponseEntity.ok(result.getMessage());
//        }
//    }
//}
