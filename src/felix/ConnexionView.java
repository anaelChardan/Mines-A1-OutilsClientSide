package felix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ConnexionView extends JFrame {

    /**
     * Connexion
     */
    private Connexion connexion = new Connexion();


    /**
     * Label d'information
     */
    private JLabel infoLabel = new JLabel("");

    /**
     * Bouton de connexion
     */
    private JButton connexionButton = new JButton(Felix.getConfig("BUTTON_LABEL"));

    private JTextField ipField;

    private JTextField portField;


    public ConnexionView(String title, Integer height, Integer width) {
        // Construction de la fenêtre.
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(width, height);

        setTitle(title);
        setResizable(false);
        loadGraphicsElement();
        this.display();
    }

    private void loadGraphicsElement()
    {
        Box verticalBox = Box.createVerticalBox();
        //Init IP field
        ipField = new JTextField(Felix.getConfig("ADRESSE_CHAT"));
        ipField.setEditable(true);
        ipField.setSize(this.getWidth(), 10);
        ipField.requestFocus();

        //Init Port field
        portField = new JTextField(Felix.getConfig("PORT_CHAT"));
        portField.setEditable(true);
        portField.setSize(this.getWidth(), 10);

        verticalBox.add(new JLabel(Felix.getConfig("CONNEXION_WINDOW_LABEL")));
        verticalBox.add(Box.createRigidArea(new Dimension(0,30)));
        verticalBox.add(new JLabel(Felix.getConfig("IP_LABEL")));
        verticalBox.add(Box.createRigidArea(new Dimension(0,10)));
        verticalBox.add(ipField);
        verticalBox.add(Box.createRigidArea(new Dimension(0,30)));
        verticalBox.add(new JLabel(Felix.getConfig("PORT_LABEL")));
        verticalBox.add(Box.createRigidArea(new Dimension(0,10)));
        verticalBox.add(portField);
        verticalBox.add(Box.createRigidArea(new Dimension(0,30)));


        connexionButton.addActionListener(new connexionButtonListener());
        verticalBox.add(connexionButton);


        verticalBox.add(infoLabel);

        this.getContentPane().add(verticalBox);
    }

    /**
     * Méthode permettant de fermer la fenêtre
     */
    private void close()
    {
        this.dispose();
    }

    /**
     * Méthode permettant d'ouvrir la fenëtre
     */
    private void display()
    {
        this.setVisible(true);
    }

    /**
     * Class interne qui ecoute le bouton de connexion
     */
    private class connexionButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String ipAddr = ipField.getText();
            String port = portField.getText();
            infoLabel.setText(getFormatedStringFromConfig("MESSAGE_CONNEXION_IN_PROGRESS", ipAddr, port));

            if (!connexion.isPortValid(port))
            {
                infoLabel.setText(getFormatedStringFromConfig("MESSAGE_CONNEXION_ERROR", ipAddr, port));

                return;
            }

            if (!connexion.isIPValid(ipAddr))
            {
                infoLabel.setText(getFormatedStringFromConfig("MESSAGE_CONNEXION_ERROR", ipAddr, port));

                return;
            }

            try {
                connexion.connect(ipAddr, Integer.parseInt(port));
            } catch (IOException e1) {
                infoLabel.setText(getFormatedStringFromConfig("MESSAGE_CONNEXION_ERROR", ipAddr, port));

                return;
            }
            close();

            Fenetre fenetre = new Fenetre(
                parseConfigInt("FENETRE_LARGEUR"),
                parseConfigInt("FENETRE_HAUTEUR"),
                Felix.getConfig("FENETRE_TITRE"),
                connexion
            );

            fenetre.setVisible(true);
        }
    }

    /**
     * Wrapper vers la methode string format pour compléter un string avec des informations de connexion
     * @param key la clé de configuration
     * @param ipAddr l'adresse IP
     * @param port le port
     * @return
     */
    private String getFormatedStringFromConfig(String key, String ipAddr, String port)
    {
        return String.format(Felix.getConfig(key), ipAddr, port);
    }

    /**
     * Wrapper qui retourne un entier parsé depuis un élément de la configuration
     *
     * @param key la clé de configuration
     * @return
     */
    private Integer parseConfigInt(String key)
    {
        return Integer.parseInt(Felix.getConfig(key));
    }
}
