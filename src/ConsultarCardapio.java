import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class ConsultarCardapio extends JInternalFrame implements ActionListener
{
	private JLabel lblConsultarPor;
	private JComboBox cboConsultarPor;
	private JTextField txtConsultarPor;
	private DefaultTableModel modelo;
	private JTable tabela;
	private String [] colunas = {"Dia", "Cardápio", "Descrição"};
	private Object [][] dados = {};
	private JButton btnConsultar,btnExcluir,btnLimpar,btnCancelar;
	private GridBagConstraints restricoes=new GridBagConstraints();
	private JPanel pnlCima,pnlBaixo,pnlConsultar;
	private JMenuBar barraMenu;
	private JMenu menuArquivo;
	private JMenuItem itemConsultar,itemExcluir,itemLimpar,itemCancelar;
	
	
	public ConsultarCardapio()
	{
		super("Consultar Cardápio Semanal",false,true,false,false);
		setSize(600,400);
		setLocation(215,40);
		restricoes.fill=GridBagConstraints.BOTH;
		barraMenu=new JMenuBar();
		menuArquivo=new JMenu("Arquivo");
		itemConsultar=new JMenuItem("Consultar");
		itemConsultar.addActionListener(this);
		menuArquivo.add(itemConsultar);
		menuArquivo.addSeparator();
		
		itemExcluir=new JMenuItem("Excluir");
		itemExcluir.addActionListener(this);
		menuArquivo.add(itemExcluir);
		itemLimpar=new JMenuItem("Limpar");
		itemLimpar.addActionListener(this);
		menuArquivo.add(itemLimpar);
		menuArquivo.addSeparator();
		
		itemCancelar=new JMenuItem("Cancelar");
		itemCancelar.addActionListener(this);
		menuArquivo.add(itemCancelar);
		barraMenu.add(menuArquivo);
		setJMenuBar(barraMenu);
			
		
		lblConsultarPor = new JLabel("Consultar Por:",JLabel.RIGHT);
		cboConsultarPor = new JComboBox();
		cboConsultarPor.setToolTipText("Selecione o dado que você deseja utilizar para realizar a consulta no banco de dados");
		cboConsultarPor.addItem("Dia");
		cboConsultarPor.addItem("Receita");
		cboConsultarPor.addItem("Tudo");
		txtConsultarPor = new JTextField(20);
		btnConsultar=new JButton("Consultar");
		btnConsultar.addActionListener(this);
		
		modelo = new DefaultTableModel(dados,colunas);
		tabela = new JTable(modelo);
		
		tabela.getColumnModel().getColumn(0).setPreferredWidth(153);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(153);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(222);
		tabela.getColumnModel().getColumn(0).setResizable(true);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.setPreferredScrollableViewportSize(new Dimension(320, 150));
		tabela.getTableHeader().setReorderingAllowed(false);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane barraRolagem = new JScrollPane(tabela);
		barraRolagem.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		barraRolagem.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		btnExcluir=new JButton("Excluir");
		btnExcluir.addActionListener(this);
		
		btnLimpar=new JButton("Limpar");
		btnLimpar.addActionListener(this);
		
		btnCancelar=new JButton("Cancelar");
		btnCancelar.addActionListener(this);
		
		pnlCima = new JPanel(new GridBagLayout());
		restricoes.insets = new Insets(8,8,8,8);
		
		
		addGridBag(0,0,lblConsultarPor,pnlCima);
		addGridBag(1,0,cboConsultarPor,pnlCima);
		addGridBag(2,0,txtConsultarPor,pnlCima);
		addGridBag(3,0,btnConsultar,pnlCima);
		
		pnlBaixo = new JPanel(new GridBagLayout());
		addGridBag(0,0,btnExcluir,pnlBaixo);
		addGridBag(1,0,btnLimpar,pnlBaixo);
		addGridBag(2,0,btnCancelar,pnlBaixo);
		
		pnlConsultar = new JPanel(new GridBagLayout());
		addGridBag(0,0,pnlCima,pnlConsultar);
		addGridBag(0,1,barraRolagem,pnlConsultar);
		addGridBag(0,2,pnlBaixo,pnlConsultar);
		
		Container tela = getContentPane();
		
		tela.add(pnlConsultar);
		
		getRootPane().setDefaultButton(btnConsultar);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object origem=event.getSource();
		if (origem == btnConsultar || origem == itemConsultar)
		{
			if (cboConsultarPor.getSelectedItem().equals("Tudo")) {
				 if (modelo.getRowCount()!=0)
		            {
		                for (int i=modelo.getRowCount()-1;i>=0;i--)
		                    modelo.removeRow(i);
		            }try {
		            	 txtConsultarPor.setText("");
		            	 ResultSet registros=null;
		            	 PreparedStatement consultar = Principal.conexao.prepareStatement
		                			("SELECT COUNT(*) as numero FROM cardapio");
		                	registros = consultar.executeQuery();
		                	registros.next();
		                	int quantidade = Integer.parseInt(registros.getString("numero"));
		                	if (quantidade!=0)
		                	{
		                		consultar = Principal.conexao.prepareStatement
			                			("SELECT * FROM cardapio");
		                		registros = consultar.executeQuery();
		                		registros.next();
		                		do
		                		{
		                			String diasemana = registros.getString("diasemana");
		                			String nomereceita = registros.getString("nomereceita");
		                			String descricao = registros.getString("descricao");
		                			modelo.addRow(new Object[] {diasemana,nomereceita,descricao});
		                		}while (registros.next());
		            	 
		            	
		            }
		            }
		            catch (SQLException e)
		            {
		            	JOptionPane.showMessageDialog(null,"Erro de SQL: " + e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
		            }catch (Exception e)
			        {
			            JOptionPane.showMessageDialog(null,"Erro: " + e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);  
			        }
				
			}
			
		
			else if (txtConsultarPor.getText().equals(""))
	        {
	            JOptionPane.showMessageDialog(null,"Preencha o dado que deseja pesquisar","Preencha o dado",JOptionPane.ERROR_MESSAGE);
	            txtConsultarPor.requestFocus();
	        }else
	        {
	            if (modelo.getRowCount()!=0)
	            {
	                for (int i=modelo.getRowCount()-1;i>=0;i--)
	                    modelo.removeRow(i);
	            }
	            try
	            {
	                ResultSet registros=null;
	                if (cboConsultarPor.getSelectedItem().equals("Dia"))
	                {
	                	PreparedStatement consultar = Principal.conexao.prepareStatement
	                			("SELECT COUNT(*) as numero FROM cardapio WHERE diasemana='" + 
	                	txtConsultarPor.getText() + "'");
	                	registros = consultar.executeQuery();
	                	registros.next();
	                	int quantidade = Integer.parseInt(registros.getString("numero"));
	                	if (quantidade!=0)
	                	{
	                		consultar = Principal.conexao.prepareStatement
		                			("SELECT * FROM cardapio WHERE diasemana = '" + txtConsultarPor.getText() + "'");
	                		registros = consultar.executeQuery();
	                		registros.next();
	                		do
	                		{
	                			String diasemana = registros.getString("diasemana");
	                			String nomereceita = registros.getString("nomereceita");
	                			String descricao = registros.getString("descricao");
	                			modelo.addRow(new Object[] {diasemana,nomereceita,descricao});
	                		}while (registros.next());
	                	}else
	                	{
	                		JOptionPane.showMessageDialog(null,"Nenhum registro encontrado","Nenhum registro",JOptionPane.INFORMATION_MESSAGE);
	                	}
	                }else if (cboConsultarPor.getSelectedItem().equals("Receita"))
	                {
	                	PreparedStatement consultar = Principal.conexao.prepareStatement
	                			("SELECT COUNT(*) as numero FROM cardapio WHERE nomereceita='" + 
	                	txtConsultarPor.getText() + "'");
	                	registros = consultar.executeQuery();
	                	registros.next();
	                	int quantidade = Integer.parseInt(registros.getString("numero"));
	                	if (quantidade!=0)
	                	{
	                		consultar = Principal.conexao.prepareStatement
		                			("SELECT * FROM cardapio WHERE nomereceita = '" + txtConsultarPor.getText() + "'");
	                		registros = consultar.executeQuery();
	                		registros.next();
	                		do
	                		{
	                			String diasemana = registros.getString("diasemana");
	                			String nomereceita = registros.getString("nomereceita");
	                			String descricao = registros.getString("descricao");
	                			modelo.addRow(new Object[] {diasemana,nomereceita,descricao});
	                		}while (registros.next());
	                	}else
	                	{
	                		JOptionPane.showMessageDialog(null,"Nenhum registro encontrado","Nenhum registro",JOptionPane.INFORMATION_MESSAGE);
	                	}
	                }
	            }catch (SQLException e)
	            {
	            	JOptionPane.showMessageDialog(null,"Erro de SQL: " + e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
	            }catch (Exception e)
		        {
		            JOptionPane.showMessageDialog(null,"Erro: " + e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);  
		        }
			}
		}else if (origem == btnExcluir || origem == itemExcluir)
		{
			if (tabela.getSelectedRow()>=0)
	        {
	            Object[] opcoes={"Sim","Não"};
	            int retorno=JOptionPane.showOptionDialog(null,"Tem certeza que deseja excluir?","Excluir",
	            		JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,opcoes,opcoes[0]);
	            if (retorno == 0)
	            {
	            	try
	            	{
	            		PreparedStatement excluir = Principal.conexao.prepareStatement
	            				("DELETE FROM cardapio WHERE diasemana = ? AND nomereceita = ? AND descricao = ?");
	            		excluir.setString(1,modelo.getValueAt(tabela.getSelectedRow(),0).toString());
	            		excluir.setString(2,modelo.getValueAt(tabela.getSelectedRow(),1).toString());
	            		excluir.setString(3,modelo.getValueAt(tabela.getSelectedRow(),2).toString());
	            		excluir.executeUpdate();
	            		modelo.removeRow(tabela.getSelectedRow());
	            		JOptionPane.showMessageDialog(null,"Receita excluída com sucesso!!!","Excluído",
	            				JOptionPane.INFORMATION_MESSAGE);
	            	}catch (SQLException erro)
	            	{
	            		JOptionPane.showMessageDialog(null,"Erro de SQL. Erro: " + erro.getMessage(),
	            				"Erro",JOptionPane.ERROR_MESSAGE);
	            	}catch (Exception erro)
	            	{
	            		JOptionPane.showMessageDialog(null,"Erro: " + erro.getMessage(),
	            				"Erro",JOptionPane.ERROR_MESSAGE);
	            	}
	            }
	        }else
	        {
	            JOptionPane.showMessageDialog(null,
	            		"Selecione um registro da tabela antes de clicar no botão excluir",
	            		"Erro",JOptionPane.ERROR_MESSAGE);
	        }
		}else if (origem == btnLimpar || origem == itemLimpar)
		{
			txtConsultarPor.setText("");
		}else if (origem == btnCancelar || origem == itemCancelar)
		{
			dispose();
		}
	}
	
	public void addGridBag(final int x,final int y,final Component obj,final JPanel pnl)
	{
		restricoes.gridx=x;
		restricoes.gridy=y;
		pnl.add(obj,restricoes);
	}
}
