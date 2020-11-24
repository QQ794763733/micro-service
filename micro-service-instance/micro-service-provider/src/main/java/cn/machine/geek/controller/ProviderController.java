package cn.machine.geek.controller;

import cn.machine.geek.dto.R;
import cn.machine.geek.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: MachineGeek
 * @Description: 测试控制器
 * @Email: 794763733@qq.com
 * @Date: 2020/11/19
 */
@RestController
@RequestMapping(value = "/provider")
public class ProviderController {
    @Value("${server.port}")
    private Integer port;
    @Autowired
    private SendService sendService;

    @Value("${instance.name}")
    private String instanceName;

    @GetMapping(value = "/get")
    public R get(){
        return R.ok("Get测试OK");
    }

    @PostMapping(value = "/post")
    public R post(@RequestBody Object object){
        return R.ok(object);
    }

    @GetMapping(value = "/config")
    public R config(){
        return R.ok(this.instanceName);
    }

    @GetMapping(value = "/loadBalancing")
    public R loadBalancing(){
        return R.ok(this.port);
    }

    @GetMapping(value = "/sendMessage/{msg}")
    public R sendMessage(@PathVariable(value = "msg")String msg){
        sendService.sendMessage(msg);
        return R.ok();
    }
}
