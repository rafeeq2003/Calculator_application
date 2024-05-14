package calculatorapplication;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CalculatorApplication extends Application implements EventHandler<ActionEvent> 
{
    private final TextField display = new TextField();
    private boolean start = true;
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("My Calculator");
        display.setFont(Font.font(20));
        display.setPrefHeight(50);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setEditable(false);

        HBox hb1 = new HBox(display);
        hb1.setPadding(new Insets(10));
        hb1.setAlignment(Pos.CENTER);

        TilePane forButtons = new TilePane();
        forButtons.setHgap(10);
        forButtons.setVgap(10);
        forButtons.setAlignment(Pos.CENTER);

        String[][] buttonLabels = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "X"},
                {"1", "2", "3", "-"},
                {"0", ".", "%", "+"},
                { " ","B", "C", "="},
        };

        for (String[] buttonLabel : buttonLabels) 
        {
            for (String buttonText : buttonLabel) 
            {
                Button button = createButton(buttonText);
                forButtons.getChildren().add(button);
            }
        }

        HBox hb2 = new HBox(forButtons);
        hb2.setPadding(new Insets(10, 10, 10, 10));

        VBox root = new VBox(hb1, hb2);
        root.setPadding(new Insets(10));
        
        Scene sc = new Scene(root, 250, 340);
        stage.setScene(sc);
        stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    private Button createButton(String buttonText) 
    {
        Button button = new Button(buttonText);
        button.setMinSize(40, 40);
        button.setFont(Font.font(18));
        button.setOnAction(this);
        return button;
    }

    private void calculate() 
    {
        String expression = display.getText();
        String[] tokens = expression.split(" ");

        if (tokens.length == 3) 
        {
            try {
                double num1 = Double.parseDouble(tokens[0]);
                String operator = tokens[1];
                double num2 = Double.parseDouble(tokens[2]);

                double result = 0;

                switch (operator) 
                {
                    case "+" -> result = num1 + num2;
                    case "-" -> result = num1 - num2;
                    case "X" -> result = num1 * num2;
                    case "/" -> {
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            display.setText("Error: Division by zero");
                            return;
                        }
                    }
                    case "%" -> result = num1 % num2;
                }
                display.setText(String.valueOf(result));
            } catch (NumberFormatException | ArithmeticException e) 
            {
                display.setText("Error");
            }
        }
    }
    @Override
    public void handle(ActionEvent t) 
    {
        if (t.getSource() instanceof Button button) 
        {
            String buttonText = button.getText();

            switch (buttonText) 
            {
                case "C" -> {
                    display.clear();
                    start = true;
                }
                case "=" -> 
                {
                    calculate();
                    start = true;
                }
                case "B" -> 
                {
                    String currentText = display.getText();
                    if (!currentText.isEmpty()) {
                        display.setText(currentText.substring(0, currentText.length() - 1));
                    }
                }
                case "." -> 
                {
                        display.appendText(".");
                }
                case "%" -> 
                {
                    try 
                    {
                        double num = Double.parseDouble(display.getText());
                        display.setText(String.valueOf(num / 100));
                    } catch (NumberFormatException e) 
                    {
                        display.setText("Error");
                    }
                }
                default -> {
                    if (Character.isDigit(buttonText.charAt(0))) 
                    {
                        display.appendText(buttonText);
                    } else if ("+-X/".contains(buttonText)) 
                    {
                        if (!start) 
                        {
                            display.appendText(buttonText);
                            start = true;
                        } else 
                        {
                            display.setText(display.getText() + " " + buttonText + " ");
                            start = false;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
