package com.lyh.admin_template.back.modules.sms.entity;

import lombok.Data;

/**
 * 用于接收并转换 sms 返回的数据
 */
@Data
public class SmsResponse {
    private String Message;
    private String RequestId;
    private String Code;
    private String BizId;
}
