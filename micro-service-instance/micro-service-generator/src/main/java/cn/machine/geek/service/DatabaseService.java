package cn.machine.geek.service;

import cn.machine.geek.entity.DatabaseTable;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @Author: MachineGeek
 * @Description: 数据库服务表
 * @Date: 2020/11/6
 */
public interface DatabaseService {
    List<String> listDatabase();

    IPage<DatabaseTable> paging(int page, int size, String keyWord);
}
