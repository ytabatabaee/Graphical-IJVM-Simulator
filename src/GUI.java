import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by y.tabatabaee on 7/7/2018.
 */
public class GUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        CPU cpu = new CPU();
        GridPane root = new GridPane();
        GridPane regRoot = new GridPane();
        GridPane buttonRoot = new GridPane();
        gridpanePlacement(regRoot);
        gridpanePlacement(buttonRoot);
        gridpanePlacement(root);
        root.add(regRoot, 0, 0);
        root.add(buttonRoot, 0, 4);
        Scene scene = new Scene(root ,1000, 600, Color.DARKGRAY);
        registers(regRoot, cpu);
        code(buttonRoot, cpu, primaryStage);
        //codeArea(root, cpu, primaryStage);
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.setTitle("JVM Emulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void gridpanePlacement(GridPane root){
        root.setVgap(10);
        root.setHgap(10);

    }

    public void registers(GridPane root, CPU cpu){
        Utility utility = new Utility();
        TextField pc = new TextField();
        TextField ar = new TextField();
        TextField dr1 = new TextField();
        TextField dr2 = new TextField();
        TextField sp = new TextField();
        TextField cpp  = new TextField();
        TextField lv = new TextField();
        TextField ir = new TextField();
        TextField wd = new TextField();

        Label regLabel = new Label("Registers");
        Label pcLabel = new Label("PC");
        Label arLabel = new Label("AR");
        Label irLabel = new Label("IR");
        Label spLabel = new Label("SP");
        Label lvLabel = new Label("LV");
        Label cppLabel = new Label("CPP");
        Label dr1Label = new Label("DR1");
        Label dr2Label = new Label("DR2");
        Label wdLabel = new Label("WD");

        pc.setEditable(false);
        ar.setEditable(false);
        dr1.setEditable(false);
        dr2.setEditable(false);
        sp.setEditable(false);
        cpp.setEditable(false);
        lv.setEditable(false);
        ir.setEditable(false);
        wd.setEditable(false);

        pc.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getPC().getData_out())));
        ar.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getAR().getData_out())));
        sp.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getSP().getData_out())));
        lv.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getLV().getData_out())));
        dr1.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getDR1().getData_out())));
        dr2.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getDR2().getData_out())));
        cpp.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getCPP().getData_out())));
        ir.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getIR().getData_out())));
        wd.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getWD().getData_out())));

        root.add(pc, 2, 1);
        root.add(ir, 2, 2);
        root.add(ar, 2, 3);
        root.add(sp, 2, 4);
        root.add(lv, 2, 5);
        root.add(cpp, 2, 6);
        root.add(wd, 2, 7);
        root.add(dr1, 2, 8);
        root.add(dr2, 2, 9);

        root.add(regLabel, 1, 0);
        root.add(pcLabel, 1, 1);
        root.add(irLabel, 1, 2);
        root.add(arLabel, 1, 3);
        root.add(spLabel, 1, 4);
        root.add(lvLabel, 1, 5);
        root.add(cppLabel, 1, 6);
        root.add(wdLabel, 1, 7);
        root.add(dr1Label, 1, 8);
        root.add(dr2Label, 1, 9);
    }

    public void code(GridPane root, CPU cpu, Stage stage) {
        Button chooseFile = new Button("Input Code");
        Button run = new Button("   Run   ");
        Button stop = new Button("   Stop   ");
        Button step = new Button("   Step   ");
        Button reset = new Button("   Reset   ");
        chooseFile.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/tests"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(stage);
        });
        root.add(chooseFile, 1, 15);
        root.add(run, 2, 15);
        root.add(stop, 3, 15);
        root.add(step, 4, 15);
        root.add(reset, 5, 15);
        String buttonStyle = "";
        try {
            buttonStyle = new String(Files.readAllBytes(Paths.get("src/styleSheets/button.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseFile.setStyle(buttonStyle);
        run.setStyle(buttonStyle);
        stop.setStyle(buttonStyle);
        step.setStyle(buttonStyle);
        reset.setStyle(buttonStyle);
    }

    public static void codeArea(GridPane root, CPU cpu, Stage stage){
        TableView tableView = new TableView();
        tableView.setEditable(false);
        TableColumn pc = new TableColumn("PC");
        TableColumn code = new TableColumn("Instructions");
        tableView.getColumns().addAll(pc, code);
        root.add(tableView, 3, 0);

    }


    public static void main(String args[]){
        launch(args);
    }
}
