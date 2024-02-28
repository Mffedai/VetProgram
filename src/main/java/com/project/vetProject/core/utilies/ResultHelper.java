package com.project.vetProject.core.utilies;

import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;


public class ResultHelper {
    public static <T> ResultData<T> created(T data){
        return new ResultData<>(true, Msg.CREATED, "201", data);
    }
    public static <T>ResultData<T> validateError(T data){
        return new ResultData<>(false, Msg.VALIDATE_ERROR, "400", data);
    }
    public static <T>ResultData<T> success(T data){
        return new ResultData<>(true, Msg.OK, "200", data);
    }
    public static Result ok(){
        return new Result(true, Msg.OK, "200");
    }
    public static <T> ResultData<T> error(String message){
        return new ResultData<>(false, message, "400", null);
    }
    public static Result NotFoundError(String msg){
        return new Result(false, msg, "404");
    }
    public static <T>ResultData<CursorResponse<T>> cursor(Page<T> pageData){
        CursorResponse<T> cursor = new CursorResponse<>();
        cursor.setItems(pageData.getContent());
        cursor.setPageNumber(pageData.getNumber());
        cursor.setPageSize(pageData.getSize());
        cursor.setTotalElements(pageData.getTotalElements());
        return ResultHelper.success(cursor);
    }
}
