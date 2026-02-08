package com.anglyao.backend.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.util.Date;

/**
* 
* @TableName bots
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bot implements Serializable {

    /**
    * Bot的id
    */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
    * 创建者ID
    */
    @TableField(value = "creator_id")
    private Integer creatorId;

    /**
    * Bot的名字
    */
    @TableField(value = "bot_name")
    private String botName;

    /**
    * Bot的描述
    */
    private String description;

    /**
    * Bot逻辑代码
    */
    private String code;

    /**
    * Bot的排位分数
    */
    private Integer rating;

    /**
    * Bot的创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
    * Bot的更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
