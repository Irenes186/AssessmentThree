package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kroy.Kroy;


//Class imports


public class TutorialScreen extends BasicScreen {

    protected Texture texture;
    protected Skin skin2;
    private String displayText;

    //Constructor
    public TutorialScreen(Kroy game) {
        super(game);

        skin2 = new Skin(Gdx.files.internal("skin/uiskin2.json"), atlas);

            displayText = "Keyboard buttons “W”, “A”, “S”, and “D”, as well as the arrow keys, are used to move the truck up, left, down, and right respectively. The truck will rotate as you change direction.\n" +
                    "There are two fire trucks available, these can be switched between by using the keyboard button “TAB”. \n Using the keyboard button “E”, you are able to toggle your hose water on and off. The water will aim in the direction of your mouse cursor." +
                    "You must go back to the fire station in order to repair and refill.";
    }


    @Override
    public void show() {

        // Allow stage to control screen inputs.
        Gdx.input.setInputProcessor(stage);

        // Create table to arrange buttons.
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center();

        // Create buttons
        TextButton menuButton = new TextButton("Menu", skin);

        //Create label
        Label winLabel = new Label(displayText,skin2);
        winLabel.setFontScale(2,2);

        // Increase size of text
        menuButton.setTransform(true);
        menuButton.scaleBy(0.25f);

        // Add listeners
        menuButton.addListener(new ClickListener(){
            @Override
            //transition to game screen
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        // Add buttons to table and style them
        buttonTable.add(winLabel).padBottom(40).padRight(40).width(150).height(150);
        buttonTable.row();
        buttonTable.add(menuButton).padBottom(40).padRight(40).width(150).height(40);
        buttonTable.row();

        // Add table to stage
        stage.addActor(buttonTable);


    }
}
