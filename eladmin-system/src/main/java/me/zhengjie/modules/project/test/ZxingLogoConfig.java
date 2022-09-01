package me.zhengjie.modules.project.test;

import java.awt.*;

/**
 * @ProjectName: yshopmall_hd
 * @Package: com.jiamu.erweimaEnDe
 * @ClassName: ZxingLogoConfig
 * @Author: fengwen
 * @Description: 进本配置类
 * @Date: 2022/7/16 14:49
 * @Version: 1.0.0
 */


public class ZxingLogoConfig {
    // logo默认边框颜色
    public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
    // logo默认边框宽度
    public static final int DEFAULT_BORDER = 2;
    // logo大小默认为照片的1/5
    public static final int DEFAULT_LOGOPART = 5;

    private final int border ;
    private final Color borderColor;
    private final int logoPart;

    /**
     * Creates a default config with on color {@link # BLACK} and off color
     * {@link # WHITE}, generating normal black-on-white barcodes.
     */
    public ZxingLogoConfig() {
        this(DEFAULT_BORDER,DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
    }

    public ZxingLogoConfig(int border,Color borderColor, int logoPart) {
        this.border = border;
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorder() {
        return border;
    }

    public int getLogoPart() {
        return logoPart;
    }
}
