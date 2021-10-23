import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

public class Splash extends JWindow{
	
	private Timer timer;
	private JLabel lblLinha1,lblLinha2,lblLinha3;
	private GridBagConstraints matriz = new GridBagConstraints();
	
	private Splash() {
		setSize(450,250);
		setLocationRelativeTo(null);
		Font f=new Font("ComicSans",Font.BOLD,50);
		Font d=new Font("ComicSans",Font.ITALIC,12);
		lblLinha1 = new JLabel ("Cardápio da");
		lblLinha1.setFont(f);
		lblLinha2 =new JLabel("Semana");
		lblLinha2.setFont(f);
		lblLinha3 =new JLabel("Filipe Cordella");
		lblLinha3.setFont(d);
		JPanel pnlSplash =new JPanel(new GridBagLayout());
		pnlSplash.setBackground(new Color(143,188,143));
		matriz.insets = new Insets(8,8,8,8);
		addGridBag(0,0,lblLinha1,pnlSplash);
		addGridBag(0,1,lblLinha2,pnlSplash);
		addGridBag(0,2,lblLinha3,pnlSplash);
		Container geraTela = getContentPane();
		geraTela.add(pnlSplash);
		ActionListener fechar=new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{				
				dispose();
				Principal principal=new Principal();
				principal.setVisible(true);
				timer.stop();
			}
		};
		
		timer = new Timer (3000,fechar); 
		timer.start();
	}
	
	

	public static void main(String[] args) {
		
		Splash tela = new Splash();
		tela.setVisible(true);

	}
	
	public void addGridBag(final int x,final int y,final Component componente,final JPanel pnl)
	{
		matriz.gridx=x;
		matriz.gridy=y;
		pnl.add(componente,matriz);
		
	}

}
