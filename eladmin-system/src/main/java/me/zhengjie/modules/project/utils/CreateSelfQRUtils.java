package me.zhengjie.modules.project.utils;

import com.github.binarywang.utils.qrcode.MatrixToImageWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: Bhopal_hd
 * @Package: com.jiamu.utils
 * @ClassName: CreateSelfQRUtils
 * @Author: fengwen
 * @Description: 整合一套属于自己的二维码工具类
 * @Date: 2022/7/16 11:27
 * @Version: 1.0.0
 */
public class CreateSelfQRUtils {
    // logo默认边框颜色
    private static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
    // logo默认边框宽度
    private static final int DEFAULT_BORDER = 2;
    // logo大小默认为照片的1/6
    private static final int DEFAULT_LOGOPART = 6;

    private final int border;
    private final Color borderColor;
    private final int logoPart;
    /**
     * Creates a default config with on color {@link # BLACK} and off color
     * {@link # WHITE}, generating normal black-on-white barcodes.
     * 在颜色 {@link # 黑}和颜色上创建一个默认配置
     * {@link # WHITE}，生成正常的黑白条码。
     */
    public CreateSelfQRUtils() {
        this(DEFAULT_BORDER, DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);

    }

    public CreateSelfQRUtils(int border, Color borderColor, int logoPart) {
        this.border = border;
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }
    /**
     * 以下为生成代有背景图片的二维码，但生成的二维码不带有logo图片
     */
    private static final int QRCOLOR = 2105119;
    private static final int BGWHITE = 16777215;
    private static final Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;
        {
            // 设置二维码的容错性，分别为：L-低、M-中、H-高
            this.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置UTF-8， 防止中文乱码
            this.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 设置二维码四周白色区域的大小
            this.put(EncodeHintType.MARGIN, 1);
        }
    };

    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorder() {
        return border;
    }

    public int getLogoPart() {
        return logoPart;
    }

    /**
     * @description：     给二维码图片添加Logo
     * @param contentUrl 推广链接或商品链接地址内容
     * @param targetPic  目标图片名称
     * @param logoPic    二维码中心需要嵌入的logo图片名称
     * @param filePath   文件路径
     */
    public static void addLogo_QRCode(String contentUrl,String targetPic, String logoPic,  String filePath) {
        // 创建绘制二维码的对象
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        // 存放二维码的图片文件，width:图片完整的宽;height:图片完整的高，因为要在二维码下方附上文字，所以把图片设置为长方形（高大于宽）
        int width = 400;
        int height = 460;

        // 绘制二维码，记得调用multiFormatWriter.encode()时最后要带上hints参数，不然上面设置无效
        BitMatrix bitMatrix;
        try {
            bitMatrix = multiFormatWriter.encode(contentUrl, BarcodeFormat.QR_CODE, width, height, hints);
            File qrcFilePic = new File(filePath, targetPic);         //qrcFile用来存放生成的二维码图片（无logo，无文字）
            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", qrcFilePic);//开始画二维码,内容存放之qrcFile图片文件中
            File logoFilePic = new File(filePath, logoPic);  //logoFile准备放在二维码中的图片(path:图片路径,图片名称)
            // 判定文件是否存在，如果文件不存在则直接退出
            if (!qrcFilePic.isFile() || !logoFilePic.isFile()) {
                System.out.print("file not find !");
                System.exit(0);
            }

            // 读取二维码图片，并构建绘图对象
            BufferedImage image = ImageIO.read(qrcFilePic);
            Graphics2D g = image.createGraphics();

            // 读取Logo图片
            BufferedImage logo = ImageIO.read(logoFilePic);
            CreateSelfQRUtils logoConfig=new CreateSelfQRUtils();
            // 保持二维码是正方形的,将长宽设置为相同
            int widthLogo = image.getWidth() / logoConfig.getLogoPart();
            int heightLogo = image.getWidth() / logoConfig.getLogoPart();

            // 计算logo图片放置二维码中图片中的位置
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;

            // 开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 10, 10);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            // 将最终生成的二维码图片覆盖原始的图片文件
            ImageIO.write(image, "jpeg", qrcFilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pressText 文字
     * @param newImg    带文字的图片
     * @param targetImg 需要添加文字的图片
     * @param fontStyle 字体样式
     * @param color     字体颜色
     * @param fontSize  字体大小
     * @param width     字体宽度
     * @param height    字体高度
     * @description：    为图片添加文字
     */
    public static void pressText(String pressText,String filePath, String newImg, String targetImg, int fontStyle, Color color, int fontSize, int width, int height) {

        //计算文字开始的位置
        //x开始的位置：（图片宽度-字体大小*字的个数）/2
        int startX = (width - (fontSize * pressText.length())) / 300;
        //y开始的位置：图片高度-（图片高度-图片宽度）/2
        int startY = height - (height - width) / 3;

        try {
            File file = new File(filePath, targetImg);
            Image src = ImageIO.read(file);
            int imageW = src.getWidth(null);
            int imageH = src.getHeight(null);
            BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, imageW, imageH, null);
            g.setColor(color);
            g.setFont(new Font(null, fontStyle, fontSize));
            g.drawString(pressText, startX, startY);
            g.drawString(pressText+"1", startX+20, startY-400);
//            g.drawString(pressText, 0, 20);
            g.dispose();

            FileOutputStream out = new FileOutputStream(filePath+"/"+newImg);
            ImageIO.write(image, "JPEG", out);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            System.out.println("image press success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @description：   方案二：用于生成普通二维码，且含有背景图片，以及基本信息，其中共含有三部分信息：url、描述1、描述2
     * @param codeFile  用于生成的二维码图片文件
     * @param bgImgFile 用于生成二维码的背景图片
     * @param WIDTH     二维码图片宽度
     * @param HEIGHT    二维码图片高度
     * @param qrUrl     配置作为图片内容的url链接地址
     * @param note      描述1
     * @param tui       描述2
     * @param size      字体大小
     * @param imagesX   生成二维码的图片x轴偏移量
     * @param imagesY   生成二维码的图片y轴偏移量
     * @param text1X    描述1字体的x轴偏移量
     * @param text1Y    描述1字体的y轴偏移量
     * @param text2X    描述2字体的x轴偏移量
     * @param text2Y    描述2字体的y轴偏移量
     */
    public static void CreatQRCode(File codeFile, File bgImgFile, Integer WIDTH, Integer HEIGHT, String qrUrl, String note, String tui, Integer size, Integer imagesX, Integer imagesY, Integer text1X, Integer text1Y, Integer text2X, Integer text2Y) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, 1);

            int y;
            for(int x = 0; x < WIDTH; ++x) {
                for(y = 0; y < HEIGHT; ++y) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }

            BufferedImage backgroundImage = ImageIO.read(bgImgFile);
            y = backgroundImage.getWidth();
            int qrWidth = image.getWidth();
            int disx = y - qrWidth - imagesX;
            int disy = imagesY;
            Graphics2D rng = backgroundImage.createGraphics();
            rng.setComposite(AlphaComposite.getInstance(10));
            rng.drawImage(image, disx, disy, WIDTH, HEIGHT, (ImageObserver)null);
            Color textColor = Color.white;
            rng.setColor(textColor);
            rng.drawImage(backgroundImage, 0, 0, (ImageObserver)null);
            rng.setFont(new Font("微软雅黑,Arial", 1, size));
            rng.setColor(Color.black);

            int strWidth = rng.getFontMetrics().stringWidth(note);
            int disx1 = y - strWidth - text1X;
            rng.drawString(note, disx1, text1Y);

            int disx2 = y - strWidth - text2X;
            rng.drawString(tui, disx2, text2Y);
            rng.dispose();
            backgroundImage.flush();
            ImageIO.write(backgroundImage, "png", codeFile);
        } catch (Exception var27) {
            var27.printStackTrace();
        }

    }

    /**
     * @description： 测试验证整理后的二维码生成工具类
     * @param  args   参数
     */
    public static void main(String[] args){
        // 1.构造生成二维码的基本参数
        String contentUrl="https://baidu.com";
        String targetPic = "edema.jpg";
        String logoPic = "user.jpg";
        String filePath = "D:/TestProgram/testfile/erweimaImage";
        String width="256";
        String height="256";
        // 2.生成图片二维码完成
        addLogo_QRCode(contentUrl, targetPic, logoPic,  filePath);

        // 3.构造生成字体参数
        int font = 20; //字体大小
        int fontStyle = 4; //字体风格

        //用来存放的带有logo+文字的二维码图片
        String newImageWithText = "D:/TestProgram/testfile/erweimaImage/newImageWithText.jpg";
        //带有logo二维码图片
        String targetImage = "D:/TestProgram/testfile/erweimaImage/targetImage.png";
        //附加在图片上的文字信息
        String text = "my name is Shi Linwei";
        //在二维码下方添加文字（文字居中）
//        CreateSelfQRUtils.pressText(text, newImageWithText, targetImage, fontStyle, Color.red, font,  width,  height) ;
        // 测试生成随机数
//        char[] letters = {'A', 'C', '1', '2', '3','4', 'a', 'e', 'f', 'z'}; //这里根据你的需要初始化不同的字符
//        char[] letters = {'0', '5', '1', '2', '3','4', '6', '7', '8', '9'}; //这里根据你的需要初始化不同的字符
//
//        String tStr = String.valueOf(System.currentTimeMillis());
//        /*因为tStr的字符只有‘0’-‘9’，我们可以把它看成索引到letters中找相应的字符，这样相当于“置换”，所以是不会重复的。*/
//        System.out.println(tStr);
//        StringBuilder sb = new StringBuilder();
//        for(int i=1;i<tStr.length();i++)
//        {
//            sb.append(letters[tStr.charAt(i)-'0']);
//        }
//        System.out.println(sb.toString());


    }




    }
