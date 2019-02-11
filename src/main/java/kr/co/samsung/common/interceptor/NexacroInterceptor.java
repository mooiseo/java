package kr.co.samsung.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nexacro.xapi.data.PlatformData;
import com.nexacro.xapi.data.VariableList;
import com.nexacro.xapi.tx.HttpPlatformRequest;
import com.nexacro.xapi.tx.HttpPlatformResponse;
import com.nexacro.xapi.tx.PlatformType;
import kr.co.samsung.common.util.NexacroLogger;


/**
 * @Package  com.bank.his.common.interceptor
 * @Class    NexacroInterceptor.java
 * @Create   2018. 05. 08.
 * @Author   �뿀誘쇱�
 * @Description �꽖�궗�겕濡� PlatformData 泥섎━ �씤�꽣�뀎�꽣 �겢�옒�뒪
 *
 */

public class NexacroInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpPlatformRequest httpPlatformRequest = new HttpPlatformRequest(request);
        httpPlatformRequest.receiveData();

        PlatformData inData = httpPlatformRequest.getData();
        PlatformData outData = new PlatformData();

        request.setAttribute("inData", inData);
        request.setAttribute("outData", outData);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {
        PlatformData outData = (PlatformData) request.getAttribute("outData");
        VariableList variableList = outData.getVariableList();

        if (exception != null) {
            exception.printStackTrace();
            variableList.add("ErrorCode", 0);
            variableList.add("ErrorMsg", "Transaction Failed");
        } else {
            variableList.add("ErrorCode", 1);
            variableList.add("ErrorMsg", "Transaction Success");
        }

        HttpPlatformResponse httpPlatformResponse = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
        //outData.setVariableList(variableList);
        httpPlatformResponse.setData(outData);
        NexacroLogger.debug(outData);

        httpPlatformResponse.sendData();
        outData = null;
        super.afterCompletion(request, response, handler, exception);
    }
}
