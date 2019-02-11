package kr.co.samsung.log.base.to;

import kr.co.samsung.common.annotation.Dataset;
import kr.co.samsung.common.to.BaseBean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Package  com.bank.his.log.base.to
 * @Class    OutLogCdBean.java
 * @Create   2018. 05. 08.
 * @Author   허민준
 * @Description 바인드된 코드 출력용
 *
 * @LastUpdated 
 */
@Getter
@Setter
@ToString
@Dataset(name = "dsOutLogCd")
public class OutLogCdBean extends BaseBean {

    private String code,    // 코드 
                   value;   // 코드명

}