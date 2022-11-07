package cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;


public class Ventana extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;	
	private JPanel textPanel, inputPanel;
	private JLabel lblUsuario, lblMensaje;
	private JTextField txtUsuario;
	private JTextField txtMensaje;
	private String name, message;
	private Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
	private Border blankBorder = BorderFactory.createEmptyBorder(10,10,20,10);//top,r,b,l
	private Cliente chatClient;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    
    protected JTextArea textArea, userArea;
    protected JFrame frame;
    protected JButton startButton, sendButton, listaBoton;
    protected JPanel clientPanel, userPanel;
	
    
    public static void main(String args[]){
		//set the look and feel to 'Nimbus'
		try{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
				if("Nimbus".equals(info.getName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}catch(Exception e){
			
		}
		
		new Ventana();
	}
    
    public Ventana(){
		
		frame = new JFrame("Cliente");	
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        
		    	if(chatClient != null){
			    	try {
			        	sendMessage("Saliendo......");
			        	chatClient.serverIF.salirChat(name);
					} catch (RemoteException e) {
						e.printStackTrace();
					}		        	
		        }
		        System.exit(0);  
		    }   
		});
		
	
		Container c = getContentPane();
		JPanel outerPanel = new JPanel(new BorderLayout());
		
		outerPanel.add(getInputPanel(), BorderLayout.NORTH);
		outerPanel.add(getTextPanel(), BorderLayout.SOUTH);
		
		c.setLayout(new BorderLayout());
		c.add(outerPanel, BorderLayout.CENTER);
		c.add(getUsersPanel(), BorderLayout.EAST);

		frame.add(c);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocation(150, 150);
		txtUsuario.requestFocus();
	
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
    
    
    public JPanel getTextPanel(){
		String welcome = "Ingrese su usuario e inicie al grupo\n";
		textArea = new JTextArea(welcome, 14, 30);
		textArea.setMargin(new Insets(7, 7, 7, 7));
		textArea.setFont(meiryoFont);
		
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel = new JPanel();
		textPanel.add(scrollPane);
	
		textPanel.setFont(new Font("Meiryo", Font.PLAIN, 14));
		return textPanel;
	}
    
    public JPanel getInputPanel(){
		
		GridBagLayout grid= new GridBagLayout();
		GridBagConstraints rest= new GridBagConstraints();
		inputPanel = new JPanel(grid);
		//inputPanel.setBorder(blankBorder);
		
		
		lblUsuario= new JLabel("Usuario:");
		//lblUsuario.setBounds(getBounds());
		rest.gridx=0;
		rest.gridy=0;
		rest.gridwidth=1;
		rest.gridheight=1;
		inputPanel.add(lblUsuario,rest);
		txtUsuario = new JTextField(15);
		rest.gridx=1;
		rest.gridy=0;
		rest.gridwidth=1;
		rest.gridheight=1;
		inputPanel.add(txtUsuario,rest);
		lblMensaje= new JLabel("Mensaje:");
		rest.gridx=0;
		rest.gridy=1;
		rest.gridwidth=1;
		rest.gridheight=1;
		inputPanel.add(lblMensaje,rest);
		
		txtMensaje= new JTextField(25);
		rest.gridx=1;
		rest.gridy=1;
		rest.gridwidth=1;
		rest.gridheight=1;
		txtMensaje.setEnabled(false);
		inputPanel.add(txtMensaje,rest);
		txtUsuario.setFont(meiryoFont);
		
		//rest.fill=GridBagConstraints.BOTH;
		//rest.gridheight=5;
		//grid.setConstraints(lblUsuario, rest);
		//grid.setConstraints(txtUsuario, rest);
		//inputPanel.setLayout(grid);
		//inputPanel.setCon
		//inputPanel.add(lblMensaje);
		//inputPanel.add(txtMensaje);
		return inputPanel;
	}
    
    public JPanel getUsersPanel(){
		
		userPanel = new JPanel(new BorderLayout());
		
		
		listaBoton= new JButton("Conectados");
		listaBoton.addActionListener(this);
		
		
		//JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
		//buttonPanel.add(listaBoton);
		
//		String  userStr = " Usuarios Conectados:       ";
//		
//		JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
//		userPanel.add(userLabel, BorderLayout.NORTH);	
//		userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));
//
		String[] noClientsYet = {"Ningun usario conectado"};
		setClientPanel(noClientsYet);
//
//		clientPanel.setFont(meiryoFont);
		userPanel.add(makeButtonPanel(), BorderLayout.SOUTH);	
		userPanel.add(listaBoton);
		//userPanel.setBorder(blankBorder);
		userPanel.setBorder(blankBorder);
		return userPanel;		
		
	}
    
    public void setClientPanel(String[] currClients) {  	
    	clientPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<String>();
        
        for(String s : currClients){
        	listModel.addElement(s);
        }
        
        //Create the list and put it in a scroll pane.
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(8);
        list.setFont(meiryoFont);
        JScrollPane listScrollPane = new JScrollPane(list);

        clientPanel.add(listScrollPane, BorderLayout.CENTER);
        //userPanel.add(clientPanel, BorderLayout.CENTER);
    }
    
    public JPanel makeButtonPanel() {		
		sendButton = new JButton("Enviar ");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);


		startButton = new JButton("Iniciar ");
		startButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(startButton);
		buttonPanel.add(sendButton);
		
		return buttonPanel;
	}
    
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Confirmo...........");
		try {
			//get connected to chat service
			if(e.getSource() == startButton){
				name = txtUsuario.getText();				
				if(name.length() != 0){
					frame.setTitle("Ventana para chat de " + name);
					//txtUsuario.setText("");
					textArea.append("Usuario: " + name + " ingresando... \n");							
					getConnected(name);
					if(!chatClient.connectionProblem){
						startButton.setEnabled(false);
						sendButton.setEnabled(true);
						txtUsuario.setEditable(false);
						txtMensaje.setEnabled(true);
						}
				}
				else{
					JOptionPane.showMessageDialog(frame, "Ingresa tu nombre para empezar");
				}
			}

			//get text and clear textField
			if(e.getSource() == sendButton){
				message = txtMensaje.getText();
				txtMensaje.setText("");
				sendMessage(message);
				System.out.println("Enviando mensaje : " + message);
			}
			
			if(e.getSource() == listaBoton) {

				JOptionPane.showMessageDialog(frame, new JScrollPane(clientPanel),"Usuarios Conectados",JOptionPane.INFORMATION_MESSAGE);
			}
			
			
		}
		catch (RemoteException remoteExc) {			
			remoteExc.printStackTrace();	
		}
	}
	
	private void sendMessage(String chatMessage) throws RemoteException {
		chatClient.serverIF.actualizarChat(name, chatMessage);
	}
	
	private void getConnected(String userName) throws RemoteException{
		//remove whitespace and non word characters to avoid malformed url
		String cleanedUserName = userName.replaceAll("\\s+","_");
		cleanedUserName = userName.replaceAll("\\W+","_");
		try {		
			chatClient = new Cliente(this, cleanedUserName);
			chatClient.startClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
