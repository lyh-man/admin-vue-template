package com.lyh.admin_template.back.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyh.admin_template.back.common.exception.GlobalException;
import com.lyh.admin_template.back.common.utils.ExceptionUtil;
import com.lyh.admin_template.back.common.utils.MessageSourceUtil;
import com.lyh.admin_template.back.common.utils.Result;
import com.lyh.admin_template.back.common.validator.group.AddGroup;
import com.lyh.admin_template.back.common.validator.group.UpdateGroup;
import com.lyh.admin_template.back.entity.User;
import com.lyh.admin_template.back.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 用于测试环境搭建各个功能是否成功
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试页面")
public class TestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @ApiOperation(value = "测试国际化返回数据")
    @GetMapping("/testLocale")
    public Result testI18n() {
        return Result.ok().data("test", messageSourceUtil.getMessage("test"));
    }

    @ApiOperation(value = "测试 JSR 303 添加时的校验规则")
    @PostMapping("testValidator/save")
    public Result testValidatorSave(@Validated({AddGroup.class}) @RequestBody User user) {
        if(userService.save(user)) {
            return Result.ok().message("数据添加成功");
        }
        return Result.error().message("数据添加失败");
    }

    @ApiOperation(value = "测试 JSR 303 更新时的校验规则")
    @PostMapping("testValidator/update")
    public Result testValidatorUpdate(@Validated({UpdateGroup.class}) @RequestBody User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", user.getName());
        if(userService.update(user, updateWrapper)) {
            return Result.ok().message("数据更新成功");
        }
        return Result.error().message("数据更新失败");
    }

    @ApiOperation(value = "测试分页插件")
    @GetMapping("/testMyBatisPlus/page/{current}/{size}")
    public Result testPage(@PathVariable("current") Long current, @PathVariable("size") Long size) {
        Page<User> page = new Page(current, size);
        return Result.ok().data("page", userService.page(page, null));
    }

    @ApiOperation(value = "获取所有用户信息")
    @GetMapping("/getListOfUser")
    public Result testGetListOfUser() {
        return Result.ok().data("user", userService.list());
    }

    @ApiOperation(value = "删除指定姓名的用户")
    @DeleteMapping("/testMyBatisPlus/LogicDel")
    public Result testMyBatisPlusLogicDel(@RequestParam String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        userService.removeByMap(hashMap);
        return Result.ok();
    }

    @ApiOperation(value = "测试 MyBatis Plus 自动填充数据功能")
    @PostMapping("/testMyBatisPlus/AutoFill")
    public Result testMyBatisPlusAutoFill(@RequestBody User user) {
        if(userService.save(user)) {
            return Result.ok().message("数据添加成功");
        }
        return Result.error().message("数据添加失败");
    }

    /**
     * 用于测试 Swagger 是否整合成功
     * @return
     */
    @ApiOperation(value = "测试 Swagger")
    @GetMapping("/testSwagger")
    public Result testSwagger() {
        return Result.ok();
    }

    /**
     * 用于测试 异常 是否能被统一处理，并返回指定格式的数据
     * @return
     */
    @GetMapping("/testGlobalException")
    public Result testGlobalException() {
        try {
            int test = 10 / 0;
        } catch (Exception e) {
            throw new GlobalException(ExceptionUtil.getMessage(e));
        }
        return Result.ok();
    }

    /**
     * 用于测试 Result 是否能够正常返回指定格式的数据
     * @return
     */
    @GetMapping("/testResult")
    public Result testResult() {
        return Result.ok();
    }
}
