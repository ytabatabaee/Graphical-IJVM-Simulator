import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by y.tabatabaee on 7/7/2018.
 */
public class GUI extends Application {
    Utility utility = new Utility();
    Button chooseFile = new Button("Input Code");
    Button run = new Button("   Run   ");
    Button stop = new Button("   Stop   ");
    Button step = new Button("   Step   ");
    Button reset = new Button("   Reset   ");
    String buttonStyle = "";
    String labelStyle = "";
    String redStyle = "";
    TextArea codeArea = new TextArea();
    TextArea stackArea = new TextArea();
    TextArea constantArea = new TextArea();
    TextArea varArea = new TextArea();
    TextField ar = new TextField();
    TextField dr = new TextField();
    TextField ir = new TextField();
    TextField pc = new TextField();
    TextField sp = new TextField();
    TextField lv = new TextField();
    TextField cpp = new TextField();
    TextField tos = new TextField();
    TextField h = new TextField();
    Label z = new Label("Z");
    Label n = new Label("N");
    Label sp_sub4 = new Label("SP- 4");
    Label sp_add4 = new Label("SP+4");
    Label pc_inc = new Label("PC+1");
    Label pc_inc2 = new Label("PC+2");
    Label ar_LD = new Label("AR_LD");
    Label dr_LD = new Label("DR_LD");
    Label ir_LD = new Label("IR_LD");
    Label pc_LD = new Label("PC_LD");
    Label sp_LD = new Label("SP_LD");
    Label lv_LD = new Label("LV_LD");
    Label cpp_LD = new Label("CPP_LD");
    Label tos_LD = new Label("TOS_LD");
    Label ready = new Label("Ready");
    Label read = new Label("Read");
    Label write = new Label("Write");
    Label fetch = new Label("Fetch");
    TextField shifter = new TextField();
    CPU cpu = new CPU();

