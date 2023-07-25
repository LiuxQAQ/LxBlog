package com.join.lx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.join.lx.annotation.SystemLog;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Category;
import com.join.lx.domain.vo.CategoryVo;
import com.join.lx.domain.vo.ExcelCategoryVo;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.service.CategoryService;
import com.join.lx.utils.BeanCopyUtils;
import com.join.lx.utils.WebUtils;
import org.apache.commons.collections4.queue.PredicatedQueue;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @SystemLog(businessName = "获取分类列表")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @SystemLog(businessName = "分类导出")
    public void export(HttpServletResponse response){
        // 设置下载文件的请求头
        try {
            WebUtils.setDownLoadHeader(SystemConstants.FILE_NAME_CATEGORY, response);
            // 获取需要导出的数据
            List<Category> list = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            // 把数据写入到Excel种
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
//            e.printStackTrace();
            // 出现错误响应json数据
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
