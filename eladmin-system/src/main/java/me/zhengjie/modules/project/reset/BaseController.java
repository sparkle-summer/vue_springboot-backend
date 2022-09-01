package me.zhengjie.modules.project.reset;

import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: Bhopal_hd
 * @Package: com.jiamu.BaseController
 * @ClassName: BaseController
 * @Author: fengwen
 * @Description:
 * @Date: 2022/7/15 11:26
 * @Version: 1.0.0
 */
public class BaseController {
    public String get(String parent_id) {
        if( parent_id!=null && !"".equals(parent_id)){
            return "0";
        }
        return "1111";
    }
    public HttpServletResponse getResponse(){
        HttpServletResponse response = null;
        //response为HttpServletResponse对象
        assert false;
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        return response;
    }
    public void renderNull() {

    }
}
