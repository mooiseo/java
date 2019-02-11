package kr.co.samsung.log.base.to;

import kr.co.samsung.common.annotation.Dataset;
import kr.co.samsung.common.to.BaseBean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Package  com.hospital.his.log.base.to
 * @Class    BindLogCdBean.java
 * @Create   2018. 5. 08.
 * @Author   허민준
 * @Description 코드바인드
 *
 * @LastUpdated 
 */
@Getter
@Setter
@ToString
@Dataset(name = "dsBindLogCd")
public class BindLogCdBean extends BaseBean {

    private String cdDiv,       // 코드구분
                   code,        // 코드
                   secCode,     // 검색조건 코드1
                   thrCode,     // 검색조건 코드2
                   cdNm,        // 코드명
                   dsLogName;   // 데이터셋명
    
}