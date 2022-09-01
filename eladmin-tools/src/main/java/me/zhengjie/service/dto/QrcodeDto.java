package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @ProjectName: yshopmall_hd
 * @Package: com.jiamu.dto.qrcode
 * @ClassName: QrcodeDto
 * @Author: fengwen
 * @Description: 二维码实体传输类
 * @Date: 2022/8/29 11:57
 * @Version: 1.0.0
 */
@Data
public class QrcodeDto {
    private String userId	;// 用户ID
    private String userNm	;// 用户名称
    private String mobile	;// 手机号
    private String prodType ;// 产品类型
    private String qrColor  ;// 二维码颜色
}
