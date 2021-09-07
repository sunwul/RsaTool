package sunwul.rsatest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/*****
 * @author sunwul
 * @date 2021/9/1 17:05
 * @description  JAXB模型
 */
@XmlRootElement(name = "rsaModels")
public class RsaModelListWrapper {

    private List<RsaModel> rsaModels;

    @XmlElement(name = "rsaModel")
    public List<RsaModel> getRsaModels(){
        return rsaModels;
    }

    public void setRsaModels(List<RsaModel> rsaModels) {
        this.rsaModels = rsaModels;
    }
}
