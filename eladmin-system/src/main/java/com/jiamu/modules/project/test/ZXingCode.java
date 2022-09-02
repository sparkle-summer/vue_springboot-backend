package com.jiamu.modules.project.test;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: Bhopal_hd
 * @Package: com.jiamu.erweimaEnDe
 * @ClassName: ZXingCode
 * @Author: fengwen
 * @Description: 二维码生成以及解析功能实现
 * @Date: 2022/7/16 15:02
 * @Version: 1.0.0
 */
public class ZXingCode {

    private static final int BLACK = 0xFF000000;//黑色
    private static final int WHITE = 0xFFFFFFFF;//白色

    private static class SingletonHolder{
        private final static  ZXingCode INSTANCE=new ZXingCode();
    }

    private ZXingCode(){}

    public static ZXingCode getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 给二维码图片添加Logo
     * @param qrPic 初版二维码图片
     * @param logoPic 需要嵌入的logo图片
     */
    public void addLogoQRCode(File qrPic, File logoPic, ZxingLogoConfig logoConfig) {
        try {
            if (!qrPic.isFile() || !logoPic.isFile()) {
                System.out.print("file not find !");
                System.exit(0);
            }
            //读取二维码图片，并构建绘图对象
            BufferedImage image = ImageIO.read(qrPic);
            Graphics2D g = image.createGraphics();

            //读取Logo图片
            BufferedImage logo = ImageIO.read(logoPic);

            // 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
            int widthLogo = Math.min(logo.getWidth(null), image.getWidth() * 2 / 10);
            int heightLogo = logo.getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10): logo.getWidth(null);

            // 计算图片放置位置,logo放在中心
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            // logo放在右下角,开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            logo.flush();
            image.flush();

            ImageIO.write(image, "png", new File("D:/test/2.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description： 添加logo图片
     * @param image 图片缓冲流
     * @param logoPic logo图片
     * @param logoConfig logo默认设置
     * @return 返回生成好的图片缓冲流
     */
    public BufferedImage addLogoQRCode(BufferedImage image, File logoPic, ZxingLogoConfig logoConfig) {
        try {
            if (image==null || !logoPic.isFile()) {
                System.out.print("file not find !");
                return image;
            }

            // 读取二维码图片，并构建绘图对象
            Graphics2D g = image.createGraphics();
            //读取Logo图片
            BufferedImage logo = ImageIO.read(logoPic);
            //设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
            int widthLogo = Math.min(logo.getWidth(null), image.getWidth() * 2 / 10),
                heightLogo = logo.getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10) : logo.getWidth(null);

            // 计算图片放置位置，logo放在中心
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            //logo放在右下角
//            int x = (image.getWidth() - widthLogo);
//            int y = (image.getHeight() - heightLogo);

            // 开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            logo.flush();
            image.flush();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * @description： 为二维码添加logo文字描述
     * @param image 二维码图片缓冲流
     * @param logoWord logo文字描述
     * @param fontSize logo字体大小
     * @param logoConfig logo文字基础配置
     * @return 返回重写后的缓冲流
     */
    public BufferedImage addLogoWordQRCode(BufferedImage image, String logoWord, Integer fontSize, ZxingLogoConfig logoConfig) {
        try {
            if (image==null || StringUtils.isBlank(logoWord)) {
                System.out.print("file not find !");
                return image;
            }
            Graphics2D g = image.createGraphics();
            int widthLogo = g.getFontMetrics().stringWidth(logoWord);
            int heightLogo= 10;
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            // 开始绘制图片
            Font font=new Font("黑体", Font.PLAIN, fontSize);
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawString(logoWord, x, y);
            g.dispose();
            image.flush();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 二维码的解析
     *
     * @param file 二维码文件
     */
    public void parseQRCODEImage(File file) {
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            if (!file.exists()) {
                return;
            }

            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            Result result = formatReader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将二维码生成为文件
     *
     * @param bm 二维码文件流
     * @param imageFormat 二维码需要生成的文件格式
     * @param file 生成的文件
     */
    public void decodeQRCODE2ImageFile(BitMatrix bm, String imageFormat, File file) {
        try {
            if (null == file || file.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("文件异常，或扩展名有问题！");
            }

            BufferedImage bi = fileToBufferedImage(bm);
            ImageIO.write(bi, !"".equals(imageFormat.trim())?imageFormat:"png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建初始化二维码
     *
     * @param bm 二维码文件流
     * @return 二维码图片缓冲流
     */
    public BufferedImage fileToBufferedImage(BitMatrix bm) {
        BufferedImage image = null;
        try {
            int w = bm.getWidth(), h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 生成二维码bufferedImage图片
     *
     * @param content 编码内容
     * @param barcodeFormat 编码类型
     * @param width 图片宽度
     * @param height 图片高度
     * @param hints 设置参数
     * @return 二维码缓冲流
     */
    public BufferedImage getQRCODEBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, ?> hints) {
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try {
            multiFormatWriter = new MultiFormatWriter();

            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);

            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFF000000）白（0xFFFFFFFF）两色
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? BLACK : WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 设置二维码的格式参数
     *
     * @return 返回二维码设定的格式配置参数
     */
    public Map<EncodeHintType, Object> getDecodeHintType() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 设置二维码边框空白，默认值为0
        hints.put(EncodeHintType.MARGIN, 0);
//        hints.put(EncodeHintType.MAX_SIZE, 350);
//        hints.put(EncodeHintType.MIN_SIZE, 100);

        return hints;
    }
}
