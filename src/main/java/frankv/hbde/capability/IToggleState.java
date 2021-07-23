package frankv.hbde.capability;

public interface IToggleState {
    void setToggleDEState(int slots[]);

    void toggleDEState(int select);

    int[] getToggleDEState();

}
