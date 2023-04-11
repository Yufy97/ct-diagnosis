package cn.nineseven.utils;

import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}

    public static <T> T copyBean(Object source, Class<T> clazz) {

        T result = null;

        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> List<T> copyBeanList(List<? extends Object> list, Class<T> clazz) {
        return list.stream()
                .map(o -> BeanCopyUtils.copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}