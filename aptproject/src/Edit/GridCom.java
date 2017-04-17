package Edit;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


/*
 * 
 * /*
	public void gPoint(Container s,GridBagLayout gbl,GridBagConstraints gdc,
			Component c,int gridx,int gridy,int gridWidth,int gridHeight,int weightx,double wighty){
	
				
				if (c==lb_takerTime){
					gdc.insets= new Insets(20, 0, 0, 10);		
				}else{
					gdc.insets= new Insets(10, 0, 2, 10);
				}
			
		
			gdc.gridx=gridx;
			gdc.gridy=gridy;
			gdc.gridwidth=gridWidth;
			gdc.gridheight=gridHeight;
			gdc.weightx=weightx;
			gdc.weighty=wighty;
			
		
			
			gbl.setConstraints(c, gdc);
			s.add(c);
		}
	*/
 
 



public class GridCom {
	
	Container s;
	GridBagLayout gbl;
	GridBagConstraints gdc;
	Component c;
	int gridx;
	int gridy;
	int gridWidth;
	int gridHeight;
	int weightx;
	double wighty;
	
	public GridCom(Container s,GridBagLayout gbl,GridBagConstraints gdc,
			Component c,int gridx,int gridy,int gridWidth,int gridHeight,int weightx,double wighty){
		this.s=s;
		this.gbl=gbl;
		this.gdc=gdc;
		this.c=c;
		this.gridx=gridx;
		this.gridy=gridy;
		this.gridWidth=gridWidth;
		this.gridHeight=gridHeight;
		this.weightx=weightx;
		this.wighty=wighty;
		
			
		
	
		gdc.insets= new Insets(10, 0, 2, 10);
		gdc.gridx=gridx;
		gdc.gridy=gridy;
		gdc.gridwidth=gridWidth;
		gdc.gridheight=gridHeight;
		gdc.weightx=weightx;
		gdc.weighty=wighty;
		
	
		
		gbl.setConstraints(c, gdc);
		s.add(c);
	}
}
