import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class Pit extends JPanel {
	private int width, height, pebbles, index;
	private String player;
	private Style style;
	private RectangularShape shape;
	
	public Pit(int n, int i, String p, Style s) {
		pebbles = n;
		index = i;
		player = p;
		style = s;
	}
	
	public void setDimen(int w, int h) {
		width = w;
		height = h;
		setPreferredSize(new Dimension(w, h));
	}
	
	public void setPebbles(int n) {
		pebbles = n;
	}
	
	public void setStyle(Style s) {
		style = s;
	}
	
	public int getPebbles() {
		return pebbles;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		shape = style.getShape();
		shape.setFrame(0, 0, width, height);
		
		g2.setPaint(style.getPitColor());
		g2.fill(shape);
		g2.draw(shape);
		
		final int pebbleSize = width / 10;
		final int x = width *3/10;
		final int y = height *2/10;
        final Ellipse2D.Double p = new Ellipse2D.Double(x, y, pebbleSize, pebbleSize);
        for (int i=0; i<pebbles; i++) {
            if (i%4 == 0) {
				p.x = x;
                p.y += pebbleSize;
			}
			else
                p.x += pebbleSize;
            
			g2.setPaint(style.getPebbleColor());
            g2.fill(p);
            g2.draw(p);
        }
	}
}