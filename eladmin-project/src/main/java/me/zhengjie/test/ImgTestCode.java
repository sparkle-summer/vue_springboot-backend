package me.zhengjie.test;

import com.alibaba.fastjson.JSONObject;
//import com.drew.imaging.ImageMetadataReader;
//import com.drew.imaging.ImageProcessingException;
//import com.drew.metadata.Directory;
//import com.drew.metadata.Metadata;
//import com.drew.metadata.Tag;
import me.zhengjie.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @ProjectName: yshopmall_hd
 * @Package: com.jiamu
 * @ClassName: test
 * @Author: fengwen
 * @Description: 从照片信息中获取定位信息
 * @Date: 2022/7/11 10:19
 * @Version: 1.0.0
 */

@Slf4j
public class ImgTestCode {
    public static void main(String[] args) throws Exception {
//        System.out.println(
//                "    _ _ _ _ __      __      __     \n" +
//                "   / __ __/ \\ \\    /  \\    / /  \n" +
//                "  / /_ __    \\ \\  / /\\ \\  / /  \n" +
//                " / /_ __/     \\ \\/ /  \\ \\/ /   \n" +
//                "/_/            \\/_/    \\/_/      \n" +
//
//                "\n迦慕FW电商系统管理后台启动成功 \n官网：https://www.yixiang.co 提供技术支持ﾞ  \n");
//
////        File file = new File("C:\\Users\\administrator\\Desktop\\test\\微信图片_20220711105338.jpg");
////        readImageInfo(file);
//    }
//
//    /**
//     * 提取照片里面的信息
//     *
//     * @param file 照片文件
//     */
//    private static void readImageInfo(File file) throws ImageProcessingException, Exception {
//        Metadata metadata = ImageMetadataReader.readMetadata(file);
//
//        System.out.println("---打印全部详情---");
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.format("[%s] - %s = %s\n",
//                        directory.getName(), tag.getTagName(), tag.getDescription());
//            }
//            if (directory.hasErrors()) {
//                for (String error : directory.getErrors()) {
//                    System.err.format("ERROR: %s", error);
//                }
//            }
//        }
//
//        System.out.println("--打印常用信息---");
//
//        Double lat = null;
//        Double lng = null;
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                String tagName = tag.getTagName();  //标签名
//                String desc = tag.getDescription(); //标签信息
//                switch (tagName) {
//                    case "Image Height":
//                        System.err.println("图片高度: " + desc);
//                        break;
//                    case "Image Width":
//                        System.err.println("图片宽度: " + desc);
//                        break;
//                    case "Date/Time Original":
//                        System.err.println("拍摄时间: " + desc);
//                        break;
//                    case "GPS Latitude":
//                        System.err.println("纬度 : " + desc);//30.477963888888887
//
//                        System.err.println("纬度(度分秒格式) : " + pointToLatlong(desc));
//                        lat = latLng2Decimal(desc);
//                        break;
//                    case "GPS Longitude":
//                        System.err.println("经度: " + desc);//114.40266944444446
//
//                        System.err.println("经度(度分秒格式): " + pointToLatlong(desc));
//                        lng = latLng2Decimal(desc);
//
//                        break;
//                }
//            }
//        }
//        System.err.println("--经纬度转地址--");
//        // 直接赋值
//        lat= latLng2Decimal("114° 24' 9.61\"");
//        System.out.println("lat"+lat);
//        lng = latLng2Decimal("30° 28' 40.67\"");//
//        System.out.println("lng"+lng);
//        //经纬度转地主使用百度api
//        convertGpsToLocations(lat, lng);


    }

    /**
     * 经纬度格式  转换为  度分秒格式 ,如果需要的话可以调用该方法进行转换
     *
     * @param point 坐标点
     */
    public static String pointToLatlong(String point) {
        double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
        double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("'")).trim());
        double miao = Double.parseDouble(point.substring(point.indexOf("'") + 1, point.indexOf("\"")).trim());
        double duStr = du + fen / 60 + miao / 60 / 60;
        return Double.toString(duStr);
    }

    /***
     * 经纬度坐标格式转换（* °转十进制格式）
     */
    public static double latLng2Decimal(String gps) {
        String a = gps.split("°")[0].replace(" ", "");
        String b = gps.split("°")[1].split("'")[0].replace(" ", "");
        String c = gps.split("°")[1].split("'")[1].replace(" ", "").replace("\"", "");
        return Double.parseDouble(a) + Double.parseDouble(b) / 60 + Double.parseDouble(c) / 60 / 60;
    }

    /**
     * api_key：注册的百度api的key
     * coords：经纬度坐标
     * http://api.map.baidu.com/reverse_geocoding/v3/?ak="+api_key+"&output=json&cordite=wgs84ll&location="+coords
     * <p>
     * 经纬度转地址信息
     *
     * @param gps_latitude  维度
     * @param gps_longitude 精度
     */
    private static void convertGpsToLocations(double gps_latitude, double gps_longitude) throws IOException {
        String apiKey = "YNxcSCAphFvuPD4LwcgWXwC3SEZZc7Ra";

        String res = "";
//        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + apiKey + "&output=json&coordtype=wgs84ll&location=" + (gps_latitude + "," + gps_longitude);
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/";
        String xmlInfo = "ak=" + apiKey + "&output=json&coordtype=wgs84ll&location=" + (gps_latitude + "," + gps_longitude);
        System.err.println("【url】" + url);

        res = HttpUtils.httpPostXml(url ,xmlInfo);
        JSONObject object = JSONObject.parseObject(res);
        System.out.println("JSONObject.parseObject(object):"+object);
        if (object.containsKey("result")) {
            JSONObject result = object.getJSONObject("result");
            if (result.containsKey("addressComponent")) {
                JSONObject address = object.getJSONObject("result").getJSONObject("addressComponent");
//                System.err.println("拍摄地点：" + address.get("country") + " " + address.get("province") + " " + address.get("city") + " " + address.get("district") + " "
//                        + address.get("street") + " " + result.get("formatted_address") + " " + result.get("business"));
                System.err.println("拍摄地点：" + address.get("country") + " \n" + address.get("province") + " \n" + address.get("city") + " \n" + address.get("district") + " \n"
                        + address.get("street") + " \n" );
            }
        }
    }
}
