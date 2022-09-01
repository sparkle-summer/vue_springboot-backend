package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.service.QRCodeService;
import me.zhengjie.service.dto.QrcodeDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @ProjectName: jiamu
 * @Package: com.jiamu.Controller
 * @ClassName: QRecodeController
 * @Author: fengwen
 * @Description: 生成圆角logo二维码
 * @Date: 2022/7/16 22:17
 * @Version: 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qrCode")
@Api(tags = "工具：生成二维码")
public class QrCodeController {

    private final QRCodeService qrCodeService;  // 由于使用了RequiredArgsConstructor构造器，因此不必在使用Resource 或 Autoware 注入，注意当前注入方式需要添加修饰符final

    /**
     * 请求方式传入参数,post请求方式一
     * @param request 请求参数
     * @param response 响应参数
     */
    @ApiOperation("创建常规二维码")        // swagger封装用于自动获取API
    @PostMapping(value = "/generateQrCodeReq") //路由
    @PreAuthorize("@el.check()")    // 访问前权限校验
    public void generateQrCodeReq(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request);
        try {

            String width = "256";
            String userId = request.getParameter("userId");
            String userNm = request.getParameter("userNm");
            String mobile = request.getParameter("mobile");
            String url = request.getParameter("prodType")==null?"http://baidu.com":"http://163.com";
            String qrColor = request.getParameter("qrColor");
            System.out.println("userId:"+userId+"---userNm:"+userNm+"---mobile:"+mobile+"---url:"+url+"---qrColor:"+qrColor);

            String logo="";
            OutputStream os = response.getOutputStream();
            if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(qrColor) && StringUtils.isNotBlank(logo)){
                int my_width = Integer.parseInt(width);
                int my_color = colorToHexa(qrColor);
                //int my_logo = Integer.valueOf(logo);
                log.info("hash logo");
                File logofile = new File("logo.jpg");
                qrCodeService.generateToStream(url, os, my_width, my_color, logofile,null);
            } else if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(qrColor)){
                int my_width = Integer.parseInt(width);
                int my_color = colorToHexa(qrColor);
                qrCodeService.generateToStream(url, os, my_width, my_color);
            } else if(StringUtils.isNotBlank(width)) {
                int my_width = Integer.parseInt(width);
                qrCodeService.generateToStream(url, os, my_width);
            } else {
                qrCodeService.generateToStream(url, os);
            }
            os.flush();
            os.close();
            log.info("generate qrcode succeed");
        } catch (IOException e) {
            log.error("generate qrcode error : ", e);
        }
    }

    /**
     * 通过JSON方式传入请求参数,post请求方式二
     * @param qrcodeDto 请求参数
     * @param response  响应参数
     */
    @ApiOperation("创建二维码-实体类方式")        // swagger封装用于自动获取API
    @PostMapping(value="generateQrCodeDto")
    @PreAuthorize("@el.check()")
    public void generateQrCodeDto(@RequestBody QrcodeDto qrcodeDto, HttpServletResponse response){
        try {
            String width = "256";
            String userId = qrcodeDto.getUserId();
            String userNm = qrcodeDto.getUserNm();
            String mobile = qrcodeDto.getMobile();
            String prodType = qrcodeDto.getProdType()==null?"http://baidu.com":"http://163.com";
            String qrColor = qrcodeDto.getQrColor()==null?"#A0A0A0":qrcodeDto.getQrColor();

            String logo="";
            String url;
            url = prodType;
            System.out.println("userId:"+userId+"---userNm:"+userNm+"---mobile:"+mobile+"---prodType:"+prodType+"---qrColor:"+qrColor);

            OutputStream os = response.getOutputStream();
            if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(qrColor) && StringUtils.isNotBlank(logo)){
                int my_width = Integer.parseInt(width);
                int my_color = colorToHexa(qrColor);
                log.info("hash logo");
                File logofile = new File("logo.jpg");
                qrCodeService.generateToStream(url, os, my_width, my_color, logofile,null);
            } else if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(qrColor)){
                int my_width = Integer.parseInt(width);
                int my_color = colorToHexa(qrColor);
                qrCodeService.generateToStream(url, os, my_width, my_color);
            } else if(StringUtils.isNotBlank(width)) {
                int my_width = Integer.parseInt(width);
                qrCodeService.generateToStream(url, os, my_width);
            } else {
                qrCodeService.generateToStream(url, os);
            }
            os.flush();
            os.close();
            log.info("generate qrcode succeed");
        } catch (IOException e) {
            log.error("generate qrcode error : ", e);
        }
    }

    /**
     * <p>传入参数带有二维码logo，且logo图片以文件流的方式传入，其传入参数为dataForm格式的数据，因此使用RequestParam，若传入参数为JSON格式则可使用map或实体对象类接收</p>
     * <p>此方法为不传入logo图片时调用</p>
     * @param reqMap 请求map参数
     * @param qrLogoFile 二维码传入logo图片文件流方式传入
     * @param response 响应参数
     */
    @ApiOperation("创建个性logo二维码2")        // swagger封装用于自动获取API
    @PostMapping(value="generateQrCodeLogo")
    @PreAuthorize("@el.check()")
    public void generateQrCodeLogo(@RequestParam Map<String,String> reqMap, @RequestParam("qrLogoFile") MultipartFile qrLogoFile, HttpServletResponse response){

        try {

            String width = "256";
            String userId = reqMap.get("userId");
            String userNm = reqMap.get("userNm");
            String mobile = reqMap.get("mobile");
            String prodType = reqMap.get("prodType");
            String qrColor = reqMap.get("qrColor");

            String logoPath = "";//备用
            System.out.println("userId:"+userId+"---userNm:"+userNm+"---mobile:"+mobile+"---prodType:"+prodType+"---qrColor:"+qrColor);
            if(StringUtils.isEmpty(prodType) || StringUtils.isEmpty(width) || StringUtils.isEmpty(qrColor) || StringUtils.isEmpty(mobile)){
                log.info("======param存在空值======");
                return;
            }
            String url=dealUrl(userId,userNm,mobile,prodType);

            OutputStream os = response.getOutputStream();
            if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(qrColor) && StringUtils.isNotBlank(logoPath)){ // logo图片以文件图片方式
                int my_width = Integer.parseInt(width);
                int my_color = colorToHexa(qrColor);
                log.info("hash logo");
//                File logofile = new File("logo.jpg");
                File logofile = new File(logoPath);
                qrCodeService.generateToStream(url, os, my_width, my_color, logofile,null);
            } else if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(qrColor) && qrLogoFile != null){ // logo图片文件流方式
                int my_width = Integer.parseInt(width);
                int my_color = colorToHexa(qrColor);
                log.info("hash logo");
                qrCodeService.generateToStream(url, os, my_width, my_color,null, qrLogoFile.getInputStream());
            } else if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(qrColor)){
                int my_width = Integer.parseInt(width);
                int my_color = colorToHexa(qrColor);
                qrCodeService.generateToStream(url, os, my_width, my_color);
            } else if(StringUtils.isNotBlank(width)) {
                int my_width = Integer.parseInt(width);
                qrCodeService.generateToStream(url, os, my_width);
            } else {
                qrCodeService.generateToStream(url, os);
            }
            os.flush();
            os.close();
            log.info("generate qrcode succeed");
        } catch (IOException e) {
            log.error("generate qrcode error : " + e);
        }
    }

    private String dealUrl(String userId, String userNm, String mobile, String prodType) {
        StringBuilder sbUrl=new StringBuilder();//定义返回的URL字符串
        if("apple".equals(prodType)){
            sbUrl.append("baidu.com");
        }else{
            sbUrl.append("163.com");
        }
        sbUrl.append("?userid=");
        sbUrl.append(userId);
        sbUrl.append("&userNm=");
        sbUrl.append(userNm);
        sbUrl.append("&mobile=");
        sbUrl.append(mobile);
        return sbUrl.toString();
    }

    /**
     * 转化处理传入的颜色格式,转化后为16进制
     * @param color 字符串颜色参数
     * @return int
     */
    private int colorToHexa(String color){
        if(color.startsWith("#")){
            color = color.replace("#","");
        }
        // 将处理后的字符串转为16进制
        return Integer.parseInt(color,16);
    }
}
