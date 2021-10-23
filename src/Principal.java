import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Principal extends JFrame implements ActionListener{
	
	private static JDesktopPane dPane;
	private static JMenu menuCardapio;
	private static JMenuItem itemAdicionarReceita,itemConsultarCardapio,itemFechar;
	private JMenuBar barraMenu;
	
	protected static Connection conexao; 
	
	public Principal()
	{
		Container P =getContentPane();
		P.setLayout(new BorderLayout());
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowListener());
		
		setTitle("Cardápio da semana");
		setSize(1200,700);
		setLocation(40,40);
		setExtendedState(MAXIMIZED_BOTH);
		
		barraMenu = new JMenuBar();
		
		menuCardapio = new JMenu("Cardápio");
		
		itemAdicionarReceita = new JMenuItem("Adicionar receita");
		itemAdicionarReceita.addActionListener(this);
		
		itemConsultarCardapio = new JMenuItem("Consultar Cardápio");
		itemConsultarCardapio.addActionListener(this);
		
		itemFechar = new JMenuItem("Fechar");
		itemFechar.addActionListener(this);
		
		menuCardapio.add(itemAdicionarReceita);
		menuCardapio.add(itemConsultarCardapio);
		menuCardapio.addSeparator();
		menuCardapio.add(itemFechar);
		
		barraMenu.add(menuCardapio);
		setJMenuBar(barraMenu);
		
		dPane = new JDesktopPane();
		dPane.putClientProperty("JDesktopPane.dragMode", "outline");
		dPane.setBackground(new Color(143,188,143));
		P.add(dPane,"Center");
		
		conexao();
	}
	
	public void fechar()
	{
		Object[] opcoes={"Sim","Não"};
		int retorno=JOptionPane.showOptionDialog(null,
				"Tem certeza que deseja encerrar a aplicação?",
				"Encerrar",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,null,
				opcoes,
				opcoes[0]);
		if (retorno==0)
		{
			try
			{
				conexao.close();
			}catch (SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"Erro ao desconectar. Erro: " +
						erro.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
			}catch (Exception erro)
			{
				JOptionPane.showMessageDialog(null,"Erro: " +
						erro.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
			}
			dispose();
		}
	}
	
	
	public class WindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			fechar();
		}
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		Object origem = e.getSource();
		if (origem == itemAdicionarReceita)
		{
			try
			{
				AdicionarReceita cadastrar= new AdicionarReceita();
				dPane.add(cadastrar);
				cadastrar.setVisible(true);
			}catch (Exception erro) {}
		}else if (origem == itemConsultarCardapio)
		{
			try
			{
				ConsultarCardapio consultar= new ConsultarCardapio();
				dPane.add(consultar);
				consultar.setVisible(true);
			}catch (Exception erro) {}
		}else if (origem == itemFechar)
		{
			fechar();
		}
	}
	
	private void conexao()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
			conexao = DriverManager.getConnection("jdbc:postgresql://Localhost:5432/sistema",
					"postgres","filipe");
		}catch (ClassNotFoundException erro)
		{
			JOptionPane.showMessageDialog(null,"Erro na conexão com o banco de dados. Erro: " +
					erro.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
		}catch (SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Erro de SQL. Erro: " +
					erro.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
		}catch (Exception erro)
		{
			JOptionPane.showMessageDialog(null,"Erro: " +
					erro.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
		}
	}
}
