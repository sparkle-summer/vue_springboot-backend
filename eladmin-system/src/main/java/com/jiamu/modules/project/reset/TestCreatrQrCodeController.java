package com.jiamu.modules.project.reset;

import com.google.zxing.BarcodeFormat;
import com.jiamu.modules.project.utils.CreateSelfQRUtils;
import com.jiamu.modules.project.test.ZXingCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @ProjectName: Bhopal_hd
 * @Package: com.jiamu.com
 * @ClassName: TestErWeiMaController，二维码Action
 * @Author: fengwen
 * @Description: 测试二维码controller层类,主要用于测试在controller层调用二维码生成工具
 * @Date: 2022/7/15 11:24
 * @Version: 1.0.0
 */

@Controller
public class TestCreatrQrCodeController extends BaseController {
    public void QRCreate() throws IOException {
        String parentId = get("parent_id");
        File bgImgFile = new File("D:/silly/lu2.jpg");//背景图片
        File QrCodeFile = new File("D:/silly/7.jpg");//生成图片位置
        String url = "http://silly.nat300.top/api/wx/login?parent_id=7";//二维码链接
        String note = "龙虎斗";//文字描述
        String tui = "龙虎斗";//文字描述

        //宣传二维码生成
        //生成图地址,背景图地址,二维码宽度,二维码高度,二维码识别地址,文字描述1,文字描述2,文字大小,图片x轴方向,图片y轴方向,文字1||2xy轴方向
        CreateSelfQRUtils.CreatQRCode(QrCodeFile, bgImgFile, 75, 75, url, note, tui, 20, 125, 420, 0, 0, 410, 210);

        ServletOutputStream out = null;
        FileInputStream ips = null;
        HttpServletResponse response = getResponse();
        //获取图片存放路径
        String imgPath = "D:/silly/7.jpg";
        ips = new FileInputStream(new File(imgPath));
        getResponse().setContentType("image/jpg");
        out = response.getOutputStream();
        //读取文件流
        int len = 0;
        byte[] buffer = new byte[1024 * 10];
        while ((len = ips.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        renderNull();
    }

    /**
     * 生成二维码图片
     * @param content   url链接
     * @param fontSize  文字大小
     * @param size      图片大小
     * @param request   页面请求
     * @param response  给页面的响应
     * @remark 前端页面调用：<img src="/o_create_dimensioncode.jspx?content=${url!}&size=90" style="width:150px;height:150px;"/>
     */
    @RequestMapping("/o_create_dimensioncode")
    public void createCodeImg(String content, Integer fontSize,Integer size, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isNotBlank(content)){
            if(size==null){
                size=100;
            }
            if(fontSize==null){
                fontSize=10;
            }
            response.setContentType("image/png");
            try {
                //使用方式
                ZXingCode zp =  ZXingCode.getInstance();
                BufferedImage bim = zp.getQRCODEBufferedImage(content, BarcodeFormat.QR_CODE, size, size, zp.getDecodeHintType());
                ImageIO.write(bim, "png", response.getOutputStream());                //如果是只要生成到本地文件夹 用以下写法                //File file=new File("存放的绝对路径，例：D://xxx.jpg");                //ImageIO.write(bim, "png", file);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
