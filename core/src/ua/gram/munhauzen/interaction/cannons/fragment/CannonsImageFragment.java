package ua.gram.munhauzen.interaction.cannons.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.interaction.cannons.CannonsStory;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryImage;
import ua.gram.munhauzen.interaction.cannons.actor.BurnWorm;
import ua.gram.munhauzen.interaction.cannons.actor.EatWorm;
import ua.gram.munhauzen.interaction.cannons.actor.FloodWorm;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonsImageFragment extends Fragment {

    private final CannonsInteraction interaction;
    FloodWorm floodWorm;
    BurnWorm burnWorm;
    EatWorm eatWorm;
    public Group root, items;
    public Image background;
    public Table backgroundTable;
    public float backgroundWidth, backgroundHeight;

    public CannonsImageFragment(CannonsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        background = new FitImage();

        floodWorm = new FloodWorm(
                interaction.assetManager.get("cannons/inter_cannons_flood_worm.png", Texture.class),
                this
        );

        burnWorm = new BurnWorm(
                interaction.assetManager.get("cannons/inter_cannons_burn_worm.png", Texture.class),
                this
        );

        eatWorm = new EatWorm(
                interaction.assetManager.get("cannons/inter_cannons_eat_worm.png", Texture.class),
                this
        );

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).right().expand().fill();

        burnWorm.setVisible(false);
        eatWorm.setVisible(false);
        floodWorm.setVisible(false);

        items = new Group();

        root = new Group();
        root.addActor(backgroundTable);

        root.addListener(new ActorGestureListener() {

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                try {
                    interaction.gameScreen.stageInputListener.clicked(event, x, y);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                moveBackground(deltaX);
            }

            private void moveBackground(float deltaX) {
                try {

                    CannonsStory story = interaction.storyManager.story;

                    if (story.currentScenario.currentImage == null) return;

                    float newX = background.getX() + deltaX;
                    float currentWidth = story.currentScenario.currentImage.width;

                    float leftBound = -currentWidth + MunhauzenGame.WORLD_WIDTH;
                    float rightBound = 0;

                    if (leftBound < newX && newX < rightBound) {
                        background.setX(newX);
                    }

                    if (background.getX() > rightBound) background.setX(rightBound);
                    if (background.getX() < leftBound) background.setX(leftBound);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        CannonsStory story = interaction.storyManager.story;

        if (story.currentScenario != null) {

            CannonsStoryImage image = story.currentScenario.currentImage;
            if (image != null && image.withWorms) {

                Inventory burnWormItem = InventoryRepository.find(interaction.gameScreen.game.gameState, "BURN_WORM");
                Inventory floodWormItem = InventoryRepository.find(interaction.gameScreen.game.gameState, "FLOOD_WORM");
                Inventory eatWormItem = InventoryRepository.find(interaction.gameScreen.game.gameState, "EAT_WORM");

                boolean hasBurnWorm = interaction.gameScreen.game.inventoryService.isInInventory(burnWormItem);
                boolean hasFloodWorm = interaction.gameScreen.game.inventoryService.isInInventory(floodWormItem);
                boolean hasEatWorm = interaction.gameScreen.game.inventoryService.isInInventory(eatWormItem);

                if (hasBurnWorm) {
                    if (burnWorm.getParent() == null) {

                        burnWorm.setVisible(true);

                        root.addActor(burnWorm);
                    }

                    burnWorm.updateBounds();
                } else {
                    burnWorm.remove();
                }

                if (hasFloodWorm) {
                    if (floodWorm.getParent() == null) {

                        floodWorm.setVisible(true);

                        root.addActor(floodWorm);
                    }

                    floodWorm.updateBounds();
                } else {
                    floodWorm.remove();
                }

                if (hasEatWorm) {
                    if (eatWorm.getParent() == null) {

                        eatWorm.setVisible(true);

                        root.addActor(eatWorm);
                    }

                    eatWorm.updateBounds();
                } else {
                    eatWorm.remove();
                }


            }
        }

    }
}
