
import java.awt.*;
import java.awt.geom.*;

public class StyleCircle implements Style {
	public RectangularShape getShape() {
		return new Ellipse2D.Double();
	}
	
	public Color getPitColor() {
		return Color.DARK_GRAY;
	}
	
	public Color getPebbleColor() {
		return Color.LIGHT_GRAY;
	}
}