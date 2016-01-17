package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rezzo.libgdxjam.LifeIsSpace;
import entities.DiscoveredElement;
import states.StageTwo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StageTwoUI {

    private final StageTwo stageTwo;

    public static int terraformPoints = 0;

    private Stage stageTwoUI;

    Group firstSelectionBox = new Group();
    Group secondSelectionBox = new Group();

    Group selGroup;

    Label congratsLabel;

    int pos1 = 1;
    int pos2 = 2;

    int rangeMin = 0, rangeMax = 5;

    float speed = -2;

    Image selBoxImageOne;
    Image selBoxImageTwo;
    Image selBoxImageSelOne;
    Image selBoxImageSelTwo;
    Image resultImg;

    Image mixImage, crossImage;

    Vector2 firstSlotPos, secondSlotPos, thirdSlotPos, fourthSlotPos, fifthSlotPos, selectOnePos, selectTwoPos, resultPos;

    Sprite elementFirst, elementSecond, resultSprite;

    Label clickLabel, setLabel, resultLabel, unknownLabel;

    ArrayList<DiscoveredElement> discoveredElements;

    DiscoveredElement discoveredElementFirst, discoveredElementSecond;

    boolean selectedOne = false, selectedTwo = false, selectedSlotOne = false, selectedSlotTwo = false, firstTime = true;

    Label leftElementName, rightElementName, terraformLabel;

    Sprite planetAtmos, planetImage;

    ArrayList<Sprite> planetElements;

    HashMap<Float[], Sprite> rotationMap;

    ParticleEffect fireParticle;

    Group imageBottomGroup;
    Label infoLabel;

    Group slotGroup;

    float angle = 270;

    float anglettes = 90;

    public StageTwoUI(final StageTwo stageTwo) {
        this.stageTwo = stageTwo;

        stageTwoUI = new Stage(new FitViewport(LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT));

        Gdx.input.setInputProcessor(stageTwoUI);

        Combinations.setCombinations();


        planetElements = new ArrayList<>();
        rotationMap = new HashMap<>();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Assets.normalFont;

        congratsLabel = new Label("You have made it!\n\nThanks for playing, vote for us if you liked our little game :)", style);
        congratsLabel.setPosition(LifeIsSpace.WIDTH / 2 - congratsLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - congratsLabel.getPrefHeight() / 2);
        congratsLabel.addAction(Actions.fadeOut(0));
        congratsLabel.setAlignment(Align.center);

        planetImage = new Sprite(Assets.planet_big);
        planetImage.setPosition(LifeIsSpace.WIDTH / 2 - planetImage.getWidth() / 2, LifeIsSpace.HEIGHT / 2 - planetImage.getHeight() / 2);

        terraformLabel = new Label(String.format("TERRAFORM %d/15", terraformPoints), style);
        terraformLabel.setPosition(12, LifeIsSpace.HEIGHT - 18);

        infoLabel = new Label("Choose 2 components\nto combine", style);
        infoLabel.setAlignment(Align.center);
        infoLabel.setPosition(LifeIsSpace.WIDTH / 2 - infoLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 + 67);

        Image coldImageBottom = new Image(Assets.e_cold);
        coldImageBottom.setPosition(-121.5f, 0);
        Label coldLabel = new Label("COLD", style);
        coldLabel.setPosition(coldImageBottom.getX() + 17, coldImageBottom.getY() + 2.5f);
        Image heatImageBottom = new Image(Assets.e_heat);
        heatImageBottom.setPosition(-60, 0);
        Label heatLabel = new Label("HEAT", style);
        heatLabel.setPosition(heatImageBottom.getX() + 17, heatImageBottom.getY() + 2.5f);
        Image gravImageBottom = new Image(Assets.e_grav);
        Label gravLabel = new Label("GRAVITY", style);
        gravLabel.setPosition(gravImageBottom.getX() + 17, gravImageBottom.getY() + 2.5f);
        Image rockImageBottom = new Image(Assets.e_rock);
        rockImageBottom.setPosition(87, 0);
        Label rockLabel = new Label("ROCK", style);
        rockLabel.setPosition(rockImageBottom.getX() + 17, rockImageBottom.getY() + 2.5f);

        imageBottomGroup = new Group();
        imageBottomGroup.addActor(coldImageBottom);
        imageBottomGroup.addActor(heatImageBottom);
        imageBottomGroup.addActor(gravImageBottom);
        imageBottomGroup.addActor(rockImageBottom);
        imageBottomGroup.addActor(coldLabel);
        imageBottomGroup.addActor(heatLabel);
        imageBottomGroup.addActor(gravLabel);
        imageBottomGroup.addActor(rockLabel);
        imageBottomGroup.setPosition(LifeIsSpace.WIDTH / 2 - imageBottomGroup.getWidth() / 2, 20);


        //SLOTS
        slotGroup = new Group();

        Image firstSlot = new Image(Assets.e_slot);
        firstSlot.setPosition(-80, 0);
        Image secondSlot = new Image(Assets.e_slot);
        secondSlot.setPosition(-40, 0);
        Image thirdSlot = new Image(Assets.e_slot);
        Image fourthSlot = new Image(Assets.e_slot);
        fourthSlot.setPosition(40, 0);
        Image fifthSlot = new Image(Assets.e_slot);
        fifthSlot.setPosition(80, 0);

        Image leftArrowSel = new Image(new Sprite(Assets.e_list_arrow));
        leftArrowSel.setPosition(-90, fifthSlot.getPrefHeight() / 2 - leftArrowSel.getPrefHeight() / 2);
        Sprite rightArrowSelSprite = new Sprite(Assets.e_list_arrow);
        rightArrowSelSprite.flip(true, false);
        Image rightArrowSel = new Image(rightArrowSelSprite);
        rightArrowSel.setPosition(115, fifthSlot.getPrefHeight() / 2 - rightArrowSel.getPrefHeight() / 2);

        setListenersToArrows(leftArrowSel, rightArrowSel);

        slotGroup.addActor(firstSlot);
        slotGroup.addActor(secondSlot);
        slotGroup.addActor(thirdSlot);
        slotGroup.addActor(fourthSlot);
        slotGroup.addActor(fifthSlot);
        slotGroup.addActor(leftArrowSel);
        slotGroup.addActor(rightArrowSel);

        slotGroup.setPosition(LifeIsSpace.WIDTH / 2 - fifthSlot.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - fifthSlot.getPrefHeight() / 2 + 35);

        selGroup = new Group();

        Image selOne = new Image(Assets.e_slot);
        selOne.setPosition(21, 0);
        Image selTwo = new Image(Assets.e_slot);
        selTwo.setPosition(-21, 0);
        resultImg = new Image(Assets.e_slot);
        resultImg.setPosition(0, -40);

        Label selOneLabel = new Label("2", style);
        selOneLabel.setPosition(selOne.getX() + selOne.getPrefWidth() / 2 - selOneLabel.getPrefWidth() / 2, selOne.getPrefHeight() + 3);

        Label selTwoLabel = new Label("1", style);
        selTwoLabel.setPosition(selTwo.getX() + selTwo.getPrefWidth() / 2 - selTwoLabel.getPrefWidth() / 2, selTwo.getPrefHeight() + 3);

        unknownLabel = new Label("?", style);
        unknownLabel.setPosition(resultImg.getX() + resultImg.getPrefWidth() / 2 - unknownLabel.getPrefWidth() / 2,
                resultImg.getY() + resultImg.getPrefHeight() / 2 - unknownLabel.getPrefHeight() / 2);

        Sprite leftArrow = new Sprite(Assets.e_slot_arrow);
        Sprite rightArrow = new Sprite(Assets.e_slot_arrow);
        rightArrow.flip(true, false);

        Image arrowImgLeft = new Image(leftArrow);
        arrowImgLeft.setPosition(selTwo.getX() + selTwo.getPrefWidth() / 2, -8);

        Image arrowImgRight = new Image(rightArrow);
        arrowImgRight.setPosition(selOne.getX(), -8);

        selBoxImageSelOne = new Image(Assets.e_slot_sel);
        selBoxImageSelOne.setPosition(selOne.getX() + selOne.getPrefWidth() / 2 - selBoxImageSelOne.getPrefWidth() / 2,
                selOne.getY() + selOne.getPrefHeight() / 2 - selBoxImageSelOne.getPrefHeight() / 2);
        selBoxImageSelTwo = new Image(Assets.e_slot_sel);
        selBoxImageSelTwo.setPosition(selTwo.getX() + selTwo.getPrefWidth() / 2 - selBoxImageSelTwo.getPrefWidth() / 2,
                selTwo.getY() + selTwo.getPrefWidth() / 2 - selBoxImageSelTwo.getPrefHeight() / 2);

        selBoxImageSelOne.setVisible(false);
        selBoxImageSelTwo.setVisible(false);
        selBoxImageSelOne.setTouchable(Touchable.disabled);
        selBoxImageSelTwo.setTouchable(Touchable.disabled);

        selGroup.addActor(selOne);
        selGroup.addActor(selTwo);
        selGroup.addActor(selOneLabel);
        selGroup.addActor(selTwoLabel);
        selGroup.addActor(arrowImgLeft);
        selGroup.addActor(arrowImgRight);
        selGroup.addActor(resultImg);
        selGroup.addActor(unknownLabel);
        selGroup.addActor(selBoxImageSelOne);
        selGroup.addActor(selBoxImageSelTwo);


        selGroup.setPosition(LifeIsSpace.WIDTH / 2 - fifthSlot.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - selOne.getPrefHeight() / 2 - 40);

        //----
        //Setup selection
        firstSlotPos = slotGroup.localToStageCoordinates(new Vector2(firstSlot.getX() + firstSlot.getPrefWidth() / 2, firstSlot.getY() + firstSlot.getPrefHeight() / 2));
        secondSlotPos = slotGroup.localToStageCoordinates(new Vector2(secondSlot.getX() + secondSlot.getPrefWidth() / 2, secondSlot.getY() + secondSlot.getPrefHeight() / 2));
        thirdSlotPos = slotGroup.localToStageCoordinates(new Vector2(thirdSlot.getX() + thirdSlot.getPrefWidth() / 2, thirdSlot.getY() + thirdSlot.getPrefHeight() / 2));
        fourthSlotPos = slotGroup.localToStageCoordinates(new Vector2(fourthSlot.getX() + fourthSlot.getPrefWidth() / 2, fourthSlot.getY() + fourthSlot.getPrefHeight() / 2));
        fifthSlotPos = slotGroup.localToStageCoordinates(new Vector2(fifthSlot.getX() + fifthSlot.getPrefWidth() / 2, fifthSlot.getY() + fifthSlot.getPrefHeight() / 2));

        firstSelectionBox = new Group();
        secondSelectionBox = new Group();

        selBoxImageOne = new Image(Assets.e_slot_sel);
        selBoxImageTwo = new Image(Assets.e_slot_sel);

        Label selBoxOneLabel = new Label("2", style);
        selBoxOneLabel.setPosition(selBoxImageOne.getWidth() / 2 - selBoxOneLabel.getPrefWidth() / 2 + 0.5f, 37);
        Label selBoxTwoLabel = new Label("1", style);
        selBoxTwoLabel.setPosition(selBoxImageTwo.getWidth() / 2 - selBoxTwoLabel.getPrefWidth() / 2 + 0.5f, 37);
        selBoxTwoLabel.setAlignment(Align.bottom);

        firstSelectionBox.addActor(selBoxImageOne);
        firstSelectionBox.addActor(selBoxOneLabel);
        firstSelectionBox.setPosition(firstSlotPos.x - selBoxImageOne.getPrefWidth() / 2, firstSlotPos.y - selBoxImageOne.getPrefHeight() / 2);

        secondSelectionBox.addActor(selBoxImageTwo);
        secondSelectionBox.addActor(selBoxTwoLabel);
        secondSelectionBox.setPosition(secondSlotPos.x - selBoxImageTwo.getPrefHeight() / 2, secondSlotPos.y - selBoxImageTwo.getPrefHeight() / 2);

        firstSelectionBox.setVisible(false);
        firstSelectionBox.setTouchable(Touchable.disabled);
        secondSelectionBox.setVisible(false);
        secondSelectionBox.setTouchable(Touchable.disabled);

        //Instruction Labels

        clickLabel = new Label("Click on any slot", style);
        clickLabel.setPosition(LifeIsSpace.WIDTH / 2 - clickLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - clickLabel.getPrefHeight() / 2);

        setLabel = new Label("Select element to put\ninto the slot", style);
        setLabel.setPosition(LifeIsSpace.WIDTH / 2 - setLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - setLabel.getPrefHeight() / 2);
        setLabel.setAlignment(Align.center);
        setLabel.setVisible(false);

        resultLabel = new Label("", style);
        //resultLabel.setAlignment(Align.center);
        resultLabel.setVisible(true);

        setListenersToSelectionBoxes();
        setListenersToSlots(firstSlot, secondSlot, thirdSlot, fourthSlot, fifthSlot);

        //Discovered Elements

        discoveredElements = new ArrayList<>();

        DiscoveredElement gravity = new DiscoveredElement(new Sprite(Assets.e_grav), "Gravity");
        DiscoveredElement rock = new DiscoveredElement(new Sprite(Assets.e_rock), "Rock");
        DiscoveredElement heat = new DiscoveredElement(new Sprite(Assets.e_heat), "Heat");
        DiscoveredElement cold = new DiscoveredElement(new Sprite(Assets.e_cold), "Cold");

        discoveredElements.add(gravity);
        discoveredElements.add(rock);
        discoveredElements.add(heat);
        discoveredElements.add(cold);

        //

        leftElementName = new Label("", style);
        leftElementName.setAlignment(Align.right);
        rightElementName = new Label("", style);

        mixImage = new Image(Assets.mix);
        mixImage.setPosition(LifeIsSpace.WIDTH / 2 - mixImage.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - mixImage.getPrefHeight() / 2 - 107);
        mixImage.setVisible(false);

        resultImg = new Image(Assets.cross);
        resultImg.setPosition(resultPos.x - resultImg.getPrefWidth() / 2, resultPos.y - resultImg.getPrefHeight() / 2);
        resultImg.setVisible(false);

        setListenerToMixButton();

        stageTwoUI.addActor(terraformLabel);
        stageTwoUI.addActor(infoLabel);
        stageTwoUI.addActor(imageBottomGroup);
        stageTwoUI.addActor(slotGroup);
        stageTwoUI.addActor(selGroup);
        stageTwoUI.addActor(firstSelectionBox);
        stageTwoUI.addActor(secondSelectionBox);
        stageTwoUI.addActor(clickLabel);
        stageTwoUI.addActor(setLabel);
        stageTwoUI.addActor(leftElementName);
        stageTwoUI.addActor(rightElementName);
        stageTwoUI.addActor(mixImage);
        stageTwoUI.addActor(resultLabel);
        stageTwoUI.addActor(resultImg);
        stageTwoUI.addActor(congratsLabel);

        planetImage.setOriginCenter();

    }

    private void setElementPosition(Sprite sprite, float distance, float angle) {

        float dX = distance * MathUtils.cosDeg(angle) + planetImage.getX() + planetImage.getWidth() / 2;
        float dY = distance * MathUtils.sinDeg(angle) + planetImage.getY() + planetImage.getHeight() / 2;
        sprite.setOriginCenter();
        sprite.setRotation(angle - 90);
        sprite.setPosition(dX - sprite.getWidth() / 2, dY - sprite.getHeight() / 2);
    }

    private void setListenerToMixButton() {
        mixImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                DiscoveredElement newElement = Combinations.combineElements(discoveredElementFirst, discoveredElementSecond);

                if (newElement != null) {
                    discoveredElements.add(newElement);
                    unknownLabel.setVisible(false);
                    resultSprite = new Sprite(newElement.getSprite());
                    resultSprite.setPosition(resultPos.x - resultSprite.getWidth() / 2, resultPos.y - resultSprite.getHeight() / 2);
                    resultLabel.setVisible(true);
                    resultLabel.setText(String.format("You've created\n%s!", newElement.getName()));
                    resultLabel.setPosition(LifeIsSpace.WIDTH / 2, LifeIsSpace.HEIGHT / 2 - resultLabel.getPrefHeight() / 2 + 7);
                    resultLabel.setAlignment(Align.center);
                    selectedOne = false;
                    selectedTwo = false;
                    selectedSlotOne = false;
                    selectedSlotTwo = false;
                    firstSelectionBox.setVisible(false);
                    secondSelectionBox.setVisible(false);
                    selBoxImageSelOne.setVisible(false);
                    selBoxImageSelTwo.setVisible(false);
                    leftElementName.setText("");
                    rightElementName.setText("");
                    Assets.element.play(0.1f);
                    terraformPoints++;

                    //PLANET WE CAN SEEE!!sd

                    float elementAngle = 0;

                    switch (newElement.getName()) {
                        case "Atmosphere":
                            planetAtmos = new Sprite(Assets.planet_atmosphere);
                            planetAtmos.setPosition(LifeIsSpace.WIDTH / 2 - planetAtmos.getWidth() / 2, LifeIsSpace.HEIGHT / 2 - planetAtmos.getHeight() / 2);
                            break;
                        case "Plants":
                            planetImage.set(new Sprite(Assets.planet_grass));
                            planetImage.setPosition(LifeIsSpace.WIDTH / 2 - planetImage.getWidth() / 2, LifeIsSpace.HEIGHT / 2 - planetImage.getHeight() / 2);
                            for (int i = 0; i < 30; i++) {
                                float angle = MathUtils.random(0, 360);
                                Sprite sprite = new Sprite(Assets.plants);
                                setElementPosition(sprite, 155.5f, angle);
                                Float[] flt = new Float[]{(float) i, 155.5f, angle};
                                rotationMap.put(flt, sprite);
                                planetElements.add(sprite);
                            }
                            break;
                        case "Mountains":
                            for (int i = 0; i < 15; i++) {
                                float angle = MathUtils.random(0, 360);
                                Sprite mountain = new Sprite(Assets.mountains);
                                setElementPosition(mountain, 152f, angle);
                                Float[] flt = new Float[]{(float) i, 152f, angle};
                                rotationMap.put(flt, mountain);
                                planetElements.add(mountain);
                            }
                            break;
                        case "Tree":
                            for (int i = 0; i < 25; i++) {
                                float angle = MathUtils.random(0, 360);
                                Sprite tree = new Sprite(Assets.tree);
                                setElementPosition(tree, 160f, angle);
                                Float[] flt = new Float[]{(float) i, 160f, angle};
                                rotationMap.put(flt, tree);
                                planetElements.add(tree);
                            }
                            break;
                        case "Hut":
                            Sprite hut = new Sprite(Assets.hut);
                            setElementPosition(hut, 161.5f, -280);
                            Float[] flt1 = new Float[]{(float) 200, 161.5f, -280f};
                            rotationMap.put(flt1, hut);
                            planetElements.add(hut);
                            break;
                        case "Hearth":
                            Sprite hearth = new Sprite(Assets.hearth);
                            setElementPosition(hearth, 151.5f, -270);
                            Float[] flt2 = new Float[]{(float) 300, 151.5f, -270f};
                            rotationMap.put(flt2, hearth);
                            planetElements.add(hearth);
                            //fireParticle = new ParticleEffect();
                            //fireParticle.load(Gdx.files.internal("particles/fire"), Gdx.files.internal("particles"));
                            //fireParticle.setPosition(hearth.getX() + 10, hearth.getY() + 7);
                            Sprite creature = new Sprite(Assets.creature);
                            creature.flip(true, false);
                            setElementPosition(creature, 159f, -262);
                            Float[] flt3 = new Float[]{(float) 400, 159f, -262f};
                            rotationMap.put(flt3, creature);
                            planetElements.add(creature);
                            break;
                        case "Animal":
                            for (int i = 0; i < 5; i++) {
                                float angle = MathUtils.random(0, 360);
                                Sprite bunny = new Sprite(Assets.animal);
                                setElementPosition(bunny, 154.5f, angle);
                                Float[] flt = new Float[]{(float) 500, 154.5f, angle};
                                rotationMap.put(flt, bunny);
                                planetElements.add(bunny);
                            }
                            break;
                    }

                } else {
                    resultImg.setVisible(true);
                    resultLabel.setVisible(true);
                    resultLabel.setText("No effect\ntry again!");
                    resultLabel.setPosition(LifeIsSpace.WIDTH / 2, LifeIsSpace.HEIGHT / 2 - resultLabel.getPrefHeight() / 2 + 7);
                    resultLabel.setAlignment(Align.center);
                    selectedOne = false;
                    selectedTwo = false;
                    selectedSlotOne = false;
                    selectedSlotTwo = false;
                    firstSelectionBox.setVisible(false);
                    secondSelectionBox.setVisible(false);
                    selBoxImageSelOne.setVisible(false);
                    selBoxImageSelTwo.setVisible(false);
                    leftElementName.setText("");
                    rightElementName.setText("");
                    unknownLabel.setVisible(false);
                    Assets.error.play(0.1f);
                }
            }
        });
    }

    private void setListenersToArrows(Image leftArrowSel, Image rightArrowSel) {
        leftArrowSel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (rangeMin > 0) {
                    rangeMin--;
                    rangeMax--;
                }
            }

        });

        rightArrowSel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (rangeMax < discoveredElements.size()) {
                    rangeMin++;
                    rangeMax++;
                }
            }
        });
    }

    private void setListenersToSlots(Image firstSlot, Image secondSlot, Image thirdSlot, Image fourthSlot, Image fifthSlot) {

        firstSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedSlotOne) {
                    firstSelectionBox.setPosition(firstSlotPos.x - selBoxImageOne.getPrefWidth() / 2, firstSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    firstSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 5), selectTwoPos, 1);
                }

                if (selectedSlotTwo) {
                    secondSelectionBox.setPosition(firstSlotPos.x - selBoxImageOne.getPrefWidth() / 2, firstSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    secondSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 5), selectOnePos, 2);
                }

            }
        });

        secondSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedSlotOne) {
                    firstSelectionBox.setPosition(secondSlotPos.x - selBoxImageOne.getPrefWidth() / 2, secondSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    firstSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 4), selectTwoPos, 1);
                }

                if (selectedSlotTwo) {
                    secondSelectionBox.setPosition(secondSlotPos.x - selBoxImageOne.getPrefWidth() / 2, secondSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    secondSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 4), selectOnePos, 2);
                }
            }
        });

        thirdSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedSlotOne) {
                    firstSelectionBox.setPosition(thirdSlotPos.x - selBoxImageOne.getPrefWidth() / 2, thirdSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    firstSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 3), selectTwoPos, 1);
                }

                if (selectedSlotTwo) {
                    secondSelectionBox.setPosition(thirdSlotPos.x - selBoxImageOne.getPrefWidth() / 2, thirdSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    secondSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 3), selectOnePos, 2);
                }
            }
        });

        fourthSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedSlotOne) {
                    firstSelectionBox.setPosition(fourthSlotPos.x - selBoxImageOne.getPrefWidth() / 2, fourthSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    firstSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 2), selectTwoPos, 1);
                }

                if (selectedSlotTwo) {
                    secondSelectionBox.setPosition(fourthSlotPos.x - selBoxImageOne.getPrefWidth() / 2, fourthSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                    secondSelectionBox.setVisible(true);
                    setSelectedElement(discoveredElements.get(rangeMax - 2), selectOnePos, 2);
                }
            }
        });

        fifthSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (discoveredElements.size() > 4) {
                    if (selectedSlotOne) {
                        firstSelectionBox.setPosition(fifthSlotPos.x - selBoxImageOne.getPrefWidth() / 2, fifthSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                        firstSelectionBox.setVisible(true);
                        setSelectedElement(discoveredElements.get(rangeMax - 1), selectTwoPos, 1);
                    }

                    if (selectedSlotTwo) {
                        secondSelectionBox.setPosition(fifthSlotPos.x - selBoxImageOne.getPrefWidth() / 2, fifthSlotPos.y - selBoxImageSelOne.getPrefHeight() / 2);
                        secondSelectionBox.setVisible(true);
                        setSelectedElement(discoveredElements.get(rangeMax - 1), selectOnePos, 2);
                    }
                }
            }
        });
    }

    private void setListenersToSelectionBoxes() {

        Image selectionBoxOne = (Image) selGroup.getChildren().get(0);
        Image selectionBoxTwo = (Image) selGroup.getChildren().get(1);

        selectTwoPos = selGroup.localToStageCoordinates(new Vector2(selectionBoxOne.getX() + selectionBoxOne.getPrefWidth() / 2,
                selectionBoxOne.getY() + selectionBoxOne.getPrefHeight() / 2));

        selectOnePos = selGroup.localToStageCoordinates(new Vector2(selectionBoxTwo.getX() + selectionBoxTwo.getPrefWidth() / 2,
                selectionBoxTwo.getY() + selectionBoxTwo.getPrefHeight() / 2));

        resultPos = selGroup.localToStageCoordinates(new Vector2(resultImg.getX() + resultImg.getPrefWidth() / 2,
                resultImg.getY() + resultImg.getPrefHeight() / 2));

        selectionBoxOne.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!selBoxImageSelOne.isVisible() && (secondSelectionBox.isVisible() || firstTime)) {
                    firstTime = false;
                    selectedOne = true;
                    selectedSlotOne = true;
                    selectedSlotTwo = false;
                    selBoxImageSelOne.setVisible(true);
                    resultLabel.setVisible(false);
                    resultImg.setVisible(false);
                    unknownLabel.setVisible(true);
                } else {
                    selectedOne = false;
                    selectedSlotOne = false;
                    selBoxImageSelOne.setVisible(false);
                    firstSelectionBox.setVisible(false);
                    rightElementName.setText("");
                }
            }
        });

        selectionBoxTwo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!selBoxImageSelTwo.isVisible() && (firstSelectionBox.isVisible() || firstTime)) {
                    firstTime = false;
                    selectedTwo = true;
                    selectedSlotTwo = true;
                    selectedSlotOne = false;
                    selBoxImageSelTwo.setVisible(true);
                    resultLabel.setVisible(false);
                    resultImg.setVisible(false);
                    unknownLabel.setVisible(true);
                } else {
                    selectedTwo = false;
                    selectedSlotTwo = false;
                    selBoxImageSelTwo.setVisible(false);
                    secondSelectionBox.setVisible(false);
                    leftElementName.setText("");
                }
            }
        });
    }

    private void setSelectedElement(DiscoveredElement element, Vector2 pos, int loc) {

        if (loc == 1) {
            if (elementFirst == null) {
                elementFirst = new Sprite(element.getSprite());
                discoveredElementFirst = element;
            } else {
                elementFirst.set(element.getSprite());
                discoveredElementFirst = element;
            }
            elementFirst.setPosition(pos.x - elementFirst.getWidth() / 2, pos.y - elementFirst.getHeight() / 2);
            rightElementName.setText(element.getName());
            rightElementName.setPosition(selectOnePos.x + 65, selectOnePos.y);

        } else {
            if (elementSecond == null) {
                elementSecond = new Sprite(element.getSprite());
                discoveredElementSecond = element;
            } else {
                elementSecond.set(element.getSprite());
                discoveredElementSecond = element;
            }
            elementSecond.setPosition(pos.x - elementSecond.getWidth() / 2, pos.y - elementSecond.getHeight() / 2);
            leftElementName.setText(element.getName());
            leftElementName.setPosition(selectTwoPos.x - 65, selectTwoPos.y);
        }
    }

    private void controlSelectBoxes(Group firstBox, Group secondBox) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (pos1 < 5) pos1++;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (pos1 > 1) pos1--;
        }

        switch (pos1) {
            case 1:
                firstBox.addAction(Actions.moveTo(firstSlotPos.x - selBoxImageOne.getPrefWidth() / 2, firstSlotPos.y - selBoxImageOne.getPrefHeight() / 2, 0.3f));
                break;
            case 2:
                firstBox.addAction(Actions.moveTo(secondSlotPos.x - selBoxImageOne.getPrefWidth() / 2, secondSlotPos.y - selBoxImageOne.getPrefHeight() / 2, 0.3f));
                break;
            case 3:
                firstBox.addAction(Actions.moveTo(thirdSlotPos.x - selBoxImageOne.getPrefWidth() / 2, thirdSlotPos.y - selBoxImageOne.getPrefHeight() / 2, 0.3f));
                break;
            case 4:
                firstBox.addAction(Actions.moveTo(fourthSlotPos.x - selBoxImageOne.getPrefWidth() / 2, fourthSlotPos.y - selBoxImageOne.getPrefHeight() / 2, 0.3f));
                break;
            case 5:
                firstBox.addAction(Actions.moveTo(fifthSlotPos.x - selBoxImageOne.getPrefWidth() / 2, fifthSlotPos.y - selBoxImageOne.getPrefHeight() / 2, 0.3f));
                break;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (pos2 < 5) pos2++;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A))
            if (pos2 > 1) pos2--;

        switch (pos2) {
            case 1:
                secondBox.addAction(Actions.moveTo(firstSlotPos.x - selBoxImageTwo.getPrefWidth() / 2, firstSlotPos.y - selBoxImageTwo.getPrefHeight() / 2, 0.3f));
                break;
            case 2:
                secondBox.addAction(Actions.moveTo(secondSlotPos.x - selBoxImageTwo.getPrefWidth() / 2, secondSlotPos.y - selBoxImageTwo.getPrefHeight() / 2, 0.3f));
                break;
            case 3:
                secondBox.addAction(Actions.moveTo(thirdSlotPos.x - selBoxImageTwo.getPrefWidth() / 2, thirdSlotPos.y - selBoxImageTwo.getPrefWidth() / 2, 0.3f));
                break;
            case 4:
                secondBox.addAction(Actions.moveTo(fourthSlotPos.x - selBoxImageTwo.getPrefWidth() / 2, fourthSlotPos.y - selBoxImageTwo.getPrefHeight() / 2, 0.3f));
                break;
            case 5:
                secondBox.addAction(Actions.moveTo(fifthSlotPos.x - selBoxImageTwo.getPrefWidth() / 2, fifthSlotPos.y - selBoxImageTwo.getPrefHeight() / 2, 0.3f));
                break;
        }

    }


    private void endGame() {
        terraformLabel.addAction(Actions.fadeOut(3));
        imageBottomGroup.addAction(Actions.fadeOut(3));
        infoLabel.addAction(Actions.fadeOut(3));
        selGroup.addAction(Actions.fadeOut(3));
        selGroup.getListeners().clear();
        for (int i = 0; i < selGroup.getChildren().size; i++)
            selGroup.getChildren().get(i).clearListeners();

        resultSprite.setAlpha(imageBottomGroup.getColor().a);
        slotGroup.addAction(Actions.fadeOut(3));
        for (int i = 0; i < slotGroup.getChildren().size; i++)
            slotGroup.getChildren().get(i).clearListeners();
        mixImage.setVisible(false);
        clickLabel.setVisible(false);
        setLabel.setVisible(false);
        resultImg.setVisible(false);

        resultLabel.addAction(Actions.sequence(Actions.fadeOut(3), Actions.run(new Runnable() {
            @Override
            public void run() {
                congratsLabel.addAction(Actions.sequence(Actions.delay(3), Actions.fadeIn(3)));
            }
        })));

    }

    public void draw(SpriteBatch batch) {

        //update points
        terraformLabel.setText(String.format("TERRAFORM %d/15", terraformPoints));

        if (terraformPoints == 15)
            endGame();

        angle += speed * Gdx.graphics.getDeltaTime();

        planetImage.setRotation(angle);

        float planetAngle = planetImage.getRotation();

        for (Map.Entry sprite : rotationMap.entrySet()) {

            Sprite currentSprite = ((Sprite) sprite.getValue());


            Float[] flt = (Float[]) sprite.getKey();

            float angle = flt[2];

            float dX = flt[1] * MathUtils.cosDeg(angle + planetAngle) + planetImage.getX() + planetImage.getWidth() / 2;
            float dY = flt[1] * MathUtils.sinDeg(angle + planetAngle) + planetImage.getY() + planetImage.getHeight() / 2;


            currentSprite.setPosition(dX - currentSprite.getWidth() / 2, dY - currentSprite.getHeight() / 2);
            currentSprite.setRotation(angle + planetAngle - 90);

        }

        if (selectedOne || selectedTwo) {
            clickLabel.setVisible(false);
            setLabel.setVisible(true);
        } else {
            if (!resultLabel.isVisible()) {
                clickLabel.setVisible(true);
            }
            setLabel.setVisible(false);
            firstTime = true;
            elementFirst = null;
            elementSecond = null;
        }

        if (selectedOne && selectedTwo)
            mixImage.setVisible(true);
        else
            mixImage.setVisible(false);

        batch.begin();
        if (planetAtmos != null)
            planetAtmos.draw(batch);
        for (Sprite planetElement : planetElements) {
            planetElement.draw(batch);
        }
        if (fireParticle != null) {
            fireParticle.draw(batch, Gdx.graphics.getDeltaTime());
        }
        planetImage.draw(batch);
        batch.end();

        stageTwoUI.act();
        stageTwoUI.draw();

        batch.begin();
        if (elementFirst != null)
            elementFirst.draw(batch);


        if (elementSecond != null)
            elementSecond.draw(batch);

        if (resultSprite != null && resultLabel.isVisible() && !resultImg.isVisible())
            resultSprite.draw(batch);


        batch.end();

        for (DiscoveredElement element : discoveredElements)
            element.getSprite().setAlpha(slotGroup.getColor().a);

        for (int i = rangeMin; i < rangeMax; i++) {

            switch (rangeMax - i) {
                case 5:
                    discoveredElements.get(i).setPosition(firstSlotPos);
                    discoveredElements.get(i).draw(batch);
                    break;
                case 4:
                    discoveredElements.get(i).setPosition(secondSlotPos);
                    discoveredElements.get(i).draw(batch);
                    break;
                case 3:
                    discoveredElements.get(i).setPosition(thirdSlotPos);
                    discoveredElements.get(i).draw(batch);
                    break;
                case 2:
                    discoveredElements.get(i).setPosition(fourthSlotPos);
                    discoveredElements.get(i).draw(batch);
                    break;
                case 1:
                    if (discoveredElements.size() > 4) {
                        discoveredElements.get(i).setPosition(fifthSlotPos);
                        discoveredElements.get(i).draw(batch);
                    }
                    break;
            }
        }


    }
}
