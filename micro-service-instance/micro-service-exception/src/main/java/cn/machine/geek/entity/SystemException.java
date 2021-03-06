package cn.machine.geek.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: MachineGeek
 * @Description: 系统异常类
 * @Date: 2020/11/6
 */
@Data
public class SystemException {
    @TableId(value = "`id`")
    private Long id;

    @TableField(value = "`service`")
    private String service;

    @TableField(value = "`host`")
    private String host;

    @TableField(value = "`uri`")
    private String uri;

    @TableField(value = "`method`")
    private String method;

    @TableField(value = "`parameter`")
    private String parameter;

    @TableField(value = "`ip`")
    private String ip;

    @TableField(value = "`exception_class`")
    private String exceptionClass;

    @TableField(value = "`exception_message`")
    private String exceptionMessage;

    @TableField(value = "`create_time`")
    private LocalDateTime createTime;
}
