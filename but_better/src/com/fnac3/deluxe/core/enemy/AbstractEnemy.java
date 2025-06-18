package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;

public abstract class AbstractEnemy {
	protected float timer1;
	protected float timer2;
	protected float timer3;
	protected float frame;
    protected int interval1;
    protected int interval2;
    protected int interval3;
    protected int interval4;
    protected int interval5;
	protected float twitchFrame;
	protected float targetFrame;
    protected boolean move;
    protected int position;
	protected int side;
    protected int reservedSide;
	protected int state;
	protected boolean lock;
	protected boolean hovered;
	protected int type;
	protected int difficulty;

	protected final int[] hitbox = new int[2];
	protected float hitboxSize;
    protected final int[] hitboxDimension = new int[2];

	public abstract void update(Data data, AudioClass audioClass);

    protected abstract void setHitbox(Data data);

    protected abstract void setJumpscare();

    public void reset(Data data, int state, int side, int type, int difficulty){
        this.type = type;
        this.side = side;
        this.difficulty = difficulty;
        this.state = state;
        hovered = false;
        setHitbox(data);
    }

    public void resetDoor(float awayTimer, float peekTimer, boolean leave) {
        timer1 = awayTimer;
        timer2 = peekTimer;
        if (leave) frame = 18;
        else frame = 0;
        targetFrame = 0;
    }

    public void resetAttack(float killTimer, float flashTimer, float healthBar) {
        timer1 = killTimer;
        timer2 = flashTimer;
        timer3 = healthBar;
        lock = false;
        position = 0;
        frame = 1;
        targetFrame = 1;
        move = false;
    }

    public void resetBed(float killTimer, float bedTimer, float tapeTimer, int targetFrame) {
        timer1 = killTimer;
        timer2 = bedTimer;
        timer3 = tapeTimer;
        frame = 0;
        this.targetFrame = targetFrame;
    }

    public void resetCrouch(float killTimer, float healthBar) {
        timer1 = killTimer;
        timer2 = healthBar;
    }

    public boolean hitboxCircleCollision(){
        float lineA = Math.abs(Player.flashlightPosition[0] - hitbox[0]);
        float lineB = Math.abs(Player.flashlightPosition[1] - hitbox[1]);
        return Math.hypot(lineA, lineB) <= hitboxSize && Player.flashlightAlpha > 0;
    }

    public boolean hitboxRectangleCollision(){
        float mx = Player.flashlightPosition[0];
        float my = Player.flashlightPosition[1];

        return Player.flashlightAlpha > 0
                && mx >= hitbox[0]
                && my >= hitbox[1]
                && mx <= hitbox[0] + hitboxDimension[0]
                && my <= hitbox[1] + hitboxDimension[1];
    }

    protected void twitchUpdate(){
        var time = Gdx.graphics.getDeltaTime() * 30;
        if (hovered) {
            twitchFrame += time;
            if (twitchFrame >= 2) twitchFrame -= 2;
        } else {
            twitchFrame = 0;
        }
    }

	protected int doorUpdate() {
        if (Player.freeze) return 0;
        var awayTimer = timer1;
        var peekTimer = timer2;
        var time = Gdx.graphics.getDeltaTime();
        var speed = time * 25;
        int returnValue = 0;

        if (hitboxCircleCollision() && (int) frame == 13) {
            returnValue = 2;
        }

        if (awayTimer > 0) {
            awayTimer -= time;
            if (awayTimer <= 0) {
                peekTimer += awayTimer;
                if (peekTimer <= 0.175f) peekTimer = 0.175f;
                returnValue = 1;
            } else {
                frame -= speed;
                if (frame <= 0) frame = 0;
            }
        } else {
            peekTimer -= time;
            if (peekTimer <= 0) returnValue = 3;
            frame += speed;
            if (frame >= 13) frame = 13;
        }

        timer1 = awayTimer;
        timer2 = peekTimer;
        return returnValue;
	}

