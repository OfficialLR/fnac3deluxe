package com.fnac3.deluxe.core.enemy;

import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.AudioClass;

public class ClassicCat extends AbstractEnemy {

    @Override
    public void reset(Data data, int state, int side, int type, int difficulty){
        super.reset(data, state, side, type, difficulty);

    }

    @Override
    public void update(Data data, AudioClass audioClass) {
        if (!isActive()) return;
        int logic;

    }

    @Override
    protected void setHitbox(Data data) {

    }

    @Override
    protected void setJumpscare() {
        String name = "Cat";
        String texture = "game/enemy/ClassicCat/Shadow/Jumpscare/Jumpscare";
        String sound = "shadowJumpscare";
        float timer = 0.95f;
        int frameTarget = -1;
        float pitch = 0.95f;
        Game.setJumpscare(name, texture, sound, timer, frameTarget, pitch);
    }
}
