package com.join.lx.utils;

import com.join.lx.domain.entity.Article;
import com.join.lx.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * bean拷贝工具类
 */
public class BeanCopyUtils {

    private BeanCopyUtils() {}

    // 单个 bean对象拷贝
    // 但是希望在返回结果的时候是传入的Class的具体类型，所以这里使用泛型
    // 第二个参数Class表示需要传入的参数类型，不是具体对象，具体对象的创建在方法体中使用反射完成
    public static <V> V copyBean(Object source, Class<V> clazz) {
        V res = null;
        try {
            // 使用反射创建目标类型的具体实例
            res = clazz.newInstance();
            BeanUtils.copyProperties(source, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static <O, V> List<V> copyBeanList(List<O> source, Class<V> clazz) {
        return source.stream()
                .map(o -> copyBean(o, clazz))  // 将source集合中的元素转换成vo对象
                .collect(Collectors.toList());  // 将流中的元素收集转换成List
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setId(222L);
        article.setTitle("sdfsdf");
        HotArticleVo hotArticleVo = new HotArticleVo();
        HotArticleVo res = copyBean(article, hotArticleVo.getClass());
        System.out.println(res);

        List<Article> articleList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Article article2 = new Article();
            article.setId(222L);
            article.setTitle("sdfsdf");
            articleList.add((article2));
        }
        List<HotArticleVo> lv = copyBeanList(articleList, HotArticleVo.class);
    }

}