    @Override
    public void start(Stage primaryStage) throws Exception {
        setStyleSheets();
        Group root = new Group();
        GridPane regRoot = new GridPane();
        GridPane buttonRoot = new GridPane();
        gridpanePlacement(regRoot, 310, 10);
        gridpanePlacement(buttonRoot, 20, 430);
        Rectangle bBus = new Rectangle(640, 30, 30, 400);
        Rectangle cBus = new Rectangle(360, 30, 28, 500);
        Rectangle aBus = new Rectangle(500, 345, 28, 90);
        Rectangle alu = new Rectangle(480, 430, 200, 70);
        Rectangle cu = new Rectangle(750, 40, 220, 170);
        Rectangle mem = new Rectangle(750, 240, 220, 100);
        bBus.setFill(new ImagePattern(new Image("images/arrow1.png")));
        cBus.setFill(new ImagePattern(new Image("images/cbus.png")));
        aBus.setFill(new ImagePattern(new Image("images/abus.png")));
        alu.setFill(new ImagePattern(new Image("images/alu.png")));
        cu.setFill(new ImagePattern(new Image("images/rect.png")));
        mem.setFill(new ImagePattern(new Image("images/mem.png")));
        z.relocate(540, 470);
        n.relocate(620, 470);
        sp_sub4.relocate(760, 80);
        sp_add4.relocate(760, 100);
        pc_inc.relocate(760, 120);
        pc_inc2.relocate(760, 140);
        read.relocate(760, 255);
        fetch.relocate(760, 275);
        write.relocate(760, 295);
        ready.relocate(930, 275);
        Arrow arrow = new Arrow(580, 500, 580, 520);
        Label aluLabel = new Label("ALU");
        aluLabel.relocate(450, 450);
        aluLabel.setStyle(labelStyle);
        shifter.setEditable(false);
        shifter.relocate(500, 520);
        root.getChildren().addAll(buttonRoot, regRoot, bBus, alu,
                shifter, cBus, aBus, aluLabel, arrow, cu, z, n, mem,
                sp_add4, sp_sub4, write, read, fetch, ready, pc_inc, pc_inc2);
        Scene scene = new Scene(root, 1100, 650, Color.DARKGRAY);
        registers(regRoot, cpu);
        code(buttonRoot, cpu, primaryStage);
        codeArea(root, cpu, primaryStage);
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.setTitle("JVM Emulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setStyleSheets() {
        try {
            labelStyle = new String(Files.readAllBytes(Paths.get("src/styleSheets/label.txt")));
            buttonStyle = new String(Files.readAllBytes(Paths.get("src/styleSheets/button.txt")));
            redStyle = new String(Files.readAllBytes(Paths.get("src/styleSheets/red.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gridpanePlacement(GridPane root, int x, int y) {
        root.setVgap(10);
        root.setHgap(10);
        root.relocate(x, y);

    }

    public void registers(GridPane root, CPU cpu) {
        Label regLabel = new Label("Registers");
        Label arLabel = new Label("AR");
        Label drLabel = new Label("DR");
        Label irLabel = new Label("IR");
        Label pcLabel = new Label("PC");
        Label spLabel = new Label("SP");
        Label lvLabel = new Label("LV");
        Label cppLabel = new Label("CPP");
        Label tosLabel = new Label("TOS");
        Label hLabel = new Label("H");


        regLabel.setStyle(labelStyle);
        pcLabel.setStyle(labelStyle);
        arLabel.setStyle(labelStyle);
        irLabel.setStyle(labelStyle);
        spLabel.setStyle(labelStyle);
        lvLabel.setStyle(labelStyle);
        cppLabel.setStyle(labelStyle);
        drLabel.setStyle(labelStyle);
        tosLabel.setStyle(labelStyle);
        hLabel.setStyle(labelStyle);
        z.setStyle(labelStyle);
        n.setStyle(labelStyle);

        pc.setEditable(false);
        ar.setEditable(false);
        dr.setEditable(false);
        tos.setEditable(false);
        sp.setEditable(false);
        cpp.setEditable(false);
        lv.setEditable(false);
        ir.setEditable(false);
        h.setEditable(false);

        pc.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getPC().getData_out())));
        ar.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getAR().getData_out())));
        sp.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getSP().getData_out())));
        lv.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getLV().getData_out())));
        dr.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getDR().getData_out())));
        h.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getH().getData_out())));
        cpp.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getCPP().getData_out())));
        ir.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getIR().getData_out())));
        tos.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getTOS().getData_out())));

        root.add(ar, 3, 1);
        root.add(dr, 3, 2);
        root.add(ir, 3, 3);
        root.add(pc, 3, 4);
        root.add(sp, 3, 5);
        root.add(lv, 3, 6);
        root.add(cpp, 3, 7);
        root.add(tos, 3, 8);
        root.add(h, 3, 9);


        Arrow[] arrow = new Arrow[9];
        for (int i = 0; i < 9; i++) {
            arrow[i] = new Arrow(10, 10, 50, 10);
            root.add(arrow[i], 4, i + 1);
        }

        Arrow[] load = new Arrow[9];
        for (int i = 0; i < 9; i++) {
            load[i] = new Arrow(10, 10, 50, 10);
            root.add(load[i], 2, i + 1);
        }

        root.add(regLabel, 1, 0);
        root.add(arLabel, 1, 1);
        root.add(drLabel, 1, 2);
        root.add(irLabel, 1, 3);
        root.add(pcLabel, 1, 4);
        root.add(spLabel, 1, 5);
        root.add(lvLabel, 1, 6);
        root.add(cppLabel, 1, 7);
        root.add(tosLabel, 1, 8);
        root.add(hLabel, 1, 9);
    }

    public void code(GridPane root, CPU cpu, Stage stage) {
        chooseFile.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/tests"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(stage);
            ArrayList<String> codeLines = new ArrayList<>();
            try {
                Scanner input = new Scanner(file);
                StringBuilder binaryCode = new StringBuilder();
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    codeLines.add(line);
                    binaryCode.append(utility.codeToBinary(line));
                    codeArea.appendText((codeLines.size()) + ".     " + line + "   " + "\n");
                }
                System.out.println(binaryCode);
                cpu.getMemory().setCell(binaryCode.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        reset.setOnAction((ActionEvent event) -> {;
            cpu.runStep(true);
        });
        step.setOnAction((ActionEvent event) -> {
            System.out.println(cpu.getSC());
            cpu.runStep(false);
            System.out.println(cpu.getSC());
            stackArea.setText("");
            for (int i = 68; i <= utility.binaryToInt(cpu.getDataPath().getSP().getData_out()); i += 4)
                stackArea.appendText((i) + ". " + utility.binaryToInt(cpu.getMemory().getCell(i)) + "\n");
            varArea.setText("");
            for (int i = 128; i <= 192 /*utility.binaryToInt(cpu.getDataPath().getLV().getData_out())*/ ; i += 4) {
                varArea.appendText((i) + ". " + utility.binaryToInt(cpu.getMemory().getCell(i)) + "\n");
            }
            pc.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getPC().getData_out())));
            ar.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getAR().getData_out())));
            sp.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getSP().getData_out())));
            lv.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getLV().getData_out())));
            dr.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getDR().getData_out())));
            h.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getH().getData_out())));
            cpp.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getCPP().getData_out())));
            ir.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getIR().getData_out())));
            tos.setText(String.valueOf(utility.binaryToInt(cpu.getDataPath().getTOS().getData_out())));
            if(cpu.getDataPath().isZ())
                z.setStyle(redStyle);
            else
                z.setStyle(labelStyle);
            if(cpu.getControlUnit().read())
                read.setTextFill(Color.RED);
            else
                read.setTextFill(Color.BLACK);
            if(cpu.getControlUnit().write())
                write.setTextFill(Color.RED);
            else
                write.setTextFill(Color.BLACK);
            if(cpu.getControlUnit().fetch())
                fetch.setTextFill(Color.RED);
            else
                fetch.setTextFill(Color.BLACK);
            if(cpu.getMemory().isReady())
                ready.setTextFill(Color.RED);
            else
                ready.setTextFill(Color.BLACK);
            if(cpu.getControlUnit().SP_ADD4())
                sp_add4.setTextFill(Color.RED);
            else
                sp_add4.setTextFill(Color.BLACK);
            if(cpu.getControlUnit().SP_SUB4())
                sp_sub4.setTextFill(Color.RED);
            else
                sp_sub4.setTextFill(Color.BLACK);
            if(cpu.getControlUnit().PC_INC(cpu.getDataPath().isZ(), cpu.getDataPath().isN()))
                pc_inc.setTextFill(Color.RED);
            else
                pc_inc.setTextFill(Color.BLACK);
            if(cpu.getControlUnit().PC_INC2(cpu.getDataPath().isZ(), cpu.getDataPath().isN()))
                pc_inc2.setTextFill(Color.RED);
            else
                pc_inc2.setTextFill(Color.BLACK);
            shifter.setText(Integer.toString(cpu.getDataPath().getShifter().shift_amt));
            if(cpu.getDataPath().getShifter().right)
                shifter.appendText("  (right)");
            else
                shifter.appendText("  (left)");
        });
        root.add(chooseFile, 1, 15);
        root.add(run, 2, 15);
        root.add(stop, 3, 15);
        root.add(step, 4, 15);
        root.add(reset, 5, 15);

        chooseFile.setStyle(buttonStyle);
        run.setStyle(buttonStyle);
        stop.setStyle(buttonStyle);
        step.setStyle(buttonStyle);
        reset.setStyle(buttonStyle);
    }

    public void codeArea(Group root, CPU cpu, Stage stage) {
        root.getChildren().addAll(codeArea, stackArea, constantArea, varArea);
        codeArea.setPrefSize(300, 200);
        stackArea.setPrefSize(90, 150);
        constantArea.setPrefSize(90, 150);
        varArea.setPrefSize(90, 150);
        codeArea.relocate(10, 35);
        stackArea.relocate(10, 270);
        constantArea.relocate(110, 270);
        varArea.relocate(210, 270);
        codeArea.setEditable(false);
        stackArea.setEditable(false);
        constantArea.setEditable(false);
        varArea.setEditable(false);
        Label codeLabel = new Label("Instructions");
        Label stackLabel = new Label("Stack");
        Label constLabel = new Label("Constant Pool");
        Label varLabel = new Label("Local Variables");
        root.getChildren().addAll(codeLabel, stackLabel, constLabel, varLabel);
        codeLabel.relocate(10, 12);
        stackLabel.relocate(30, 250);
        constLabel.relocate(110, 250);
        varLabel.relocate(210, 250);
        codeLabel.setStyle(labelStyle);
        stackLabel.setStyle(labelStyle);
        constLabel.setStyle(labelStyle);
        varLabel.setStyle(labelStyle);
    }


    public static void main(String args[]) {
        launch(args);
    }
}
