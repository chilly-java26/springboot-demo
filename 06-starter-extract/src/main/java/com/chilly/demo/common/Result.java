package com.chilly.demo.common;

// 导入代码生成工具库lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 成功（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // 失败（默认500）
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}