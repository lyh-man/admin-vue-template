package com.lyh.admin_template.back.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MailVo {

    /**
     * 邮件发送人
     */
    private String from;
    /**
     * 邮件接收人
     */
    private String[] to;
    /**
     * 邮件抄送
     */
    private String[] cc;
    /**
     * 邮件密送
     */
    private String[] bcc;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String text;
    /**
     * 邮件附件
     */
    private MultipartFile[] files;
    /**
     * 邮件附件在服务器存储的地址
     */
    private String[] fileUrls;
}
