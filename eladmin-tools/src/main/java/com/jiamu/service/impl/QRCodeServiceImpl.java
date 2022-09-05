package com.jiamu.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jiamu.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: jiamu
 * @Package: com.jiamu.service.Impl
 * @ClassName: QRcodeServiceImpl
 * @Author: fengwen
 * @Description: 实现二维码接口类生成圆角logo二维码
 * @Date: 2022/7/16 22:18
 * @Version: 1.0.0
 */

@Service
@RequiredArgsConstructor
public class QRCodeServiceImpl implements QRCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeServiceImpl.class);
    // 二维码的宽
    private static final int WIDTH = 250;
    // 中间图片的宽
    private static final int IMGWIDTH = 60;
    // 圆角半径
    private static final int RADIUS = 10;
    // 留白填充宽度
    private static final int MARGIN = 4;

    private static final int FRONTCOLOR = 0x00000000;//0x00808080;

    /**
     * 功能描述：生成普通二维码到输出流
     */
    @Override
    public void generateToStream(String code, OutputStream stream) {
        this.generateToStream(code, stream, WIDTH, FRONTCOLOR, null, null);
    }

    @Override
    public void generateToStream(String code, OutputStream stream, int width) {
        this.generateToStream(code, stream, width, FRONTCOLOR, null,null);
    }

    @Override
    public void generateToStream(String code, OutputStream stream, int width, int frontColor) {
        this.generateToStream(code, stream, width, frontColor, null, null);
    }

    @Override
    public void generateToStream(String code, OutputStream stream, int width, int frontColor, File logoFile, InputStream logoSteam) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 修正容量高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 边框留白
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(code, BarcodeFormat.QR_CODE, width, width, hints);
        } catch (WriterException e) {
            LOGGER.error("", e);
        }
        // 将最终生成的二维码图片覆盖原始的图片文件
        assert matrix != null;
        proc(matrix, stream, frontColor, logoFile, logoSteam);
    }


    /**
     *
     * @param matrix 生成的原始二维码图片
     * @param stream 输出响应流
     * @param frontColor 二维码颜色
     * @param logoFile logo图片
     * @param logoStream logo图片流
     */
    public void proc(BitMatrix matrix, OutputStream stream, int frontColor, File logoFile,InputStream logoStream) {
        int width = matrix.getWidth();
        int high = matrix.getWidth();
        // 处理后图片的数据
        int[] pixels = new int[width * width];
        // 中间图片数组数据
        int[][] src = null;
        // logo文件传入判定标记
        boolean hashlogo = false;
        if(logoFile != null){
            src = getPic(logoFile,null);
            hashlogo = true;
        }else if(logoStream !=null){
            src = getPic(null,logoStream);
            hashlogo = true;
        }
        // 填充色
        int margincolor = 0xffffffff;// 白色
        int w_half = width / 2;
        int frame = MARGIN;
        int img_half = IMGWIDTH / 2;
        int r = RADIUS;
        int near = width / 2 - img_half - frame + r;//101
        int far = width / 2 + img_half + frame - r;//149
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                if(!hashlogo){
                    // 二维码
                    pixels[y * width + x] = matrix.get(x, y) ? frontColor : margincolor;
                } else {
                    // 中间图片
                    if (x > w_half - img_half && x < w_half + img_half
                            && y > w_half - img_half && y < w_half + img_half) {
                        //
                        pixels[y * width + x] = src[x - w_half + img_half][y - w_half + img_half];

                    } else if ((x > w_half - img_half - frame // 左边框
                            && x < w_half - img_half + frame && y > w_half - img_half - frame && y < w_half
                            + img_half + frame)
                            || (x > w_half + img_half - frame // 右边框
                            && x < w_half + img_half + frame
                            && y > w_half - img_half - frame && y < w_half + img_half
                            + frame)
                            || (x > w_half - img_half - frame // 上边框
                            && x < w_half + img_half + frame
                            && y > w_half - img_half - frame && y < w_half - img_half
                            + frame)
                            || (x > w_half - img_half - frame // 下边框
                            && x < w_half + img_half + frame
                            && y > w_half + img_half - frame && y < w_half + img_half
                            + frame)) {

                        // 圆角处理
                        if(x<near && y<near && (near-x)*(near-x)+(near-y)*(near-y)> r*r){
                            // 左上圆角
                            pixels[y * width + x] = matrix.get(x, y) ? frontColor : margincolor;
                        } else if(x>far && y<near && (x-far)*(x-far)+(near-y)*(near-y) > r*r){
                            // 右上圆角
                            pixels[y * width + x] = matrix.get(x, y) ? frontColor : margincolor;
                        } else if(x<near && y>far && (near-x)*(near-x)+(y-far)*(y-far) > r*r){
                            // 左下圆角
                            pixels[y * width + x] = matrix.get(x, y) ? frontColor : margincolor;
                        } else if(x>far && y>far && (x-far)*(x-far)+(y-far)*(y-far) > r*r){
                            // 右下圆角
                            pixels[y * width + x] = matrix.get(x, y) ? frontColor : margincolor;
                        } else {
                            // 边框填充颜色
                            pixels[y * width + x] = margincolor;
                        }
                    } else {
                        // 二维码
                        pixels[y * width + x] = matrix.get(x, y) ? frontColor : margincolor;
                    }
                }
            }
        }
        BufferedImage image = new BufferedImage(width, high, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, width, pixels);
        try {

            File qrcFilePic = new File("D:\\TestProgram\\testfile\\erweimaImage", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))+"qr.png");
            if(stream!=null){// 判定输出流不为空则输出至页面
                ImageIO.write(image, "png", stream);
            }else{// 输出之图片文件，主要用于测试
                ImageIO.write(image, "png", qrcFilePic);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片的压缩、圆角处理,并生成数组,其中两个参数紧能传入一个值
     * @param logoFile logo图片文件
     * @param logoStream logo图片流
     * @return 返回二维数组
     */
    public int[][] getPic(File logoFile,InputStream logoStream) {
        BufferedImage biSrc = null;
        try {
            // 判定传入参数类型
            biSrc = (logoFile==null?ImageIO.read(logoStream):ImageIO.read(logoFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage biTarget = new BufferedImage(IMGWIDTH, IMGWIDTH, BufferedImage.TYPE_3BYTE_BGR);
        assert biSrc != null;
        biTarget.getGraphics().drawImage(biSrc.getScaledInstance(IMGWIDTH, IMGWIDTH, Image.SCALE_SMOOTH), 0, 0, null);
        int[][] src = new int[IMGWIDTH][IMGWIDTH];
        // 圆角处理半径
        int r = RADIUS;
        int max = IMGWIDTH;
        int bordercolor = 0x00000000;
        int whitecolor = 0xffffffff;
        for (int x = 0; x < IMGWIDTH; x++) {
            for (int y = 0; y < IMGWIDTH; y++) {
                int i = (r - x) * (r - x) + (r - y) * (r - y);
                if(x<r&&y<r&&(i >(r-1)*(r-1))){
                    // 左上圆角
                    if(i >r*r){
                        src[x][y] = whitecolor;
                    } else {
                        src[x][y] = bordercolor;
                    }
                } else {
                    int i1 = (x + r - max) * (x + r - max);
                    int i5 = i1 + (r - y) * (r - y);
                    if (x>(max-r)&&y<r&& i5 >(r-1)*(r-1)){
                        // 右上圆角
                        if(i5 >r*r){
                            src[x][y] = whitecolor;
                        }else{
                            src[x][y] = bordercolor;
                        }
                    } else {
                        int i4 = (y + r - max) * (y + r - max);
                        int i3 = (r - x) * (r - x) + i4;
                        if (x<r&&y>(max-r)&& i3 >(r-1)*(r-1)){
                            // 左下圆角
                            if(i3 >r*r){
                                src[x][y] = whitecolor;
                            }else{
                                src[x][y] = bordercolor;
                            }
                        } else if (x>(max-r)&&y>(max-r)&& i1 + i4 >(r-1)*(r-1)){
                            // 右下圆角
                            if(i1 + i4 >r*r){
                                src[x][y] = whitecolor;
                            }else{
                                src[x][y] = bordercolor;
                            }
                        } else {
                            if(x >= r && x <= max - r && (y == 0 || y == 1 || y == max - 1) || y >= r && y <= max - r && (x == 0 || x == 1 || x == max - 1)){
                                // 四周除圆角的边框
                                src[x][y] = bordercolor;
                            } else {
                                // 图片值
                                src[x][y] = biTarget.getRGB(x, y);
                            }
                        }
                    }
                }
            }
        }
        return src;
    }
}
