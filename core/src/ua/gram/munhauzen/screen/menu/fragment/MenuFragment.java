package ua.gram.munhauzen.screen.menu.fragment;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.Fragment;

public abstract class MenuFragment extends Fragment {

    public final MenuScreen screen;

    protected MenuFragment(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {
        screen.game.stopCurrentSfx();
    }

    @Override
    public void dispose() {
        super.dispose();

        screen.game.stopCurrentSfx();
    }
}
