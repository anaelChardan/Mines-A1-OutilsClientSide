package felix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Classe de connexion au chat. 
 * 
 * @version 2.0.0.etu
 * @author Matthias Brun
 * 
 */
public class Connexion
{
	/**
	 * Encodage du protocole.
	 */
	public static final String ENCODAGE = "UTF8";
	
	/**
	 * Socket de connexion au chat.
	 */
	private Socket socket;

	/**
	 * Buffer d'écriture sur la socket de connexion au chat.
	 */
	private BufferedWriter bufferEcriture;

	/**
	 * Buffer de lecture de la socket de connexion au chat.
	 */
	private BufferedReader bufferLecture;

	/**
	 * La limite haute d'un port
	 */
	private Integer portLimitHigh = 65535;

	/**
	 * La limite basse d'un port
	 */
	private Integer portLimitLow = 1;

	/**
	 * La limite haute de la partie d'une IP
	 */
	private Integer partIpLimitHigh = 255;

	/**
	 * La limite basse de la partie d'une IP
	 */
	private Integer partIpLimitLow = 0;

	/**
	 * Constructeur par défaut de la classe connexion
	 *
	 */
	public Connexion()
	{	}

	/**
	 * @param adresse
	 * @param port
	 * @throws IOException
     */
	public void connect(String adresse, Integer port) throws IOException {
		try {
			// Initialisation de la socket.
			this.socket = new Socket(adresse, port);

			// Initialisation des buffers de lecture et d'écriture sur la socket.
			this.bufferLecture = new BufferedReader(
					new InputStreamReader(this.socket.getInputStream(), Connexion.ENCODAGE));
			this.bufferEcriture = new BufferedWriter(
					new OutputStreamWriter(this.socket.getOutputStream(), Connexion.ENCODAGE));
		}
		catch (IOException ex) {
			System.err.println("Problème de connexion au chat.");
			throw ex;
		}
	}

	/**
	 * Envoi d'un message au chat.
	 *
	 * @param message le message à envoyer au chat.
	 *
	 * @throws IOException les exceptions d'entrée/sortie.
	 */
	public void ecrire(String message) throws IOException
	{
		try {
			this.bufferEcriture.write(message, 0, message.length());
			this.bufferEcriture.newLine();
			this.bufferEcriture.flush();
		}
		catch (IOException ex) {
			System.err.println("Problème d'envoi de message au chat.");
			throw ex;
		}
	}

	/**
	 * Réception d'un message du chat.
	 * 
	 * @return le message provenant du chat.
	 *
	 * @throws IOException les exceptions d'entrée/sortie.
	 *
	 */
	public String lire() throws IOException
	{
		String message = null;

		try {
			message = this.bufferLecture.readLine();

			if (System.getProperty("os.name") != null && System.getProperty("os.name").startsWith("Mac")) {
				if (message != null)
				{
					String[] messageParsed = message.split(" > ");
					if (messageParsed.length == 2)
					{
						displayNotification("Nouveau message", messageParsed[0] + " a écrit " + messageParsed[1]);
					}
				}
			}

			if (message == null) {
				System.err.println("Fin d'émission du serveur.");
				throw new EOFException();
			}
		} 
		catch (EOFException ex) {
			System.err.println("Flux d'émission du serveur terminé.");
			throw ex;
		}
		catch (IOException ex) {
			System.err.println("Problème de lecture d'un message du chat.");
			throw ex;
		}
		return message;
	}

	/**
	 * Fermeture de la connexion.
	 */
	public void ferme()
	{
		try {
			this.bufferEcriture.close();
		}
		catch (IOException ex) {
			System.err.println("Problème de fermeture du buffer d'écriture sur la socket.");
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Affiche une notification sur le dashboard d'un Mac
	 *
	 * @param title Le titre de la notifiaction
	 * @param message Le message de la notification
	 * @throws IOException
     */
	public void displayNotification(String title, String message) throws IOException {
		Runtime.getRuntime().exec(new String[] { "osascript", "-e", "display notification \"" + message + "\" with title \"" + title + "\""});
	}

	public boolean isIPValid(String ip) {
		if (!contains3SeparatedDots(ip)) {
			return false;
		}

		for (String splittedString : this.getSplittedStringByDots(ip))
		{
			if (!this.isPartOfIpValide(splittedString))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Test si un string contient bien 4 bloc de characteres séparés par des points
	 * @param ip
	 * @return
     */
	protected boolean contains3SeparatedDots(String ip)
	{
		String[] splittedStrings = this.getSplittedStringByDots(ip);

		if (splittedStrings.length != 4) {
			return false;
		}

		for (String splittedString : splittedStrings) {
			if (splittedString.isEmpty())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Test si une partie d'un IP est valide (0 <= integer <= 255)
	 * @param partOfIP
	 * @return
     */
	protected boolean isPartOfIpValide(String partOfIP)
	{
		if (!isInteger(partOfIP))
		{
			return false;
		}

		Integer intPartOfIP = Integer.parseInt(partOfIP);

		return !(intPartOfIP < partIpLimitLow || partIpLimitHigh < intPartOfIP);
	}

	/**
	 * Test si un port est valide
	 *
	 * @param port Le port
	 * @return Si le port est valide
     */
	public boolean isPortValid(String port)
	{
		if (!isInteger(port))
		{
			return false;
		}

		Integer intPort = Integer.parseInt(port);

		return !(intPort < portLimitLow || portLimitHigh < intPort);
	}

	/**
	 * Test si une methode est un entier
	 * Ce n'est pas la methode la plus performante mais elle fonctione
	 *
	 * {@see http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java}
	 *
	 * @param s le string a test
	 * @return
	 */
	protected boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return false;
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	/**
	 * Split un string par les points
	 * Pas besoin de tester, c'est un wrapper
	 * @param stringToSplit
	 * @return
     */
	protected String[] getSplittedStringByDots(String stringToSplit)
	{
		return stringToSplit.split("\\.");
	}
}

