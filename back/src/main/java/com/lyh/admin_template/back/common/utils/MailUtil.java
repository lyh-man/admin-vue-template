package com.lyh.admin_template.back.common.utils;

import com.lyh.admin_template.back.vo.MailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.Date;

/**
 * 邮件发送 工具类
 */
@Component
public class MailUtil {
    /**
     * 用于操作邮件
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送邮件
     */
    public void sendMail(MailVo mailVo) {
        // 检查邮件地址是否正确
        if (checkMail(mailVo)) {
            // 发送邮件
            sendMailReal(mailVo);
        } else {
            throw new RuntimeException("邮件地址异常");
        }
    }

    /**
     * 邮件必须项检查
     */
    private boolean checkMail(MailVo mailVo) {
        return checkAddress(mailVo.getFrom()) && checkAddress(mailVo.getTo())
                && checkAddress(mailVo.getCc()) && checkAddress(mailVo.getBcc());
    }

    /**
     * 检查邮箱地址是否正确
     */
    private boolean checkAddress(String... address) {
        if (address.length == 0 || address == null) {
            return true;
        }
        String regex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        for (String item : address) {
            if (!item.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 发送邮件真实操作
     */
    private void sendMailReal(MailVo mailVo) {
        // 构造一个邮件助手
        MimeMessageHelper mimeMessageHelper = null;
        try {
            // 传入 MimeMessage，并对其进行一系列操作,true 表示支持复杂邮件（支持附件等）
            mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            // 设置邮件发送人
            mimeMessageHelper.setFrom(mailVo.getFrom());
            // 设置邮件接收人
            mimeMessageHelper.setTo(mailVo.getTo());
            // 设置邮件抄送人
            mimeMessageHelper.setCc(mailVo.getCc());
            // 设置邮件密送人
            mimeMessageHelper.setBcc(mailVo.getBcc());
            // 设置邮件主题
            mimeMessageHelper.setSubject(mailVo.getSubject());
            // 设置邮件内容
            mimeMessageHelper.setText(mailVo.getText());
            // 设置邮件日期
            mimeMessageHelper.setSentDate(new Date());
            // 设置附件
            for (MultipartFile file : mailVo.getFiles()) {
                mimeMessageHelper.addAttachment(file.getOriginalFilename(), file);
            }

            // 发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败");
        }
    }
}
