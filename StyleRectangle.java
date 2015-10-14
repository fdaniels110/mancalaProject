
import java.awt.*;
import java.awt.geom.*;

public class StyleRectangle implements Style {
	public RectangularShape getShape() {
		return new Rectangle2D.Double();
	}
	
	public Color getPitColor() {
		return Color.LIGHT_GRAY;
	}
	
	public Color getPebbleColor() {
		return Color.DARK_GRAY;
	}
}