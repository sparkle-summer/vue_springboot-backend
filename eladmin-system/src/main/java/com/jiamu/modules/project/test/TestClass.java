package com.jiamu.modules.project.test;
import com.jiamu.modules.project.utils.CreateSelfQRUtils;
import com.jiamu.service.QRCodeService;
import com.jiamu.service.impl.QRCodeServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;

/**
 * @ProjectName: Bhopal_hd
 * @Package: com.jiamu
 * @ClassName: TestClass
 * @Author: fengwen
 * @Description: 测试各个功能发放的测试类
 * @Date: 2022/7/16 13:06
 * @Version: 1.0.0
 */
public class TestClass {
    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    /**
     * @Description: 测试生成的二维码中心代有图片的方法类
     * @Date: 2022年7月16日 13点15分
     */
    @Test
    public void testCenterPicQr(){
        logger.info("---------调用生成二维码--------");
        System.out.println("---------调用生成二维码--------");
        // 1.构造生成二维码的基本参数
        String contentUrl="https://baidu.com";
        String targetPic = "edema.jpg";
        String logoPic = "user.jpg";
        String filePath = "D:/TestProgram/testfile/erweimaImage";
        // 2.生成图片二维码完成
        CreateSelfQRUtils.addLogo_QRCode(contentUrl, targetPic, logoPic,  filePath);

        logger.info("---------给生成二维码添加文字描述--------");
        System.out.println("---------给生成二维码添加文字描述--------");
        // 3.构造生成字体参数
        int font = 20; //字体大小
        int fontStyle = 4; //字体风格
        //因为要在二维码下方附上文字，所以把图片设置为长方形（高大于宽）
        int width = 400;
        int height = 460;
        //用来存放的带有logo+文字的二维码图片
//        String newImageWithText = "D:/TestProgram/testfile/erweimaImage/newImageWithText.jpg";
        //带有logo二维码图片
//        String targetImage = "D:/TestProgram/testfile/erweimaImage/targetImage.png";
        //附加在图片上的文字信息
        String text = "author： fengwen";
        //在二维码下方添加文字（文字居中）
//        CreateSelfQRUtils.pressText(text, filePath, newImageWithText, targetImage, fontStyle, Color.blue, font, width, height) ;
        CreateSelfQRUtils.pressText(text, filePath, targetPic, targetPic, fontStyle, Color.blue, font, width, height) ;
        logger.info("---------生成调用二维码工具类完成！！！--------");
        System.out.println("---------生成调用二维码工具类完成！！！----------");

    }

    /**
     * @Description: 测试二维码controller层类,主要用于测试在controller层调用二维码生成工具
     * @Date: 2022年7月16日 13点16分
     */
    @Test
    public void testGeneralQr(){
        System.out.println("---------生成调用二维码工具类开始！！！----------");
        File bgImgFile = new File("D:\\TestProgram\\testfile\\erweimaImage\\user.jpg");//背景图片
        File QrCodeFile = new File("D:\\TestProgram\\testfile\\erweimaImage\\erweimaFinish.jpg");//生成图片位置
        // 判定文件是否存在，如果文件不存在则直接退出
        if (!bgImgFile.isFile()) {
            System.out.print("file not find !");
            System.exit(0);
        }
//        String url = "http://silly.nat300.top/api/wx/login?parent_id=7";//二维码链接
        String url = "https://www.baidu.com/";//二维码链接
        String note = "客户经理：冯文";//文字描述
        String tui = "联系电话：16602340632";//文字描述

        //宣传二维码生成，其中参数对应为：生成图地址,背景图地址,二维码宽度,二维码高度,二维码识别地址,文字描述1,文字描述2,文字大小,图片x轴方向,图片y轴方向,文字1||2xy轴方向
        CreateSelfQRUtils.CreatQRCode(QrCodeFile, bgImgFile, 450, 450, url, note, tui, 20, 100, 100, 450, 30, 450, 60);
        System.out.println("---------生成调用二维码工具类完成！！！----------");
    }

    /**
     * @Description: 用于测试生成logo为圆角二维码
     * @Date: 2022年8月29日15:31:21
     */
    @Test
    public void testCirleQr(){
        String code ="http://baidu.com";
        String width = "400";
        String color = "400";
        String logo = "user.jpg";
//            OutputStream os = response.getOutputStream();
        if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(color) && StringUtils.isNotBlank(logo)){
            int my_width = Integer.parseInt(width);
            int my_color = Integer.parseInt(color);
            //int my_logo = Integer.valueOf(logo);
//                LOGGER.info("hash logo");
            File logofile = new File("D:\\TestProgram\\testfile\\erweimaImage\\user.jpg");
            QRCodeService qrCodeService=new QRCodeServiceImpl();
            qrCodeService.generateToStream(code, null, my_width, my_color, logofile,null);
        }
//            os.flush();
//            os.close();
        System.out.println("generate qrcode succeed");
    }

}
