package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kroy.Kroy;

/**
 * Implements a screen to display the text of the story.
 */
public class StoryLineScreen extends BasicScreen {
    protected Texture texture;
    protected Skin skin2;
    private String displayText;

    /**
     * Constructor, saves the text to display to memory.
     * 
     * @param game The game object for the screen to be displayed on.
     */
    public StoryLineScreen(Kroy game) {
        super(game);

        skin2 = new Skin(Gdx.files.internal("skin/uiskin2.json"), atlas);
            displayText = "It is a period of wars in the galaxy. A brave alliance of underground fire fighters has challenged the tyranny and oppression of the awesome ET EMPIRE.\n" +
                    "\n" +
                    "Striking from a fortress hidden among the billion buildings of york, rebel fire engines have won\n" +
                    "their first victory in a battle with the powerful ET Starfleet. The Kroy EMPIRE fears that\n" +
                    "another defeat could bring a thousand more cites into the rebellion, and kroy control over the\n" +
                    "world would be lost forever.\n" +
                    "\n" +
                    "To crush the rebellion once and for all, the EMPIRE is constructing a sinister new ignition\n" +
                    "system. Powerful enough to burn an entire city, its completion spells certain doom for the champions of freedom.";
    }

    /**
     * Displays all the buttons and text on the screen.
     */
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
        winLabel.setAlignment(Align.center);

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