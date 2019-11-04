package com.gmail.tictactoe;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class PlayGrid extends AppCompatActivity implements View.OnClickListener {

    private ImageButton currentShapeButton;
    private ImageButton imgButtonUndo;
    private ImageButton imgButtonExit;
    private ImageButton tmpButtonID;
    private ImageButton[][] buttonsArray2D;
    private int rows;
    private int columns;
    private int marginSize;
    private int currentPlayer = 1;
    private int currentShape = 1;
    private int previousShape = 1;
    private TableLayout tablePlayField;
    private TableRow.LayoutParams tableRowParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_grid);

        setRowsAndColumnsOrientation();
        createButtonsInArray2D();
        setupUndoButton();
        setupExitButton();
        setupCurrentShapeButton();
        tableGridCreator();
        currentPlayer(currentShape);
        previousShape(currentPlayer);
    }

    private void setRowsAndColumnsOrientation() {
        if(MainActivity.GRID_X >= MainActivity.GRID_Y){
            rows = MainActivity.GRID_X;
            columns = MainActivity.GRID_Y;
        }else {
            rows = MainActivity.GRID_Y;
            columns = MainActivity.GRID_X;
        }
    }

    private void createButtonsInArray2D() {
        buttonsArray2D = new ImageButton[rows][columns];
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
                ImageButton button = createAndSetupButton(tableRowParams, row, col);
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

    private ImageButton createAndSetupButton(TableRow.LayoutParams tableRowParams, int row, int col) {
        ImageButton button = new ImageButton(this);
        button.setLayoutParams(tableRowParams);
        button.setPadding(0,0,0,0 );
        int buttonID = Integer.parseInt("9" + row +"99" + col);
        button.setId(buttonID);
        button.setBackgroundResource(R.color.colorWhite);
        button.setOnClickListener(this);
        return button;
    }

    private void addButtonToArray2D(int row, int col, ImageButton button) {
        buttonsArray2D[row][col] = button;
    }

    private void lockButtonSizes() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                ImageButton button = buttonsArray2D[row][col];

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
        previousPlayer();
        previousShape();
        setCurrentShapeButton(previousShape);
        setButtonImage(tmpButtonID, previousShape);
        imgButtonUndo.setClickable(false);
        tmpButtonID.setClickable(true);
        tmpButtonID.setBackgroundResource(R.color.colorWhite);
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

    public void setButtonImage(ImageButton button, int buttonShape){
        switch (buttonShape){
            case 1:
                button.setBackground(imageSizeForButton(R.drawable.ring, button));
                break;

            case 2:
                button.setBackground(imageSizeForButton(R.drawable.x, button));
                break;

            case 3:
                button.setBackground(imageSizeForButton(R.drawable.trojkat, button));
                break;

            case 4:
                button.setBackground(imageSizeForButton(R.drawable.kwadrat, button));
                break;

            case 5:
                button.setBackground(imageSizeForButton(R.drawable.star, button));
                break;

            case 6:
                button.setBackground(imageSizeForButton(R.drawable.trapez, button));
                break;
        }

    }

    public BitmapDrawable imageSizeForButton(int drawableRes, ImageButton button){
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resources = getResources();
        return new BitmapDrawable(resources, scaledBitmap);
    }


    @Override
    public void onClick(View v) {
//        System.out.println("height: " + tablePlayField.getHeight() + " width: " + tablePlayField.getWidth());
        v.setClickable(false);
        tmpButtonID = findViewById(v.getId());
        System.out.println("button id: " + tmpButtonID.getId());
        lockButtonSizes();
        setCurrentShapeButton(currentPlayer);
        setButtonImage(tmpButtonID, previousShape);
        currentPlayer(currentShape);
        previousShape(currentPlayer);
        imgButtonUndo.setClickable(true);
    }

}
