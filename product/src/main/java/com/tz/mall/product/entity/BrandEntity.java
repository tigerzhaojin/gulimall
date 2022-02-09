package com.tz.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.tz.common.valid.AddGroup;
import com.tz.common.valid.ListValue;
import com.tz.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:06
 * JSR 303校验，标注字段校验注解
 */

@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "修改商品，Id不能为空",groups = {UpdateGroup.class})
	@Null(message = "新增商品，不能指定Id",groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌不能为空字符串",groups = {UpdateGroup.class,AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "Logo不能为空字符串",groups = {UpdateGroup.class,AddGroup.class})
	@URL(message = "Logo必须是合法的Url地址",groups = {UpdateGroup.class,AddGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
//
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@Pattern(regexp="^[A-Za-z]",message = "必须是英文字母",groups = {UpdateGroup.class,AddGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序字段不能为空",groups = {UpdateGroup.class,AddGroup.class})
	@Min(value = 0,message = "排序值不能小于0",groups = {UpdateGroup.class,AddGroup.class})
	private Integer sort;

}
