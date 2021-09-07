package sunwul.rsatest.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sunwul.rsatest.MainApp;
import sunwul.rsatest.model.RsaModel;
import sunwul.rsatest.tool.RsaUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

/*****
 * @author sunwul
 * @date 2021/9/1 17:43
 * @description
 */
public class OverviewController {

    @FXML
    private TableView<RsaModel> rsaModelTable;
    @FXML
    private TableColumn<RsaModel, String> qymcColumn;

    @FXML
    private TextArea publicKeyArea;
    @FXML
    private TextArea privateKeyArea;
    @FXML
    private TextField enableUrlEncodeField;
    @FXML
    private TextArea plainTextArea;
    @FXML
    private TextArea cipherTextArea;


    private MainApp mainApp;

    public OverviewController() {
    }

    /**
     * 由主应用程序调用以返回对自身的引用
     *
     * @param mainApp main
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // 将显示的列表数据添加到表中
        rsaModelTable.setItems(mainApp.getRsaModelData());
    }

    /**
     * 加载fxml文件后, 初始化控制器类, 此方法将自动调用
     */
    @FXML
    private void initialize() {
        qymcColumn.setCellValueFactory(cellData -> cellData.getValue().dataNameProperty());

        // 清除元素
        showAllDetails(null);
        rsaModelTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showAllDetails(newValue));
    }

    /**
     * 显示详细信息
     *
     * @param rsaModel 待显示数据封装
     */
    private void showAllDetails(RsaModel rsaModel) {
        if (rsaModel != null) {
            publicKeyArea.setText(rsaModel.getPublicKey());
            privateKeyArea.setText(rsaModel.getPrivateKey());
            enableUrlEncodeField.setText(rsaModel.getEnableUrlEncode());
            plainTextArea.setText(rsaModel.getPlainText());
            cipherTextArea.setText(rsaModel.getCipherText());
        } else {
            publicKeyArea.setText("");
            privateKeyArea.setText("");
            enableUrlEncodeField.setText("");
            plainTextArea.setText("");
            cipherTextArea.setText("");
        }
    }

    /**
     * 单击新建按钮
     */
    @FXML
    private void handleNew() {
        if (isInputValid()) {
            boolean isNew = true; // 是否新增
            RsaModel tempModel = new RsaModel();
            // 输入对话框
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("名称");
            textInputDialog.setHeaderText("请输入需要保存的名称!");
            Optional<String> result = textInputDialog.showAndWait();
            String dataName = textInputDialog.getResult();
            // 按下返回按钮 isPresent()将会返回false
            if (!result.isPresent()) {
                return;
            }
            if (dataName == null || dataName.length() == 0) {
                // 显示一个对话框
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("名称不能为空!");
                alert.showAndWait();
                isNew = false;
                handleNew();
            }
            if (isNew) {
                tempModel.setDataName(dataName);
                tempModel.setPublicKey(this.publicKeyArea.getText());
                tempModel.setPrivateKey(this.privateKeyArea.getText());
                tempModel.setEnableUrlEncode(this.enableUrlEncodeField.getText());
                // 非必填项
                tempModel.setPlainText(this.plainTextArea.getText());
                tempModel.setCipherText(this.cipherTextArea.getText());
                showAllDetails(tempModel);
                mainApp.getRsaModelData().add(tempModel);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("新建成功!");
                alert.showAndWait();
            }
        }
    }


    /**
     * 单击保存按钮
     */
    @FXML
    public void handleSave() {
        // 获取选择的人员下标
        int selectedIndex = rsaModelTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // 获取此下标的数据
//            RsaModel temp = rsaModelTable.getItems().get(selectedIndex);
            // 直接更改现有数据
            if (isInputValid()) {
                rsaModelTable.getItems().get(selectedIndex).setPublicKey(this.publicKeyArea.getText());
                rsaModelTable.getItems().get(selectedIndex).setPrivateKey(this.privateKeyArea.getText());
                rsaModelTable.getItems().get(selectedIndex).setEnableUrlEncode(this.enableUrlEncodeField.getText());
                // 非必填项
                rsaModelTable.getItems().get(selectedIndex).setPlainText(this.plainTextArea.getText());
                rsaModelTable.getItems().get(selectedIndex).setCipherText(this.cipherTextArea.getText());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("保存当前数据成功!\r\n\r\n若需要保存至数据库请使用Ctrl+S保存所有");
                alert.showAndWait();
            }
        } else {
            // 未选择执行新建方法
            handleNew();
        }
    }

    /**
     * 单击删除按钮时调用
     */
    @FXML
    private void handleDelete() {
        // 获取选择的人员下标
        int selectedIndex = rsaModelTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // 在数组中删除此下标的数据
            rsaModelTable.getItems().remove(selectedIndex);
        } else {
            // 显示一个对话框
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText("未选择信息");
            alert.setContentText("没有选中有效的数据信息!");
            alert.showAndWait();
        }
    }


    /**
     * 必填信息验证
     *
     * @return 验证结果
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if (publicKeyArea.getText() == null || publicKeyArea.getText().length() == 0) {
            errorMessage += "公钥不能为空!\r\n";
        }
        if (privateKeyArea.getText() == null || privateKeyArea.getText().length() == 0) {
            errorMessage += "私钥不能为空!\r\n";
        }
        if (!enableUrlEncodeField.getText().equals("false") && !enableUrlEncodeField.getText().equals("true")) {
            errorMessage += "url编码只能设置为true或false!\r\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("必填项检查");
            alert.setHeaderText("必填项不能设置为空");
            alert.setContentText("请填写必填项:\r\n" + errorMessage);
            alert.showAndWait();
            return false;
        }
    }


    /* 加解密按钮 *****************************************/

    @FXML
    private void encryptByPublicKey() {
        try {
            // 编码
            String dataEnc = URLEncoding(this.plainTextArea.getText());
            // 公钥加密
            String cipherText = RsaUtil.encryptByPublicKey(this.publicKeyArea.getText(), dataEnc);
            if (cipherText.length() > 0) {
                this.cipherTextArea.setText(cipherText);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("【公钥加密】成功!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            showException(e);
        }

    }

    @FXML
    private void decryptByPrivateKey() {
        try {
            // 私钥解密
            String plainText = RsaUtil.decryptByPrivateKey(this.privateKeyArea.getText(), this.cipherTextArea.getText());
            // 解码
            String dataDEnc = URLDecoding(plainText);
            if (plainText.length() > 0) {
                this.plainTextArea.setText(dataDEnc);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("【私钥解密】成功!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void encryptByPrivateKey() {
        try {
            // 编码
            String dataEnc = URLEncoding(this.plainTextArea.getText());
            // 私钥加密
            String cipherText = RsaUtil.encryptByPrivateKey(this.privateKeyArea.getText(), dataEnc);
            if (cipherText.length() > 0) {
                this.cipherTextArea.setText(cipherText);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("【私钥加密】成功!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void decryptByPublicKey() {
        try {
            // 公钥解密
            String plainText = RsaUtil.decryptByPublicKey(this.publicKeyArea.getText(), this.cipherTextArea.getText());
            // 解码
            String dataDEnc = URLDecoding(plainText);
            if (plainText.length() > 0) {
                this.plainTextArea.setText(dataDEnc);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("【公钥解密】成功!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            showException(e);
        }
    }

    // URL编码设置
    @FXML
    private String URLEncoding(String dataStr) {
        boolean isURL = Boolean.parseBoolean(this.enableUrlEncodeField.getText().trim());
        if (isURL) {
            try {
                return URLEncoder.encode(dataStr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("URL编码转换异常!\r\n\r\n该字符串不支持编码!\r\n\r\n将以原始数据进行加解密操作");
                alert.showAndWait();
                return dataStr;
            }
        } else {
            return dataStr;
        }
    }

    // URL解码设置
    @FXML
    private String URLDecoding(String dataStr) {
        boolean isURL = Boolean.parseBoolean(this.enableUrlEncodeField.getText().trim());
        if (isURL) {
            try {
                return URLDecoder.decode(dataStr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("URL解码转换异常!\r\n\r\n将以原始数据进行加解密操作");
                alert.showAndWait();
                return dataStr;
            }
        } else {
            return dataStr;
        }
    }

    private void showException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("异常");
        alert.setHeaderText("加解密发生异常, 检查秘钥对是否正确!");
        TextArea textArea = new TextArea();
        textArea.setText(e.getLocalizedMessage());
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    /* 秘钥对生成按钮*********************/
    @FXML
    private void createKey() {
        Map<String, Object> keyMap = RsaUtil.generateKey();
        String publicKey = RsaUtil.getPublicKey(keyMap);
        String privateKey = RsaUtil.getPrivateKey(keyMap);
        this.publicKeyArea.setText(publicKey);
        this.privateKeyArea.setText(privateKey);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("秘钥对生成成功!注意保存数据.");
        alert.showAndWait();
    }

}
