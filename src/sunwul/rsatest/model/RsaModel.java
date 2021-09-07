package sunwul.rsatest.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*****
 * @author sunwul
 * @date 2021/9/1 16:42
 * @description
 */
public class RsaModel {

    // 区域名称
    private final StringProperty dataName;
    // 公钥
    private final StringProperty publicKey;
    // 私钥
    private final StringProperty privateKey;
    // URL编码
    private final StringProperty enableUrlEncode;
    // 明文
    private final StringProperty plainText;
    // 密文
    private final StringProperty cipherText;

    public RsaModel(){
        this("","","","","","");
    }

    public RsaModel(String dataName, String publicKey, String privateKey, String enableUrlEncode, String plainText, String cipherText) {
        this.dataName = new SimpleStringProperty(dataName);
        this.publicKey = new SimpleStringProperty(publicKey);
        this.privateKey = new SimpleStringProperty(privateKey);
        this.enableUrlEncode = new SimpleStringProperty(enableUrlEncode);
        this.plainText = new SimpleStringProperty(plainText);
        this.cipherText = new SimpleStringProperty(cipherText);
    }


    public String getDataName() {
        return dataName.get();
    }

    public StringProperty dataNameProperty() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName.set(dataName);
    }

    public String getPublicKey() {
        return publicKey.get();
    }

    public StringProperty publicKeyProperty() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey.set(publicKey);
    }

    public String getPrivateKey() {
        return privateKey.get();
    }

    public StringProperty privateKeyProperty() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey.set(privateKey);
    }

    public String getEnableUrlEncode() {
        return enableUrlEncode.get();
    }

    public StringProperty enableUrlEncodeProperty() {
        return enableUrlEncode;
    }

    public void setEnableUrlEncode(String enableUrlEncode) {
        this.enableUrlEncode.set(enableUrlEncode);
    }

    public String getPlainText() {
        return plainText.get();
    }

    public StringProperty plainTextProperty() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText.set(plainText);
    }

    public String getCipherText() {
        return cipherText.get();
    }

    public StringProperty cipherTextProperty() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText.set(cipherText);
    }
}
