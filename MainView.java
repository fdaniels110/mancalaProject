import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

public class MainView extends JFrame {
	private DataModel dataModel;
	private GameBoard board;
	
	public MainView() {
		dataModel = new DataModel();
		board = new GameBoard(dataModel);
		dataModel.attach(board);
		board.initBoard();
		
		newGame();
		
		JLabel titleLabel = new JLabel("MANCALA");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		titleLabel.setForeground(Color.BLUE);
		JPanel titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setPreferredSize(new Dimension(400, 48));
		titlePanel.add(titleLabel);
		
		JButton newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dataModel.clearData();
				if (dataModel.getCurrentPlayer().equals("Player B"))
					dataModel.switchPlayer();
				newGame();
			}
		});
		JPanel nPanel = new JPanel(new GridBagLayout());
		nPanel.setPreferredSize(new Dimension(200, 48));
		nPanel.add(newGameButton);
		
		JButton undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dataModel.undo();
			}
		});
		JPanel uPanel = new JPanel(new GridBagLayout());
		uPanel.setPreferredSize(new Dimension(200, 48));
		uPanel.add(undoButton);
		
		JPanel menuBarPanel = new JPanel(new BorderLayout());
		menuBarPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		menuBarPanel.add(nPanel, BorderLayout.WEST);
		menuBarPanel.add(titlePanel, BorderLayout.CENTER);
		menuBarPanel.add(uPanel, BorderLayout.EAST);
		
		add(menuBarPanel, BorderLayout.NORTH);
		add(board, BorderLayout.CENTER);
		
		setTitle("Mancala");
		pack();
		setLocationRelativeTo(this);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void newGame() {
		JDialog newGameDialog = new JDialog(this, "Set Up Mancala", true);
		newGameDialog.setAlwaysOnTop(true);
		newGameDialog.setResizable(false);
		
		JComboBox stylesComboBox = new JComboBox();
		stylesComboBox.addItem("Rectangle");
		stylesComboBox.addItem("Circle");
		
		JComboBox pebblesComboBox = new JComboBox();
		pebblesComboBox.addItem("3");
		pebblesComboBox.addItem("4");
		
		JButton startButton = new JButton("Start Game");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				newGameDialog.setVisible(false);
				if (stylesComboBox.getSelectedItem().toString().equals("Circle")) {
					if (pebblesComboBox.getSelectedItem().toString().equals("4"))
						dataModel.initDataModel(4, new StyleCircle());
					else
						dataModel.initDataModel(3, new StyleCircle());
				}
				else {
					if (pebblesComboBox.getSelectedItem().toString().equals("4"))
						dataModel.initDataModel(4, new StyleRectangle());
					else
						dataModel.initDataModel(3, new StyleRectangle());
				}
			}
		});
		
		JPanel selectionsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints sC = new GridBagConstraints();
		sC.gridx = 0;
		sC.insets = new Insets(8, 8, 8, 8);
		sC.anchor = GridBagConstraints.WEST;
		selectionsPanel.add(new JLabel("Board style"), sC);
		selectionsPanel.add(new JLabel("Initial number of pebbles"), sC);
		sC.gridx = 1;
		sC.gridy = GridBagConstraints.RELATIVE;
		selectionsPanel.add(stylesComboBox, sC);
		selectionsPanel.add(pebblesComboBox, sC);
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints bC = new GridBagConstraints();
		bC.insets = new Insets(8, 8, 8, 8);
		bC.anchor = GridBagConstraints.CENTER;
		buttonPanel.add(startButton, bC);
		
		newGameDialog.add(selectionsPanel, BorderLayout.NORTH);
		newGameDialog.add(buttonPanel, BorderLayout.SOUTH);
		
		newGameDialog.pack();
		newGameDialog.setLocationRelativeTo(this);
		newGameDialog.setVisible(true);
		newGameDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
}