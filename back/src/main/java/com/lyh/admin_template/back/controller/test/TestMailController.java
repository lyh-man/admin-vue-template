package com.lyh.admin_template.back.controller.test;

import com.lyh.admin_template.back.common.utils.GsonUtil;
import com.lyh.admin_template.back.common.utils.MailUtil;
import com.lyh.admin_template.back.common.utils.Result;
import com.lyh.admin_template.back.vo.MailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 测试邮件发送功能
 */
@RestController
@RequestMapping("/test/mail")
public class TestMailController {

    @Autowired
    private MailUtil mailUtil;

    /**
     * 获取邮件方式一，使用 HttpServletRequest 获取，并手动解析数据
     */
    @PostMapping("/send")
    public Result send(HttpServletRequest request) {
        MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = params.getFiles("files");
        MailVo mailVoReal = GsonUtil.fromJson(params.getParameter("mailVo"), MailVo.class);
        mailVoReal.setFiles(files.toArray(new MultipartFile[]{}));
        mailUtil.sendMail(mailVoReal);
        return Result.ok();
    }

    /**
     * 获取邮件方式二，使用 @RequestParam 获取 json 字符串（使用 Gson 手动转换为 对象）
     */
    @PostMapping("/send2")
    public Result send2(@RequestParam String mailVo, MultipartFile[] files) {
        MailVo mailVoReal = GsonUtil.fromJson(mailVo, MailVo.class);
        mailVoReal.setFiles(files);
        mailUtil.sendMail(mailVoReal);
        return Result.ok();
    }
}
