package com.aaroncarsonart.tarotrl.gui;

import com.aaroncarsonart.tarotrl.Globals;
import org.hexworks.cobalt.databinding.internal.property.DefaultProperty;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.ColorThemes;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.component.ColorTheme;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.component.Panel;
import org.hexworks.zircon.api.component.TextBox;
import org.hexworks.zircon.api.component.renderer.impl.BoxDecorationRenderer;
import org.hexworks.zircon.api.component.renderer.impl.HalfBlockDecorationRenderer;
import org.hexworks.zircon.api.component.renderer.impl.ShadowDecorationRenderer;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.TilesetResource;
import org.hexworks.zircon.api.screen.Screen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Holds code for laying out the Zircon display used by the game window, roughly like the
 * following:
 *
 * +--------------+ +------------------------------+ +---------------------+
 * | Player Stats | |      Map Data View Port      | | Map Inspect Details |
 * |              | |                              | |                     |
 * |              | |                              | |                     |
 * |              | |                              | |                     |
 * |              | |                              | |                     |
 * |              | |                              | |                     |
 * |              | |                              | |                     |
 * |              | |                              | |                     |
 * |              | |                              | |                     |
 * +--------------+ +------------------------------+ +---------------------+
 * +-----------------------------------------------------------------------+
 * |  Activity Log                                                         |
 * |                                                                       |
 * |                                                                       |
 * +-----------------------------------------------------------------------+
 */
public class WindowLayout {

    private int screenWidth;
    private int screenHeight;

    private TilesetResource tilesetResource;
    private Screen screen;
    private TileGrid tileGrid;

    private Panel leftPanel;
    private Panel rightPanel;
    private Panel bottomPanel;
    private TextBox bottomTextBox;

    public WindowLayout() {
    }

    public void init() {
        screenWidth = 100;
        screenHeight = 50;

        tilesetResource = CP437TilesetResources.rexPaint10x10();
        tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(screenWidth, screenHeight))
                        .withDefaultTileset(tilesetResource)
                        .build());

//        leftPanel = Components.panel()
//                .wrapWithBox(true)
//                .withTitle("Player Stats")
//                .wrapWithShadow(true)
//                .withSize(16, 20)
//                .withPosition(Positions.offset1x1())
//                .build();

        int activityLogWidth = screenWidth - 2;
        int activityLogHeight = 7;
//        bottomPanel = Components.panel()
//                .wrapWithBox(true)
//                .withTitle("Activity Log")
//                //.wrapWithShadow(true)
//                .withSize(activityLogWidth, activityLogHeight)
//                .withPosition(Positions.create(0, screenHeight - activityLogHeight))
//                .build();

        String paragraph = "To explore the tarot using a roguelike interface. Therefore, the set of 78 tarot cards is essential to every aspect of this gameplay.  Whenever something must be randomized, some property of this set of cards will be used to determine an effect.";

        LogArea logArea = Components.logArea()
                .wrapWithBox(true)
                .withTitle("Activity Log")
                .withSize(98, 7)
                .withPosition(Positions.create(0, 41))
                .build();
//        bottomPanel.addComponent(logArea);
        logArea.onMouseClicked(mouseAction -> {
            String logEntry = "This is paragraph " + i++;
            logArea.addParagraph(logEntry, true, 0);
            screen.display();
        });

//        bottomPanel.addComponent(bottomTextBox);

//        rightPanel = Components.panel()
//                .wrapWithBox(true)
//                .withTitle("Map Inspect Details")
//                .wrapWithShadow(true)
//                .withSize(20, 20)
//                .withPosition(Positions.topRightOf(bottomPanel).withRelativeY(-1))
//                .build();


        screen = Screens.createScreenFor(tileGrid);

