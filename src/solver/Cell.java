package solver;

public class Cell {
    
    private final boolean isModifiable;
    private int value;
    
    public Cell(boolean isModifiable, int value) {
        this.isModifiable = isModifiable;
        this.value = value;
    }

    public boolean getIsModifiable() {
        return isModifiable;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) throws Exception {
        if (isModifiable)
            this.value = value;
        else {
            throw new Exception("Illegal attempt to modify cell");
        }
    }

}
