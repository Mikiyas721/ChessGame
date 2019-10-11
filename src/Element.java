import javafx.scene.control.Button;
import javafx.scene.text.Font;



public class Element {
    private String name;
    private int atomicNumber;
    private double massNumber;
    private Button button;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
    }

    public double getMassNumber() {
        return massNumber;
    }

    public void setMassNumber(double massNumber) {
        this.massNumber = massNumber;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Element(String name, int atomicNumber, double massNumber, Button button) {
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.massNumber = massNumber;
        this.button = button;
        this.button.setFont(new Font(20));
        this.button.setText(name);
        this.button.setFont(new Font(10));
        this.button.setText(atomicNumber+"\n"+massNumber);
    }

}
