import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class MainMenu extends JFrame {

    private static final long serialVersionUID = 2451829341034438685L;

    public static JButton inputButton = new JButton("Send");
    public static JTextArea editTextArea = new JTextArea("Type Here!");
    public static JTextArea uneditTextArea = new JTextArea();

    public MainMenu(String title) {
        //SET LAYOUT MANAGER (How it arranges components)
        setLayout(new BorderLayout());
        //////CREATE SWING COMPONENTS////////////
        //OUTPUT TEXT AREA
        uneditTextArea.setEditable(false);

        //INPUT TEXT AREA
        editTextArea.setBackground(Color.BLUE);
        editTextArea.setForeground(Color.WHITE);

        //SET CONTENT PANE
        Container c = getContentPane();

        //ADD COMPONENTS TO CONTENT PANE        
        c.add(uneditTextArea, BorderLayout.CENTER);
        c.add(editTextArea, BorderLayout.SOUTH);
        c.add(inputButton, BorderLayout.WEST);

        MainMenu.inputButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String str = editTextArea.getText();
                editTextArea.setText(" ");
                System.out.println(str);                
            }
        });
    }
}