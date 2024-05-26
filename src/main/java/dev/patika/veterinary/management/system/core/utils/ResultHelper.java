package dev.patika.veterinary.management.system.core.utils;

import dev.patika.veterinary.management.system.core.result.Result;
import dev.patika.veterinary.management.system.core.result.ResultData;
import dev.patika.veterinary.management.system.dto.response.CursorResponse;
import org.springframework.data.domain.Page;

public class ResultHelper {

    public static <T> ResultData <T> created(T date){
        return new ResultData<>(true,Msg.CREATED,"201",date);
    }

    public static  <T> ResultData<T> validateError(T data){
        return   new ResultData<>(false, Msg.VALIDATE_ERROR,"400",data);
    }

    public  static  Result successResult(){
        return new Result(true,Msg.OK,"200");
    }
    public static <T> ResultData <T> success(T data){
        return new ResultData<>(true,Msg.OK,"200",data);
    }

    public static Result  notFoundError(String msg){
        return new Result(false,msg,"404");
    }

    public static Result  badRequestError(String msg){
        return new Result(false,msg,"400");
    }


    public static <T> ResultData <CursorResponse<T>> cursor(Page<T> pageData){
        CursorResponse<T> cursor = new CursorResponse<>();
        cursor.setPageNumber(pageData.getNumber());
        cursor.setPageSize(pageData.getSize());
        cursor.setTotalElement(pageData.getTotalElements());
        cursor.setItems(pageData.getContent());
        return ResultHelper.success(cursor);
    }


}