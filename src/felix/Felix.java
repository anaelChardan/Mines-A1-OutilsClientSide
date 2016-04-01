package felix;

import java.util.ResourceBundle;

/**
 * Classe principale de Felix. 
 * 
 * @version 2.0.0.etu
 * @author Matthias Brun
 * @author Anaël CHARDAN
 *
 */
public final class Felix
{
	/**
	 * Fichier de configuration de Felix.
	 */
	public static final ResourceBundle CONFIGURATION = ResourceBundle.getBundle("Configuration");

	/**
	 * Constructeur privé de Felix.
	 * 
	 * Ce constructeur privé assure la non-instanciation de Felix dans un programme.
	 * (Felix est la classe principale du programme Felix)
	 */
	private Felix() 
	{
		// Constructeur privé pour assurer la non-instanciation de Felix.
	}

	/**
	 * Main du programme.
	 *
	 * <p>
	 * Cette fonction main lance le programme Felix qui consiste à :
	 * <ul>
	 * <li> Ouvrir une connexion avec le chat.
	 * <li> Ouvrir une fenêtre de communication via cette connexion.
	 * </ul>
	 * </p>
	 *
	 * @param args aucun argument attendu.
	 */
	public static void main(String[] args)
	{
		System.out.println("Felix v.2.0");

		try {
			ConnexionView connexionView = new ConnexionView("Felix", 500, 350);
		} 
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Wrapper de la config
	 *
	 * @param key la clé de la config
	 * @return la valeur associé à la clé
     */
	public static String getConfig(String key)
	{
		return CONFIGURATION.getString(key);
	}
}

