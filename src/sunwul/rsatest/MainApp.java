package sunwul.rsatest;

/**
 * @author sunwul
 * @date 2021/9/1 16:25
 * @description
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sunwul.rsatest.model.RsaModel;
import sunwul.rsatest.model.RsaModelListWrapper;
import sunwul.rsatest.view.OverviewController;
import sunwul.rsatest.view.RootLayoutController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    // 显示的列表
    private ObservableList<RsaModel> rsaModelData = FXCollections.observableArrayList();

    public ObservableList<RsaModel> getRsaModelData() {
        return rsaModelData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RSA加解密工具");

        initRootLayout();

        showOverview();
    }


    /**
     * 初始化根布局
     */
    public void initRootLayout() {
        try {
            // 从fxml文件加载根布局
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // 显示包含根布局的场景
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // 让控制器访问主应用程序
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 尝试加载上次打开的文件
        File file = getFilePath();
        if (file != null) {
            loadFile(file);
        }
    }

    /**
     * 显示根布局内的其它内容
     */
    public void showOverview() {
        try {
            // 装载人员概述
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Overview.fxml"));
            AnchorPane overview = loader.load();

            // 将人员概览设置到根布局的中心
            rootLayout.setCenter(overview);

            // 让控制器访问主应用程序
            OverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将当前数据保存到指定文件
     */
    public void saveDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(RsaModelListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // 包装数据
            RsaModelListWrapper wrapper = new RsaModelListWrapper();
            wrapper.setRsaModels(rsaModelData);

            // 编组XML并将其保存到文件中
            m.marshal(wrapper, file);

            // 将文件路径保存到注册表
            setFilePath(file);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("所有数据保存成功!\r\n\r\n数据保存位置: " + file.getPath());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText("保存文件异常!");
            alert.setContentText("无法将数据保存到文件:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    // 加载文件
    public void loadFile(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(RsaModelListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            RsaModelListWrapper wrapper = (RsaModelListWrapper)um.unmarshal(file);

            rsaModelData.clear();
            rsaModelData.addAll(wrapper.getRsaModels());

            // 注册表操作,保存文件路径
            setFilePath(file);
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText("文件加载出现异常!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    /**
     * 设置当前加载文件的文件路径。该路径将保留在操作系统特定的注册表中。
     * @param file file
     */
    public void setFilePath(File file) {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            preferences.put("filePath", file.getPath());

            // 更新标题
            primaryStage.setTitle("RSA加解密工具 - " + file.getName());
        } else {
            preferences.remove("filePath");

            // 更新标题
            primaryStage.setTitle("AddressApp");
        }
    }

    /**
     * 返回文件首选项，即上次打开的文件。
     * 首选项从特定的操作系统的注册表中读取
     * 如果无法找到首选项，则返回null
     * @return file
     */
    public File getFilePath() {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        String filePath = preferences.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
}
