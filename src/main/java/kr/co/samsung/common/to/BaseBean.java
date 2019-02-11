package kr.co.samsung.common.to;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Package  com.bank.his.common.to
 * @Class    BaseBean.java
 * @Create   2018. 05. 08.
 * @Author   허민준
 * @Description 모든 TO(Transfer Object)의 부모, 상태값과 에러코드, 에러메세지를 저장한다
 */
@Getter
@Setter
@Data
public class BaseBean {

	private String status = "normal";
    private String errorCode;
    private String errorMsg;
}
