package me.zhengjie.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ProjectName: jiamu
 * @Package: com.jiamu.service
 * @ClassName: QRcodeUtils
 * @Author: fengwen
 * @Description: 二维码业务层接口
 * @Date: 2022/7/16 22:14
 * @Version: 1.0.0
 */

public interface QRCodeService {

    public void generateToStream(String code, OutputStream stream);

    public void generateToStream(String code, OutputStream stream, int width);

    public void generateToStream(String code, OutputStream stream, int width, int frontColor);

    public void generateToStream(String code, OutputStream stream, int width, int frontColor, File logoFile, InputStream logoStream);

}
