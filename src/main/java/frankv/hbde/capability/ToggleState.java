package frankv.hbde.capability;

public class ToggleState implements IToggleState{
    private int[] slotsDEState = new int[9];

    @Override
    public void setToggleDEState(int[] slots) {
        this.slotsDEState = slots;
    }

    @Override
    public void toggleDEState(int select) {
        if(this.slotsDEState[select] == 0 ){
            this.slotsDEState[select] = 1;
        } else {
            this.slotsDEState[select] = 0;
        }
    }

    @Override
    public int[] getToggleDEState() {
        return this.slotsDEState;
    }
}
