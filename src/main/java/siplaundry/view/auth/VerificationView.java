package siplaundry.view.auth;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import siplaundry.controller.auth.CodeVerifyController;
import siplaundry.controller.auth.UsernameVerifyController;
import siplaundry.entity.UserEntity;
import siplaundry.entity.VerificationEntity;
import siplaundry.entity.WhatsappMessage;
import siplaundry.repository.VerificationRepo;
import siplaundry.service.WhatsAppService;
import siplaundry.util.MessageUtil;
import siplaundry.util.NumberUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class VerificationView {
    private Stage stage;
    private VerificationRepo verifyRepo = new VerificationRepo();
    public VerificationView(Node shadowRoot, Stage parentStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/verify-username.fxml"));
        loader.setController(new UsernameVerifyController(this::verifyCode));

        try {
            Parent root = loader.load();
            this.stage = new Stage();
            Scene scene = new Scene(root);

            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initOwner(parentStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);

            stage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
            });

            shadowRoot.setVisible(true);
            stage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }

    public void verifyCode(UserEntity user) {
        WhatsAppService message = new WhatsAppService();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/verify-code.fxml"));
        String code = NumberUtil.generateCode();

        loader.setController(new CodeVerifyController(user));
        message.sendVerification(new WhatsappMessage(
                user.getPhone(),
                MessageUtil.verifyMessGen(code)
        ));

        sendVerifyCode(user, code);

        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void sendVerifyCode(UserEntity user, String code) {
        List<VerificationEntity> codes = verifyRepo.get(new HashMap<>() {{
            put("user_id", user.getID());
        }});

        if(codes.size() > 0) {
            verifyRepo.delete(codes.get(0));
        }

        verifyRepo.add(new VerificationEntity(user, code));
    }
}
