package com.example.server.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日期转换
 * Converter<String , LocalDate>
 *     日期转换格式，由什么格式转换为什么格式
 */
@Component
public class DataConverter implements Converter<String , LocalDate> {

    /**
     * 将源数据转换为想要的数据格式
     * @param source
     * @return
     */
    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
