package kr.co.samsung.com.sockjs.ws.system.to;

import kr.co.samsung.common.annotation.Dataset;
import kr.co.samsung.common.to.BaseBean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Dataset(name = "dsBindCode")
public class BindCodeBean extends BaseBean {

    private String code;            // 코드그룹코드
    private String dsName;          // 데이터셋명

}