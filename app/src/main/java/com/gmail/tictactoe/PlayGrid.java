package com.gmail.tictactoe;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.ArrayList;
import java.util.HashSet;


public class PlayGrid extends AppCompatActivity implements View.OnClickListener {

    private ImageButton currentShapeButton;
    private ImageButton imgButtonUndo;
    private ImageButton imgButtonExit;
    private CustomButton tmpButtonID;
    private CustomButton[][] buttonsArray2D;
    private int rows;
    private int columns;
    private int marginSize;
    private int currentPlayer = 1;
    private int currentShape = 1;
    private int previousShape = 1;
    private int buttonBackgroundColor;
    private int inLineToWin = MainActivity.IN_A_LINE_TO_WIN;
    private TableLayout tablePlayField;
    private TableRow.LayoutParams tableRowParams;
    private ArrayList<CustomButton> usedButtons;
    private HashSet<CustomButton> winInRow;
    private HashSet<CustomButton> winInColumn;
    private HashSet<CustomButton> winInDecreasing;
    private HashSet<CustomButton> winInIncreasing;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_grid);

        setRowsAndColumnsOrientation();
        createButtonsInArray2D();
        setupUndoButton();
        setupExitButton();
        setBackgroundMode();
        setupCurrentShapeButton();
        tableGridCreator();
        assignLists();
        currentPlayer(currentShape);
        previousShape(currentPlayer);
    }


    private void setRowsAndColumnsOrientation() {
        if(MainActivity.GRID_ROWS >= MainActivity.GRID_COLUMNS){
            rows = MainActivity.GRID_ROWS;
            columns = MainActivity.GRID_COLUMNS;
        }else {
            rows = MainActivity.GRID_COLUMNS;
            columns = MainActivity.GRID_ROWS;
        }
    }

    private void createButtonsInArray2D() {
        buttonsArray2D = new CustomButton[rows][columns];
    }

    private void setupUndoButton() {
        imgButtonUndo = findViewById(R.id.imageButtonUndo);
        imgButtonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
    }

    private void setupExitButton() {
        imgButtonExit = findViewById(R.id.imageButtonExit);
        imgButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupCurrentShapeButton() {
        currentShapeButton = findViewById(R.id.imageButtonCurrentShape);
    }


    private void tableGridCreator(){
        setupTableRowParams();
        createRowsColumnsAndButtons();
    }

    private void assignLists() {
        usedButtons =new ArrayList<>();
        winInRow = new HashSet<>(3);
        winInColumn = new HashSet<>(3);
        winInDecreasing = new HashSet<>(3);
        winInIncreasing = new HashSet<>(3);
    }

    private void setBackgroundMode() {
        LinearLayout backgroundColor = findViewById(R.id.playGridBackground);
        TableLayout playFiledBackground = findViewById(R.id.playFieldLayout);
        if(MainActivity.darkMode){
            backgroundColor.setBackgroundResource(R.color.colorBlack);
            playFiledBackground.setBackgroundResource(R.drawable.background_white);
            buttonBackgroundColor = android.R.color.black;
        } else {
            backgroundColor.setBackgroundResource(R.color.colorWhite);
            playFiledBackground.setBackgroundResource(R.drawable.background);
            buttonBackgroundColor = android.R.color.white;
        }
    }


    private void setupTableRowParams() {
        tableRowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f );
        setMarginsSize();
        tableRowParams.setMargins(marginSize, marginSize, marginSize, marginSize);
    }

    private void setMarginsSize() {
        if(rows*columns>120)
            marginSize = 3;
        else if(rows*columns>60)
            marginSize = 5;
        else
            marginSize = 10;
    }

    private void createRowsColumnsAndButtons() {
        tablePlayField = findViewById(R.id.playFieldLayout);
        for(int row = 0; row < rows; row++){
            TableRow tableRow = createAndSetupTableRow(tablePlayField);
            for(int col = 0; col < columns; col++){
                CustomButton button = createAndSetupButton(tableRowParams, row, col);
                tableRow.addView(button);
                addButtonToArray2D(row, col, button);
            }
        }
    }

    private TableRow createAndSetupTableRow(TableLayout tablePlayField) {
        TableRow tableRow = new TableRow(this);
        tablePlayField.addView(tableRow);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f ));
        return tableRow;
    }

    private CustomButton createAndSetupButton(TableRow.LayoutParams tableRowParams, int row, int col) {
        CustomButton button = new CustomButton(this);
        button.setLayoutParams(tableRowParams);
        button.setPadding(0,0,0,0 );
        String nameID = convertRowColumnToName(row, col);
        int buttonID = Integer.parseInt("9" + nameID);
        button.setId(buttonID);
        button.setBackgroundResource(buttonBackgroundColor);
        button.setOnClickListener(this);
        button.setName(nameID);
        button.setImInRow(row);
        button.setImInColumn(col);
        return button;
    }

    private String convertRowColumnToName(int row, int column) {
        String tmpRow = "00" + row;
        tmpRow = tmpRow.substring(tmpRow.length()-3);
        String tmpColumn = "00" + column;
        tmpColumn = tmpColumn.substring(tmpColumn.length()-3);

        return tmpRow + tmpColumn;
    }

    private void addButtonToArray2D(int row, int col, CustomButton button) {
        buttonsArray2D[row][col] = button;
    }

    private void lockButtonSizes() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                CustomButton button = buttonsArray2D[row][col];

                int width = button.getWidth();
                button.setMinimumWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinimumHeight(height);
                button.setMaxHeight(height);
            }
        }
    }


    public void currentPlayer(int currentNumber){
        if(currentPlayer<MainActivity.NUMBER_OF_PLAYERS){
            currentPlayer = currentNumber +1;
        }else {
            currentPlayer = 1;
        }
    }

    public void previousPlayer(){
        if(currentPlayer>1){
            currentPlayer--;
        }else {
            currentPlayer = MainActivity.NUMBER_OF_PLAYERS;
        }
    }

    public void previousShape(){
        if(previousShape >1){
            previousShape--;
        }else {
            previousShape = MainActivity.NUMBER_OF_PLAYERS;
        }
        if(currentShape >1){
            currentShape--;
        }else {
            currentShape = MainActivity.NUMBER_OF_PLAYERS;
        }
    }

    public void previousShape(int player){
        if(currentShape == 1 && previousShape == 2 && currentPlayer != 2){
            previousShape = 1;
        }else {

            if(currentShape == previousShape && currentShape !=1){
                currentShape--;
                previousShape--;
            }
            previousShape = currentShape;
            currentShape = player;
        }
    }

    private void undo() {
        imgButtonUndo.setClickable(false);
        if(tmpButtonID!=null) {
            previousPlayer();
            previousShape();
            setCurrentShapeButton(previousShape);
            setButtonImage(tmpButtonID, previousShape);
            tmpButtonID.setClickable(true);
            tmpButtonID.setImageDrawable(null);
        }

    }

    public void setCurrentShapeButton(int player){
        switch (player){
             case 1:
                 currentShapeButton.setImageResource(R.drawable.ring);
                 break;
             case 2:
                 currentShapeButton.setImageResource(R.drawable.x);
                 break;
             case 3:
                 currentShapeButton.setImageResource(R.drawable.trojkat);
                 break;

             case 4:
                currentShapeButton.setImageResource(R.drawable.kwadrat);
                break;

             case 5:
                currentShapeButton.setImageResource(R.drawable.star);
                break;

             case 6:
                currentShapeButton.setImageResource(R.drawable.trapez);
                break;
        }

    }

    public void setButtonImage(CustomButton button, int buttonShape){
        switch (buttonShape){
            case 1:
                button.setImageDrawable(imageSizeForButton(R.drawable.ring, button));
                break;

            case 2:
                button.setImageDrawable(imageSizeForButton(R.drawable.x, button));
                break;

            case 3:
                button.setImageDrawable(imageSizeForButton(R.drawable.trojkat, button));
                break;

            case 4:
                button.setImageDrawable(imageSizeForButton(R.drawable.kwadrat, button));
                break;

            case 5:
                button.setImageDrawable(imageSizeForButton(R.drawable.star, button));
                break;

            case 6:
                button.setImageDrawable(imageSizeForButton(R.drawable.trapez, button));
                break;
        }

    }

    public BitmapDrawable imageSizeForButton(int drawableRes, CustomButton button){
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resources = getResources();
        return new BitmapDrawable(resources, scaledBitmap);
    }


    @Override
    public void onClick(View v) {
        v.setClickable(false);
        tmpButtonID = findViewById(v.getId());
        lockButtonSizes();
        setCurrentShapeButton(currentPlayer);
        setButtonImage(tmpButtonID, previousShape);
        tmpButtonID.setMyShape(previousShape);
        checkForWin(previousShape);
        currentPlayer(currentShape);
        previousShape(currentPlayer);
        imgButtonUndo.setClickable(true);
    }

    private void checkForWin(int previousShape) {
        usedButtons.clear();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (buttonsArray2D[row][col].getMyShape() == previousShape){
                    usedButtons.add(buttonsArray2D[row][col]);
                }
            }
        }
        searchForWinningButtons();
        for(CustomButton customButton : usedButtons){
            System.out.println(customButton.getId());
        }
    }

    private void searchForWinningButtons() {
        for(int i = 0; i < usedButtons.size()-1; i++){
            CustomButton tmpButton1 = usedButtons.get(i);
            CustomButton tmpButton2 = usedButtons.get(i+1);
            System.out.println("btn1 row: " + tmpButton1.getImInRow());
            System.out.println("btn1 col: " + tmpButton1.getImInColumn());
            System.out.println("btn2 row: " + tmpButton2.getImInRow());
            System.out.println("btn2 col: " + tmpButton2.getImInColumn());
            if(tmpButton1.getImInRow() == tmpButton2.getImInRow()+1 &&
                    tmpButton1.getImInColumn() == tmpButton2.getImInColumn()){
                winInColumn.add(tmpButton1);
                winInColumn.add(tmpButton2);
                System.out.println("win in columns: " + winInColumn.size());
            }
            if(tmpButton1.getImInRow() == tmpButton2.getImInRow() &&
                    tmpButton1.getImInColumn() == tmpButton2.getImInColumn()+1){
                winInRow.add(tmpButton1);
                winInRow.add(tmpButton2);
                System.out.println("win in row: " + winInRow.size());
            }
            if(tmpButton1.getImInRow() == tmpButton2.getImInRow()+1 &&
                    tmpButton1.getImInColumn() == tmpButton2.getImInColumn()+1){
                winInDecreasing.add(tmpButton1);
                winInDecreasing.add(tmpButton2);
                System.out.println("win in Decreasing: " + winInDecreasing.size());
            }
            if(tmpButton1.getImInRow() == tmpButton2.getImInRow()+1 &&
                    tmpButton1.getImInColumn() == tmpButton2.getImInColumn()-1){
                winInIncreasing.add(tmpButton1);
                winInIncreasing.add(tmpButton2);
                System.out.println("win in Increasing: " + winInIncreasing.size());
            }
        }
        if(winInRow.size() >= inLineToWin){
            setWinner(winInRow, "row");
        }
        if(winInColumn.size() >= inLineToWin){
            setWinner(winInColumn, "col");
        }
        if(winInDecreasing.size() >= inLineToWin){
            setWinner(winInDecreasing, "dec");
        }
        if(winInIncreasing.size() >= inLineToWin){
            setWinner(winInIncreasing, "inc");
        }
    }

    private void setWinner(HashSet winnerList, String direction) {
        System.out.println("lista zwycięsców HashSet=====: " + direction);
    }

}

