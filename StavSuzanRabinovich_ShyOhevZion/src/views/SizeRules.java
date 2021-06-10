package views;

public enum SizeRules { /* Easy to set same properties to panes*/
	MAIN_WINDOW(400, 700), BUTTON(150, 30), TEXT_FIELD(400,30), GRID_PANE(300, 700);

	private double width;
	private double height;

	public double width() {
		return width;
	}

	public double height() {
		return height;
	}

	SizeRules(double width, double height) {
		this.width = width;
		this.height = height;
	}
}
