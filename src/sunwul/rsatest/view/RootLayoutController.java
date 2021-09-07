package sunwul.rsatest.view;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sunwul.rsatest.MainApp;

import java.io.File;

/*****
 * @author sunwul
 * @date 2021/9/1 16:28
 * @description
 */
public class RootLayoutController {

    // 引用主程序
    private MainApp mainApp;

    // 由主应用程序调用以返回对自身的引用
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    /**
     * 打开xml
     * 打开文件选择器
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // 设置扩展筛选器
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // 显示保存文件对话框
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadFile(file);
        }
    }


    /**
     * 将文件保存到当前打开的文件中。如果没有打开文件，将显示“另存为”对话框
     */
    @FXML
    private void handleSave() {
        File personFile = mainApp.getFilePath();
        if (personFile != null) {
            mainApp.saveDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }


    /**
     * 另存为..
     * 打开文件选择器，让用户选择要保存到的文件
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // 设置扩展筛选器
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // 显示保存文件对话框(显示在主Stage的上面)
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // 确保文件格式正确
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveDataToFile(file);
        }
    }


    /**
     * 关于
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("关于");

        Button origin = new Button("查看加解密源码!");
        // 事件
        origin.setOnAction(event -> handleOrigin());

        alert.getDialogPane().setHeaderText("RSA加解密工具");
        Label auth = new Label("作者: sunwul\r\n\r\nJavaFX程序,RSA加解密工具,支持公私钥加解密、URL编码");

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(auth,origin);
        alert.getDialogPane().setContent(vBox);
        alert.showAndWait();
    }

    /**
     * 加解密源码
     */
    private void handleOrigin(){
        Pane pane = new Pane();
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(500);
        textArea.setText("import javax.crypto.Cipher;\n" +
                "import java.io.ByteArrayOutputStream;\n" +
                "import java.net.URLEncoder;\n" +
                "import java.security.*;\n" +
                "import java.security.interfaces.RSAPrivateKey;\n" +
                "import java.security.interfaces.RSAPublicKey;\n" +
                "import java.security.spec.PKCS8EncodedKeySpec;\n" +
                "import java.security.spec.X509EncodedKeySpec;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "\n" +
                "public class RsaUtil {\n" +
                "    /**\n" +
                "     * 定义加密方式\n" +
                "     */\n" +
                "    private final static String KEY_RSA = \"RSA\";\n" +
                "    /**\n" +
                "     * 定义签名算法\n" +
                "     */\n" +
                "    private final static String KEY_RSA_SIGNATURE = \"MD5withRSA\";\n" +
                "    /**\n" +
                "     * 定义公钥算法\n" +
                "     */\n" +
                "    private final static String KEY_RSA_PUBLICKEY = \"RSAPublicKey\";\n" +
                "    /**\n" +
                "     * 定义私钥算法\n" +
                "     */\n" +
                "    private final static String KEY_RSA_PRIVATEKEY = \"RSAPrivateKey\";\n" +
                "\n" +
                "    /**\n" +
                "     * RSA最大加密明文大小\n" +
                "     */\n" +
                "    private static final int MAX_ENCRYPT_BLOCK = 117;\n" +
                "\n" +
                "    /**\n" +
                "     * RSA最大解密密文大小\n" +
                "     */\n" +
                "    private static final int MAX_DECRYPT_BLOCK = 128;\n" +
                "\n" +
                "\n" +
                "    private RsaUtil() {\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 创建密钥\n" +
                "     *\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static Map<String, Object> generateKey() {\n" +
                "        Map<String, Object> map = null;\n" +
                "        try {\n" +
                "            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);\n" +
                "            generator.initialize(1024);\n" +
                "            KeyPair keyPair = generator.generateKeyPair();\n" +
                "            // 公钥\n" +
                "            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();\n" +
                "            // 私钥\n" +
                "            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();\n" +
                "            // 将密钥封装为map\n" +
                "            map = new HashMap<>(2);\n" +
                "            map.put(KEY_RSA_PUBLICKEY, publicKey);\n" +
                "            map.put(KEY_RSA_PRIVATEKEY, privateKey);\n" +
                "        } catch (NoSuchAlgorithmException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "        return map;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 用私钥对信息生成数字签名\n" +
                "     *\n" +
                "     * @param data       加密数据\n" +
                "     * @param privateKey 私钥\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String sign(String privateKey, byte[] data) {\n" +
                "        String str = \"\";\n" +
                "        try {\n" +
                "            // 解密由base64编码的私钥\n" +
                "            byte[] bytes = decryptBase64(privateKey);\n" +
                "            // 构造PKCS8EncodedKeySpec对象\n" +
                "            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);\n" +
                "            // 指定的加密算法\n" +
                "            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);\n" +
                "            // 取私钥对象\n" +
                "            PrivateKey key = factory.generatePrivate(pkcs);\n" +
                "            // 用私钥对信息生成数字签名\n" +
                "            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);\n" +
                "            signature.initSign(key);\n" +
                "            signature.update(data);\n" +
                "            str = encryptBase64(signature.sign());\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "        return str;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * 用私钥对信息生成数字签名\n" +
                "     *\n" +
                "     * @param privateKey\n" +
                "     * @param dataStr\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String sign(String privateKey, String dataStr) {\n" +
                "        String str = \"\";\n" +
                "        try {\n" +
                "            byte[] data = dataStr.getBytes(\"UTF-8\");\n" +
                "            return sign(privateKey, data);\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * 校验数字签名\n" +
                "     *\n" +
                "     * @param data      加密数据\n" +
                "     * @param publicKey 公钥\n" +
                "     * @param sign      数字签名\n" +
                "     * @return 校验成功返回true，失败返回false\n" +
                "     */\n" +
                "    public static boolean verify(String publicKey, byte[] data, String sign) {\n" +
                "        boolean flag = false;\n" +
                "        try {\n" +
                "            // 解密由base64编码的公钥\n" +
                "            byte[] bytes = decryptBase64(publicKey);\n" +
                "            // 构造X509EncodedKeySpec对象\n" +
                "            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);\n" +
                "            // 指定的加密算法\n" +
                "            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);\n" +
                "            // 取公钥对象\n" +
                "            PublicKey key = factory.generatePublic(keySpec);\n" +
                "            // 用公钥验证数字签名\n" +
                "            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);\n" +
                "            signature.initVerify(key);\n" +
                "            signature.update(data);\n" +
                "            flag = signature.verify(decryptBase64(sign));\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "        return flag;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public static boolean verify(String publicKey, String dataStr, String sign) {\n" +
                "\n" +
                "        try {\n" +
                "            byte[] data = dataStr.getBytes(\"UTF-8\");\n" +
                "            return verify(publicKey, data, sign);\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 公钥加密\n" +
                "     *\n" +
                "     * @param key  公钥\n" +
                "     * @param data 待加密数据\n" +
                "     * @return byte[]\n" +
                "     */\n" +
                "    public static byte[] encryptByPublicKey(String key, byte[] data) {\n" +
                "        byte[] result = null;\n" +
                "        try {\n" +
                "            // 获取公钥字符串时,进行了encryptBase64操作,因此此处需对公钥钥解密\n" +
                "            byte[] bytes = decryptBase64(key);\n" +
                "            // 取得公钥\n" +
                "            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);\n" +
                "            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);\n" +
                "            PublicKey publicKey = factory.generatePublic(keySpec);\n" +
                "            // 对数据加密\n" +
                "            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());\n" +
                "            cipher.init(Cipher.ENCRYPT_MODE, publicKey);\n" +
                "            return blockEncrypt(cipher, data);\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 公钥加密\n" +
                "     *\n" +
                "     * @param key key\n" +
                "     * @param dataStr str\n" +
                "     * @return str\n" +
                "     */\n" +
                "    public static String encryptByPublicKey(String key, String dataStr) {\n" +
                "        try {\n" +
                "            byte[] result = encryptByPublicKey(key, dataStr.getBytes(\"UTF-8\"));\n" +
                "            return encryptBase64(result);\n" +
                "\n" +
                "        } catch (Exception ex) {\n" +
                "            throw new RuntimeException(ex);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * 私钥解密\n" +
                "     *\n" +
                "     * @param data 加密数据\n" +
                "     * @param key  私钥\n" +
                "     * @return byte[]\n" +
                "     */\n" +
                "    public static byte[] decryptByPrivateKey(String key, byte[] data) {\n" +
                "        byte[] result = null;\n" +
                "        try {\n" +
                "            // 获取私钥字符串时,进行了encryptBase64操作,因此此处需对私钥解密\n" +
                "            byte[] bytes = decryptBase64(key);\n" +
                "            // 取得私钥\n" +
                "            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);\n" +
                "            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);\n" +
                "            PrivateKey privateKey = factory.generatePrivate(keySpec);\n" +
                "            // 对数据解密\n" +
                "            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());\n" +
                "            cipher.init(Cipher.DECRYPT_MODE, privateKey);\n" +
                "            return blockDecrypt(cipher, data);\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    public static String decryptByPrivateKey(String key, String dataStr) {\n" +
                "        try {\n" +
                "            byte[] result = decryptByPrivateKey(key, decryptBase64(dataStr));\n" +
                "            return new String(result);\n" +
                "        } catch (Exception ex) {\n" +
                "            throw new RuntimeException(ex);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 私钥加密\n" +
                "     *\n" +
                "     * @param data 待加密数据\n" +
                "     * @param key  私钥\n" +
                "     * @return byte[]\n" +
                "     */\n" +
                "    public static byte[] encryptByPrivateKey(String key, byte[] data) {\n" +
                "        byte[] result = null;\n" +
                "        try {\n" +
                "            byte[] bytes = decryptBase64(key);\n" +
                "            // 取得私钥\n" +
                "            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);\n" +
                "            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);\n" +
                "            PrivateKey privateKey = factory.generatePrivate(keySpec);\n" +
                "            // 对数据加密\n" +
                "            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());\n" +
                "            cipher.init(Cipher.ENCRYPT_MODE, privateKey);\n" +
                "            return blockEncrypt(cipher, data);\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 对数据分块加密，防止太长超出加密长度\n" +
                "     *\n" +
                "     * @param cipher cipher\n" +
                "     * @param data data\n" +
                "     * @return byte[]\n" +
                "     */\n" +
                "    private static byte[] blockEncrypt(Cipher cipher, byte[] data) {\n" +
                "        int inputLen = data.length;\n" +
                "        int offSet = 0;\n" +
                "        byte[] cache;\n" +
                "        int i = 0;\n" +
                "        ByteArrayOutputStream out = new ByteArrayOutputStream();\n" +
                "        try {\n" +
                "            // 对数据分段加密\n" +
                "            while (inputLen - offSet > 0) {\n" +
                "                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {\n" +
                "                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);\n" +
                "                } else {\n" +
                "                    cache = cipher.doFinal(data, offSet, inputLen - offSet);\n" +
                "                }\n" +
                "                out.write(cache, 0, cache.length);\n" +
                "                i++;\n" +
                "                offSet = i * MAX_ENCRYPT_BLOCK;\n" +
                "            }\n" +
                "            byte[] encryptedData = out.toByteArray();\n" +
                "            return encryptedData;\n" +
                "        } catch (Exception ex) {\n" +
                "            throw new RuntimeException(ex);\n" +
                "        } finally {\n" +
                "            if (out != null) {\n" +
                "                try {\n" +
                "                    out.close();\n" +
                "                } catch (Exception ex) {\n" +
                "                    throw new RuntimeException(ex);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 私钥加密\n" +
                "     *\n" +
                "     * @param key\n" +
                "     * @param dataStr\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String encryptByPrivateKey(String key, String dataStr) {\n" +
                "        try {\n" +
                "            byte[] result = encryptByPrivateKey(key, dataStr.getBytes(\"UTF-8\"));\n" +
                "            return encryptBase64(result);\n" +
                "        } catch (Exception ex) {\n" +
                "            throw new RuntimeException(ex);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 公钥钥解密\n" +
                "     *\n" +
                "     * @param key  公钥\n" +
                "     * @param data 加密数据\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static byte[] decryptByPublicKey(String key, byte[] data) {\n" +
                "        byte[] result = null;\n" +
                "        try {\n" +
                "            // 对公钥解密\n" +
                "            byte[] bytes = decryptBase64(key);\n" +
                "            // 取得公钥\n" +
                "            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);\n" +
                "            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);\n" +
                "            PublicKey publicKey = factory.generatePublic(keySpec);\n" +
                "            // 对数据解密\n" +
                "            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());\n" +
                "            cipher.init(Cipher.DECRYPT_MODE, publicKey);\n" +
                "            return blockDecrypt(cipher, data);\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 分块解密\n" +
                "     *\n" +
                "     * @param cipher\n" +
                "     * @param data\n" +
                "     * @return\n" +
                "     */\n" +
                "    private static byte[] blockDecrypt(Cipher cipher, byte[] data) {\n" +
                "        int inputLen = data.length;\n" +
                "        int offSet = 0;\n" +
                "        byte[] cache;\n" +
                "        int i = 0;\n" +
                "\n" +
                "        ByteArrayOutputStream out = new ByteArrayOutputStream();\n" +
                "        try {\n" +
                "            // 对数据分段解密\n" +
                "            while (inputLen - offSet > 0) {\n" +
                "                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {\n" +
                "                    cache = cipher\n" +
                "                            .doFinal(data, offSet, MAX_DECRYPT_BLOCK);\n" +
                "                } else {\n" +
                "                    cache = cipher\n" +
                "                            .doFinal(data, offSet, inputLen - offSet);\n" +
                "                }\n" +
                "                out.write(cache, 0, cache.length);\n" +
                "                i++;\n" +
                "                offSet = i * MAX_DECRYPT_BLOCK;\n" +
                "            }\n" +
                "            byte[] decryptedData = out.toByteArray();\n" +
                "            return decryptedData;\n" +
                "        } catch (Exception ex) {\n" +
                "            throw new RuntimeException(ex);\n" +
                "        } finally {\n" +
                "            if (out != null) {\n" +
                "                try {\n" +
                "                    out.close();\n" +
                "                } catch (Exception ex) {\n" +
                "                    throw new RuntimeException(ex);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public static String decryptByPublicKey(String key, String dataStr) {\n" +
                "        try {\n" +
                "            byte[] result = decryptByPublicKey(key, decryptBase64(dataStr));\n" +
                "            return new String(result);\n" +
                "        } catch (Exception ex) {\n" +
                "            throw new RuntimeException(ex);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 获取公钥\n" +
                "     *\n" +
                "     * @param map\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String getPublicKey(Map<String, Object> map) {\n" +
                "        String str = \"\";\n" +
                "        try {\n" +
                "            Key key = (Key) map.get(KEY_RSA_PUBLICKEY);\n" +
                "            str = encryptBase64(key.getEncoded());\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "        return str;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 获取私钥\n" +
                "     *\n" +
                "     * @param map\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String getPrivateKey(Map<String, Object> map) {\n" +
                "        String str = \"\";\n" +
                "        try {\n" +
                "            Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);\n" +
                "            str = encryptBase64(key.getEncoded());\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "        return str;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * BASE64 解密\n" +
                "     *\n" +
                "     * @param key 需要解密的字符串\n" +
                "     * @return 字节数组\n" +
                "     * @throws Exception\n" +
                "     */\n" +
                "    public static byte[] decryptBase64(String key) {\n" +
                "        return javax.xml.bind.DatatypeConverter.parseBase64Binary(key);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * BASE64 加密\n" +
                "     *\n" +
                "     * @param key 需要加密的字节数组\n" +
                "     * @return 字符串\n" +
                "     * @throws Exception\n" +
                "     */\n" +
                "    public static String encryptBase64(byte[] key) {\n" +
                "        return javax.xml.bind.DatatypeConverter.printBase64Binary(key);\n" +
                "    }\n" +
                "\n" +
                "}");
        pane.getChildren().add(textArea);
        Scene scene = new Scene(pane,490,490);
        Stage stage = new Stage();
        stage.setTitle("加解密工具类");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // 关闭应用程序
    @FXML
    private void handleExit() {
        System.exit(0);
    }

}
