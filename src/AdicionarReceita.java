import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class AdicionarReceita extends JInternalFrame implements ActionListener {
	private JMenuBar barraMenu;
	private JMenu menuArquivo;
	private JMenuItem itemCadastrar,itemCancelar;
	
	private JLabel lblNome,lblDia,lblDescricao;
	private JTextField txtNome;
	private JButton btnCadastrar,btnCancelar;
	private GridBagConstraints montaTela=new GridBagConstraints();
	private JPanel pnlCima,pnlBaixo,pnlCadastrar;
	private JComboBox cboDia;
	private JTextArea areaDescricao=new JTextArea(10,10);
	
	public AdicionarReceita()
	{
		super("Adicionar Receita",false,true,false,false);
		setSize(500,400);
		setLocation(215,40);
		montaTela.fill = GridBagConstraints.BOTH;
		
		barraMenu =new JMenuBar();
		
		menuArquivo = new JMenu("Arquivo");
		
		itemCadastrar = new JMenuItem("Cadastrar");
		itemCadastrar.addActionListener(this);
		
		itemCancelar = new JMenuItem("Cancelar");
		itemCancelar.addActionListener(this);
		
		menuArquivo.add(itemCadastrar);
		menuArquivo.addSeparator();
		menuArquivo.add(itemCancelar);
		
		barraMenu.add(menuArquivo);
		setJMenuBar(barraMenu);
		
		lblDia=new JLabel("Selecione o dia da semana");
		cboDia=new JComboBox();
		cboDia.setToolTipText("Selecione o dia da semana para inserir o cardápio");
		cboDia.addItem("Segunda feira");
		cboDia.addItem("Terça feira");
		cboDia.addItem("Quarta feira");
		cboDia.addItem("Quinta feira");
		cboDia.addItem("Sexta feira");
		cboDia.addItem("Sábado");
		cboDia.addItem("Domingo");
	  
		
		lblDescricao=new JLabel("Descrição:");
		areaDescricao.setLineWrap(true);
		areaDescricao.setWrapStyleWord(true);
		JScrollPane sp=new JScrollPane(areaDescricao);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		lblNome = new JLabel("Nome da receita: ");
		txtNome = new JTextField(20);
		
		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(this);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(this);
		
		pnlCima = new JPanel(new GridBagLayout());
		montaTela.insets = new Insets(8,8,8,8);
		
		
		addGridBag(0,0,lblDia,pnlCima);
		addGridBag(1,0,cboDia,pnlCima);
		addGridBag(0,1,lblNome,pnlCima);
		addGridBag(1,1,txtNome,pnlCima);
		addGridBag(0,2,lblDescricao,pnlCima);
		addGridBag(1,2,sp,pnlCima);
		
		pnlBaixo = new JPanel(new GridBagLayout());
		addGridBag(0,0,btnCadastrar,pnlBaixo);
		addGridBag(1,0,btnCancelar,pnlBaixo);
		
		pnlCadastrar = new JPanel(new GridBagLayout());
		addGridBag(0,0,pnlCima,pnlCadastrar);
		addGridBag(0,1,pnlBaixo,pnlCadastrar);
		
		Container tela = getContentPane();
		tela.add(pnlCadastrar);
		
		getRootPane().setDefaultButton(btnCadastrar);
		
		pack();
	}
	public void actionPerformed(ActionEvent e)
	{
		Object origem=e.getSource();
		if (origem==btnCadastrar || origem==itemCadastrar)
		{
			String texto = cboDia.getSelectedItem().toString();
			if (txtNome.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null,
						"Digite o nome da receita!","Nome",
						JOptionPane.ERROR_MESSAGE);
				txtNome.requestFocus();
			}else if (areaDescricao.getText().equals("")){
				JOptionPane.showMessageDialog(null,
						"Digite a descrição da receita!","Descrição",
						JOptionPane.ERROR_MESSAGE);
				areaDescricao.requestFocus();
			} else
			{
				try
				{
					PreparedStatement cadastrar = 
							Principal.conexao.prepareStatement
							("INSERT INTO cardapio(diasemana,nomereceita,descricao) VALUES (?,?,?)");
					cadastrar.setString(1, texto);
					cadastrar.setString(2, txtNome.getText());				
					cadastrar.setString(3, areaDescricao.getText());
					cadastrar.executeUpdate();
					JOptionPane.showMessageDialog(null,"Receita incluída com sucesso",
							"Cadastrado",JOptionPane.INFORMATION_MESSAGE);
					dispose();
					txtNome.setText("");
					areaDescricao.setText("");
				}catch (SQLException erro)
				{
					JOptionPane.showMessageDialog(null,"Erro de SQL: " + 
							erro.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
				}catch (Exception erro)
				{
					JOptionPane.showMessageDialog(null,"Erro: " + 
							erro.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
				}
			}
		}else if (origem==btnCancelar || origem==itemCancelar)
		{
			dispose();
		}
	}
	public void addGridBag(final int x,final int y,final Component obj,final JPanel pnl)
	{
		montaTela.gridx=x;
		montaTela.gridy=y;
		pnl.add(obj,montaTela);
	}

}
