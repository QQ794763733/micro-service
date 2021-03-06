package ${packageName}.controller;

import cn.machine.geek.common.P;
import cn.machine.geek.common.R;
import ${packageName}.entity.${className};
import ${packageName}.service.${className}Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
* @Author: MachineGeek
* @Description: ${moduleName}控制器
* @Email: 794763733@qq.com
* @Date: ${date}
*/
@Api(tags = "${moduleName}接口")
@RestController
@RequestMapping(value = "/${instanceName}")
public class ${className}Controller {
@Autowired
private ${className}Service ${instanceName}Service;

@ApiOperation(value = "获取所有${moduleName}",notes = "获取所有${moduleName}")
@GetMapping(value = "/list")
@PreAuthorize("hasAuthority('${className?upper_case}:GET')")
public R list(){
return R.ok(${instanceName}Service.list());
}

@ApiOperation(value = "分页获取${moduleName}",notes = "分页获取${moduleName}")
@GetMapping(value = "/paging")
@PreAuthorize("hasAuthority('${className?upper_case}:GET')")
public R paging(@Validated P p){
QueryWrapper<${className}> queryWrapper = new QueryWrapper<>();
// 在这里写条件查询逻辑逻辑
String keyWord = p.getKeyword();
if(!StringUtils.isEmpty(keyWord)){
}
return R.ok(${instanceName}Service.page(new Page<>(p.getPage(),p.getSize()),queryWrapper));
}

@ApiOperation(value = "增加${moduleName}",notes = "增加${moduleName}")
@PostMapping(value = "/add")
@Transactional
@PreAuthorize("hasAuthority('${className?upper_case}:ADD')")
public R add(@RequestBody ${className} ${instanceName}){
${instanceName}.setCreateTime(LocalDateTime.now());
return R.ok(${instanceName}Service.save(${instanceName}));
}

@PreAuthorize("hasAuthority('${className?upper_case}:DELETE')")
@ApiOperation(value = "根据ID删除${moduleName}",notes = "根据ID删除${moduleName}")
@DeleteMapping(value = "/deleteById")
public R deleteById(@RequestParam(value = "id") Long id){
return R.ok(${instanceName}Service.removeById(id));
}

@PreAuthorize("hasAuthority('${className?upper_case}:MODIFY')")
@ApiOperation(value = "根据ID更新${moduleName}",notes = "根据ID更新${moduleName}")
@PutMapping(value = "/modifyById")
@Transactional
public R modifyById(@RequestBody ${className} ${instanceName}){
${instanceName}.setUpdateTime(LocalDateTime.now());
return R.ok(${instanceName}Service.updateById(${instanceName}));
}

@PreAuthorize("hasAuthority('${className?upper_case}:GET')")
@ApiOperation(value = "根据ID获取${moduleName}",notes = "根据ID获取${moduleName}")
@GetMapping(value = "/getById")
public R getById(@RequestParam(value = "id") Long id){
return R.ok(${instanceName}Service.getById(id));
}
}