package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class SimpleFragment extends Fragment {

    final Actor root;

    public SimpleFragment(Actor root) {
        this.root = root;
    }

    @Override
    public Actor getRoot() {
        return root;
    }
}