//        screen.addComponent(leftPanel);
//        screen.addComponent(rightPanel);
        screen.addComponent(logArea);
        screen.applyColorTheme(ColorThemes.monokaiBlue());
        screen.display();

    }

    int i = 0;

    public void initLogAreaBehavior() {
        TileGrid tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withDefaultTileset(CP437TilesetResources.rexPaint12x12())
                .enableBetaFeatures()
                .withSize(Sizes.create(72, 32))
                .build());

        Screen screen = Screens.createScreenFor(tileGrid);

        LogArea logArea = Components.logArea()
                //.wrapWithBox(true)
                .withSize(70, 30)
                .withTitle("Log")
                .build();

        logArea.addParagraph("This is a simple log row that is kind of long and hopefully wraps.  I guess, we shall see, hmm???", false, 0);
        logArea.addNewRows(1);
        logArea.addParagraph("This is a simple log row", true, 0);
        logArea.addParagraph("This is a simple log row", true, 0);
        logArea.addParagraph("This is a simple log row", true, 0);

        logArea.addParagraph("This is a simple log row", false, 0);
        logArea.addParagraph("This is a simple log row", false, 0);
        logArea.addParagraph("This is a simple log row", false, 0);

        logArea.addParagraph("This is a log row with a typing effect", false, 200);
        logArea.addNewRows(2);

        logArea.addInlineText("This is a log row with a ");
        Button btn = Components.button()
                .withDecorationRenderers()
                .withText("Button")
                .build();
        logArea.addInlineComponent(btn);
        logArea.commitInlineElements();

        logArea.addNewRows(2);
        logArea.addParagraph("This is a long log row, which gets wrapped, since it is long", false, 0);


        logArea.addNewRows(1);

        logArea.addParagraph(Components.paragraph()
                        .withText("This is a long log row, which gets wrapped, since it is long with a different style"),
                false);

        screen.addComponent(logArea);
        screen.applyColorTheme(ColorThemes.amigaOs());
        screen.display();
    }

    public void initTextBoxBehavior() {
        TileGrid tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withDefaultTileset(CP437TilesetResources.rexPaint12x12())
                .enableBetaFeatures()
                .withSize(Sizes.create(70, 40))
                .build());

        Screen screen = Screens.createScreenFor(tileGrid);

        TextBox textBox = Components.textBox()
                .withContentWidth(40)
                .addParagraph("This is a row of text.")
                .addParagraph("This is another row of text.")
                .addParagraph("This ought to be a long enough row of text that the contents should wrap around and loop to at least one more line of the text box, possibly even two!")
                .addParagraph("This is a newline test.")

                .addListItem("Bullet one.")
                .addListItem("Bullet two?")
                .addListItem("Oh my, we have three bullets now!!!")
                .addNewLine()
                .addHeader("My header")
                .addInlineText("This is in-line text")
                .addInlineComponent(Components.button().withText("I am a button").build())
                .commitInlineElements()
                .withTitle("My Text Box")
                .wrapWithBox(true)
                .wrapWithShadow(true)
                .withPosition(5, 5)
                .build();

        screen.addComponent(textBox);
        textBox.applyColorTheme(ColorThemes.amigaOs());
        screen.display();
    }

    public void initScrollableLogAreaBehavior() {

        ColorTheme theme = ColorThemes.nord();
        List<String> texts = Arrays.asList(
                "Locating the required gigapixels to render...",
                "Spinning up the hamster...",
                "Shovelling coal into the server...",
                "Programming the flux capacitor",
                "640K ought to be enough for anybody",
                "The architects are still drafting",
                "The bits are breeding",
                "We're building the buildings as fast as we can",
                "Would you prefer chicken, steak, or tofu?",
                "Pay no attention to the man behind the curtain",
                "A few bits tried to escape, but we caught them",
                "Checking the gravitational constant in your locale",
                "Go ahead -- hold your breath",
                "Hum something loud while others stare",
                "You're not in Kansas any more",
                "The server is powered by a lemon and two electrodes",
                "We're testing your patience",
                "Don't think of purple hippos",
                "Follow the white rabbit",
                "Why don't you order a sandwich?",
                "The bits are flowing slowly today",
                "Dig on the 'X' for buried treasure... ARRR!");

        TilesetResource tileset = CP437TilesetResources.rogueYun16x16();
        TileGrid tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withDefaultTileset(tileset)
                .enableBetaFeatures()
                .withSize(Sizes.create(72, 32))
                .build());

        Screen screen = Screens.createScreenFor(tileGrid);

        Panel panel = Components.panel()
                .wrapWithBox(true)
                .withSize(Sizes.create(60, 25))
                .withTitle("Log")
                .build();

        LogArea logArea = Components.logArea()
                .withSize(58, 23)
//                .withTitle("Log")
                .build();

        panel.addComponent(logArea);
        screen.addComponent(panel);
        //screen.applyColorTheme(ColorThemes.amigaOs());
        screen.display();

        Random random = Globals.RANDOM;

        for (i = 0; i < 40; i++) {
            try {
                Thread.sleep(random.nextInt(500));
            } catch (Exception e) {
            }
            logArea.addParagraph(texts.get(random.nextInt(texts.size())), false, 0);
        }

    }

    public void initLabelBehavior() {
        ColorTheme theme = ColorThemes.nord();
        TilesetResource tileset = CP437TilesetResources.rogueYun16x16();

        TileGrid tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withDefaultTileset(tileset)
                .enableBetaFeatures()
                .withSize(Sizes.create(72, 32))
                .build());

        Screen screen = Screens.createScreenFor(tileGrid);


        screen.addComponent(Components.label()
                .withText("Foobar")
                .wrapWithShadow(true)
                .withPosition(Positions.create(2, 2))
                .build());

        screen.addComponent(Components.label()
                .withText("Barbaz wombat")
                .withSize(Sizes.create(5, 2))
                .withPosition(Positions.create(2, 6))
                .build());

        screen.addComponent(Components.label()
                .withText("Qux")
                .wrapWithShadow(true)
                .wrapWithBox(true)
                .withPosition(Positions.create(2, 10))
                .build());

        screen.addComponent(Components.label()
                .withText("Qux")
                .wrapWithShadow(true)
                .withBoxType(BoxType.DOUBLE)
                .wrapWithBox(true)
                .withPosition(Positions.create(15, 2))
                .build());

        screen.addComponent(Components.label()
                .withText("Wtf")
                .withDecorationRenderers(
                        new ShadowDecorationRenderer(),
                        new BoxDecorationRenderer(BoxType.DOUBLE, new DefaultProperty<>("")),
                        new BoxDecorationRenderer(BoxType.SINGLE, new DefaultProperty<>("")))
                .withPosition(Positions.create(15, 7))
                .build());

        screen.addComponent(Components.label()
                .withText("Wtf")
                .withDecorationRenderers(
                        new ShadowDecorationRenderer(),
                        new HalfBlockDecorationRenderer())
                .withPosition(Positions.create(15, 14))
                .build());

        screen.applyColorTheme(theme);
        screen.display();
    }

    public static void main(String[] args) {
        WindowLayout windowLayout = new WindowLayout();
//        windowLayout.init();
//        windowLayout.initLogAreaBehavior();
//        windowLayout.initTextBoxBehavior();
//        windowLayout.initScrollableLogAreaBehavior();
        windowLayout.initLabelBehavior();
    }
}
