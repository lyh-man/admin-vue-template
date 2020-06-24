package com.lyh.admin_template.back.modules.oss.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件上传
 * </p>
 *
 * @author lyh
 * @since 2020-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BackOss对象", description="文件上传")
public class BackOss implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "文件 ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
//    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "URL 地址")
    private String fileUrl;

    @ApiModelProperty(value = "存储在 OSS 中的文件名")
    private String ossName;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
}
