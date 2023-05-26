import javax.swing.JPanel;
import java.awt.BorderLayout;
public class SolitairePanel extends JPanel {
    private GameComponent gameComponent;
    private TextPanel textPanel;

    public SolitairePanel(){
        gameComponent = new GameComponent(1000, 600);
        textPanel = new TextPanel(gameComponent);
        init();
    }

    private void init() {
        // set layout to use a border layout
        setLayout(new BorderLayout()); // INHERITED METHOD from a class in JPanel's inheritance hierarchy!

        // add the shape canvas to the center section of the border layout
        add("Center", gameComponent); // INHERITED METHOD from a class in JPanel's inheritance hierarchy!

        // add the button panel to the north section of the border layout
        add("South", textPanel); // INHERITED METHOD from a class in JPanel's inheritance hierarchy!
    }
}
