package lando.systems.ld44.entities;

import lando.systems.ld44.screens.GameScreen;

public class Nickel extends Enemy {
    public Nickel(GameScreen screen) {
        super(screen, screen.assets.nickelAnimation, screen.assets.nickelPickupAnimation, 30, 0.05f);
    }
}
