package com.lyh.admin_template.back.modules.sms.controller;

import com.lyh.admin_template.back.common.utils.Result;
import com.lyh.admin_template.back.common.utils.SmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
@Api(tags = "短信发送")
public class TestSMSController {

    @Autowired
    private SmsUtil smsUtil;

    @ApiOperation(value = "测试短信发送功能")
    @ApiImplicitParam(name = "phoneNumber", required = true, value = "手机号", paramType = "query", dataType = "String")
    @PostMapping("/testSend")
    public Result testSend(@RequestParam String phoneNumber) {
        if (StringUtils.isNotEmpty(smsUtil.sendSms(phoneNumber))) {
            return Result.ok().message("短信发送成功");
        }
        return Result.error().message("短信发送失败");
    }
}