    protected int attackUpdate(){
        var killTimer = timer1;
        var flashTimer = timer2;
        var healthBar = timer3;
        var time = Gdx.graphics.getDeltaTime();
        int returnValue = 0;

        if (lock) hovered = hitboxCircleCollision();

        twitchUpdate();

        if (lock) {
            if (!Player.freeze && !move) {
                if (!hovered) killTimer -= time / 1.3f;
                else {
                    killTimer += time;
                    if (killTimer > 1) killTimer = 1;
                }
            }
            if (hovered) {
                flashTimer -= time;
                healthBar -= time;
            }
            if (flashTimer <= 0) {
                if (healthBar > 0) {
                    move = true;
                    hovered = false;
                } else {
                    returnValue = 2;
                }
            }
            if (!Player.snapPosition && healthBar > 0){
                Player.inititiateSnapPosition(side, false);
                if (Player.snapPosition) {
                    killTimer += time;
                    returnValue = 4;
                }
            }
        } else {
            if (!Player.freeze) killTimer -= time;
            if (!Player.snapPosition && healthBar > 0){
                lock = Player.inititiateSnapPosition(side, false);
                if (lock) {
                    killTimer += time;
                    returnValue = 4;
                }
            }
        }

        if (killTimer <= 0) returnValue = 3;
        else if (move) returnValue = 1;

        timer1 = killTimer;
        timer2 = flashTimer;
        timer3 = healthBar;

        return returnValue;
    }

    protected void blackout(){
        lock = false;
        Player.freeze = true;
        Player.snapPosition = false;
        Player.setBlackness(3, 6, 0.5f);
    }

    protected int bedUpdate(Data data, AudioClass audioClass){
        var killTimer = timer1;
        var bedTimer = timer2;
        var tapeTimer = timer3;
        var time = Gdx.graphics.getDeltaTime();
        int returnValue = 0;

        if (Player.room == 1 && (int) Player.turningPosition == 0) {
            killTimer -= time;
            if (hitboxRectangleCollision()){
                Player.foundUnderBed = true;
            }
        }
        if (Player.room == 2 && (int) Player.turningPosition == 0) {
            if (frame < targetFrame) frame += time * 30;
            if (frame > targetFrame) frame = targetFrame;
        } else if (frame < targetFrame && (data.hardCassette || Player.tape.isPlaying())) {
            if (tapeTimer > 0) {
                tapeTimer -= time;
                if (tapeTimer <= 0) {
                    Player.tapeWeasel(audioClass);
                }
            }
            if (frame > 0) frame = targetFrame;
        }
        bedTimer -= time;

        if (killTimer <= 0) returnValue = 2;
        else if (bedTimer <= 0) returnValue = 1;

        timer1 = killTimer;
        timer2 = bedTimer;
        timer3 = tapeTimer;
        return returnValue;
    }

    protected int crouchUpdate(){
        var killTimer = timer1;
        var healthBar = timer2;
        var time = Gdx.graphics.getDeltaTime();
        int returnValue = 0;

        if (healthBar > 0) {
            hovered = hitboxCircleCollision();

            if (!hovered){
                killTimer -= time;
                if (killTimer <= 0) returnValue = 1;
            } else {
                healthBar -= time;
                if (healthBar <= 0) returnValue = 2;
            }
        }

        twitchUpdate();

        timer1 = killTimer;
        timer2 = healthBar;
        return returnValue;
    }

	public float getTimer1() {
		return timer1;
	}

    public int getState() {
		return state;
	}

    public int getSide() {
        return side;
    }

	public boolean isHovered() {
		return hovered;
	}

    public int[] getHitbox(){
        return hitbox;
    }

    public float getHitboxSize(){
        return hitboxSize;
    }

    public boolean isActive(){
        return difficulty > 0;
    }

    public boolean isAttack(){
        return lock && (state == 1 || (state == 3 && timer2 <= 0));
    }

    public int getType() {
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
}