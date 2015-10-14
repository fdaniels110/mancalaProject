import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

public class GameBoard extends JPanel implements ChangeListener {
	private DataModel dataModel;
	private ArrayList<Pit> pits;
	private JPanel boardPanel;
	private JLabel statusLabel, scoreALabel, scoreBLabel;
	
	public GameBoard(DataModel data) {
		setLayout(new BorderLayout());
		
		dataModel = data;
		pits = data.getData();
		
		boardPanel = new JPanel(new BorderLayout());
		boardPanel.setPreferredSize(new Dimension(810, 230));
		boardPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		statusLabel = new JLabel();
		statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		statusLabel.setForeground(Color.RED);
		JPanel statusPanel = new JPanel(new GridBagLayout());
		statusPanel.setPreferredSize(new Dimension(410, 32));
		statusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		statusPanel.add(statusLabel);
		
		scoreALabel = new JLabel();
		scoreALabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		scoreALabel.setForeground(Color.BLUE);
		JPanel scoreAPanel = new JPanel(new GridBagLayout());
		scoreAPanel.setPreferredSize(new Dimension(200, 32));
		scoreAPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		scoreAPanel.add(scoreALabel);
		
		scoreBLabel = new JLabel();
		scoreBLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		scoreBLabel.setForeground(Color.BLUE);
		JPanel scoreBPanel = new JPanel(new GridBagLayout());
		scoreBPanel.setPreferredSize(new Dimension(200, 32));
		scoreBPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		scoreBPanel.add(scoreBLabel);
		
		JPanel infoBarPanel = new JPanel(new BorderLayout());
		infoBarPanel.add(statusPanel, BorderLayout.CENTER);
		infoBarPanel.add(scoreAPanel, BorderLayout.EAST);
		infoBarPanel.add(scoreBPanel, BorderLayout.WEST);
		
		add(boardPanel, BorderLayout.CENTER);
		add(infoBarPanel, BorderLayout.NORTH);
	}
	
	public void initBoard() {
		JPanel gamePanel = new JPanel(new GridLayout(0, 8));
		
		JPanel topLabelPanel = new JPanel();
		topLabelPanel.setLayout(new GridLayout(0, 8));
		
		JPanel bottomLabelPanel = new JPanel();
		bottomLabelPanel.setLayout(new GridLayout(0, 8));
		
		JPanel mBPanel = new JPanel(new GridBagLayout());
		mBPanel.setPreferredSize(new Dimension(102, 204));
		Pit mancalaB = dataModel.getMancala("Player B");
		mancalaB.setDimen(100, 202);
		mBPanel.add(mancalaB);
		gamePanel.add(mBPanel);
		
		JPanel bLabelPanel1 = new JPanel(new GridBagLayout());
		bLabelPanel1.setPreferredSize(new Dimension(100, 16));
		bLabelPanel1.add(new JLabel("Mancala B"));
		topLabelPanel.add(bLabelPanel1);
		
		JPanel aLabelPanel1 = new JPanel(new GridBagLayout());
		aLabelPanel1.setPreferredSize(new Dimension(100, 16));
		bottomLabelPanel.add(aLabelPanel1);
		
		for (int i=0; i<=5; i++) {
			JPanel pitsCol = new JPanel(new GridLayout(2, 0));
			
			JPanel bPanel = new JPanel(new GridBagLayout());
			bPanel.setPreferredSize(new Dimension(102, 102));
			Pit pitB = pits.get(12-i);
			pitB.setDimen(100, 100);
			pitB.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent event) {
					dataModel.makeAMove(pitB);
				}
			});
			bPanel.add(pitB);
			pitsCol.add(bPanel);
			
			JPanel aPanel = new JPanel(new GridBagLayout());
			aPanel.setPreferredSize(new Dimension(102, 102));
			Pit pitA = pits.get(i);
			pitA.setDimen(100, 100);
			pitA.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent event) {
					dataModel.makeAMove(pitA);
				}
			});
			aPanel.add(pitA);
			pitsCol.add(aPanel);
			
			JPanel bLabelPanel = new JPanel(new GridBagLayout());
			bLabelPanel.setPreferredSize(new Dimension(100, 16));
			bLabelPanel.add(new JLabel("B"+(6-i)));
			
			JPanel aLabelPanel = new JPanel(new GridBagLayout());
			aLabelPanel.setPreferredSize(new Dimension(100, 16));
			aLabelPanel.add(new JLabel("A"+(i+1)));
			
			gamePanel.add(pitsCol);
			topLabelPanel.add(bLabelPanel);
			bottomLabelPanel.add(aLabelPanel);
		}
		
		JPanel mAPanel = new JPanel(new GridBagLayout());
		mAPanel.setPreferredSize(new Dimension(102, 204));
		Pit mancalaA = dataModel.getMancala("Player A");
		mancalaA.setDimen(100, 202);
		mAPanel.add(mancalaA);
		gamePanel.add(mAPanel);
		
		JPanel bLabelPanel2 = new JPanel(new GridBagLayout());
		bLabelPanel2.setPreferredSize(new Dimension(100, 16));
		topLabelPanel.add(bLabelPanel2);
		
		JPanel aLabelPanel2 = new JPanel(new GridBagLayout());
		aLabelPanel2.setPreferredSize(new Dimension(100, 16));
		aLabelPanel2.add(new JLabel("Mancala A"));
		bottomLabelPanel.add(aLabelPanel2);
		
		boardPanel.add(gamePanel, BorderLayout.CENTER);
		boardPanel.add(topLabelPanel, BorderLayout.NORTH);
		boardPanel.add(bottomLabelPanel, BorderLayout.SOUTH);
	}
	
	public void stateChanged(ChangeEvent event) {
		pits = dataModel.getData();
		repaint();
		
		int playerAScore = dataModel.getMancala("Player A").getPebbles();
		int playerBScore = dataModel.getMancala("Player B").getPebbles();
		scoreALabel.setText("Player A's Score: " + playerAScore);
		scoreBLabel.setText("Player B's Score: " + playerBScore);
		
		if (dataModel.isGameOver())
			statusLabel.setText(dataModel.getWinner());
		else
			statusLabel.setText(dataModel.getCurrentPlayer() + "'s Turn");
	}
}