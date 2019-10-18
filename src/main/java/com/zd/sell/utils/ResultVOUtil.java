package com.zd.sell.utils;

import com.zd.sell.VO.ResultVO;

/**
 * 前端返回数据的全部的json数据
 */
public class ResultVOUtil {

    /**
     * 当数据处理正常时，返回正常状态码、状态文字、和数据
     * @param object
     * @return
     */
    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success(){
        return success(null);
    }


    /**
     * 返回失败的状态码、信息
     * @param code
     * @param msg
     * @return
     */
    public static ResultVO error(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(code);
        return resultVO;
    }
}
