package ua.gram.munhauzen.interaction.cannons.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.interaction.cannons.CannonsStory;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryImage;
import ua.gram.munhauzen.interaction.cannons.actor.BurnWorm;
import ua.gram.munhauzen.interaction.cannons.actor.EatWorm;
import ua.gram.munhauzen.interaction.cannons.actor.FloodWorm;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonsImageFragment extends InteractionFragment {

    private final CannonsInteraction interaction;
    FloodWorm floodWorm;
    BurnWorm burnWorm;
    EatWorm eatWorm;
    public FragmentRoot root;
    public Group items;
    public BackgroundImage backgroundImage;

    public CannonsImageFragment(CannonsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

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

        burnWorm.setVisible(false);
        eatWorm.setVisible(false);
        floodWorm.setVisible(false);

        items = new Group();

        root = new FragmentRoot();
        root.addContainer(backgroundImage);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        if (interaction.storyManager == null) return;

        CannonsStory story = interaction.storyManager.story;

        if (story != null && story.currentScenario != null) {

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

                        root.addContainer(new Container<>(burnWorm));
                    }

                    burnWorm.updateBounds();
                } else {
                    burnWorm.remove();
                }

                if (hasFloodWorm) {
                    if (floodWorm.getParent() == null) {

                        floodWorm.setVisible(true);

                        root.addActor(new Container<>(floodWorm));
                    }

                    floodWorm.updateBounds();
                } else {
                    floodWorm.remove();
                }

                if (hasEatWorm) {
                    if (eatWorm.getParent() == null) {

                        eatWorm.setVisible(true);

                        root.addActor(new Container<>(eatWorm));
                    }

                    eatWorm.updateBounds();
                } else {
                    eatWorm.remove();
                }


            } else {
                floodWorm.remove();
                eatWorm.remove();
                burnWorm.remove();
            }
        }

    }
}
