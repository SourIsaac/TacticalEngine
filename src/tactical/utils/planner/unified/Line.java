package tactical.utils.planner.unified;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import tactical.map.MapObject;
import tactical.utils.planner.PlannerContainer;
import tactical.utils.planner.PlannerContainerDef;
import tactical.utils.planner.PlannerLine;
import tactical.utils.planner.PlannerReference;
import tactical.utils.planner.unified.UnifiedViewPanel.UnifiedRenderable;

public class Line implements UnifiedRenderable {
	public String text;
	public boolean removable;
	public MapObject mo;
	public PlannerContainer pc;
	public PlannerLine pl;
	// This is ugly as shit, but I don't wanna deal with it otherwise
	public int indent, y;
	public UnifiedViewPanel uvp;
	
	public Line(String text, MapObject mo, PlannerContainer pc, PlannerLine pl,
			UnifiedViewPanel uvp) {
		super();
		this.text = text;
		this.mo = mo;
		this.pc = pc;
		this.pl = pl;
		this.uvp = uvp;
	}
	
	@Override
	public void render(int indent, int y, int panelWidth, Graphics g) {
		// g.drawLine(20 + 50 * indent, yOffset + (y + 1) * 50, panelWidth - 20 - 50 * indent, yOffset + (y + 1) * 50);
		boolean conditional = false;
		if (pc != null && pc.getPcdef().getDefiningLine().getName().equalsIgnoreCase("trigger")) {
			conditional = hasValuesSpecified(1) || hasValuesSpecified(2);			
		}
		
		g.setFont(g.getFont().deriveFont(Font.BOLD, 13));
		
		g.setColor(Color.white);
		g.fillRect(11 + 50 * indent, y * 50 + UnifiedViewPanel.yOffset + 1, panelWidth - 22 - 50 * indent, 48);
		g.setColor(Color.black);
		if (!conditional)
			g.drawString(text, 30 + 50 * indent, UnifiedViewPanel.yOffset + y * 50 + 30);
		else {
			g.drawString(text, 30 + 50 * indent, UnifiedViewPanel.yOffset + y * 50 + 18);
			g.drawString("(This trigger may not run because it has required/excluded quests)", 30 + 50 * indent, UnifiedViewPanel.yOffset + y * 50 + 40);
		}
		
		this.indent = indent;
		this.y = y;
	}

	protected boolean hasValuesSpecified(int valueIdx) {
		boolean conditional = false;
		ArrayList<PlannerReference> quests = (ArrayList<PlannerReference>) pc.getDefLine().getValues().get(valueIdx);
		if (quests.size() > 1 || quests.get(0).getName().length() > 0) {
			conditional = true;
		}
		return conditional;
	}

	@Override
	public int getHeight() {
		return 1;
	}
	
	public void checkClick(int x, int y) {
		Rectangle r = new Rectangle(11 + 50 * indent, this.y * 50 + UnifiedViewPanel.yOffset + 1, uvp.getRenderPanel().getWidth() - 22 - 50 * indent, 48);
		if (r.contains(x, y)) {
			if (mo != null) {
				uvp.getMapEditorPanel().editMapObject(mo);
				uvp.setupPanel((String) uvp.getDrivers().getSelectedItem());
			} else if (pl != null) {					
				editPL(pl);
			} else {
				
				// PlannerLine pl = pc.getDefLine();
				// editPL(pl);
				
				SingleEditPanel sep = new SingleEditPanel(pc);												
				uvp.showScrollableOptionPane(sep, false);
				for (PlannerLine pl : pc.getLines())
					pl.commitChanges();
				pc.getDefLine().commitChanges();
				uvp.setupPanel((String) uvp.getDrivers().getSelectedItem());
			}
		}
	}

	private void editPL(PlannerLine pl) {
		PlannerContainerDef pcdef = pc.getPcdef();			
		pl.setupUI(pcdef.getAllowableLines(), null, 1, pcdef.getListOfLists(), false, null);

		uvp.showScrollableOptionPane(pl.getUiAspect(), false);
		
		pl.commitChanges();
		
		uvp.setupPanel((String) uvp.getDrivers().getSelectedItem());

	}
	
	public boolean isMayNotRun() {
		return hasValuesSpecified(1) || hasValuesSpecified(2);
	}
}