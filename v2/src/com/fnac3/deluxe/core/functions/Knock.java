package com.fnac3.deluxe.core.functions;

import com.badlogic.gdx.Gdx;
import com.fnac3.deluxe.core.util.AudioClass;

import java.util.Arrays;

public class Knock {
	private final int[] amounts = new int[3];
	private final int[] types = new int[3];
	private final float[] timers = new float[3];
	private final float[] targets = new float[3];

	public void reset(){
        Arrays.fill(amounts, 0);
		Arrays.fill(types, 0);
        Arrays.fill(timers, 0);
		Arrays.fill(targets, 0);
    }

	public void update(AudioClass audioClass) {
		for (int i = 0; i < 3; i++) {
			if (types[i] == -1) {
				audioClass.play("spotted");
				types[i] = 0;
			}

			if (timers[i] > 0) {
				timers[i] -= Gdx.graphics.getDeltaTime();
			}
			if (timers[i] > 0 || amounts[i] == 0) continue;
			amounts[i]--;
			timers[i] = targets[i];
            if (types[i] == 1) audioClass.play("knock");
            else if (types[i] == 2) audioClass.play("hard_knock");
            else if (types[i] == 3) audioClass.play("vinnieKnock");
		}
	}

	public void retreat(int door) {
		amounts[door] = 0;
		types[door] = -1;
		targets[door] = 0;
	}

	public void set(int door, int amount, int type, float target) {
		amounts[door] = amount;
		types[door] = type;
		targets[door] = target;
	}
}