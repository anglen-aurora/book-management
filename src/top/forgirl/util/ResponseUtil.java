package top.forgirl.util;

import com.alibaba.fastjson.JSON;
import top.forgirl.domain.ResultType;
import top.forgirl.domain.vo.ResultVo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResponseUtil {

    private ResponseUtil() {
    }

    // 从输入流中读取json串，还原成JavaBean
    public static Object getBeanFromInputStream(InputStream inputStream, Class clazz) {
        byte[] bytes = new byte[10];
        int len = 0;
        String s = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            while ((len = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            s = byteArrayOutputStream.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(s);

        return JSON.parseObject(s, clazz);
    }

    public static <T> String generateFinalJson(T t) {
        return generateFinalJson(ResultType.SUCCESS,t);
    }

    public static <T> String generateFinalJson(ResultType resultType, T t) {
            return JSON.toJSONString(new ResultVo<T>(resultType,t));
    }
}
