import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
/** 
 * This class will make the frames onto the screen and start the game
 * @author harman Randhawa
 *
 */
public class GameFrame extends JFrame{
   private final static Card[] card = new Card[10];
   private static JButton[] buttons = new JButton[10];
   private static JFrame frame ;
   private static JPanel mainPanel;
   private static JPanel upperPanel;
   private static JPanel commandPanel;
   private static JPanel buttonPanel;
   private static JTextArea msg;
   private static JLabel player1Score;
   private static JLabel player2Score;
   private static int currentPlayer;
   public final static String DEFAULT_FILE_NAME = "Default.jpg";
   
    /**
	 * Main method that will initialize the game
	 * @param args command line argument
	 */
	public static void main ( String[] args ){
      createCards();
      frame = createComponents();
		frame.setSize(700,700);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle(" MEMORY GAME  ");
		frame.setVisible(true );
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * This method will create all the components of the screen and then add them to the frame
	 * @return JFrame on which all the components will be added 
	 */
	private static JFrame createComponents() {
		JFrame frame = new JFrame();
		mainPanel = new JPanel();
		upperPanel = createUpperPanel();
		buttonPanel = createButtons();
		commandPanel = createCommandPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(buttonPanel,BorderLayout.CENTER);
		mainPanel.add(upperPanel,BorderLayout.NORTH);
		mainPanel.add(commandPanel,BorderLayout.SOUTH);
		frame.add(mainPanel);
		return frame;
	}
	/**
	 * This method creates a jpanel to add to jframe. 
	 * It will have a scroll menu that the user can select for all the possible commands send from server
	 * @return JPanel with all the possible commands to send to player from sever
	 */
	private static JPanel createCommandPanel() {
		JPanel panel = new JPanel();
		final JComboBox commands = new JComboBox();
		commands.addItem("Turn Player" );
		commands.addItem("Evaluate 2 cards");
		commands.addItem("Flip cards back ");
		commands.addItem("Increment score");
		commands.addItem("Quit Game");
      commands.addItem("Disable Cards");
      commands.addItem("Enable Cards");
		commands.setEditable(false);
		final JTextField value1 = new JTextField();
		final JTextField value2 = new JTextField();
		panel.setLayout(new GridLayout(1,4));
		panel.add(commands);
		panel.add(value1);
		panel.add(value2);
		JButton click = new JButton("Go");
		click.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				processCommand(commands.getSelectedIndex(), value1.getText(), value2.getText());
            value1.setText("");
            value2.setText("");
			}
		});
		panel.add(click);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Commands"));
		return panel;
	}
	/** This method creates a panel to put in top of screen 
	 * It will have 3 text fields, one showing the turn of the player and other 2 showing scores
	 * @return JPanel with 3 text fields 
	 */
	private static JPanel createUpperPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		msg = new JTextArea("All the messages will be displayed here ");
		msg.setBackground(Color.GREEN);
		msg.setEditable(false);
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		player1Score = new JLabel("Player1 Score = 0");
		player2Score = new JLabel("Player2 Score = 0");
		JPanel shortPanel = new JPanel();
		shortPanel.setLayout(new GridLayout(1,3));
		shortPanel.add(msg);
		shortPanel.add(player1Score);
		shortPanel.add(player2Score);
		panel.add(shortPanel);
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Display"));
		return panel;
	}
	/**
	 * This method will create 10 buttons, add them to a panel and return the panel 
	 * @return JPanel with 10 buttons on it 
	 */
	private static JPanel createButtons() {
		JPanel panel = new JPanel();
		int j =0;
      final int[] arr = {1,2,3,4,5,6,7,8,9,10};
		panel.setLayout(new GridLayout(2,5));
		for ( final int i : arr ){
			final JButton button = new JButton();
			button.setVisible(true);
			button.setOpaque(true);
			setImage(button, DEFAULT_FILE_NAME);
			buttons[j] = button;
         j++;
         button.addActionListener(
					(ActionEvent event) -> {
                  System.out.println("Button pressed " + i);
                  setImage( button, card[i-1].getFileName());
					   card[i-1].setShown(true); 
                  }
					);
			panel.add(button);
		}
		return panel;
	}
   /**
    * This method sets the image to the button
    * @param button Button where the image is to be set
    * @param fileName File name of the image 
    */
   private static void setImage( JButton button, String fileName){
      try{
			Image image = ImageIO.read(GameFrame.class.getResource(fileName));
			Image temp = image.getScaledInstance(70,70,3);
			button.setIcon(new ImageIcon (temp) );
			}
		catch( IllegalArgumentException e ){
			System.out.println( "Image not found for " + fileName );
         System.exit(1);
			}
      catch( IOException e ){
			System.out.println( "Image not found for " + fileName );
			}
      }
   /**
    * This method will process each command along with its input 
    * @param selectedIndex Option selected by the user 
    * @param option1 argument 1 for that command
    * @param option2 argument 2 for that command
    */
	private static void processCommand(int selectedIndex, String option1,String option2) {
		msg.setText(null);
		msg.setBackground(null);
		try{
			switch( selectedIndex ){
				case 0: {
					setPlayer(option1);
					break;
				}
				case 1: {
					evaluateCards(option1, option2 );
					break;
				}
				case 2:{
					flipCardBack( option1 ,option2);
					break;
				}
				case 3:{
					incrementScore ( option1 );
					break;
				}
				case 4: {
					quitGame(option1);
					break;
				}
            case 5: {
					disableCards(option1, option2);
					break;
				}
            case 6: {
					enableCards(option1, option2);
					break;
				}
			}
         checkGameStatus();
		}
		catch ( ArrayIndexOutOfBoundsException e ){
			setErrorMessage("Player " + currentPlayer + " please enter cards from 1-10");
			}
		catch ( NumberFormatException e ){
			setErrorMessage("Player " + currentPlayer + " please enter text in both the columns");
			}
	}
	/**
	 * This method sets the current player for the game
	 * @param player Current Player
	 */
	private static void setPlayer(String player) {
		if ( Integer.parseInt(player) <= 0 || Integer.parseInt(player) >=3 ){
			setErrorMessage("Please enter 1 or 2");
			return;
		}
		setMessage("Player " + player + " your turn ");
		currentPlayer=Integer.parseInt(player);
}
	/**
	 * This method will quit the game if current player opts to quit 
	 * @param player This player will quit the game
	 */
	private static void quitGame(String player) {
		int user = Integer.parseInt(player);
		if ( user != currentPlayer ){
			setErrorMessage("Player " + user + " please wait for your turn to quit the game");
		}
		else {
			setErrorMessage("Player " + user + ", you lose by forfeiting the game " );
			quitGame();
		}
   }
   /**
    * This method makes the command panel invisible and disables the buttons
    */
   private static void quitGame(){
      commandPanel.setVisible(false);
      for ( JButton b : buttons ){
				b.setEnabled(false);
			}
      }
	/**
	 * This method will display whether the 2 cards are equal or not. If they are equal, it will increment the score for that player
	 * else it will flip those cards back 
	 * @param card1 first card to be evaluated 
	 * @param card2 second card to be evaluated 
	 * @throws ArrayIndexOutOfBoundsException If user enters any invalid card
	 * @throws NumberFormatException If user doesn't enter proper data in proper format
	 */
	private static void evaluateCards(String card1, String card2) throws ArrayIndexOutOfBoundsException, NumberFormatException {
		if ( card1.equals(card2)){
			setErrorMessage("Same cards entered");
			return;
		}
		boolean check = checkEqualCards( card1, card2 );
		if ( check ){
         setMessage("Cards Matched, Player " + currentPlayer + " 1 point earned");
         disableCards( card1, card2);
			incrementScore(currentPlayer + "");
			}
		else {
			flipCardBack( card1, card2);
			}
	}
   /**
   * This method will disable 2 buttons when they would be matched
   * @param card1 Card 1 to be disabled
   * @param card2 Card 2 to be disabled
   */
   private static void disableCards( String card1, String card2){
      int one = Integer.parseInt( card1 );
      int two = Integer.parseInt( card2 );
      buttons[ one -1 ].setEnabled(false);
      buttons[ two -1 ].setEnabled(false);
      }
   
   /**
   * This method will enable 2 buttons when they would be matched
   * @param card1 Card 1 to be enabled
   * @param card2 Card 2 to be enabled
   */
   private static void enableCards( String card1, String card2){
      int one = Integer.parseInt( card1 );
      int two = Integer.parseInt( card2 );
      buttons[ one -1 ].setEnabled(true);
      buttons[ two -1 ].setEnabled(true);
      }
	/**
	 * This method will flip those cards back 
	 * @param card1 to be flipped back
	 * @param card2 to be flipped back
	 * @throws ArrayIndexOutOfBoundsException If user enters any invalid card
	 * @throws NumberFormatException If user doesn't enter proper data in proper format
	 */
	private static void flipCardBack(String card1, String card2) throws ArrayIndexOutOfBoundsException,NumberFormatException{
		try{
			int c1 = Integer.parseInt(card1) -1;
			int c2 = Integer.parseInt(card2) -1;
			setErrorMessage("Cards " + (c1+1) + "," + (c2+1) + " not matched");	
			card[c1].setShown(false);
			card[c2].setShown(false);
			setImage( buttons[c1], DEFAULT_FILE_NAME );
			setImage( buttons[c2], DEFAULT_FILE_NAME );
			}
		finally{}
	}
	/** This method will increment the jlabel on top for the current player
	 * @param num Player whose score is incremented 
	 */
	private static void incrementScore(String num ) {
		if ( Integer.parseInt(num) <= 0 || Integer.parseInt(num) >=3 ){
			setErrorMessage("Please enter 1 or 2 in column 1 ");
			return;
		}
		int score = getPreviousScore(num);
		score++;
		if ( currentPlayer == 1 ){
			player1Score.setText("Player 1 \t Score = " + score );
			}
		else {
			player2Score.setText("Player 2 \t Score = " + score );
			}
	}
	/**
	 * This method will get the previous score of current player from jlabel text
	 * @param player player entered 
	 * @return Score of current Player
	 */
	private static int getPreviousScore(String player ) {
		int num = Integer.parseInt( player );
		String str ="";
		if ( num == 1){
			str = player1Score.getText();
			}
		else {
			str = player2Score.getText();
			}
		str = str.charAt(str.length()-1) + "";
		return Integer.parseInt(str);
	}
	/**
	 * This will check whether the cards are equal or not
	 * @param card1 first card
	 * @param card2 second card
	 * @return True if cards are equal else returns false
	 * @throws ArrayIndexOutOfBoundsException If user enters any invalid card
	 * @throws NumberFormatException If user doesn't enter proper data in proper format
	 */
	private static boolean checkEqualCards(String card1, String card2) throws ArrayIndexOutOfBoundsException,NumberFormatException{
		try{ 
			int one = Integer.parseInt(card1) - 1;
			int two = Integer.parseInt(card2) - 1;
			if ( card[one].getIsShown() || card[two].getIsShown() )
				setErrorMessage("Invalid Cards");
			return card[one].equals(card[two]);
			}
		finally{}
	}
   /**
   * This method will check if all the cards are flipped or not 
   */
   private static void checkGameStatus(){
      int count =0;
      for ( JButton b : buttons ){
         if ( b.isEnabled() == false ){
            count++;
            }
         }
      if ( count == 10 ){//all the buttons are disabled so the game is finished 
         declareWinner();
         }
   }
   /**
   *This method will declare who the winner is according to their points 
   */   
   private static void declareWinner(){
      int score = getPreviousScore(1+"");
      int score1 = getPreviousScore(2+"");
      if ( score > score1 ) {
         setMessage("Congrats Player 1, you won by " + score + "-" + score1 );
         }
      else if ( score < score1) {
         setMessage("Congrats Player 2, you won by " + score1 + "-" + score );
         }
      else {
         setMessage("Well played guys, it's a tie. Final Score is " + score + "-" + score1 );
         }
      }
	/**
	 * This method will display the message to the player
	 * @param str Message to be displayed
	 */
	private static void setMessage(String str) {
		msg.setOpaque(true);
		msg.setVisible(true);
		msg.setText(str);
		msg.setBackground(Color.GREEN);
	}
	/**
	 * This message prints a string onto the window if there is any wrong input
	 * @param str String to be printed 
	 */
	private static void setErrorMessage(String str ){
		msg.setOpaque(true);
		msg.setVisible(true);
		msg.setBackground(Color.RED);
		msg.setText(str );
	}
	/**
	 * This method initialises cards in an array 
	 */
   private static void createCards(){
      for ( int i =0; i< 5; i++){
         card[i] = new Card(i); 
         }
      for ( int i =5; i< 10; i++){
         card[i] = new Card(9-i); 
         }
   }
}
